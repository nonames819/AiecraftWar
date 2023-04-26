package edu.hitsz.observer;

import java.util.LinkedList;
import java.util.List;

/**
 * @author hitsz
 */
public class BombReminder {
    protected List<BombObserver> observers = new LinkedList<>();

    public void addObserver(BombObserver bombObserver) {
        this.observers.add(bombObserver);
    }

    public void deleteObserver(BombObserver bombObserver) {
        this.observers.remove(bombObserver);
    }

    public void deleteAll() {
        this.observers.removeIf(BombObserver::invalid);
    }

    public void notifyObservers() {
        for (BombObserver eo : this.observers) {
            eo.update();
        }
    }


}
