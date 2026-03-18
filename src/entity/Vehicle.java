package entity;

import pattern.observer.TrafficSignalObserver;
import engine.Intersection;
import exception.CollisionException;
import exception.TrafficJamException;
import util.TrafficLogger;

//Lớp cha trừu tượng đại diện cho tất cả phương tiện giao thông. Mỗi Vehicle chạy trên một thread riêng (Runnable)

public abstract class Vehicle implements Runnable, TrafficSignalObserver {
    private String id;
    private String type;        // "Car", "Motorbike", ...
    private int speed;          // Tốc độ di chuyển
    private int priority;       // 0 = thường, 1 = ưu tiên
    private String direction;   // NORTH, SOUTH, EAST, WEST

    //Biến trạng thái nội bộ
    protected volatile boolean isRunning = true;   // thread còn chạy
    protected volatile String currentSignal = "GREEN"; // trạng thái đèn hiện tại (bắt đầu XANH)

    // Tham chiếu đến ngã tư
    protected Intersection intersection;

    //CONSTRUCTOR
    public Vehicle(String id, String type, int speed, int priority, String direction) {
        this.id = id;
        this.type = type;
        this.speed = speed;
        this.priority = priority;
        this.direction = direction;
    }

    //GETTER
    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getSpeed() {
        return speed;
    }

    public int getPriority() {
        return priority;
    }

    public String getDirection() {
        return direction;
    }

    //SETTER
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setIntersection(Intersection intersection) {
        this.intersection = intersection;
    }

    //ABSTRACT METHODS

    //Di chuyển về phía ngã tư
    public abstract void moveTowardIntersection();

    //dừng lại
    public abstract void stop();

     //Kiểm tra có phải xe ưu tiên không
    public abstract boolean isPriority();

    //THREAD LOGIC
    @Override
    public void run() {
        try {
            while (isRunning && !Thread.currentThread().isInterrupted()) {

                // 1. Di chuyển về phía ngã tư
                moveTowardIntersection();
                Thread.sleep(2000);

                // 2. Nếu đèn đỏ và không phải xe ưu tiên → dừng chờ
                if ("RED".equals(currentSignal) && !isPriority()) {
                    stop(); // log 1 lần duy nhất
                    // Chờ đến khi đèn chuyển xanh (không spam log)
                    while ("RED".equals(currentSignal) && isRunning && !Thread.currentThread().isInterrupted()) {
                        Thread.sleep(2000);
                    }
                    if (!isRunning || Thread.currentThread().isInterrupted()) break;
                }

                // 3. Đèn xanh hoặc xe ưu tiên → đi qua ngã tư
                if (intersection != null) {
                    try {
                        intersection.enterIntersection(this);
                        TrafficLogger.log(this + " đang đi qua ngã tư");
                        Thread.sleep(3000); // thời gian qua ngã tư
                        intersection.exitIntersection(this);
                    } catch (TrafficJamException e) {
                        TrafficLogger.log(this + " gặp kẹt xe, chờ thử lại...");
                    } catch (CollisionException e) {
                        TrafficLogger.log(this + " phát hiện nguy cơ va chạm!");
                    }
                } else {
                    TrafficLogger.log(this + " đang đi qua ngã tư");
                    Thread.sleep(3000);
                }

                // 4. Nghỉ trước khi lặp lại
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            TrafficLogger.log(this + " đã bị gián đoạn.");
        }
    }

    //OBSERVER
     //Nhận tín hiệu từ TrafficLight
    @Override
    public void onSignalChanged(String signalState) {
        TrafficLogger.log(this + " nhận tín hiệu: " + signalState);
        this.currentSignal = signalState;

        // Nếu đèn đỏ và không phải xe ưu tiên → dừng
        if ("RED".equals(signalState) && !isPriority()) {
            stop();
        }
    }

    //TO STRING
    @Override
    public String toString() {
        return type + " #" + id + " [" + direction + "]";
    }
}