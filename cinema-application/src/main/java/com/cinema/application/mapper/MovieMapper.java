package com.cinema.application.mapper;

import com.cinema.application.dto.MovieRequestDTO;
import com.cinema.common.enums.GenreCode;
import com.cinema.core.domain.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    /*@Mapping(source = "genreCd", target = "genreCd", qualifiedByName = "convertGenreCode")
    Movie toEntity(MovieRequestDTO dto);

    MovieRequestDTO toDTO(Movie movie);

    @Named("convertGenreCode")
    default String convertGenreCode(String genreCode) {
        try {
            return GenreCode.fromCode(genreCode).getDescription();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid genre code: " + genreCode, e);
        }
    }*/
}
