SET search_path TO Nhom01;

INSERT INTO thi_sinh (ho_ten, cccd, ngay_sinh, gioi_tinh) VALUES
('Nguyễn Văn An', '123456789012', '2000-01-15', 'Nam'),
('Trần Thị Bình', '123456789013', '1999-05-20', 'Nữ'),
('Lê Văn Cường', '123456789014', '2001-03-10', 'Nam'),
('Phạm Thị Dung', '123456789015', '1998-07-25', 'Nữ'),
('Hoàng Văn Em', '123456789016', '2002-11-30', 'Nam'),
('Ngô Thị Phương', '123456789017', '1997-09-05', 'Nữ'),
('Đặng Văn Giang', '123456789018', '2000-12-12', 'Nam'),
('Vũ Thị Hương', '123456789019', '1999-04-18', 'Nữ'),
('Bùi Văn Inh', '123456789020', '2001-08-22', 'Nam'),
('Đỗ Thị Kim', '123456789021', '1998-02-14', 'Nữ'),
('Lý Văn Long', '123456789022', '2002-06-19', 'Nam'),
('Mai Thị Ngọc', '123456789023', '1997-10-08', 'Nữ');

INSERT INTO khach_hang (ho_ten, cccd, email, sdt, dia_chi, loai_kh) VALUES
('Nguyễn Văn Anh', '234567890123', 'anh.nguyen@email.com', '0901234567', 'Quận 1, TP.HCM', 'Cá nhân'),
('Trần Thị Bích', '234567890124', 'bich.tran@email.com', '0901234568', 'Quận 2, TP.HCM', 'Cá nhân'),
('Công ty TNHH ABC', '234567890125', 'contact@abc.com', '0901234569', 'Quận 3, TP.HCM', 'Đơn vị'),
('Lê Văn Cường', '234567890126', 'cuong.le@email.com', '0901234570', 'Quận 4, TP.HCM', 'Cá nhân'),
('Trường Đại học XYZ', '234567890127', 'info@xyz.edu.vn', '0901234571', 'Quận 5, TP.HCM', 'Đơn vị'),
('Phạm Thị Dung', '234567890128', 'dung.pham@email.com', '0901234572', 'Quận 6, TP.HCM', 'Cá nhân'),
('Công ty Cổ phần DEF', '234567890129', 'info@def.com', '0901234573', 'Quận 7, TP.HCM', 'Đơn vị'),
('Hoàng Văn Em', '234567890130', 'em.hoang@email.com', '0901234574', 'Quận 8, TP.HCM', 'Cá nhân'),
('Trường THPT Lê Quý Đôn', '234567890131', 'contact@lqd.edu.vn', '0901234575', 'Quận 9, TP.HCM', 'Đơn vị'),
('Ngô Thị Phương', '234567890132', 'phuong.ngo@email.com', '0901234576', 'Quận 10, TP.HCM', 'Cá nhân'),
('Công ty TNHH GHI', '234567890133', 'contact@ghi.com', '0901234577', 'Quận 11, TP.HCM', 'Đơn vị'),
('Đặng Văn Giang', '234567890134', 'giang.dang@email.com', '0901234578', 'Quận 12, TP.HCM', 'Cá nhân');

INSERT INTO nhan_vien (ho_ten, cccd, loai_nv, trang_thai) VALUES
('Nguyễn Thị Hoa', '345678901234', 'tiếp nhận', 'Đang làm việc'),
('Trần Văn Bình', '345678901235', 'kế toán', 'Đang làm việc'),
('Lê Thị Cúc', '345678901236', 'khảo thí', 'Đang làm việc'),
('Phạm Văn Dũng', '345678901237', 'nhập liệu', 'Đang làm việc'),
('Hoàng Thị Em', '345678901238', 'tiếp nhận', 'Đang làm việc'),
('Ngô Văn Phúc', '345678901239', 'kế toán', 'Đang làm việc'),
('Đặng Thị Giang', '345678901240', 'khảo thí', 'Đang làm việc'),
('Vũ Văn Hùng', '345678901241', 'nhập liệu', 'Đang làm việc'),
('Bùi Thị Ích', '345678901242', 'tiếp nhận', 'Đang làm việc'),
('Đỗ Văn Khánh', '345678901243', 'kế toán', 'Đang làm việc'),
('Lý Thị Lan', '345678901244', 'khảo thí', 'Đang làm việc'),
('Mai Văn Nam', '345678901245', 'nhập liệu', 'Đang làm việc');

