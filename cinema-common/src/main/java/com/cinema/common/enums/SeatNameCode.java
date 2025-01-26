package com.cinema.common.enums;

/**
 * 좌석명 코드
 *
 * (참고) 모든 상영관의 좌석이 A1~E5(5x5)로 구성됨을 가정함.
 * */
public enum SeatNameCode {
    A1, A2, A3, A4, A5,
    B1, B2, B3, B4, B5,
    C1, C2, C3, C4, C5,
    D1, D2, D3, D4, D5,
    E1, E2, E3, E4, E5;

    public static SeatNameCode fromString(String seatName) {
        try {
            return SeatNameCode.valueOf(seatName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 좌석명: " + seatName);
        }
    }
}
