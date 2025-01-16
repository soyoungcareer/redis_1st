package com.cinema.infra.repository;

import com.cinema.core.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("""
        SELECT DISTINCT m FROM Movie m
          JOIN Screening s ON m.movieId = s.movieId
          JOIN Theater t ON t.theaterId = s.theaterId
         WHERE s.showDate > CURRENT_DATE
            OR (s.showDate = CURRENT_DATE AND s.startTime > CURRENT_TIME)
         ORDER BY m.rlseDate DESC
    """)
    List<Movie> findBookableMovies();
}
