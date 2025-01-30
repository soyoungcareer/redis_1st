package com.cinema.application.service;

import com.cinema.application.dto.TicketRequestDTO;
import com.cinema.common.enums.SeatNameCode;
import com.cinema.core.domain.Ticket;
import com.cinema.core.domain.TicketSeat;
import com.cinema.infra.lock.DistributedLockUtil;
import com.cinema.infra.repository.SeatRepository;
import com.cinema.infra.repository.TicketRepository;
import com.cinema.infra.repository.TicketSeatRepository;
import com.cinema.infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {
    private final TicketRepository ticketRepository;
    private final TicketSeatRepository ticketSeatRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;

    private final RedissonClient redissonClient;
    private final DistributedLockUtil lockUtil;

    @Value("${max-count.theater-bookable}")
    private int maxTheaterBookableCnt;

    /**
     * 예매하기
     * */
    @Retryable(value = OptimisticLockingFailureException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void bookTickets(TicketRequestDTO ticketRequestDTO) {
        List<SeatNameCode> seatNameEnums = ticketRequestDTO.getSeatNames().stream()
                .map(SeatNameCode::fromString)
                .collect(Collectors.toList());

        // 사용자 확인
        if (!this.isUserExists(ticketRequestDTO.getUserId())) {
            throw new NoSuchElementException("사용자 정보가 없습니다. ID : " + ticketRequestDTO.getUserId());
        }

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

        // lock을 획득한 요청에 대해서만 Transactional 적용하기 위해 분리
        String lockKey = "lock:screening:" + ticketRequestDTO.getScreeningId();
        lockUtil.executeWithLock(lockKey, 5, 3, () -> {
            bookTicketsWithTransaction(ticketRequestDTO, seatNameEnums);
            return null;
        });
    }

    /**
     * 예매 정보 저장
     * */
    @Transactional
    public void bookTicketsWithTransaction(TicketRequestDTO ticketRequestDTO, List<SeatNameCode> seatNameEnums) {
        // 예매 저장
        Ticket ticket = Ticket.builder()
                .userId(ticketRequestDTO.getUserId())
                .screeningId(ticketRequestDTO.getScreeningId())
                .build();
        Ticket savedTicket = ticketRepository.save(ticket);
        ticketRepository.flush();

        // 예매 좌석 저장
        List<TicketSeat> ticketSeats = seatNameEnums.stream()
                .map(seat -> {
                    Long seatId = seatRepository.findSeatIdByScreeningIdAndSeatNameCd(
                            ticketRequestDTO.getScreeningId(), seat.name()
                    ).orElseThrow(() -> new NoSuchElementException("좌석 정보를 찾을 수 없습니다. 상영시간표ID : " + ticketRequestDTO.getScreeningId() + ", 좌석명 : " + seat.name()));

                    return new TicketSeat(savedTicket.getTicketId(), seatId);
                })
                .collect(Collectors.toList());

        ticketSeatRepository.saveAll(ticketSeats);
        ticketSeatRepository.flush();
    }

    /**
     * 사용자 확인
     * */
    private boolean isUserExists(Long userId) {
        if (userId == null) {
            throw new NullPointerException("사용자 정보가 없습니다.");
        }

        return userRepository.existsById(userId);
    }

    /**
     * 좌석 연속 여부 확인
     * */
    private boolean areSeatsConsecutive(List<SeatNameCode> seatNameEnums) {
        if (seatNameEnums == null || seatNameEnums.isEmpty()) {
            throw new NullPointerException("좌석 정보가 없습니다.");
        }

        Iterator<Integer> seatIterator = getIntegerIterator(seatNameEnums);

        if (!seatIterator.hasNext()) {
            return false;
        }

        int prev = seatIterator.next(); // 첫 번째 좌석 번호

        while (seatIterator.hasNext()) {
            int current = seatIterator.next();
            if (prev + 1 != current) {
                throw new IllegalArgumentException("좌석은 연속적으로만 선택할 수 있습니다.");
            }
            prev = current;
        }

        return true;
    }

    /**
     * 좌석 동일행 확인
     * */
    private static Iterator<Integer> getIntegerIterator(List<SeatNameCode> seatNameEnums) {
        String firstRow = seatNameEnums.get(0).name().substring(0, 1);

        Iterator<Integer> seatIterator = seatNameEnums.stream()
                .map(SeatNameCode::name)
                .peek(seatName -> {
                    if (!seatName.startsWith(firstRow)) {
                        throw new IllegalArgumentException("좌석은 동일한 행에 있어야 합니다.");
                    }
                })
                .map(seatName -> Integer.parseInt(seatName.substring(1)))
                .sorted()
                .iterator();
        return seatIterator;
    }

    /**
     * 좌석 예매 가능 여부 확인
     * */
    private boolean areSeatsBookable(Long screeningId, List<SeatNameCode> seatNameEnums) {
        List<Long> bookedSeats = ticketSeatRepository.findBookedSeatsByScreeningId(screeningId);

        for (SeatNameCode seat : seatNameEnums) {
            Long seatId = seatRepository.findSeatIdByScreeningIdAndSeatNameCd(screeningId, seat.name())
                    .orElseThrow(() -> new NoSuchElementException("좌석 정보를 찾을 수 없습니다. 상영시간표ID : " + screeningId + ", 좌석명 : " + seat.name()));

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
