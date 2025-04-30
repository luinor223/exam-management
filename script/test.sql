SELECT * FROM khach_hang WHERE loai_kh = 'Đơn vị';

SELECT * FROM phieu_dang_ky pdk JOIN hoa_don hd ON pdk.ma_pdk = hd.ma_pdk;

SELECT * FROM chi_tiet_phong_thi ct RIGHT JOIN phong p on ct.ma_phong = p.ma_phong
