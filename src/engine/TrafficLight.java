package engine;

import pattern.state.TrafficLightState;
import pattern.state.GreenState;
import pattern.observer.TrafficSignalSubject;
import pattern.observer.TrafficSignalObserver;
import util.TrafficLogger;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TrafficLight implements Runnable, TrafficSignalSubject {

    private TrafficLightState currentState;
    private final List<TrafficSignalObserver> observers = new CopyOnWriteArrayList<>();

    public TrafficLight() {
        this.currentState = new GreenState(); // bắt đầu GREEN
    }

    public String getCurrentStateName() {
        return currentState.getStateName();
    }

    // Alias cho test
    public String getStateName() {
        return getCurrentStateName();
    }

    // Getter cho test
    public TrafficLightState getCurrentState() {
        return currentState;
    }

    public void changeState() {
        currentState = currentState.nextState();
        TrafficLogger.log("🚦 Đèn giao thông chuyển sang: " + getCurrentStateName());

        notifyObservers(); // báo cho xe
    }

    // Alias cho test
    public void nextState() {
        changeState();
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {

                currentState.handle(); // in trạng thái

                Thread.sleep(currentState.getDuration()); // đợi theo state

                changeState(); // chuyển trạng thái
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Observer Pattern

    @Override
    public void registerObserver(TrafficSignalObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(TrafficSignalObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (TrafficSignalObserver observer : observers) {
            observer.onSignalChanged(getCurrentStateName());
        }
    }


}