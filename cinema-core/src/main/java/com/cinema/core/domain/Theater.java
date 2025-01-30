package com.cinema.core.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;

@Entity
@Getter
@Table(name = "theater")
public class Theater extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 8678295794324077135L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theater_id", columnDefinition = "INT UNSIGNED")
    private Long theaterId;

    @Column(name = "theater_nm")
    private String theaterNm;

    @Column(name = "location")
    private String location;
}
