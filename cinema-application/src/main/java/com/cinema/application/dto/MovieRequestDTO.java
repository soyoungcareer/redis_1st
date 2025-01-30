package com.cinema.application.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class MovieRequestDTO {
    @Size(max=100, message="제목은 100자 이하로 입력해야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]*$", message = "특수문자는 허용되지 않습니다.")
    private String title;

    private String genreCd;
}
