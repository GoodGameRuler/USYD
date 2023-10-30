package invaders.observer;

import java.util.ArrayList;

public class CounterSubject {
    private ArrayList<CounterObserver> observers = new ArrayList<CounterObserver>();

    public void attach(CounterObserver co) {
        if(!observers.contains(co)) {
            observers.add(co);
        }
    }

    public void detach(CounterObserver co) {
        observers.remove(co);
    }

    public void informObservers() {
        for(CounterObserver co : observers) {
            co.update();
        }
    }

}