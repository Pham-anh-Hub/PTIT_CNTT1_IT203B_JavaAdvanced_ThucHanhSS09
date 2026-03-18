package entity;

import util.TrafficLogger;

//Xe cứu thương - phương tiện ƯU TIÊN, được vượt đèn đỏ và nhường đường.
public class Ambulance extends PriorityVehicle {

    public Ambulance(String id, String direction) {
        super(id, "Ambulance", 10, 1, direction);
    }

    //Di chuyển nhanh, thể hiện tính khẩn cấp
    @Override
    public void moveTowardIntersection() {
        TrafficLogger.log(this + " đang lao nhanh đến ngã tư!");
    }

    //Xe cứu thương hầu như không dừng, chỉ giảm tốc nếu cần
    @Override
    public void stop() {
        TrafficLogger.log(this + " giảm tốc nhưng vẫn tiếp tục di chuyển (quyền ưu tiên).");
    }
}