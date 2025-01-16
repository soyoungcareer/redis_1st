package com.cinema.application.dto;

import java.time.LocalDate;

public class MovieDTO {
    private Long movieId;
    private String title;
    private String gradeCd; // 영상물 등급 코드 [ENUM]
    private LocalDate rlseDate;
    private String thumbImg;
    private Integer runtimeMin;
    private String genreCd; // 장르 코드 [ENUM]
}
