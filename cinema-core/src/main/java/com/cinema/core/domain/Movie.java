package com.cinema.core.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Movie extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private Long movieId;

    private String title;

    private String gradeCd; // 영상물 등급 코드 [ENUM]

    private LocalDate rlseDate;

    private String thumbImg;

    private Integer runtimeMin;

    private String genreCd; // 장르 코드 [ENUM]
}
