package entity;

import util.TrafficLogger;

//Lớp trung gian đại diện cho các phương tiện THƯỜNG (không ưu tiên). Các xe thường phải tuân thủ đèn giao thông.
public abstract class StandardVehicle extends Vehicle {

    public StandardVehicle(String id, String type, int speed, int priority, String direction) {
        super(id, type, speed, priority, direction);
    }

    //Xe thường KHÔNG có quyền ưu tiên
    @Override
    public boolean isPriority() {
        return false;
    }

    //Logic dừng chung cho tất cả xe thường
    @Override
    public void stop() {
        TrafficLogger.log(this + " đang chờ đèn ĐỎ...");
    }

    //Logic di chuyển chung (có thể override ở class con nếu cần)
    @Override
    public void moveTowardIntersection() {
        TrafficLogger.log(this + " đang di chuyển về phía ngã tư...");
    }
}