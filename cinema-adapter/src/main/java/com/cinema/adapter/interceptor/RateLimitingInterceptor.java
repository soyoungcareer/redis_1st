package com.cinema.adapter.interceptor;

import com.cinema.application.dto.TicketRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.BufferedReader;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class RateLimitingInterceptor implements HandlerInterceptor {

    private final RateLimiter rateLimiter;
    private final Map<String, Integer> requestCounts = new ConcurrentHashMap<>();
    private final Map<String, Long> blockedIps = new ConcurrentHashMap<>();
    private final Map<String, RateLimiter> reservationRateLimiters = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 파싱을 위한 ObjectMapper

    private static final int MAX_REQUESTS = 50;     // 1분 내 최대 50회
    private static final long BLOCK_TIME_MS = 60 * 60 * 1000;   // 1시간 차단
    private static final double RESERVATION_RATE = 1.0 / 300;   // 5분에 1회 제한

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = request.getRemoteAddr();
        long currentTime = System.currentTimeMillis();

        // 1. 조회 API 제한 - 차단된 IP인지 확인
        if (blockedIps.containsKey(clientIp)) {
            long blockedTime = blockedIps.get(clientIp);
            if (currentTime - blockedTime < BLOCK_TIME_MS) {
                String unblockTime = Instant.ofEpochMilli(blockedTime + BLOCK_TIME_MS)
                        .atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("해당 IP는 1시간 동안 요청이 차단되었습니다. 차단 해제 시각: " + unblockTime);
                return false;
            } else {
                blockedIps.remove(clientIp);
                requestCounts.remove(clientIp);
            }
        }

        // 2. 조회 API 제한 - 1분 내 50회
        if (request.getRequestURI().startsWith("/api/v1/movies") && request.getMethod().equalsIgnoreCase("GET")) {
            requestCounts.put(clientIp, requestCounts.getOrDefault(clientIp, 0) + 1);
            if (requestCounts.get(clientIp) > MAX_REQUESTS) {
                blockedIps.put(clientIp, currentTime);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("너무 많은 요청으로 해당 IP는 요청이 차단되었습니다.");
                return false;
            }

            // 실시간 요청 속도 제한 적용 (RateLimiter)
            if (!rateLimiter.tryAcquire()) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("현재 요청량이 너무 많습니다. 잠시 후 다시 시도해주세요.");
                return false;
            }
        }

        // 3. 예매 API 요청 제한 (같은 시간대의 영화 5분에 1번) - TODO : 예매 성공한 경우에 대해서만 체크해야함.
        if (request.getRequestURI().startsWith("/api/v1/tickets") && request.getMethod().equalsIgnoreCase("POST")) {
            TicketRequestDTO ticketRequestDTO = this.extractTicketRequestDTO(request);
            if (ticketRequestDTO == null) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.getWriter().write("예매 요청 데이터가 잘못되었습니다.");
                return false;
            }

            Long userId = ticketRequestDTO.getUserId();
            Long screeningId = ticketRequestDTO.getScreeningId();

            if (userId == null || screeningId == null) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.getWriter().write("예매 요청에 필요한 정보가 부족합니다.");
                return false;
            }

            String reservationKey = userId + "-" + screeningId;
            reservationRateLimiters.computeIfAbsent(reservationKey, key -> RateLimiter.create(RESERVATION_RATE));

            if (!reservationRateLimiters.get(reservationKey).tryAcquire()) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("해당 시간대에 대해 5분 내에 다시 예매할 수 없습니다.");
                return false;
            }
        }

        return true;
    }

    /**
     * request 에서 TicketRequestDTO 추출
     * */
    private TicketRequestDTO extractTicketRequestDTO(HttpServletRequest request) {
        try {
            BufferedReader reader = request.getReader();
            return objectMapper.readValue(reader, TicketRequestDTO.class);
        } catch (Exception e) {
            return null; // JSON 파싱 실패 시 null 반환
        }
    }
}
