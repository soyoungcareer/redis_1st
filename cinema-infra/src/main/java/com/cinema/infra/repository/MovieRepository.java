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
          JOIN FETCH m.genre g
          JOIN Timetable t ON m.movieId = t.movieId
          JOIN Box b ON t.boxId = b.boxId
         WHERE t.showDate > CURRENT_DATE
            OR (t.showDate = CURRENT_DATE AND t.startTime > CURRENT_TIME)
         ORDER BY m.rlseDate DESC, t.startTime ASC
    """)
    List<Movie> findBookableMovies();
}
