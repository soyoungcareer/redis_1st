package com.cinema.infra.repository;

import com.cinema.core.domain.Movie;
import com.cinema.infra.dto.MovieScreeningData;
import com.cinema.infra.dto.ScreeningData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    // 필수 조회 정보 : 영화 제목, 영상물 등급, 개봉일, 썸네일 이미지, 러닝 타임(분), 영화 장르, 상영관 이름, 상영 시간표(시작시각, 종료시각)
    /*@Query("""
        SELECT new com.cinema.infra.dto.MovieScreeningData(
            m.movieId, s.screeningId, 
            m.title, m.gradeCd, m.rlseDate,
            m.thumbImg, m.runtimeMin, m.genreCd, t.theaterNm, 
            s.startTime, s.endTime
        )
        FROM Screening s
        JOIN Movie m ON s.movieId = m.movieId
        JOIN Theater t ON s.theaterId = t.theaterId
        WHERE (:title IS NULL OR m.title LIKE %:title%)
          AND (:genre IS NULL OR m.genreCd = :genre)
        ORDER BY m.rlseDate, s.startTime
    """)
    List<MovieScreeningData> fetchMoviesWithScreenings(
            @Param("title") String title,
            @Param("genre") String genre
    );*/

    // 영화 정보
    @Query("""
        SELECT DISTINCT new com.cinema.infra.dto.MovieScreeningData(
            m.movieId, 
            m.title, m.gradeCd, m.rlseDate, 
            m.thumbImg, m.runtimeMin, m.genreCd, null
        )
          FROM Movie m
         WHERE (:title IS NULL OR m.title LIKE %:title%)
           AND (:genre IS NULL OR m.genreCd = :genre)
         ORDER BY m.rlseDate
    """)
    List<MovieScreeningData> fetchMovies(
            @Param("title") String title,
            @Param("genre") String genre
    );

    // 상영 정보
    @Query("""
        SELECT new com.cinema.infra.dto.ScreeningData(
            s.screeningId, t.theaterNm, s.startTime, s.endTime
        )
          FROM Screening s
          JOIN Theater t ON s.theaterId = t.theaterId
         WHERE s.movieId = :movieId
         ORDER BY s.startTime
    """)
    List<ScreeningData> fetchScreeningsByMovieId(@Param("movieId") Long movieId);


}