INSERT INTO chung_chi (ten_chung_chi, thoi_gian_hieu_luc, mo_ta, le_phi) VALUES
('TOEIC', 730, 'Chứng chỉ Tiếng Anh giao tiếp quốc tế', 1500000),
('IELTS', 730, 'Chứng chỉ Hệ thống kiểm tra Tiếng Anh quốc tế', 4500000),
('TOEFL', 730, 'Chứng chỉ Kiểm tra Tiếng Anh như một ngoại ngữ', 5000000),
('Cambridge', 1095, 'Chứng chỉ Tiếng Anh Cambridge', 4000000),
('HSK', 730, 'Chứng chỉ Tiếng Trung Quốc', 2000000),
('JLPT', 730, 'Chứng chỉ Năng lực Tiếng Nhật', 1800000),
('DELF', 1095, 'Chứng chỉ Tiếng Pháp', 2500000),
('DELE', 1095, 'Chứng chỉ Tiếng Tây Ban Nha', 2300000),
('TOPIK', 730, 'Chứng chỉ Tiếng Hàn Quốc', 1900000),
('Goethe', 1095, 'Chứng chỉ Tiếng Đức', 2700000),
('CILS', 1095, 'Chứng chỉ Tiếng Ý', 2400000),
('TRKI', 1095, 'Chứng chỉ Tiếng Nga', 2200000);

INSERT INTO phong (ten_phong, so_ghe) VALUES
('Phòng A101', 30),
('Phòng A102', 25),
('Phòng A103', 35),
('Phòng B201', 40),
('Phòng B202', 30),
('Phòng B203', 45),
('Phòng C301', 50),
('Phòng C302', 35),
('Phòng C303', 40),
('Phòng D401', 30),
('Phòng D402', 25),
('Phòng D403', 35);

INSERT INTO giam_thi (ho_ten, sdt, email) VALUES
('Nguyễn Văn Giám', '0912345678', 'giam.nguyen@email.com'),
('Trần Thị Thị', '0912345679', 'thi.tran@email.com'),
('Lê Văn Sát', '0912345680', 'sat.le@email.com'),
('Phạm Thị Kiểm', '0912345681', 'kiem.pham@email.com'),
('Hoàng Văn Tra', '0912345682', 'tra.hoang@email.com'),
('Ngô Thị Soát', '0912345683', 'soat.ngo@email.com'),
('Đặng Văn Xét', '0912345684', 'xet.dang@email.com'),
('Vũ Thị Giám', '0912345685', 'giam.vu@email.com'),
('Bùi Văn Thị', '0912345686', 'thi.bui@email.com'),
('Đỗ Thị Sát', '0912345687', 'sat.do@email.com'),
('Lý Văn Kiểm', '0912345688', 'kiem.ly@email.com'),
('Mai Thị Tra', '0912345689', 'tra.mai@email.com');

