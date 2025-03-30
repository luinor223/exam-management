-- DROP DATABASE IF EXISTS "ExamCenterDB";
-- CREATE DATABASE "ExamCenterDB"
--     WITH
--     OWNER = postgres
--     ENCODING = 'UTF8'
--     LOCALE_PROVIDER = 'libc'
--     CONNECTION LIMIT = -1
--     IS_TEMPLATE = False;
\connect "ExamCenterDB" postgres;

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
DROP TABLE IF EXISTS thi_sinh CASCADE;
CREATE TABLE thi_sinh (
    ma_ts TEXT PRIMARY KEY DEFAULT 'TS' || LPAD(nextval('seq_thi_sinh')::TEXT, 6, '0'),
    ho_ten VARCHAR(100) NOT NULL,
    cccd CHAR(12) NOT NULL,
    ngay_sinh DATE,
    gioi_tinh VARCHAR(10) CHECK(gioi_tinh IN ('Nam', 'Nữ', 'Khác'))
);

-- Bảng Khách Hàng
DROP TABLE IF EXISTS khach_hang CASCADE;
CREATE TABLE khach_hang (
    ma_kh TEXT PRIMARY KEY DEFAULT 'KH' || LPAD(nextval('seq_khach_hang')::TEXT, 6, '0'),
    ho_ten VARCHAR(100) NOT NULL,
    cccd CHAR(12) NOT NULL,
    sdt VARCHAR(15) UNIQUE,
    dia_chi TEXT,
    loai_kh VARCHAR(20) CHECK(loai_kh IN ('Cá nhân', 'Đơn vị'))
);

-- Bảng Nhân Viên
DROP TABLE IF EXISTS nhan_vien CASCADE;
CREATE TABLE nhan_vien (
    ma_nv TEXT PRIMARY KEY DEFAULT 'NV' || LPAD(nextval('seq_nhan_vien')::TEXT, 6, '0'),
    ho_ten VARCHAR(100) NOT NULL,
    cccd CHAR(12) NOT NULL,
    loai_nv VARCHAR(20) CHECK(loai_nv IN ('tiếp nhận', 'kế toán', 'khảo thí', 'nhập liệu')),
    trang_thai VARCHAR(50)
);

-- Bảng Phiếu Đăng Ký
DROP TABLE IF EXISTS phieu_dang_ky CASCADE;
CREATE TABLE phieu_dang_ky (
    ma_pdk TEXT PRIMARY KEY DEFAULT 'PDK' || LPAD(nextval('seq_phieu_dang_ky')::TEXT, 6, '0'),
    han_nop DATE,
    trang_thai VARCHAR(50),
    ngay_lap DATE DEFAULT CURRENT_DATE,
    dia_chi_giao VARCHAR(100),
    ma_kh TEXT NOT NULL,
    nhan_vien_tao TEXT NOT NULL,
    FOREIGN KEY (ma_kh) REFERENCES khach_hang(ma_kh) ON DELETE CASCADE,
    FOREIGN KEY (nhan_vien_tao) REFERENCES nhan_vien(ma_nv) ON DELETE CASCADE
);

-- Bảng Chứng Chỉ
DROP TABLE IF EXISTS chung_chi CASCADE;
CREATE TABLE chung_chi (
    ma_cchi TEXT PRIMARY KEY DEFAULT 'CC' || LPAD(nextval('seq_chung_chi')::TEXT, 6, '0'),
    ten_chung_chi VARCHAR(100),
    thoi_gian_hieu_luc INT, --ngày (hay năm?)
    mo_ta TEXT,
    le_phi FLOAT
);

-- Bảng Phòng
DROP TABLE IF EXISTS phong CASCADE;
CREATE TABLE phong (
    ma_phong TEXT PRIMARY KEY DEFAULT 'P' || LPAD(nextval('seq_phong')::TEXT, 6, '0'),
    ten_phong VARCHAR(50) NOT NULL,
    suc_chua_toi_da INT,
    trang_thai VARCHAR(50)
);

-- Bảng Lịch Thi
DROP TABLE IF EXISTS lich_thi CASCADE;
CREATE TABLE lich_thi (
    ma_lt TEXT PRIMARY KEY DEFAULT 'LT' || LPAD(nextval('seq_lich_thi')::TEXT, 6, '0'),
    ngay_gio_thi TIMESTAMP,
    thoi_luong_thi INT, --phút
    so_luong_thi_sinh INT,
    ma_cchi TEXT NOT NULL,
    FOREIGN KEY (ma_cchi) REFERENCES chung_chi(ma_cchi) ON DELETE CASCADE
);

