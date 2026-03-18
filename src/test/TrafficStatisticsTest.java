package test;

import org.junit.jupiter.api.Test;
import util.TrafficStatistics;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

//Test TrafficStatistics - kiểm tra thống kê thread-safe.
public class TrafficStatisticsTest {

    //Test ban đầu = 0
    @Test
    void testInitialCount() {
        TrafficStatistics stats = new TrafficStatistics();

        assertEquals(String.valueOf(0), stats.getTotalVehiclesPassed(), 0); //Ban đầu số xe phải = 0
        assertEquals(String.valueOf(0), stats.getTrafficJamCount(), 0); // Ban đầu kẹt xe = 0
    }

    //Test ghi nhận xe qua ngã tư
    @Test
    void testVehicleCountIncrease() {
        TrafficStatistics stats = new TrafficStatistics();

        stats.recordVehiclePassed();
        stats.recordVehiclePassed();

        assertEquals(2, stats.getTotalVehiclesPassed());
    }

    //Test ghi nhận kẹt xe
    @Test
    void testTrafficJamCount() {
        TrafficStatistics stats = new TrafficStatistics();

        stats.recordTrafficJam();
        stats.recordTrafficJam();
        stats.recordTrafficJam();

        assertEquals(3, stats.getTrafficJamCount());
    }

    //Test thread-safe: nhiều thread cùng ghi nhận
    @Test
    void testThreadSafety() throws InterruptedException {
        TrafficStatistics stats = new TrafficStatistics();

        int threads = 10;
        int actionsPerThread = 100;

        ExecutorService executor = Executors.newFixedThreadPool(threads);

        // Mỗi thread ghi nhận 100 xe
        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                for (int j = 0; j < actionsPerThread; j++) {
                    stats.recordVehiclePassed();
                }
            });
        }

        executor.shutdown();

        // Đợi thread chạy xong
        while (!executor.isTerminated()) {
            Thread.sleep(10);
        }

        int expected = threads * actionsPerThread;

        assertEquals(String.valueOf(expected), stats.getTotalVehiclesPassed(),
                "Phải đúng số lượng, không được mất dữ liệu khi đa luồng");
    }
}