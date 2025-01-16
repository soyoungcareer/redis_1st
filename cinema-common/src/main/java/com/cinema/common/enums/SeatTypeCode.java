package com.cinema.common.enums;

/**
 * 좌석 유형 코드
 * */
public enum SeatTypeCode {
    STANDARD("일반석"),
    ACCESSIBLE("장애인석"),
    PREMIUM("프리미엄 좌석"),
    VIP("VIP 좌석"),
    RECLINING("리클라이닝 좌석"),
    SWEET_BOX("스위트석");

    private final String description;

    SeatTypeCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static SeatTypeCode fromCode(String code) {
        for (SeatTypeCode seatTypeCode : SeatTypeCode.values()) {
            if (seatTypeCode.name().equalsIgnoreCase(code)) {
                return seatTypeCode;
            }
        }
        throw new IllegalArgumentException("Invalid seat type code: " + code);
    }
}
