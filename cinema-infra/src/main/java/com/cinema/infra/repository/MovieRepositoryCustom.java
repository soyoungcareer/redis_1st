package com.cinema.infra.repository;

import com.cinema.infra.dto.MovieScreeningData;

import java.util.List;

public interface MovieRepositoryCustom {
    List<MovieScreeningData> findMoviesWithScreenings(String title, String genreCd);
}
