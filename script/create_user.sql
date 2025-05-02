SET search_path TO Nhom01;
DO
$$
DECLARE
    r RECORD;
BEGIN
    FOR r IN SELECT ma_nv FROM Nhom01.nhan_vien LOOP
        -- Kiểm tra user đã tồn tại chưa
        IF EXISTS (SELECT 1 FROM pg_roles WHERE rolname = r.ma_nv) THEN
            RAISE NOTICE 'User % already exists, dropping...', r.ma_nv;
            EXECUTE format('DROP USER %I', r.ma_nv);
        END IF;
        -- Tạo user với username và password đều là ma_nv
        EXECUTE format('CREATE USER %I WITH PASSWORD %L', r.ma_nv, r.ma_nv);

        -- Cấp quyền sử dụng schema
        EXECUTE format('GRANT USAGE ON SCHEMA Nhom01 TO %I', r.ma_nv);

        -- Cấp quyền truy cập dữ liệu (chỉnh theo nhu cầu)
        EXECUTE format('GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA Nhom01 TO %I', r.ma_nv);

        -- Cấp quyền sử dụng sequence
        EXECUTE format('GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA Nhom01 TO %I', r.ma_nv);

        -- Gán quyền mặc định cho bảng/sequence tạo sau này
        EXECUTE format('ALTER DEFAULT PRIVILEGES IN SCHEMA Nhom01 GRANT SELECT ON TABLES TO %I', r.ma_nv);
        EXECUTE format('ALTER DEFAULT PRIVILEGES IN SCHEMA Nhom01 GRANT USAGE, SELECT ON SEQUENCES TO %I', r.ma_nv);

        RAISE NOTICE 'Created and granted access to user: %', r.ma_nv;
    END LOOP;
END
$$;
