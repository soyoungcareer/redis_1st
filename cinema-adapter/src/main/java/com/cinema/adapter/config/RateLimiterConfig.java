package com.cinema.adapter.config;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {
    @Bean
    public RateLimiter rateLimiter() {
        return RateLimiter.create(50.0 / 60.0); // 1분당 50회
    }
}
