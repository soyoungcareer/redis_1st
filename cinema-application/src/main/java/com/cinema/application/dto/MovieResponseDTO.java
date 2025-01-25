package com.cinema.application.dto;

import com.cinema.infra.dto.ScreeningData;

import java.time.LocalDate;
import java.util.List;

public record MovieResponseDTO (
    String title,
    String gradeDescription, // 영상물 등급 코드 [ENUM]
    LocalDate releaseDate,
    String thumbImg,
    int runtimeMin,
    String genreDescription, // 장르 코드 [ENUM]
    List<ScreeningData> screenings
) {}
