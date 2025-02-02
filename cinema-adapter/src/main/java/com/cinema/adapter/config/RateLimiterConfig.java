package com.cinema.adapter.config;

import com.google.common.util.concurrent.RateLimiter;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {

    // Google Guava
    /*@Bean
    public RateLimiter rateLimiter() {
        return RateLimiter.create(50.0 / 60.0); // 1분당 50회
    }*/


    // Redisson
    private static final String RATE_LIMIT_KEY = "rate_limit:requests";

    private final RedissonClient redissonClient;

    public RateLimiterConfig(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Bean
    public RRateLimiter rateLimiter() {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(RATE_LIMIT_KEY);
        rateLimiter.trySetRate(RateType.OVERALL, 50, 1, RateIntervalUnit.MINUTES); // 1분당 50회
        return rateLimiter;
    }
}
