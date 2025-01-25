package com.cinema.infra.repository;

import com.cinema.core.domain.QMovie;
import com.cinema.core.domain.QScreening;
import com.cinema.infra.dto.MovieScreeningData;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class MovieRepositoryCustomImpl implements MovieRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<MovieScreeningData> findMoviesWithScreenings(String title, String genreCd) {
        QMovie qMovie = QMovie.movie;
        QScreening qScreening = QScreening.screening;

        return jpaQueryFactory.select(
                Projections.fields(MovieScreeningData.class,
                        qMovie.movieId.as("movieId"),
                        qMovie.title.as("title"),
                        qMovie.gradeCd.as("gradeCd"),
                        qMovie.releaseDate.as("releaseDate"),
                        qMovie.thumbImg.as("thumbImg"),
                        qMovie.runtimeMin.as("runtimeMin"),
                        qMovie.genreCd.as("genreCd"),
                        qScreening.screeningId.as("screeningId"),
                        qScreening.theater.theaterNm.as("theaterNm"),
                        qScreening.startTime.as("startTime"),
                        qScreening.endTime.as("endTime")
                )
        )
        .from(qMovie)
        .leftJoin(qMovie.screenings, qScreening)
        .where(
                qScreening.screeningId.isNotNull(),
                title != null && !"".equals(title) ? qMovie.title.containsIgnoreCase(title) : null,
                genreCd != null && !"".equals(genreCd) ? qMovie.genreCd.equalsIgnoreCase(genreCd) : null
        )
        .fetch();
    }
}
