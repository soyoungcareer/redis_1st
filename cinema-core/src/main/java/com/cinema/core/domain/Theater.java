package com.cinema.core.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
public class Theater extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 8678295794324077135L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private Long theaterId;

    private String theaterNm;

    private String location;
}