INSERT INTO lich_thi (ngay_gio_thi, thoi_luong_thi, ma_cchi) VALUES
-- Past exam schedules
('2023-06-15 08:00:00', 120, 'CC000001'),  -- TOEIC
('2023-06-15 13:00:00', 180, 'CC000002'),  -- IELTS
('2023-06-16 08:00:00', 240, 'CC000003'),  -- TOEFL
('2023-06-16 13:00:00', 150, 'CC000004'),  -- Cambridge
('2023-06-17 08:00:00', 120, 'CC000005'),  -- HSK
('2023-06-17 13:00:00', 120, 'CC000006'),  -- JLPT
('2023-06-18 08:00:00', 150, 'CC000007'),  -- DELF
('2023-06-18 13:00:00', 150, 'CC000008'),  -- DELE
('2023-06-19 08:00:00', 120, 'CC000009'),  -- TOPIK
('2023-06-19 13:00:00', 180, 'CC000010'),  -- Goethe
('2023-06-20 08:00:00', 150, 'CC000011'),  -- CILS
('2023-06-20 13:00:00', 180, 'CC000012'),  -- TRKI

-- Future exam schedules
('2025-06-15 08:00:00', 120, 'CC000001'),  -- TOEIC
('2025-06-15 13:00:00', 180, 'CC000002'),  -- IELTS
('2025-06-16 08:00:00', 240, 'CC000003'),  -- TOEFL
('2025-06-16 13:00:00', 150, 'CC000004'),  -- Cambridge
('2025-06-17 08:00:00', 120, 'CC000005'),  -- HSK
('2025-06-17 13:00:00', 120, 'CC000006'),  -- JLPT
('2025-06-18 08:00:00', 150, 'CC000007'),  -- DELF
('2025-06-18 13:00:00', 150, 'CC000008'),  -- DELE
('2025-06-19 08:00:00', 120, 'CC000009'),  -- TOPIK
('2025-06-19 13:00:00', 180, 'CC000010'),  -- Goethe
('2025-06-20 08:00:00', 150, 'CC000011'),  -- CILS
('2025-06-20 13:00:00', 180, 'CC000012'),  -- TRKI
('2025-07-15 08:00:00', 120, 'CC000001'),  -- TOEIC
('2025-07-15 13:00:00', 180, 'CC000002'),  -- IELTS
('2025-07-16 08:00:00', 240, 'CC000003');  -- TOEFL

INSERT INTO phieu_dang_ky (trang_thai, ngay_lap, dia_chi_giao, ma_kh, nhan_vien_tao) VALUES
-- Past registrations
('Đã xác nhận', '2023-05-15', 'Quận 1, TP.HCM', 'KH000001', 'NV000001'),
('Đã xác nhận', '2023-05-16', 'Quận 2, TP.HCM', 'KH000002', 'NV000001'),
('Đã xác nhận', '2023-05-17', 'Quận 3, TP.HCM', 'KH000003', 'NV000002'),
('Đã xác nhận', '2023-05-18', 'Quận 4, TP.HCM', 'KH000004', 'NV000002'),
('Đã xác nhận', '2023-05-19', 'Quận 5, TP.HCM', 'KH000005', 'NV000003'),
('Đã xác nhận', '2023-05-20', 'Quận 6, TP.HCM', 'KH000006', 'NV000003'),
('Đã xác nhận', '2023-05-21', 'Quận 7, TP.HCM', 'KH000007', 'NV000004'),
('Đã xác nhận', '2023-05-22', 'Quận 8, TP.HCM', 'KH000008', 'NV000004'),
('Đã xác nhận', '2023-05-23', 'Quận 9, TP.HCM', 'KH000009', 'NV000005'),
('Đã xác nhận', '2023-05-24', 'Quận 10, TP.HCM', 'KH000010', 'NV000005'),
('Đã xác nhận', '2023-05-25', 'Quận 11, TP.HCM', 'KH000011', 'NV000006'),
('Đã xác nhận', '2023-05-26', 'Quận 12, TP.HCM', 'KH000012', 'NV000006'),

