package com.cinema.application.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;

import java.util.List;

@Getter
public class TicketRequestDTO {
    @NotNull
    private Long screeningId;

    @NotNull
    private Long userId;

    @NotEmpty
    private List<String> seatNames;
}
