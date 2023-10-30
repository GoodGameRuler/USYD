package invaders.gameobject;

import invaders.observer.Score;

public interface ScoreCollectable {

    public void setScoreCollector(Score score);
    public void incrementCollector();
    public int getScore();
}
