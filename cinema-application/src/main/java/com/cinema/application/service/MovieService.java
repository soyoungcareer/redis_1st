package com.cinema.application.service;

import com.cinema.application.dto.MovieRequestDTO;
import com.cinema.application.dto.MovieResponseDTO;
import com.cinema.application.dto.TicketRequestDTO;
import com.cinema.common.enums.GenreCode;
import com.cinema.common.enums.GradeCode;
import com.cinema.core.domain.Ticket;
import com.cinema.infra.dto.MovieScreeningData;
import com.cinema.infra.dto.ScreeningData;
import com.cinema.infra.repository.MovieRepository;
import com.cinema.infra.repository.SeatRepository;
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
    private final SeatRepository seatRepository;

    /**
     * 영화별 상영시간표 조회
     * */
    // TODO : 캐시 전략 변경해볼 것! (전체 데이터 캐싱 후 필터링하는 방식?)
    @Cacheable(
            value = "movieScreenings",
            key = "(#title ?: '').concat('_').concat(#genreCd ?: '')",
            unless = "#result.isEmpty()"
    )
    public List<MovieResponseDTO> getMovieScreenings(MovieRequestDTO movieRequestDTO) {
        // 필수 조회 정보 : 영화 제목, 영상물 등급, 개봉일, 썸네일 이미지, 러닝 타임(분), 영화 장르, 상영관 이름, 상영 시간표(시작시각, 종료시각)
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
