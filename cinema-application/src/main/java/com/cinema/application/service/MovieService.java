package com.cinema.application.service;

import com.cinema.core.domain.Movie;
import com.cinema.infra.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MovieService {
    private final MovieRepository movieRepository;

//    @Cacheable(value = "movies", key = "#title + #genre")
    @Cacheable(value = "movies", key = "T(String).valueOf(#title).concat('_').concat(T(String).valueOf(#genre))", unless = "#result.isEmpty()")
    public List<Movie> getMovies(String title, String genre) {
        // 영화제목 글자수 초과 validation check
        if (StringUtils.hasLength(title) && title.length() > 100) {
            throw new IllegalArgumentException("제목은 100자를 초과할 수 없습니다.");
        }

        // Cache Miss 시 DB 조회 결과를 응답으로 리턴
        List<Movie> movies = movieRepository.getMovies(title, genre);

        return movies != null ? movies : Collections.emptyList();
    }
}
