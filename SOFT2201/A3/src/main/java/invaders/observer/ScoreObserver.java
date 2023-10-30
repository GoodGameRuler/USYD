package invaders.observer;

import javafx.scene.control.Label;

public class ScoreObserver implements CounterObserver {

    Label label = new Label("Score: 0");
    Score concreteSubject;

    public ScoreObserver(Score concreteSubject) {
        this.concreteSubject = concreteSubject;
    }

    public Label getLabel() {
        return this.label;
    }

    @Override
    public void update() {
        label.setText("Score: " + concreteSubject.getScore());

    }
}
