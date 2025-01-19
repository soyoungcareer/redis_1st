/* theater */
insert into theater (theater_nm, location, created_by) 
values
('1관', '지하1층', 'admin'),
('2관', '함장실', 'admin'),
('3관', '갑판', 'admin'),
('4관', '조타실', 'admin'),
('5관', '기관실', 'admin');


/* seat */
insert into seat (seat_type_cd, seat_nm, created_by)
values
('ACCESSIBLE', 'A1', 'admin'),
('ACCESSIBLE', 'A2', 'admin'),
('ACCESSIBLE', 'A3', 'admin'),
('ACCESSIBLE', 'A4', 'admin'),
('ACCESSIBLE', 'A5', 'admin'),
('RECLINING', 'B1', 'admin'),
('RECLINING', 'B2', 'admin'),
('RECLINING', 'B3', 'admin'),
('RECLINING', 'B4', 'admin'),
('RECLINING', 'B5', 'admin'),
('STANDARD', 'C1', 'admin'),
('STANDARD', 'C2', 'admin'),
('STANDARD', 'C3', 'admin'),
('STANDARD', 'C4', 'admin'),
('STANDARD', 'C5', 'admin'),
('PREMIUM', 'D1', 'admin'),
('PREMIUM', 'D2', 'admin'),
('PREMIUM', 'D3', 'admin'),
('PREMIUM', 'D4', 'admin'),
('PREMIUM', 'D5', 'admin'),
('SWEET_BOX', 'E1', 'admin'),
('SWEET_BOX', 'E2', 'admin'),
('VIP', 'E3', 'admin'),
('SWEET_BOX', 'E4', 'admin'),
('SWEET_BOX', 'E5', 'admin');
