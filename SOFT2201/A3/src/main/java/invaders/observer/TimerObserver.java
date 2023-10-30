package invaders.observer;

import javafx.scene.control.Label;

public class TimerObserver implements CounterObserver {

    Label label = new Label("Time: 0:00");
    Timer concreteSubject;

    public TimerObserver(Timer concreteSubject) {
        this.concreteSubject = concreteSubject;
    }

    public Label getLabel() {
        return this.label;
    }

    @Override
    public void update() {
        label.setText("Time: " + concreteSubject.getTime());

    }
}
