package entity;

import util.TrafficLogger;

//Ô tô - phương tiện thường, tốc độ trung bình.
public class Car extends StandardVehicle {

    public Car(String id, String direction) {
        super(id, "Car", 5, 0, direction);
    }

    //Di chuyển với tốc độ trung bình
    @Override
    public void moveTowardIntersection() {
        TrafficLogger.log(this + " đang di chuyển ổn định về phía ngã tư...");
    }

    //Dừng xe khi gặp đèn đỏ hoặc bị chặn
    @Override
    public void stop() {
        TrafficLogger.log(this + " đang dừng lại tại đèn ĐỎ.");
    }
}