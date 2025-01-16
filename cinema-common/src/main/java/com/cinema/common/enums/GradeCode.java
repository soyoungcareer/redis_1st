package com.cinema.common.enums;

/**
 * 영상물 등급 코드
 * */
public enum GradeCode {
    G("전체관람가"),
    PG("부모 동반 관람가"),
    PG13("13세 이상 관람가"),
    R("17세 이상 관람가");

    private final String description;

    GradeCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static GradeCode fromCode(String code) {
        for (GradeCode gradeCode : GradeCode.values()) {
            if (gradeCode.name().equalsIgnoreCase(code)) {
                return gradeCode;
            }
        }
        throw new IllegalArgumentException("Invalid grade code: " + code);
    }
}
