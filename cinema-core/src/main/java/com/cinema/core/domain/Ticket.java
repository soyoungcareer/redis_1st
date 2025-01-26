package com.cinema.core.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "ticket")
public class Ticket extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -5368501456583801672L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id", columnDefinition = "INT UNSIGNED")
    private Long ticketId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "screening_id")
    private Long screeningId;

    // FIXME : Optimistic Lock
    /*@Version
    @Column(name = "version", nullable = false)
    private Long version;*/
}
