package com.cinema.core.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
public class Seat extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -8004503224579450742L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private Long seatId;

    private String seatTypeCd;  // 좌석 유형 코드 [ENUM]

    private String seatNm;
}
