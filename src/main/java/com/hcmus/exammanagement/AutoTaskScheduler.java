package com.hcmus.exammanagement;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.hcmus.exammanagement.bus.ThanhToanBUS;

public class AutoTaskScheduler {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void start() {
        Runnable task = () -> {
            System.out.println("Đang kiểm tra hóa đơn quá hạn...");
            ThanhToanBUS bus = new ThanhToanBUS();
            bus.xuLyHoaDonQuaHan();
        };

        // Chạy mỗi 24 giờ, sau khi chờ 1 giờ ban đầu
        scheduler.scheduleAtFixedRate(task, 0, 24, TimeUnit.HOURS);
    }

    public void stop() {
        scheduler.shutdown();
    }
}

