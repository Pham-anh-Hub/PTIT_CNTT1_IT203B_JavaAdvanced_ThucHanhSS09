import engine.SimulationEngine;
import util.TrafficLogger;

/**
 * Điểm khởi chạy chương trình - Smart Traffic Simulator.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== HỆ THỐNG MÔ PHỎNG GIAO THÔNG THÔNG MINH ===");
        System.out.println("Khởi động hệ thống mô phỏng giao thông...\n");

        // Khởi tạo và chạy SimulationEngine
        SimulationEngine engine = new SimulationEngine();
        engine.startSimulation();
    }
}