package util;

import entity.Vehicle;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tiện ích thống kê lưu lượng giao thông.
 * Dùng các cấu trúc dữ liệu thread-safe (ConcurrentHashMap, AtomicInteger).
 *
 * ★ NGƯỜI 4 phụ trách implement ★
 */
public class TrafficStatistics {

    // Shared instance cho static access (SimulationEngine)
    private static final TrafficStatistics INSTANCE = new TrafficStatistics();

    private final AtomicInteger totalVehiclesPassed = new AtomicInteger(0);
    private final AtomicInteger trafficJamCount = new AtomicInteger(0);
    private final ConcurrentHashMap<String, AtomicInteger> vehicleCountByType = new ConcurrentHashMap<>();

    /**
     * Ghi nhận 1 xe đã đi qua ngã tư thành công.
     * 
     * @param vehicle xe đã qua
     */
    public void recordVehiclePassed(Vehicle vehicle) {
        totalVehiclesPassed.incrementAndGet();

        String vehicleType = vehicle.getClass().getSimpleName();
        vehicleCountByType
                .computeIfAbsent(vehicleType, k -> new AtomicInteger(0))
                .incrementAndGet();
    }

    /**
     * Ghi nhận 1 xe đã qua (không cần Vehicle - dùng trong test).
     */
    public void recordVehiclePassed() {
        totalVehiclesPassed.incrementAndGet();
    }

    /**
     * Ghi nhận 1 lần kẹt xe.
     */
    public void recordTrafficJam() {
        trafficJamCount.incrementAndGet();
    }

    /**
     * Lấy tổng số xe đã qua ngã tư.
     * 
     * @return tổng xe
     */
    public int getTotalVehiclesPassed() {
        return totalVehiclesPassed.get();
    }

    /**
     * Lấy số lần kẹt xe.
     * 
     * @return số lần kẹt
     */
    public int getTrafficJamCount() {
        return trafficJamCount.get();
    }

    /**
     * In báo cáo thống kê cuối cùng ra console.
     * Dùng Stream API để thống kê số xe theo loại.
     */
    public void printReport() {
        System.out.println("\n========== BÁO CÁO THỐNG KÊ GIAO THÔNG ==========");
        System.out.println("Tổng số xe đã qua ngã tư: " + getTotalVehiclesPassed());

        System.out.println("\nSố lượng xe theo loại:");
        vehicleCountByType.entrySet()
                .stream()
                .forEach(entry -> System.out.println("- " + entry.getKey() + ": " + entry.getValue().get()));

        System.out.println("\nSố lần kẹt xe: " + getTrafficJamCount());
        System.out.println("===============================================\n");
    }

    // Static access cho SimulationEngine
    public static TrafficStatistics getInstance() {
        return INSTANCE;
    }

    // Static printReport cho SimulationEngine
    public static void printGlobalReport() {
        INSTANCE.printReport();
    }
}
