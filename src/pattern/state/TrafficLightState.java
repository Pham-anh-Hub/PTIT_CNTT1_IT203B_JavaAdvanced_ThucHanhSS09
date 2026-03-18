package pattern.state;

public interface TrafficLightState {

    void handle();
    TrafficLightState nextState();
    String getStateName();
    long getDuration();
}