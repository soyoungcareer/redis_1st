package com.cinema.core.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
public class Ticket extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -5368501456583801672L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private Long ticketId;

    private Long userId;

    private Long seatId;

    private Long timeId;
}