-- Future registrations
('Đã xác nhận', '2025-04-26', 'Quận 1, TP.HCM', 'KH000001', 'NV000001'),
('Đã xác nhận', '2025-04-30', 'Quận 2, TP.HCM', 'KH000002', 'NV000001'),
('Chờ xử lý', '2025-04-29', 'Quận 3, TP.HCM', 'KH000003', 'NV000002'),
('Đã xác nhận', '2025-04-20', 'Quận 4, TP.HCM', 'KH000004', 'NV000002'),
('Chờ xử lý', '2025-04-25', 'Quận 5, TP.HCM', 'KH000005', 'NV000003'),
('Đã xác nhận', '2025-04-30', 'Quận 6, TP.HCM', 'KH000006', 'NV000003'),
('Chờ xử lý', '2025-04-29', 'Quận 7, TP.HCM', 'KH000007', 'NV000004'),
('Đã xác nhận', '2025-04-30', 'Quận 8, TP.HCM', 'KH000008', 'NV000004'),
('Chờ xử lý', '2025-04-29', 'Quận 9, TP.HCM', 'KH000009', 'NV000005'),
('Chờ xử lý', '2025-04-30', 'Quận 10, TP.HCM', 'KH000010', 'NV000005'),
('Chờ xử lý', '2025-04-29', 'Quận 11, TP.HCM', 'KH000011', 'NV000006'),
('Chờ xử lý', '2025-04-30', 'Quận 12, TP.HCM', 'KH000012', 'NV000006'),
('Chờ xếp lịch', CURRENT_DATE, 'Quận 3, TP.HCM', 'KH000003', 'NV000001'),
('Chờ xếp lịch', CURRENT_DATE, 'Quận 5, TP.HCM', 'KH000005', 'NV000001'),
('Chờ xếp lịch', CURRENT_DATE, 'Quận 7, TP.HCM', 'KH000007', 'NV000001'),
('Chờ xếp lịch', CURRENT_DATE, 'Quận 9, TP.HCM', 'KH000009', 'NV000001');

INSERT INTO chi_tiet_phieu_dk (ma_pdk, ma_ts, ma_lt) VALUES
-- Past registrations
('PDK000001', 'TS000001', 'LT000001'),
('PDK000002', 'TS000002', 'LT000002'),
('PDK000003', 'TS000003', 'LT000003'),
('PDK000004', 'TS000004', 'LT000004'),
('PDK000005', 'TS000005', 'LT000005'),
('PDK000006', 'TS000006', 'LT000006'),
('PDK000007', 'TS000007', 'LT000007'),
('PDK000008', 'TS000008', 'LT000008'),
('PDK000009', 'TS000009', 'LT000009'),
('PDK000010', 'TS000010', 'LT000010'),
('PDK000011', 'TS000011', 'LT000011'),
('PDK000012', 'TS000012', 'LT000012'),

-- Future registrations (using LT000013 to LT000024 for future exam schedules)
('PDK000013', 'TS000001', 'LT000013'),
('PDK000014', 'TS000002', 'LT000014'),
('PDK000015', 'TS000003', 'LT000015'),
('PDK000016', 'TS000004', 'LT000016'),
('PDK000017', 'TS000005', 'LT000017'),
('PDK000017', 'TS000004', 'LT000017'),
('PDK000017', 'TS000003', 'LT000017'),
('PDK000017', 'TS000002', 'LT000017'),
('PDK000018', 'TS000006', 'LT000018'),
('PDK000019', 'TS000007', 'LT000019'),
('PDK000020', 'TS000008', 'LT000020'),
('PDK000021', 'TS000009', 'LT000021'),
('PDK000022', 'TS000010', 'LT000022'),
('PDK000022', 'TS000010', 'LT000021'),
('PDK000023', 'TS000011', 'LT000023'),
('PDK000024', 'TS000012', 'LT000024'),
('PDK000025', 'TS000001', NULL),
('PDK000025', 'TS000002', NULL),
('PDK000025', 'TS000003', NULL),
('PDK000026', 'TS000004', NULL),
('PDK000026', 'TS000005', NULL),
('PDK000026', 'TS000006', NULL),
('PDK000026', 'TS000007', NULL),
('PDK000026', 'TS000008', NULL),
('PDK000027', 'TS000009', NULL),
('PDK000027', 'TS000010', NULL),
('PDK000028', 'TS000001', NULL),
('PDK000028', 'TS000003', NULL),
('PDK000028', 'TS000005', NULL),
('PDK000028', 'TS000007', NULL);

