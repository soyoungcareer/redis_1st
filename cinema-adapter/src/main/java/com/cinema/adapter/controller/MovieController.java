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

    // 참고 : 상영일자가 추가되는 경우, bookable 쿼리파라미터를 추가하여 상영가능한 상태의 영화만 조회할 수 있도록 확장 가능
    @GetMapping
    public List<Movie> getMovies(@RequestParam(required = false) String title,
                                 @RequestParam(required = false) String genre) {
        
        // TODO : 장르 enum description으로 리턴 필요
        return movieService.getMovies(title, genre);
    }
}

