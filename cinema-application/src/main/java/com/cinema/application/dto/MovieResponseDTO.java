package com.cinema.application.dto;

import com.cinema.infra.dto.ScreeningData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieResponseDTO {
    private String title;
    private String gradeDescription; // 영상물 등급 코드 [ENUM]
    private LocalDate rlseDate;
    private String thumbImg;
    private int runtimeMin;
    private String genreDescription; // 장르 코드 [ENUM]
    private List<ScreeningData> screenings;
}
