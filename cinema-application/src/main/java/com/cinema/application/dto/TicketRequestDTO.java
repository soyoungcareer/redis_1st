package com.cinema.application.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TicketRequestDTO {
    @NotNull
    private Long screeningId;

    @NotNull
    private Long userId;

    @NotEmpty
    private List<String> seatNames;
}
