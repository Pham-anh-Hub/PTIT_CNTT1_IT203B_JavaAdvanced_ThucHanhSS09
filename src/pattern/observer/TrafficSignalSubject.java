package pattern.observer;

public interface TrafficSignalSubject {
    void registerObserver(TrafficSignalObserver observer);
    void removeObserver(TrafficSignalObserver observer);
    void notifyObservers();
}
