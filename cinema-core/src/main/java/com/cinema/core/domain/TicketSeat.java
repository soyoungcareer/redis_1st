package com.cinema.core.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;

@Entity
@Getter
@Table(name = "ticket_seat")
public class TicketSeat extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -9104093970044754227L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_seat_id", columnDefinition = "INT UNSIGNED")
    private Long ticketSeatId;

    @Column(name = "ticket_id")
    private Long ticketId;

    @Column(name = "seat_id")
    private Long seatId;

    public TicketSeat(Long ticketId, Long seatId) {
        this.ticketId = ticketId;
        this.seatId = seatId;
    }
}
