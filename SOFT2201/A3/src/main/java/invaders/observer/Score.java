package invaders.observer;

public class Score extends CounterSubject {

    private int score = 0;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void incrementScore(int increment) {
        this.score += increment;
    }
}
