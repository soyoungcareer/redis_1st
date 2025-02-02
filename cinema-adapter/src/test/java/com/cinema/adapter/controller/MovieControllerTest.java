package com.cinema.adapter.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MovieControllerTest {

    @LocalServerPort
    private int port;

    private String baseUrl;
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1/movies";
        restTemplate = new RestTemplate();
    }

    @Test
    void testRateLimitForMovies() throws InterruptedException {
        int requestCount = 51;
        int delayBetweenRequestsMs = 100; // 각 요청 사이의 대기 시간 (100ms)

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        int[] rateLimitExceededCount = {0}; // 429 응답 개수 카운트

        for (int i = 0; i < requestCount; i++) {
            executorService.submit(() -> {
                try {
                    ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class);
                    System.out.println("Response: " + response.getStatusCode());
                } catch (HttpClientErrorException.TooManyRequests e) {
                    System.out.println("429 Too Many Requests received.");
                    synchronized (rateLimitExceededCount) {
                        rateLimitExceededCount[0]++;
                    }
                }

                try {
                    Thread.sleep(delayBetweenRequestsMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        // 429 응답이 하나라도 나와야 테스트 통과
        assertThat(rateLimitExceededCount[0]).isGreaterThanOrEqualTo(1);
    }
}
