package com.cinema.application.service;

import com.cinema.application.dto.MovieResponseDTO;
import com.cinema.common.enums.GenreCode;
import com.cinema.common.enums.GradeCode;
import com.cinema.infra.dto.MovieScreeningData;
import com.cinema.infra.dto.ScreeningData;
import com.cinema.infra.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MovieService {
    private final MovieRepository movieRepository;

    @Cacheable(
            value = "movieScreenings",
            key = "(#title ?: '').concat('_').concat(#genre ?: '')",
            unless = "#result.isEmpty()"
    )
    public List<MovieResponseDTO> getMovieScreenings(String title, String genre) {
        // 영화제목 글자수 초과 validation check
        if (StringUtils.hasLength(title) && title.length() > 100) {
            throw new IllegalArgumentException("제목은 100자를 초과할 수 없습니다.");
        }

        return movieRepository.fetchMovies(title, genre).stream()
                .peek(movie -> {
                    List<ScreeningData> screenings = movieRepository.fetchScreeningsByMovieId(movie.getMovieId());
                    movie.setScreenings(screenings);
                })
                .map(data -> new MovieResponseDTO(
                        data.getTitle(),
                        GradeCode.fromCode(data.getGradeCd()).getDescription(),
                        data.getRlseDate(),
                        data.getThumbImg(),
                        data.getRuntimeMin(),
                        GenreCode.fromCode(data.getGenreCd()).getDescription(),
                        data.getScreenings() // 상영 정보 추가
                ))
                .collect(Collectors.toList());
    }

    /**
     * 영화별 상영시간표 캐시 삭제
     */
    @CacheEvict(value = "movieScreenings", allEntries = true)
    public void evictShowingMovieCache() {
        // 캐시를 강제로 삭제
    }
}
