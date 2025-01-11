package com.cinema.core.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "box")
@Getter
@Setter
public class Box extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "box_id")
    private Long boxId;

    @Column(name = "box_nm")
    private String boxNm;

    @Column(name = "location")
    private String location;
}
