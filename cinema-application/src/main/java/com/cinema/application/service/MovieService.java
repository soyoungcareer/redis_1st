package com.cinema.application.service;

import com.cinema.core.domain.Movie;
import com.cinema.infra.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getBookableMovies() {
        return movieRepository.findBookableMovies();
    }
}
