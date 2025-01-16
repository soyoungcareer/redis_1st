package com.cinema.core.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "screening")
public class Screening extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -1116415509867579126L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "screening_id", columnDefinition = "INT UNSIGNED")
    private Long screeningId;

    @Column(name = "movie_id")
    private Long movieId;

    @Column(name = "theater_id")
    private Long theaterId;

    @Column(name = "show_date")
    private LocalDate showDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;
}

