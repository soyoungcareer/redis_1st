package com.cinema.core.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "seat")
public class Seat extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -8004503224579450742L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id", columnDefinition = "INT UNSIGNED")
    private Long seatId;

    @Column(name = "seat_type_cd")
    private String seatTypeCd;  // 좌석 유형 코드 [ENUM]

    @Column(name = "seat_nm")
    private String seatNm;
}
