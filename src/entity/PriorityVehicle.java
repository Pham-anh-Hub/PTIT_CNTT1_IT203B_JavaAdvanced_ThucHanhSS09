package entity;

import util.TrafficLogger;

//Lớp trung gian đại diện cho các phương tiện ƯU TIÊN(xe cứu thương, xe cứu hỏa...).Xe ưu tiên có thể vượt đèn đỏ và được nhường đường.
public abstract class PriorityVehicle extends Vehicle {

    public PriorityVehicle(String id, String type, int speed, int priority, String direction) {
        super(id, type, speed, priority, direction);
    }

    //Xe ưu tiên CÓ quyền ưu tiên
    @Override
    public boolean isPriority() {
        return true;
    }

    //Xe ưu tiên vẫn di chuyển ngay cả khi đèn đỏ
    @Override
    public void moveTowardIntersection() {
        TrafficLogger.log(this + " đang lao nhanh về phía ngã tư (ưu tiên)!");
    }

    //Xe ưu tiên gần như KHÔNG dừng (trừ khi có xử lý đặc biệt)
    @Override
    public void stop() {
        TrafficLogger.log(this + " giảm tốc nhưng vẫn tiếp tục di chuyển (quyền ưu tiên).");
    }
}