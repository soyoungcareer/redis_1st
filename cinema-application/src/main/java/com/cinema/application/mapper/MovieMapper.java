package com.cinema.application.mapper;

import org.mapstruct.Mapper;

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