INSERT INTO chi_tiet_phong_thi (ma_phong, ma_lt, ma_gt, so_luong_hien_tai, so_luong_toi_da) VALUES
-- Past exam room allocations
('P000001', 'LT000001', 'GT000001', 25, 30),
('P000002', 'LT000002', 'GT000003', 20, 25),
('P000003', 'LT000003', 'GT000005', 30, 35),
('P000004', 'LT000004', 'GT000007', 25, 40),
('P000005', 'LT000005', 'GT000009', 20, 30),
('P000006', 'LT000006', 'GT000011', 25, 45),
('P000007', 'LT000007', 'GT000001', 15, 50),
('P000008', 'LT000008', 'GT000003', 20, 35),
('P000009', 'LT000009', 'GT000005', 25, 40),
('P000010', 'LT000010', 'GT000007', 15, 30),
('P000011', 'LT000011', 'GT000009', 10, 25),
('P000012', 'LT000012', 'GT000011', 15, 35),

-- Future exam room allocations
('P000001', 'LT000013', 'GT000001', 0, 30),
('P000002', 'LT000014', 'GT000003', 0, 25),
('P000003', 'LT000015', 'GT000005', 0, 35),
('P000004', 'LT000016', 'GT000007', 0, 40),
('P000005', 'LT000017', 'GT000009', 0, 30),
('P000006', 'LT000018', 'GT000011', 0, 45),
('P000007', 'LT000019', 'GT000001', 0, 50),
('P000008', 'LT000020', 'GT000003', 0, 35),
('P000009', 'LT000021', 'GT000005', 0, 40),
('P000010', 'LT000022', 'GT000007', 0, 30),
('P000011', 'LT000023', 'GT000009', 0, 25),
('P000012', 'LT000024', 'GT000011', 0, 35),
('P000001', 'LT000025', 'GT000001', 0, 3),
('P000002', 'LT000026', 'GT000003', 0, 10),
('P000003', 'LT000027', 'GT000005', 2, 5);

INSERT INTO phieu_du_thi (ma_lt, sbd, ma_phong, ngay_cap, ma_ctpdk) VALUES
-- Past exam tickets
('LT000001', 'A00001', 'P000001', '2023-06-01', 'CTPDK000001'),
('LT000002', 'A00002', 'P000002', '2023-06-01', 'CTPDK000002'),
('LT000003', 'A00003', 'P000003', '2023-06-02', 'CTPDK000003'),
('LT000004', 'A00004', 'P000004', '2023-06-02', 'CTPDK000004'),
('LT000005', 'A00005', 'P000005', '2023-06-03', 'CTPDK000005'),
('LT000006', 'A00006', 'P000006', '2023-06-03', 'CTPDK000006'),
('LT000007', 'A00007', 'P000007', '2023-06-04', 'CTPDK000007'),
('LT000008', 'A00008', 'P000008', '2023-06-04', 'CTPDK000008'),
('LT000009', 'A00009', 'P000009', '2023-06-05', 'CTPDK000009'),
('LT000010', 'A00010', 'P000010', '2023-06-05', 'CTPDK000010'),
('LT000011', 'A00011', 'P000011', '2023-06-06', 'CTPDK000011'),
('LT000012', 'A00012', 'P000012', '2023-06-06', 'CTPDK000012'),

