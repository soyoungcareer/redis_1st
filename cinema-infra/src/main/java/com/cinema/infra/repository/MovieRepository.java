package com.cinema.infra.repository;

import com.cinema.core.domain.Movie;
import com.cinema.infra.dto.MovieScreeningData;
import com.cinema.infra.dto.ScreeningData;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>, MovieRepositoryCustom {
    // 필수 조회 정보 : 영화 제목, 영상물 등급, 개봉일, 썸네일 이미지, 러닝 타임(분), 영화 장르, 상영관 이름, 상영 시간표(시작시각, 종료시각)

}
