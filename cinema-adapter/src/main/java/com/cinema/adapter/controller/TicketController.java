package com.cinema.adapter.controller;

import com.cinema.application.dto.TicketRequestDTO;
import com.cinema.application.service.TicketService;
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
    public ResponseEntity<String> bookTickets(@Valid @RequestBody TicketRequestDTO ticketRequestDTO ) {
        ticketService.bookTickets(ticketRequestDTO);
        return ResponseEntity.ok("예매가 성공적으로 완료되었습니다.");
    }
}

