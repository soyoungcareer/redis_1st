package com.cinema.common.enums;

// 참고 : AttributeConverter 를 사용하면 DB 리소스를 절약해서 사용할 수 있음.

/**
 * 장르 코드
 * */
public enum GenreCode {
    HORROR("공포"),
    ROMANCE("로맨스"),
    ACTION("액션"),
    DRAMA("드라마"),
    COMEDY("코미디"),
    THRILLER("스릴러"),
    SCI_FI("SF"),
    FANTASY("판타지"),
    ANIMATION("애니메이션"),
    DOCUMENTARY("다큐멘터리");

    private final String description;

    GenreCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static GenreCode fromCode(String code) {
        for (GenreCode genreCode : GenreCode.values()) {
            if (genreCode.name().equalsIgnoreCase(code)) {
                return genreCode;
            }
        }
        throw new IllegalArgumentException("Invalid genre code: " + code);
    }
}
