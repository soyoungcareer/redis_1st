package com.cinema.core.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "movie")
public class Movie extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -4290967803761576977L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id", columnDefinition = "INT UNSIGNED")
    private Long movieId;

    @Column(name = "title")
    private String title;

    @Column(name = "grade_cd")
    private String gradeCd; // 영상물 등급 코드 [ENUM]

    @Column(name = "rlse_date")
    private LocalDate rlseDate;

    @Column(name = "thumb_img")
    private String thumbImg;

    @Column(name = "runtime_min")
    private Integer runtimeMin;

    @Column(name = "genre_cd")
    private String genreCd; // 장르 코드 [ENUM]
}
