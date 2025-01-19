package com.cinema.infra.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MovieScreeningData {
    private Long movieId;
    private String title;
    private String gradeCd; // GradeCode Enum의 name 값
    private LocalDate rlseDate;
    private String thumbImg;
    private int runtimeMin;
    private String genreCd; // GenreCode Enum의 name 값
    private List<ScreeningData> screenings;
}
