package com.cinema.core.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "user")
public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 3213156872610256916L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", columnDefinition = "INT UNSIGNED")
    private Long userId;

    @Column(name = "user_nm")
    private String userNm;

    @Column(name = "birth_date")
    private LocalDate birthDate;
}