-- Future exam tickets
('LT000013', 'B00001', 'P000001', '2024-06-01', 'CTPDK000013'),
('LT000014', 'B00002', 'P000002', '2024-06-01', 'CTPDK000014'),
('LT000015', 'B00003', 'P000003', '2024-06-02', 'CTPDK000015'),
('LT000016', 'B00004', 'P000004', '2024-06-02', 'CTPDK000016'),
('LT000017', 'B00005', 'P000005', '2024-06-03', 'CTPDK000017'),
('LT000018', 'B00006', 'P000006', '2024-06-03', 'CTPDK000018'),
('LT000019', 'B00007', 'P000007', '2024-06-04', 'CTPDK000019');

INSERT INTO ket_qua (ma_lt, sbd, diem, xep_loai, nhan_xet, ngay_cap_chung_chi, ngay_het_han, trang_thai) VALUES
-- Results for first batch of exams
('LT000001', 'A00001', 750, 'Khá', 'Đạt yêu cầu', '2023-06-30', '2025-06-30', 'Đã cấp'),
('LT000002', 'A00002', 6.5, 'Khá', 'Đạt yêu cầu', '2023-06-30', '2025-06-30', 'Đã cấp'),
('LT000003', 'A00003', 85, 'Giỏi', 'Đạt yêu cầu', '2023-07-01', '2025-07-01', 'Đã cấp'),
('LT000004', 'A00004', 170, 'Khá', 'Đạt yêu cầu', '2023-07-01', '2025-07-01', 'Đã cấp'),
('LT000005', 'A00005', 4, 'Trung bình', 'Đạt yêu cầu', '2023-07-02', '2025-07-02', 'Đã cấp'),
('LT000006', 'A00006', 3, 'Trung bình', 'Đạt yêu cầu', '2023-07-02', '2025-07-02', 'Đã cấp'),
('LT000007', 'A00007', 75, 'Khá', 'Đạt yêu cầu', '2023-07-03', '2026-07-03', 'Đã cấp'),
('LT000008', 'A00008', 80, 'Khá', 'Đạt yêu cầu', '2023-07-03', '2026-07-03', 'Chưa cấp'),
('LT000009', 'A00009', 5, 'Trung bình', 'Đạt yêu cầu', '2023-07-04', '2025-07-04', 'Chưa cấp'),
('LT000010', 'A00010', 65, 'Trung bình', 'Đạt yêu cầu', '2023-07-04', '2026-07-04', 'Chưa cấp'),
('LT000011', 'A00011', 70, 'Khá', 'Đạt yêu cầu', '2023-07-05', '2026-07-05', 'Chưa cấp'),
('LT000012', 'A00012', 75, 'Khá', 'Đạt yêu cầu', '2023-07-05', '2026-07-05', 'Chưa cấp');

INSERT INTO hoa_don (tong_tien, pt_thanh_toan, so_tien_giam, ngay_lap, nhan_vien_tao, ma_pdk, ma_thanh_toan) VALUES
-- past invoices
(1000000, 'Tiền mặt',     0,        '2023-05-15', 'NV000001', 'PDK000001', NULL),
(1000000, 'Tiền mặt',     0,        '2023-05-16', 'NV000001', 'PDK000002', NULL),
(1890000, 'Chuyển khoản', 210000,   '2023-05-17', 'NV000002', 'PDK000003', 'TT000010'),
(1000000, 'Tiền mặt',     0,        '2023-05-18', 'NV000002', 'PDK000004', NULL),
(900000, 'Chuyển khoản', 100000,   '2023-05-19', 'NV000003', 'PDK000005', 'TT000011'),
(1000000, 'Tiền mặt',     0,        '2023-05-20', 'NV000003', 'PDK000006', NULL),
(4500000, 'Chuyển khoản', 500000,   '2023-05-21', 'NV000004', 'PDK000007', 'TT000012'),
(1000000, 'Tiền mặt',     0,        '2023-05-22', 'NV000004', 'PDK000008', NULL),
(4230000, 'Chuyển khoản', 470000,   '2023-05-23', 'NV000005', 'PDK000009', 'TT000013'),
(1000000, 'Tiền mặt',     0,        '2023-05-24', 'NV000005', 'PDK000010', NULL),
(8100000, 'Chuyển khoản', 900000,   '2023-05-25', 'NV000006', 'PDK000011', 'TT000014'),
(1000000, 'Tiền mặt',     0,        '2023-05-26', 'NV000006', 'PDK000012', NULL),

