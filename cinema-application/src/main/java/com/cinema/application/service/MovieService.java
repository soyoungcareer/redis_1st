package com.cinema.application.service;

import com.cinema.core.domain.Movie;
import com.cinema.infra.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public List<Movie> getBookableMovies() {
        return movieRepository.findBookableMovies();
    }
}
