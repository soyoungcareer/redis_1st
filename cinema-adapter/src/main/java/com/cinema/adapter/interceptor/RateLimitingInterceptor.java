package com.cinema.adapter.interceptor;

import com.google.common.util.concurrent.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RRateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RateLimitingInterceptor implements HandlerInterceptor {

//    private final RateLimiter rateLimiter;    // Google Guava
    private final RRateLimiter rateLimiter;     // Redisson

    private final Map<String, Integer> requestCounts = new ConcurrentHashMap<>();
    private final Map<String, Long> blockedIps = new ConcurrentHashMap<>();

    private static final int MAX_REQUESTS = 50;     // 1분 내 최대 50회
    private static final long BLOCK_TIME_MS = 60 * 60 * 1000;   // 1시간 차단

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
            // Google Guava
            /*if (!rateLimiter.tryAcquire()) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("현재 요청량이 너무 많습니다. 잠시 후 다시 시도해주세요.");
                return false;
            }*/

            // Redisson
            if (!rateLimiter.tryAcquire(1, TimeUnit.SECONDS)) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("현재 요청량이 너무 많습니다. 잠시 후 다시 시도해주세요.");
                return false;
            }
        }

        return true;
    }
}
