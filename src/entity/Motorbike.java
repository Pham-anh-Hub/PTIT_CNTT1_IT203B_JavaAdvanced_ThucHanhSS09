package entity;

import util.TrafficLogger;

//Xe máy - phương tiện thường, tốc độ nhanh, kích thước nhỏ.
public class Motorbike extends StandardVehicle {

    public Motorbike(String id, String direction) {
        super(id, "Motorbike", 8, 0, direction);
    }

    //Di chuyển nhanh về phía ngã tư
    @Override
    public void moveTowardIntersection() {
        TrafficLogger.log(this + " đang di chuyển nhanh về phía ngã tư...");
    }

    //Dừng xe (phanh nhanh hơn xe khác)
    @Override
    public void stop() {
        TrafficLogger.log(this + " phanh gấp tại đèn ĐỎ.");
    }
}