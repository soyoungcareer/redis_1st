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
public class Screening extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -1116415509867579126L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private Long timeId;

    private Long movieId;

    private Long theaterId;

    private LocalDate showDate;

    private LocalTime startTime;

    private LocalTime endTime;
}

