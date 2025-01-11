package com.cinema.adapter.controller;

import com.cinema.application.service.MovieService;
import com.cinema.core.domain.Movie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/bookable")
    public List<Movie> getBookableMovies() {
        return movieService.getBookableMovies();
    }
}
