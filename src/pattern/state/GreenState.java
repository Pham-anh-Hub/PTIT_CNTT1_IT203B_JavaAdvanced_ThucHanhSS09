package pattern.state;

import util.TrafficLogger;

public class GreenState implements TrafficLightState {
    private static final long DURATION = 10000;

    @Override
    public void handle() {
        TrafficLogger.log("Đèn XANH - Xe được phép đi");
    }

    @Override
    public TrafficLightState nextState() {
        return new YellowState();
    }

    @Override
    public String getStateName() {
        return "GREEN";
    }

    @Override
    public long getDuration() {
        return DURATION;
    }
}