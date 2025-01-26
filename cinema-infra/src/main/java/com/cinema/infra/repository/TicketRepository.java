package com.cinema.infra.repository;

import com.cinema.core.domain.Seat;
import com.cinema.core.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("""
            SELECT COUNT(*) 
              FROM Ticket t 
             INNER JOIN TicketSeat ts ON t.ticketId = ts.ticketId 
             WHERE t.userId = :userId 
               AND t.screeningId = :screeningId
            """)
    int countUserBookedSeats(Long userId, Long screeningId);
}
