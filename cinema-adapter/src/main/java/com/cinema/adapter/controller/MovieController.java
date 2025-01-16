package com.cinema.adapter.controller;

import com.cinema.application.service.MovieService;
import com.cinema.core.domain.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    public List<Movie> getMovies(@RequestParam(required = false, defaultValue = "false") boolean bookable) {
        return movieService.getMovies(bookable);
    }
}

