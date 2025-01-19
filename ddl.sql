CREATE DATABASE IF NOT EXISTS cinema;

USE cinema;

DROP TABLE IF EXISTS `movie`;

CREATE TABLE `movie` (
	`movie_id`	INT UNSIGNED	NOT NULL AUTO_INCREMENT	COMMENT '영화ID',
	`title`	VARCHAR(100)	NULL	COMMENT '영화 제목',
	`grade_cd`	VARCHAR(10)	NULL	COMMENT '영상물 등급 코드[ENUM]',
	`rlse_date`	DATE	NULL	COMMENT '개봉일',
	`thumb_img`	VARCHAR(200)	NULL	COMMENT '썸네일 이미지',
	`runtime_min`	INT	NULL	COMMENT '러닝 타임(분)',
	`genre_cd`	VARCHAR(20)	NULL	COMMENT '장르코드[ENUM]',
	`created_by`	VARCHAR(20)	NULL	COMMENT '작성자',
	`created_at`	DATETIME	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '작성일',
	`updated_by`	VARCHAR(20)	NULL	COMMENT '수정자',
	`updated_at`	DATETIME	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '수정일',
	PRIMARY KEY (`movie_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `theater`;

CREATE TABLE `theater` (
	`theater_id`	INT UNSIGNED	NOT NULL AUTO_INCREMENT	COMMENT '상영관ID',
	`theater_nm`	VARCHAR(100)	NULL	COMMENT '상영관명',
	`location`	VARCHAR(100)	NULL	COMMENT '위치',
	`created_by`	VARCHAR(20)	NULL	COMMENT '작성자',
	`created_at`	DATETIME	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '작성일',
	`updated_by`	VARCHAR(20)	NULL	COMMENT '수정자',
	`updated_at`	DATETIME	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '수정일',
	PRIMARY KEY (`theater_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `screening`;

CREATE TABLE `screening` (
	`screening_id`	INT UNSIGNED	NOT NULL AUTO_INCREMENT	COMMENT '상영시간표ID',
	`movie_id`	INT UNSIGNED	NOT NULL	COMMENT '영화ID',
	`theater_id`	INT UNSIGNED	NOT NULL	COMMENT '상영관ID',
	`show_date`	DATE	NOT NULL	COMMENT '상영일자',
	`start_time`	TIME	NOT NULL	COMMENT '상영시작시각',
	`end_time`	TIME	NOT NULL	COMMENT '상영종료시각',
	`created_by`	VARCHAR(20)	NULL	COMMENT '작성자',
	`created_at`	DATETIME	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '작성일',
	`updated_by`	VARCHAR(20)	NULL	COMMENT '수정자',
	`updated_at`	DATETIME	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '수정일',
	PRIMARY KEY (`screening_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `seat`;

CREATE TABLE `seat` (
	`seat_id`	INT UNSIGNED	NOT NULL AUTO_INCREMENT	COMMENT '좌석ID',
	`seat_type_cd`	VARCHAR(20)	NULL	COMMENT '좌석유형코드[ENUM]',
	`seat_nm`	VARCHAR(20)	NULL	COMMENT '좌석명',
	`created_by`	VARCHAR(20)	NULL	COMMENT '작성자',
	`created_at`	DATETIME	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '작성일',
	`updated_by`	VARCHAR(20)	NULL	COMMENT '수정자',
	`updated_at`	DATETIME	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '수정일',
	PRIMARY KEY (`seat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
	`user_id`	INT UNSIGNED	NOT NULL AUTO_INCREMENT	COMMENT '회원ID',
	`user_nm`	VARCHAR(20)	NULL	COMMENT '회원명',
	`birth_date`	DATE	NULL	COMMENT '생년월일',
	`created_by`	VARCHAR(20)	NULL	COMMENT '작성자',
	`created_at`	DATETIME	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '작성일',
	`updated_by`	VARCHAR(20)	NULL	COMMENT '수정자',
	`updated_at`	DATETIME	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '수정일',
	PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `ticket`;

CREATE TABLE `ticket` (
	`ticket_id`	INT UNSIGNED	NOT NULL AUTO_INCREMENT	COMMENT '예매ID',
	`user_id`	VARCHAR(20)	NOT NULL	COMMENT '회원ID',
	`seat_id`	VARCHAR(20)	NOT NULL	COMMENT '좌석ID',
	`screening_id`	VARCHAR(20)	NOT NULL	COMMENT '상영시간표ID',
	`created_by`	VARCHAR(20)	NULL	COMMENT '작성자',
	`created_at`	DATETIME	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '작성일',
	`updated_by`	VARCHAR(20)	NULL	COMMENT '수정자',
	`updated_at`	DATETIME	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '수정일',
	PRIMARY KEY (`ticket_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


/* INDEX 추가 */
/* movie */
-- 영화 ID (조인 키)
CREATE INDEX idx_movie_movie_id ON movie (movie_id);
-- 제목 + 장르 복합 인덱스
CREATE INDEX idx_movie_title_genre ON movie (title, genre_cd);
-- 개봉일 인덱스 (정렬 조건)
CREATE INDEX idx_movie_rlse_date ON movie (rlse_date);

/* screening */
-- 상영시간표 ID (조인 키)
CREATE INDEX idx_screening_screening_id ON screening (screening_id);
-- 영화 ID (조인 키)
CREATE INDEX idx_screening_movie_id ON screening (movie_id);
-- 상영관 ID (조인 키)
CREATE INDEX idx_screening_theater_id ON screening (theater_id);
-- 시작 시각 인덱스 (정렬 조건)
CREATE INDEX idx_screening_start_time ON screening (start_time);

/* theater */
-- 상영관 ID (조인 키)
CREATE INDEX idx_theater_theater_id ON theater (theater_id);

/* seat */
-- 좌석 ID (조인 키)
CREATE INDEX idx_seat_seat_id ON seat (seat_id);

/* ticket */
-- 예매 ID (조인 키)
CREATE INDEX idx_ticket_ticket_id ON ticket (ticket_id);
-- 좌석 ID (조인 키)
CREATE INDEX idx_ticket_seat_id ON ticket (seat_id);
-- 회원 ID (조인 키)
CREATE INDEX idx_ticket_user_id ON ticket (user_id);
-- 상영시간표 ID (조인 키)
CREATE INDEX idx_ticket_screening_id ON ticket (screening_id);

/* user */
-- 회원 ID (조인 키)
CREATE INDEX idx_user_user_id ON `user` (user_id);