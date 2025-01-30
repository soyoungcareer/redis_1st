package com.cinema.infra.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MovieScreeningData {
    private Long movieId;
    private String title;
    private String gradeCd; // GradeCode Enum의 name 값
    private LocalDate releaseDate;
    private String thumbImg;
    private Integer runtimeMin;
    private String genreCd; // GenreCode Enum의 name 값
    private Long screeningId;
    private String theaterNm;
    private LocalTime startTime;
    private LocalTime endTime;
}
