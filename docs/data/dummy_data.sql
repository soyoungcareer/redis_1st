/* theater */
insert into theater (theater_nm, location, created_by) 
values
('1관', '지하1층', 'admin'),
('2관', '함장실', 'admin'),
('3관', '갑판', 'admin'),
('4관', '조타실', 'admin'),
('5관', '기관실', 'admin');

/* user */
insert into `user` (user_nm, birth_date, created_by)
values
('차주영', '1990-01-01', 'admin'),
('카리나', '2020-01-01', 'admin');

/* seat */
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (1,'ACCESSIBLE','A1','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (1,'ACCESSIBLE','A2','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (1,'ACCESSIBLE','A3','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (1,'ACCESSIBLE','A4','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (1,'ACCESSIBLE','A5','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (1,'RECLINING','B1','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (1,'RECLINING','B2','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (1,'RECLINING','B3','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (1,'RECLINING','B4','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (1,'RECLINING','B5','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (1,'STANDARD','C1','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (1,'STANDARD','C2','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (1,'STANDARD','C3','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (1,'STANDARD','C4','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (1,'STANDARD','C5','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (1,'PREMIUM','D1','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (1,'PREMIUM','D2','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (1,'PREMIUM','D3','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (1,'PREMIUM','D4','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (1,'PREMIUM','D5','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (1,'SWEET_BOX','E1','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (1,'SWEET_BOX','E2','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (1,'VIP','E3','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (1,'SWEET_BOX','E4','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (1,'SWEET_BOX','E5','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (2,'ACCESSIBLE','A1','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (2,'ACCESSIBLE','A2','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (2,'ACCESSIBLE','A3','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (2,'ACCESSIBLE','A4','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (2,'ACCESSIBLE','A5','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (2,'RECLINING','B1','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (2,'RECLINING','B2','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (2,'RECLINING','B3','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (2,'RECLINING','B4','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (2,'RECLINING','B5','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (2,'STANDARD','C1','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (2,'STANDARD','C2','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (2,'STANDARD','C3','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (2,'STANDARD','C4','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (2,'STANDARD','C5','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (2,'PREMIUM','D1','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (2,'PREMIUM','D2','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (2,'PREMIUM','D3','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (2,'PREMIUM','D4','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (2,'PREMIUM','D5','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (2,'SWEET_BOX','E1','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (2,'SWEET_BOX','E2','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (2,'VIP','E3','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (2,'SWEET_BOX','E4','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (2,'SWEET_BOX','E5','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (3,'ACCESSIBLE','A1','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (3,'ACCESSIBLE','A2','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (3,'ACCESSIBLE','A3','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (3,'ACCESSIBLE','A4','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (3,'ACCESSIBLE','A5','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (3,'RECLINING','B1','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (3,'RECLINING','B2','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (3,'RECLINING','B3','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (3,'RECLINING','B4','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (3,'RECLINING','B5','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (3,'STANDARD','C1','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (3,'STANDARD','C2','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (3,'STANDARD','C3','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (3,'STANDARD','C4','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (3,'STANDARD','C5','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (3,'PREMIUM','D1','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (3,'PREMIUM','D2','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (3,'PREMIUM','D3','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (3,'PREMIUM','D4','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (3,'PREMIUM','D5','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (3,'SWEET_BOX','E1','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (3,'SWEET_BOX','E2','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (3,'VIP','E3','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (3,'SWEET_BOX','E4','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (3,'SWEET_BOX','E5','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (4,'ACCESSIBLE','A1','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (4,'ACCESSIBLE','A2','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (4,'ACCESSIBLE','A3','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (4,'ACCESSIBLE','A4','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (4,'ACCESSIBLE','A5','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (4,'RECLINING','B1','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (4,'RECLINING','B2','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (4,'RECLINING','B3','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (4,'RECLINING','B4','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (4,'RECLINING','B5','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (4,'STANDARD','C1','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (4,'STANDARD','C2','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (4,'STANDARD','C3','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (4,'STANDARD','C4','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (4,'STANDARD','C5','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (4,'PREMIUM','D1','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (4,'PREMIUM','D2','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (4,'PREMIUM','D3','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (4,'PREMIUM','D4','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (4,'PREMIUM','D5','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (4,'SWEET_BOX','E1','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (4,'SWEET_BOX','E2','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (4,'VIP','E3','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (4,'SWEET_BOX','E4','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (4,'SWEET_BOX','E5','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (5,'ACCESSIBLE','A1','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (5,'ACCESSIBLE','A2','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (5,'ACCESSIBLE','A3','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (5,'ACCESSIBLE','A4','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (5,'ACCESSIBLE','A5','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (5,'RECLINING','B1','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (5,'RECLINING','B2','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (5,'RECLINING','B3','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (5,'RECLINING','B4','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (5,'RECLINING','B5','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (5,'STANDARD','C1','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (5,'STANDARD','C2','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (5,'STANDARD','C3','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (5,'STANDARD','C4','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (5,'STANDARD','C5','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (5,'PREMIUM','D1','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (5,'PREMIUM','D2','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (5,'PREMIUM','D3','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (5,'PREMIUM','D4','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (5,'PREMIUM','D5','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (5,'SWEET_BOX','E1','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (5,'SWEET_BOX','E2','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (5,'VIP','E3','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (5,'SWEET_BOX','E4','admin');
insert into seat (theater_id, seat_type_cd, seat_name_cd, created_by) values (5,'SWEET_BOX','E5','admin');

