package com.cinema.adapter.controller;

import com.cinema.application.dto.MovieRequestDTO;
import com.cinema.application.dto.MovieResponseDTO;
import com.cinema.application.service.MovieService;
import com.cinema.common.response.ApiResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {
    private final MovieService movieService;

    /**
     * 영화별 상영시간표 조회
     * */
    // TODO : 상영일자가 추가되는 경우, bookable 쿼리파라미터를 추가하여 상영가능한 상태의 영화만 조회할 수 있도록 확장 가능
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<MovieResponseDTO>>> getMovieScreenings(@Valid @RequestBody MovieRequestDTO movieRequestDTO) {
        List<MovieResponseDTO> movieList = movieService.getMovieScreenings(movieRequestDTO);
        return ResponseEntity.ok(ApiResponseDTO.success(movieList, "영화 목록 조회 성공"));
    }

    /**
     * 영화별 상영시간표 캐시 삭제
     */
    @GetMapping("/evictRedisCache")
    public ResponseEntity<ApiResponseDTO<String>> evictRedisCache() {
        movieService.evictShowingMovieCache();
        return ResponseEntity.ok(ApiResponseDTO.success(null, "캐시 삭제 완료"));
    }
}

