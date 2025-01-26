package com.cinema.infra.repository;

import com.cinema.core.domain.Ticket;
import com.cinema.core.domain.TicketSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketSeatRepository extends JpaRepository<TicketSeat, Long> {

    // 특정 상영 시간표에서 이미 예약된 좌석 ID 목록 조회
    @Query("""
            SELECT ts.seatId 
              FROM TicketSeat ts 
             INNER JOIN Ticket t ON ts.ticketId = t.ticketId 
             WHERE t.screeningId = :screeningId 
            """)
    List<Long> findBookedSeatsByScreeningId(Long screeningId);
}
