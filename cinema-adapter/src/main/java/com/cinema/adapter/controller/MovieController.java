package com.cinema.adapter.controller;

import com.cinema.application.service.MovieService;
import com.cinema.core.domain.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;

    // TODO : 예약 가능 여부는 query parameter로 구분: /api/movies?bookable=true
    @GetMapping("/bookable")
    public List<Movie> getBookableMovies() {
        return movieService.getBookableMovies();
    }
}

