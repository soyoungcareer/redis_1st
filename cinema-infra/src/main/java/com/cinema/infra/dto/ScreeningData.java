package com.cinema.infra.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class ScreeningData {
    private Long screeningId;
    private String theaterNm;
    private LocalTime startTime;
    private LocalTime endTime;
}
