-- DROP DATABASE IF EXISTS "ExamCenterDB";
-- CREATE DATABASE "ExamCenterDB"
--     WITH
--     OWNER = postgres
--     ENCODING = 'UTF8'
--     LOCALE_PROVIDER = 'libc'
--     CONNECTION LIMIT = -1
--     IS_TEMPLATE = False;
--\connect "ExamCenterDB" postgres;
DROP SCHEMA IF EXISTS Nhom01 CASCADE;
CREATE SCHEMA Nhom01;
SET search_path TO Nhom01;

CREATE SEQUENCE seq_thi_sinh START 1;
CREATE SEQUENCE seq_khach_hang START 1;
CREATE SEQUENCE seq_nhan_vien START 1;
CREATE SEQUENCE seq_phieu_dang_ky START 1;
CREATE SEQUENCE seq_phieu_du_thi START 1;
CREATE SEQUENCE seq_lich_thi START 1;
CREATE SEQUENCE seq_giam_thi START 1;
CREATE SEQUENCE seq_giam_sat_thi START 1;
CREATE SEQUENCE seq_chi_tiet_phong_thi START 1;
CREATE SEQUENCE seq_ket_qua START 1;
CREATE SEQUENCE seq_chung_chi START 1;
CREATE SEQUENCE seq_hoa_don START 1;
CREATE SEQUENCE seq_phieu_gia_han START 1;
CREATE SEQUENCE seq_chi_tiet_phieu_dk START 1;
CREATE SEQUENCE seq_phong START 1;

-- Bảng Thí Sinh
CREATE TABLE thi_sinh (
    ma_ts TEXT PRIMARY KEY DEFAULT 'TS' || LPAD(nextval('seq_thi_sinh')::TEXT, 6, '0'),
    ho_ten VARCHAR(100) NOT NULL,
    cccd CHAR(12) NOT NULL,
    ngay_sinh DATE,
    gioi_tinh VARCHAR(10) CHECK(gioi_tinh IN ('Nam', 'Nữ', 'Khác'))
);

-- Bảng Khách Hàng
CREATE TABLE khach_hang (
    ma_kh TEXT PRIMARY KEY DEFAULT 'KH' || LPAD(nextval('seq_khach_hang')::TEXT, 6, '0'),
    ho_ten VARCHAR(100) NOT NULL,
    cccd CHAR(12) NOT NULL,
    email VARCHAR(50) UNIQUE,
    sdt VARCHAR(15) UNIQUE,
    dia_chi TEXT,
    loai_kh VARCHAR(20) CHECK(loai_kh IN ('Cá nhân', 'Đơn vị'))
);

-- Bảng Nhân Viên
CREATE TABLE nhan_vien (
    ma_nv TEXT PRIMARY KEY DEFAULT 'NV' || LPAD(nextval('seq_nhan_vien')::TEXT, 6, '0'),
    ho_ten VARCHAR(100) NOT NULL,
    cccd CHAR(12) NOT NULL,
    loai_nv VARCHAR(20) CHECK(loai_nv IN ('tiếp nhận', 'kế toán', 'khảo thí', 'nhập liệu')),
    trang_thai VARCHAR(50)
);

-- Bảng Phiếu Đăng Ký
CREATE TABLE phieu_dang_ky (
    ma_pdk TEXT PRIMARY KEY DEFAULT 'PDK' || LPAD(nextval('seq_phieu_dang_ky')::TEXT, 6, '0'),
    trang_thai VARCHAR(50) CHECK(trang_thai IN ('Đã xác nhận', 'Chờ xét duyệt', 'Chờ xử lý', 'Chờ xếp lịch', 'Đã hủy')),
    ngay_lap DATE DEFAULT CURRENT_DATE,
    dia_chi_giao VARCHAR(100),
    ma_kh TEXT NOT NULL,
    nhan_vien_tao TEXT NOT NULL,
    FOREIGN KEY (ma_kh) REFERENCES khach_hang(ma_kh) ON DELETE CASCADE,
    FOREIGN KEY (nhan_vien_tao) REFERENCES nhan_vien(ma_nv) ON DELETE CASCADE
);

-- Bảng Chứng Chỉ
CREATE TABLE chung_chi (
    ma_cchi TEXT PRIMARY KEY DEFAULT 'CC' || LPAD(nextval('seq_chung_chi')::TEXT, 6, '0'),
    ten_chung_chi VARCHAR(100),
    thoi_gian_hieu_luc INT, --ngày (hay năm?)
    mo_ta TEXT,
    le_phi FLOAT
);

-- Bảng Phòng
CREATE TABLE phong (
    ma_phong TEXT PRIMARY KEY DEFAULT 'P' || LPAD(nextval('seq_phong')::TEXT, 6, '0'),
    ten_phong VARCHAR(50) NOT NULL,
    so_ghe INT
);

-- Bảng Lịch Thi
CREATE TABLE lich_thi (
    ma_lt TEXT PRIMARY KEY DEFAULT 'LT' || LPAD(nextval('seq_lich_thi')::TEXT, 6, '0'),
    ngay_gio_thi TIMESTAMP,
    thoi_luong_thi INT, --phút
    ma_cchi TEXT NOT NULL,
    FOREIGN KEY (ma_cchi) REFERENCES chung_chi(ma_cchi) ON DELETE CASCADE
);

