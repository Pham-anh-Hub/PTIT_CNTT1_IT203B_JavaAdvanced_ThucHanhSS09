package engine;

import entity.PriorityVehicle;
import entity.Vehicle;
import exception.CollisionException;
import exception.TrafficJamException;
import util.TrafficLogger;
import util.TrafficStatistics;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Intersection {

    private static final int MAX_VEHICLES_IN_INTERSECTION = 2;
    private final int maxQueueCapacity;

    private final Semaphore semaphore = new Semaphore(MAX_VEHICLES_IN_INTERSECTION, true);
    private final ReentrantLock lock = new ReentrantLock(true);
    private final ConcurrentLinkedQueue<Vehicle> waitingQueue = new ConcurrentLinkedQueue<>();

    private int totalPassed = 0;
    private int trafficJamCount = 0;

    public Intersection() {
        this.maxQueueCapacity = 4;
    }

    public Intersection(int maxQueueCapacity) {
        this.maxQueueCapacity = maxQueueCapacity;
    }

    public void enterIntersection(Vehicle vehicle)
            throws TrafficJamException, CollisionException {

        try {
            lock.lock();
            // kiểm tra kẹt xe
            if (waitingQueue.size() > maxQueueCapacity) {
                trafficJamCount++;
                TrafficLogger.log("⚠️ Phát hiện kẹt xe!");
                throw new TrafficJamException("Quá nhiều xe đang chờ!");
            }

            // xe ưu tiên
            if (!(vehicle instanceof PriorityVehicle)) {
                waitingQueue.add(vehicle);
                TrafficLogger.log(vehicle + " đang chờ...");
            }

        } finally {
            lock.unlock();
        }

        try {
            // 🚦 acquire slot trong ngã tư
            semaphore.acquire();

            lock.lock();
            try {
                waitingQueue.remove(vehicle);
            } finally {
                lock.unlock();
            }
            TrafficLogger.log(vehicle + " ĐÃ VÀO ngã tư");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CollisionException("Luồng bị gán đoạn → có thể xảy ra va chạm!");
        }
    }

    public void exitIntersection(Vehicle vehicle) {
        semaphore.release();
        lock.lock();
        try {
            totalPassed++;
        } finally {
            lock.unlock();
        }

        // Cập nhật thống kê
        TrafficStatistics.getInstance().recordVehiclePassed(vehicle);

        TrafficLogger.log(vehicle + " ĐÃ RA KHỎI ngã tư");
    }

    // Alias method - dùng trong test
    public void enter(Vehicle vehicle) throws TrafficJamException, CollisionException {
        enterIntersection(vehicle);
    }

    public void addToWaitingQueue(Vehicle vehicle) {
        waitingQueue.add(vehicle);
    }

    public int getWaitingCount() {
        return waitingQueue.size();
    }

    public int getTotalPassed() {
        return totalPassed;
    }

    public int getTrafficJamCount() {
        return trafficJamCount;
    }

}