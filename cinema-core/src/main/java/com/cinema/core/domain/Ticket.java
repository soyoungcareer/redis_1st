package com.cinema.core.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "ticket")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
    @Version
    @Column(name = "version", nullable = false)
    private Long version;
}
