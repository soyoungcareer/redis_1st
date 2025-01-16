package com.cinema.application.mapper;

import com.cinema.application.dto.MovieDTO;
import com.cinema.common.enums.GradeCode;
import com.cinema.core.domain.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface MovieMapper {
//    @Mapping(source = "gradeCd", target = "gradeCd", qualifiedByName = "convertGradeCode")
//    Movie toEntity(MovieDTO dto);

//    MovieDTO toDTO(Movie movie);

    /*@Named("convertGradeCode")
    default String convertGradeCode(String gradeCode) {
        try {
            return GradeCode.fromCode(gradeCode).getDescription();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid grade code: " + gradeCode, e);
        }
    }*/
}
