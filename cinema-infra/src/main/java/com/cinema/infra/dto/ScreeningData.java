package com.cinema.infra.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScreeningData {
    private Long screeningId;
    private String theaterNm;
    private LocalTime startTime;
    private LocalTime endTime;
}