(1000000, 'Tiền mặt', 0, '2025-04-26', 'NV000001', 'PDK000013', NULL),
(1000000, 'Tiền mặt', 0, '2025-04-30', 'NV000001', 'PDK000014', NULL),

(900000, 'Chuyển khoản', 100000, '2025-04-29', 'NV000003', 'PDK000015', NULL),
(1000000, 'Tiền mặt', 0, '2025-04-20', 'NV000002', 'PDK000016', NULL),
(1080000, 'Chuyển khoản', 120000, '2025-04-25', 'NV000002', 'PDK000017', NULL),
(1000000, 'Tiền mặt', 0, '2025-04-30', 'NV000003', 'PDK000018', NULL),
(1000000, 'Tiền mặt', 0, '2025-04-30', 'NV000004', 'PDK000020', NULL);

INSERT INTO phieu_gia_han (loai_gh, phi_gh, nhan_vien_tao, da_thanh_toan, ma_ctpdk) VALUES
-- Past extension forms
('Gia hạn 1 năm', 500000, 'NV000001', TRUE, 'CTPDK000001'),
('Gia hạn 2 năm', 900000, 'NV000001', TRUE, 'CTPDK000002'),
('Gia hạn 1 năm', 500000, 'NV000003', TRUE, 'CTPDK000003'),
('Gia hạn 2 năm', 900000, 'NV000003', TRUE, 'CTPDK000004'),
('Gia hạn 1 năm', 500000, 'NV000005', TRUE, 'CTPDK000005'),
('Gia hạn 2 năm', 900000, 'NV000005', TRUE, 'CTPDK000006'),
('Gia hạn 1 năm', 500000, 'NV000007', FALSE, 'CTPDK000007'),
('Gia hạn 2 năm', 900000, 'NV000007', FALSE, 'CTPDK000008'),
('Gia hạn 1 năm', 500000, 'NV000009', FALSE, 'CTPDK000009'),
('Gia hạn 2 năm', 900000, 'NV000009', FALSE, 'CTPDK000010'),
('Gia hạn 1 năm', 500000, 'NV000011', FALSE, 'CTPDK000011'),
('Gia hạn 2 năm', 900000, 'NV000011', FALSE, 'CTPDK000012'),

-- Additional extension forms
('Gia hạn 3 năm', 1200000, 'NV000001', TRUE, 'CTPDK000001'),
('Gia hạn 3 năm', 1200000, 'NV000001', TRUE, 'CTPDK000002'),
('Gia hạn 3 năm', 1200000, 'NV000003', TRUE, 'CTPDK000003'),
('Gia hạn 3 năm', 1200000, 'NV000003', TRUE, 'CTPDK000004'),
('Gia hạn 3 năm', 1200000, 'NV000005', TRUE, 'CTPDK000005'),
('Gia hạn 3 năm', 1200000, 'NV000005', TRUE, 'CTPDK000006'),
('Gia hạn 3 năm', 1200000, 'NV000007', FALSE, 'CTPDK000007'),
('Gia hạn 3 năm', 1200000, 'NV000007', FALSE, 'CTPDK000008'),
('Gia hạn 3 năm', 1200000, 'NV000009', FALSE, 'CTPDK000009'),
('Gia hạn 3 năm', 1200000, 'NV000009', FALSE, 'CTPDK000010'),
('Gia hạn 3 năm', 1200000, 'NV000011', FALSE, 'CTPDK000011'),
('Gia hạn 3 năm', 1200000, 'NV000011', FALSE, 'CTPDK000012');
