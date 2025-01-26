package com.cinema.application.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TicketRequestDTO {
    @NotNull
    private Long screeningId;

    @NotNull
    private Long userId;

    @NotEmpty
    private List<String> seatNames;
}
