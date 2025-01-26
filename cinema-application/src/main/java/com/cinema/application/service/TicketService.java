package com.cinema.application.service;

import com.cinema.application.dto.TicketRequestDTO;
import com.cinema.common.enums.SeatNameCode;
import com.cinema.core.domain.Ticket;
import com.cinema.core.domain.TicketSeat;
import com.cinema.infra.repository.SeatRepository;
import com.cinema.infra.repository.TicketRepository;
import com.cinema.infra.repository.TicketSeatRepository;
import jakarta.persistence.PessimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {
    private final TicketRepository ticketRepository;
    private final TicketSeatRepository ticketSeatRepository;
    private final SeatRepository seatRepository;

    @Value("${max-count.theater-bookable}")
    private int maxTheaterBookableCnt;

    /**
     * 예매하기
     * */
    @Retryable(value = ObjectOptimisticLockingFailureException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    @Transactional
    public void bookTickets(TicketRequestDTO ticketRequestDTO) {
        // 좌석명을 ENUM으로 변환
        List<SeatNameCode> seatNameEnums = ticketRequestDTO.getSeatNames().stream()
                .map(SeatNameCode::fromString)
                .collect(Collectors.toList());

        if (seatNameEnums == null || seatNameEnums.isEmpty()) {
            throw new NullPointerException("좌석 정보가 없습니다.");
        }

        try {
            // 좌석 예약 가능 여부 확인
            if (!this.areSeatsBookable(ticketRequestDTO.getScreeningId(), seatNameEnums)) {
                throw new IllegalStateException("선택한 좌석 중 예약이 불가능한 좌석이 있습니다.");
            }

            // 좌석 연속 여부 확인 (동일한 라인인지)
            if (!this.areSeatsConsecutive(seatNameEnums)) {
                throw new IllegalStateException("좌석은 동일한 행이면서 연속적이어야 합니다.");
            }

            // 상영시간표별 최대 5개 좌석 예약 가능
            if (!this.isBookingExceed(ticketRequestDTO, seatNameEnums.size())) {
                throw new IllegalStateException("상영시간표당 예매 가능 좌석 수를 초과하였습니다.");
            }

            // 예매 저장
            Ticket ticket = new Ticket();
            ticket.setUserId(ticketRequestDTO.getUserId());
            ticket.setScreeningId(ticketRequestDTO.getScreeningId());
            Ticket savedTicket = ticketRepository.save(ticket);

            // 예매 좌석 저장
            List<TicketSeat> ticketSeats = seatNameEnums.stream()
                    .map(seat -> {
                        Long seatId = seatRepository.findSeatIdByTheaterIdAndSeatNameCd(
                                ticketRequestDTO.getScreeningId(), seat.name()
                        ).orElseThrow(() -> new IllegalArgumentException("좌석 정보를 찾을 수 없습니다: " + seat.name()));

                        return new TicketSeat(savedTicket.getTicketId(), seatId);
                    })
                    .collect(Collectors.toList());

            ticketSeatRepository.saveAll(ticketSeats);

        // FIXME : Pessimistic Lock
//        } catch (PessimisticLockException e) {

        // FIXME : Optimistic Lock
        } catch (ObjectOptimisticLockingFailureException e) {
            log.error("좌석 예약 중 락 충돌 발생: screeningId={}, seatNameEnums={}",
                    ticketRequestDTO.getScreeningId(),
                    seatNameEnums);
            throw new IllegalStateException("동일 좌석이 이미 예약 중입니다. 나중에 다시 시도해 주세요.");
        }
    }

    /**
     * 좌석 연속 여부 확인 (동일한 라인인지)
     * */
    private boolean areSeatsConsecutive(List<SeatNameCode> seatNameEnums) {
        if (seatNameEnums == null || seatNameEnums.isEmpty()) {
            return false;
        }

        // 좌석명 코드에서 첫 글자를 추출
        char firstRow = seatNameEnums.get(0).name().charAt(0);
        List<Integer> seatNumbers = seatNameEnums.stream()
                .map(seat -> {
                    String seatName = seat.name();
                    // 첫 번째 글자가 동일한지 확인
                    if (seatName.charAt(0) != firstRow) {
                        throw new IllegalArgumentException("좌석은 동일한 행에 있어야 합니다: " + seatName);
                    }
                    // 두 번째 글자부터 숫자 추출
                    return Integer.parseInt(seatName.substring(1));
                })
                .sorted()
                .collect(Collectors.toList());

        for (int i = 1; i < seatNumbers.size(); i++) {
            if (seatNumbers.get(i) - seatNumbers.get(i - 1) != 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 좌석 예매 가능 여부 확인
     * */
    private boolean areSeatsBookable(Long screeningId, List<SeatNameCode> seatNameEnums) {
        List<Long> bookedSeats = ticketSeatRepository.findBookedSeatsByScreeningId(screeningId);

        for (SeatNameCode seat : seatNameEnums) {
            Long seatId = seatRepository.findSeatIdByTheaterIdAndSeatNameCd(screeningId, seat.name())
                    .orElseThrow(() -> new IllegalArgumentException("좌석 정보를 찾을 수 없습니다: " + seat.name()));

            if (bookedSeats.contains(seatId)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 상영시간표별 최대 좌석 예약 가능 수 초과 여부
     * */
    private boolean isBookingExceed(TicketRequestDTO ticketRequestDTO, int requestedSeats) {
        Long userId = ticketRequestDTO.getUserId();
        Long screeningId = ticketRequestDTO.getScreeningId();

        int bookedSeats = ticketRepository.countUserBookedSeats(userId, screeningId);
        return bookedSeats + requestedSeats <= maxTheaterBookableCnt;
    }
}
