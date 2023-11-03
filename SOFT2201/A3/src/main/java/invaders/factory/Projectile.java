package invaders.factory;

import invaders.gameobject.ScoreCollectable;
import invaders.gameobject.GameObject;
import invaders.observer.Score;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import javafx.scene.image.Image;

public abstract class Projectile implements Renderable, GameObject, ScoreCollectable {
    private int lives = 1;
    private Vector2D position;
    private final Image image;
    private Score scoreCollector;
    private boolean destroyed = true;

    public Projectile(Vector2D position, Image image) {
        this.position = position;
        this.image = image;
    }

    public void setDestroyed() {
        this.destroyed = false;
    }

    public boolean getDestroyed() {
        return this.destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    @Override
    public Vector2D getPosition() {
        return position;
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }

    @Override
    public void start() {}

    @Override
    public double getWidth() {
        return 10;
    }

    @Override
    public double getHeight() {
        return 10;
    }

    @Override
    public void takeDamage(double amount) {
        this.lives-=1;
        if(this.lives <= 0 && this.scoreCollector != null && this.destroyed) {
            this.incrementCollector();
            this.scoreCollector.informObservers();
        }
    }

    @Override
    public double getHealth() {
        return this.lives;
    }

    public void setHealth(int health) {
        this.lives = health;
    }

    @Override
    public boolean isAlive() {
        return this.lives>0;
    }

    @Override
    public void setScoreCollector(Score score) {
        this.scoreCollector = score;
    }

    @Override
    public void incrementCollector() {
        if(this.scoreCollector != null)
            this.scoreCollector.incrementScore(this.getScore());
    }

    @Override
    public abstract int getScore();
}
