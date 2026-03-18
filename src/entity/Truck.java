package entity;

import util.TrafficLogger;


 //Xe tải phương tiện thường, tốc độ chậm, kích thước lớn.
public class Truck extends StandardVehicle {

    //Constructor:type = "Truck" speed = thấp (ví dụ: 2) priority = 0 (xe thường)
    public Truck(String id, String direction) {
        super(id, "Truck", 2, 0, direction);
    }

    //Di chuyển về phía ngã tư với tốc độ chậm
    @Override
    public void moveTowardIntersection() {
        TrafficLogger.log(this + " đang di chuyển chậm về phía ngã tư...");
    }

     // Dừng xe (khi đèn đỏ hoặc bị chặn)
    @Override
    public void stop() {
        TrafficLogger.log(this + " đang dừng lại do đèn ĐỎ hoặc có chướng ngại vật.");
    }

     //Xe tải KHÔNG phải xe ưu tiên
    @Override
    public boolean isPriority() {
        return false;
    }
}