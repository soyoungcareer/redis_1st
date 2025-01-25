package com.cinema.application.service;

import com.cinema.application.dto.MovieRequestDTO;
import com.cinema.application.dto.MovieResponseDTO;
import com.cinema.common.enums.GenreCode;
import com.cinema.common.enums.GradeCode;
import com.cinema.core.domain.QMovie;
import com.cinema.core.domain.QScreening;
import com.cinema.infra.dto.MovieScreeningData;
import com.cinema.infra.dto.ScreeningData;
import com.cinema.infra.repository.MovieRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MovieService {
    private final MovieRepository movieRepository;

    @Cacheable(
            value = "movieScreenings",
            key = "(#title ?: '').concat('_').concat(#genreCd ?: '')",
            unless = "#result.isEmpty()"
    )
    public List<MovieResponseDTO> getMovieScreenings(MovieRequestDTO movieRequestDTO) {
        String title = movieRequestDTO.getTitle();
        String genreCd = movieRequestDTO.getGenreCd();
        List<MovieScreeningData> movies = movieRepository.findMoviesWithScreenings(title, genreCd);

        // 조회 후 Enum 코드 -> Description 변환
        List<MovieResponseDTO> response = movies.stream()
                .map(movie -> new MovieResponseDTO(
                        movie.getTitle(),
                        GradeCode.fromCode(movie.getGradeCd()).getDescription(),  // 변환 처리
                        movie.getReleaseDate(),
                        movie.getThumbImg(),
                        movie.getRuntimeMin(),
                        GenreCode.fromCode(movie.getGenreCd()).getDescription(),  // 변환 처리
                        movie.getScreeningId() != null ?
                            List.of(new ScreeningData(
                                    movie.getScreeningId(),
                                    movie.getTheaterNm(),
                                    movie.getStartTime(),
                                    movie.getEndTime()
                            )) : Collections.emptyList()
                ))
                .collect(Collectors.toList());

        return response;
    }

    /**
     * 영화별 상영시간표 캐시 삭제
     */
    @CacheEvict(value = "movieScreenings", allEntries = true)
    public void evictShowingMovieCache() {
        // 캐시를 강제로 삭제
    }
}
