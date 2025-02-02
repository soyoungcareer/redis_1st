package com.cinema.adapter.controller;

import com.cinema.application.dto.TicketRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TicketControllerTest {

    @LocalServerPort
    private int port;

    private String baseUrl;
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1/tickets";
        restTemplate = new RestTemplate();
    }

    @Test
    void testRateLimitForTickets() {
        Long userId = 1L;
        Long screeningId = 3L;

        HttpHeaders headers = new HttpHeaders();

        // 첫 번째 요청: 정상 예매
        TicketRequestDTO requestDTO = new TicketRequestDTO(screeningId, userId, List.of("A1"));
        HttpEntity<TicketRequestDTO> request = new HttpEntity<>(requestDTO, headers);
        ResponseEntity<String> firstResponse = restTemplate.exchange(baseUrl, HttpMethod.POST, request, String.class);
        assertThat(firstResponse.getStatusCodeValue()).isEqualTo(200);

        // 두 번째 요청: 5분 제한 적용되어야 함 (429 반환 예상)
        TicketRequestDTO requestDTO2 = new TicketRequestDTO(screeningId, userId, List.of("A2"));
        HttpEntity<TicketRequestDTO> request2 = new HttpEntity<>(requestDTO2, headers);

        try {
            restTemplate.exchange(baseUrl, HttpMethod.POST, request2, String.class);
            System.out.println("test failed!!!");
        } catch (HttpClientErrorException.TooManyRequests e) {
            System.out.println("429 Too Many Requests received as expected.");
            assertThat(e.getStatusCode().value()).isEqualTo(429);
        }
    }
}
