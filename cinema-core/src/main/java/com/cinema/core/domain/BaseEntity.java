package com.cinema.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {
    @Column(name = "reg_user_id")
    private String regUserId;

    @CreatedDate
    @Column(name = "reg_dttm", nullable = false, updatable = false)
    private LocalDateTime regDttm;

    @Column(name = "chg_user_id")
    private String chgUserId;

    @LastModifiedDate
    @Column(name = "chg_dttm")
    private LocalDateTime chgDttm;
}