-- Bảng Chi Tiết Phiếu Đăng Ký
DROP TABLE IF EXISTS chi_tiet_phieu_dk CASCADE;
CREATE TABLE chi_tiet_phieu_dk (
    ma_ctpdk TEXT PRIMARY KEY DEFAULT 'CTPDK' || LPAD(nextval('seq_chi_tiet_phieu_dk')::TEXT, 6, '0'),
    ma_pdk TEXT NOT NULL,
    ma_ts TEXT NOT NULL,
    ma_lt TEXT NOT NULL,

    FOREIGN KEY (ma_pdk) REFERENCES phieu_dang_ky(ma_pdk) ON DELETE CASCADE,
    FOREIGN KEY (ma_ts) REFERENCES thi_sinh(ma_ts) ON DELETE CASCADE,
    FOREIGN KEY (ma_lt) REFERENCES lich_thi(ma_lt) ON DELETE CASCADE
);

-- Bảng Chi Tiết Phòng Thi
DROP TABLE IF EXISTS chi_tiet_phong_thi CASCADE;
CREATE TABLE chi_tiet_phong_thi (
    ma_ctpt TEXT PRIMARY KEY DEFAULT 'CTPT' || LPAD(nextval('seq_chi_tiet_phong_thi')::TEXT, 6, '0'),
    ma_phong TEXT NOT NULL,
    ma_lt TEXT NOT NULL,
    so_luong_hien_tai INT,
    so_luong_toi_da INT,

    CHECK (so_luong_hien_tai <= chi_tiet_phong_thi.so_luong_toi_da),
    FOREIGN KEY (ma_phong) REFERENCES phong(ma_phong) ON DELETE CASCADE,
    FOREIGN KEY (ma_lt) REFERENCES lich_thi(ma_lt) ON DELETE CASCADE
);

-- Bảng Giám Thị
DROP TABLE IF EXISTS giam_thi CASCADE;
CREATE TABLE giam_thi (
    ma_gt TEXT PRIMARY KEY DEFAULT 'GT' || LPAD(nextval('seq_giam_thi')::TEXT, 6, '0'),
    ho_ten VARCHAR(100) NOT NULL,
    sdt VARCHAR(15) UNIQUE,
    email VARCHAR(50)
);

-- Bảng Giám Sát Thi
DROP TABLE IF EXISTS giam_sat_thi CASCADE;
CREATE TABLE giam_sat_thi (
    ma_lt TEXT,
    ma_gt TEXT,

    PRIMARY KEY (ma_lt, ma_gt),
    FOREIGN KEY (ma_lt) REFERENCES lich_thi(ma_lt),
    FOREIGN KEY (ma_gt) REFERENCES giam_thi(ma_gt)
);

-- Bảng Phiếu Dự Thi
DROP TABLE IF EXISTS phieu_du_thi CASCADE;
CREATE TABLE phieu_du_thi (
    ma_pdt TEXT PRIMARY KEY DEFAULT 'PDT' || LPAD(nextval('seq_phieu_du_thi')::TEXT, 6, '0'),
    sbd CHAR(6),
    ngay_cap DATE DEFAULT CURRENT_DATE,
    ma_ctpdk TEXT,

    FOREIGN KEY (ma_ctpdk) REFERENCES chi_tiet_phieu_dk(ma_ctpdk) ON DELETE CASCADE
);

-- Bảng Kết Quả
DROP TABLE IF EXISTS ket_qua CASCADE;
CREATE TABLE ket_qua (
    ma_kq TEXT PRIMARY KEY DEFAULT 'KQ' || LPAD(nextval('seq_ket_qua')::TEXT, 6, '0'),
    ngay_gio_thi DATE,
    diem INT,
    xep_loai VARCHAR(20),
    nhan_xet TEXT,
    ngay_cap_chung_chi DATE,
    ngay_het_han DATE,
    trang_thai VARCHAR(50),
    ma_pdt TEXT,

    FOREIGN KEY (ma_pdt) REFERENCES phieu_du_thi(ma_pdt) ON DELETE CASCADE
);

-- Bảng Hóa Đơn
DROP TABLE IF EXISTS hoa_don CASCADE;
CREATE TABLE hoa_don (
    ma_hd TEXT PRIMARY KEY DEFAULT 'HD' || LPAD(nextval('seq_hoa_don')::TEXT, 6, '0'),
    tong_tien FLOAT,
    pt_thanh_toan VARCHAR(20) CHECK(pt_thanh_toan IN ('Tiền mặt', 'Chuyển khoản')),
    so_tien_giam FLOAT,
    ngay_lap DATE DEFAULT CURRENT_DATE,
    nhan_vien_tao TEXT,
    ma_pdk TEXT,

    FOREIGN KEY (nhan_vien_tao) REFERENCES nhan_vien(ma_nv) ON DELETE CASCADE,
    FOREIGN KEY (ma_pdk) REFERENCES phieu_dang_ky(ma_pdk) ON DELETE CASCADE
);

-- Bảng Phiếu Gia Hạn
DROP TABLE IF EXISTS phieu_gia_han CASCADE;
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

