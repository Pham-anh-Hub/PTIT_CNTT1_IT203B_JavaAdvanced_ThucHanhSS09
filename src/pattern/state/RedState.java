package pattern.state;

import util.TrafficLogger;

public class RedState implements TrafficLightState {
    private static final long DURATION = 10000;

    @Override
    public void handle() {
        TrafficLogger.log("Đèn ĐỎ - Dừng lại");
    }

    @Override
    public TrafficLightState nextState() {
        return new GreenState();
    }

    @Override
    public String getStateName() {
        return "RED";
    }

    @Override
    public long getDuration() {
        return DURATION;
    }
}