-- Bảng Chi Tiết Phiếu Đăng Ký
CREATE TABLE chi_tiet_phieu_dk (
    ma_ctpdk TEXT PRIMARY KEY DEFAULT 'CTPDK' || LPAD(nextval('seq_chi_tiet_phieu_dk')::TEXT, 6, '0'),
    ma_pdk TEXT NOT NULL,
    ma_ts TEXT NOT NULL,
    ma_lt TEXT,

    FOREIGN KEY (ma_pdk) REFERENCES phieu_dang_ky(ma_pdk) ON DELETE CASCADE,
    FOREIGN KEY (ma_ts) REFERENCES thi_sinh(ma_ts) ON DELETE CASCADE,
    FOREIGN KEY (ma_lt) REFERENCES lich_thi(ma_lt) ON DELETE CASCADE
);

-- Bảng Giám Thị
CREATE TABLE giam_thi (
    ma_gt TEXT PRIMARY KEY DEFAULT 'GT' || LPAD(nextval('seq_giam_thi')::TEXT, 6, '0'),
    ho_ten VARCHAR(100) NOT NULL,
    sdt VARCHAR(15) UNIQUE,
    email VARCHAR(50)
);

-- Bảng Chi Tiết Phòng Thi
CREATE TABLE chi_tiet_phong_thi (
    ma_lt TEXT NOT NULL,
    ma_phong TEXT NOT NULL,
    ma_gt TEXT NOT NULL,
    so_luong_hien_tai INT,
    so_luong_toi_da INT,

    CHECK (so_luong_hien_tai <= chi_tiet_phong_thi.so_luong_toi_da),
    PRIMARY KEY (ma_lt, ma_phong),
    FOREIGN KEY (ma_phong) REFERENCES phong(ma_phong) ON DELETE CASCADE,
    FOREIGN KEY (ma_lt) REFERENCES lich_thi(ma_lt) ON DELETE CASCADE,
    FOREIGN KEY (ma_gt) REFERENCES giam_thi(ma_gt) ON DELETE CASCADE
);

-- Bảng Phiếu Dự Thi
CREATE TABLE phieu_du_thi (
    ma_lt TEXT NOT NULL, -- Mã lịch thi
    sbd CHAR(6),
    ma_phong TEXT NOT NULL,
    ngay_cap DATE DEFAULT CURRENT_DATE,
    ma_ctpdk TEXT,

    PRIMARY KEY (ma_lt, sbd),
    FOREIGN KEY (ma_ctpdk) REFERENCES chi_tiet_phieu_dk(ma_ctpdk) ON DELETE CASCADE,
    FOREIGN KEY (ma_lt) REFERENCES lich_thi(ma_lt) ON DELETE CASCADE,
    FOREIGN KEY (ma_lt, ma_phong) REFERENCES chi_tiet_phong_thi(ma_lt, ma_phong) ON DELETE CASCADE
);

-- Bảng Kết Quả
CREATE TABLE ket_qua (
    ma_lt TEXT,
    sbd CHAR(6),
    diem INT,
    xep_loai VARCHAR(20),
    nhan_xet TEXT,
    ngay_cap_chung_chi DATE,
    ngay_het_han DATE,
    trang_thai VARCHAR(50) CHECK(trang_thai IN ('Đã cấp', 'Chưa cấp')),
    
    PRIMARY KEY (ma_lt, sbd),
    FOREIGN KEY (ma_lt, sbd) REFERENCES phieu_du_thi(ma_lt, sbd) ON DELETE CASCADE
);

-- Bảng Hóa Đơn
CREATE TABLE hoa_don (
    ma_hd TEXT PRIMARY KEY DEFAULT 'HD' || LPAD(nextval('seq_hoa_don')::TEXT, 6, '0'),
    tong_tien FLOAT,
    pt_thanh_toan VARCHAR(20) CHECK(pt_thanh_toan IN ('Tiền mặt', 'Chuyển khoản')),
    so_tien_giam FLOAT,
    ngay_lap DATE DEFAULT CURRENT_DATE,
    nhan_vien_tao TEXT,
    ma_pdk TEXT,
    ma_thanh_toan TEXT,

    FOREIGN KEY (nhan_vien_tao) REFERENCES nhan_vien(ma_nv) ON DELETE CASCADE,
    FOREIGN KEY (ma_pdk) REFERENCES phieu_dang_ky(ma_pdk) ON DELETE CASCADE
);

-- Bảng Phiếu Gia Hạn
CREATE TABLE phieu_gia_han (
    ma_pgh TEXT PRIMARY KEY DEFAULT 'PGH' || LPAD(nextval('seq_phieu_gia_han')::TEXT, 6, '0'),
    loai_gh VARCHAR(20),
    phi_gh FLOAT,
    nhan_vien_tao TEXT,
    da_thanh_toan BOOLEAN,
    ma_ctpdk TEXT,

    FOREIGN KEY (nhan_vien_tao) REFERENCES nhan_vien(ma_nv) ON DELETE CASCADE,
    FOREIGN KEY (ma_ctpdk) REFERENCES chi_tiet_phieu_dk(ma_ctpdk) ON DELETE CASCADE
);

