package pattern.state;

import util.TrafficLogger;

public class YellowState implements TrafficLightState {
    private static final long DURATION = 3000;

    @Override
    public void handle() {
        TrafficLogger.log("Đèn VÀNG - Chuẩn bị dừng");
    }

    @Override
    public TrafficLightState nextState() {
        return new RedState();
    }

    @Override
    public String getStateName() {
        return "YELLOW";
    }

    @Override
    public long getDuration() {
        return DURATION;
    }
}