package com.cinema.application.service;

import com.cinema.application.dto.TicketRequestDTO;
import com.cinema.common.enums.SeatNameCode;
import com.cinema.common.exception.TooManyRequestsException;
import com.cinema.common.response.ApiResponseDTO;
import com.cinema.core.domain.Ticket;
import com.cinema.core.domain.TicketSeat;
import com.cinema.infra.lock.DistributedLockUtil;
import com.cinema.infra.repository.SeatRepository;
import com.cinema.infra.repository.TicketRepository;
import com.cinema.infra.repository.TicketSeatRepository;
import com.cinema.infra.repository.UserRepository;
import com.google.common.util.concurrent.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {
    private final TicketRepository ticketRepository;
    private final TicketSeatRepository ticketSeatRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;

    private final DistributedLockUtil lockUtil;

    // Google Guava
    private final Map<String, RateLimiter> reservationRateLimiters = new ConcurrentHashMap<>();
    private static final double RESERVATION_RATE = 1.0 / 300; // 5분에 1회 제한

    // Redisson
    private final RedissonClient redissonClient;
    private static final String RESERVATION_LIMIT_PREFIX = "rate_limit:reservation:";

    @Value("${max-count.theater-bookable}")
    private int maxTheaterBookableCnt;

    /**
     * 예매하기
     * */
    @Retryable(value = OptimisticLockingFailureException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void bookTickets(TicketRequestDTO ticketRequestDTO) {
        Long userId = ticketRequestDTO.getUserId();
        Long screeningId = ticketRequestDTO.getScreeningId();

        if (userId == null || screeningId == null) {
            throw new NoSuchElementException("유효하지 않은 요청 데이터입니다.");
        }

        // RateLimit 적용
        // 예매 가능 여부 확인
        // 1) Google Guava
        /*
        String reservationKey = userId + "-" + screeningId;

        if (reservationRateLimiters.containsKey(reservationKey) &&
            !reservationRateLimiters.get(reservationKey).tryAcquire()) {
            throw new TooManyRequestsException("해당 상영 일정에 대해 5분 내에 다시 예매할 수 없습니다.");
        }*/

        // 2) Redisson
        String reservationKey = RESERVATION_LIMIT_PREFIX + userId + ":" + screeningId;
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(reservationKey);

        if (rateLimiter.isExists() && !rateLimiter.tryAcquire()) {
            throw new TooManyRequestsException("해당 상영 일정에 대해 5분 내에 다시 예매할 수 없습니다.");
        }

        List<SeatNameCode> seatNameEnums = ticketRequestDTO.getSeatNames().stream()
                .map(SeatNameCode::fromString)
                .collect(Collectors.toList());

        this.ticketsValidCheck(ticketRequestDTO, seatNameEnums);

        // lock을 획득한 요청에 대해서만 Transactional 적용하기 위해 분리
        String lockKey = "lock:screening:" + ticketRequestDTO.getScreeningId();
        boolean bookingSuccess = lockUtil.executeWithLock(lockKey, 5, 3, () ->
                bookTicketsWithTransaction(ticketRequestDTO, seatNameEnums)
        );

        // 예매 성공 시에만 5분 제한 적용
        if (bookingSuccess) {
            // 1) Google Guava
            /*reservationRateLimiters.computeIfAbsent(reservationKey, key -> RateLimiter.create(RESERVATION_RATE));
            reservationRateLimiters.get(reservationKey).tryAcquire();*/

            // 2) Redisson
            rateLimiter.trySetRate(RateType.PER_CLIENT, 1, 5, RateIntervalUnit.MINUTES); // 5분에 1회 제한
            rateLimiter.tryAcquire();
            log.info("예매 성공 - {} 제한 적용 (5분)", reservationKey);
        } else {
            log.error("예매 실패 - {} 제한 적용되지 않음");
        }
    }

    /**
     * 예매 정보 저장
     * */
    @Transactional
    public boolean bookTicketsWithTransaction(TicketRequestDTO ticketRequestDTO, List<SeatNameCode> seatNameEnums) {
        try {
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
            return true;
        } catch (Exception e) {
            log.error("예매 정보 저장 실패: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 유효성 검증
     * */
    private void ticketsValidCheck(TicketRequestDTO ticketRequestDTO, List<SeatNameCode> seatNameEnums) {
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
    }

    /**
     * 사용자 확인
     * */
    private boolean isUserExists(Long userId) {
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
