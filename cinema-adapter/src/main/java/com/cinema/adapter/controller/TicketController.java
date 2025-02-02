package com.cinema.adapter.controller;

import com.cinema.application.dto.TicketRequestDTO;
import com.cinema.application.service.TicketService;
import com.cinema.common.response.ApiResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {
    private final TicketService ticketService;

    /**
     * 예매
     * */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<String>> bookTickets(@Valid @RequestBody TicketRequestDTO ticketRequestDTO) {
        ticketService.bookTickets(ticketRequestDTO);
        return ResponseEntity.ok(ApiResponseDTO.success(null, "예매 성공"));
    }
}

