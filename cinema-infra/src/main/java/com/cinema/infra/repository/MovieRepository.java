package com.cinema.infra.repository;

import com.cinema.core.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    // 필수 조회 정보 : 영화 제목, 영상물 등급, 개봉일, 썸네일 이미지, 러닝 타임(분), 영화 장르, 상영관 이름, 상영 시간표(시작시각, 종료시각)
    @Query("""
        SELECT m.title, m.gradeCd, m.rlseDate, m.thumbImg, m.runtimeMin, m.genreCd,
               t.theaterNm,
               s.startTime, s.endTime
          FROM Screening s
          JOIN Movie m ON m.movieId = s.movieId
          JOIN Theater t ON t.theaterId = s.theaterId
         WHERE m.title = :title
           AND m.genreCd = :genre
         ORDER BY m.rlseDate, s.startTime
    """)
    List<Movie> getMovies(String title, String genre);
}
