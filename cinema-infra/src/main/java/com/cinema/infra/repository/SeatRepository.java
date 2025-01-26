package com.cinema.infra.repository;

import com.cinema.core.domain.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    @Query("""
            SELECT s.seatId 
              FROM Seat s 
             INNER JOIN Screening sc ON sc.theater.theaterId = s.theaterId 
             WHERE sc.screeningId = :screeningId 
               AND s.seatNameCd = :seatNameCd 
            """)
    Optional<Long> findSeatIdByTheaterIdAndSeatNameCd(@Param("screeningId") Long screeningId,
                                                      @Param("seatNameCd") String seatNameCd);
}
