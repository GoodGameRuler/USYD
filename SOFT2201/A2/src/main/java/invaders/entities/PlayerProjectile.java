package invaders.entities;

import invaders.GameObject;
import invaders.logic.Damagable;
import invaders.physics.Moveable;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import javafx.scene.image.Image;

import java.io.File;

public class PlayerProjectile implements Renderable, Moveable, Damagable, Projectile, GameObject {

    private Vector2D position;
    private Vector2D direction;
    private Image image;
    private final double width = 5;
    private final double height = 15;

    private double damage;
    private int speed;
    private boolean alive;

    public PlayerProjectile(Vector2D position) {
        this.image = new Image(new File("src/main/resources/player.png").toURI().toString(), width, height, true, true);
        this.damage = 1;
        this.speed = 5;
        this.position = position;
        this.alive = true;
    }

    @Override
    public Image getImage() {
        return this.image;
    }

    @Override
    public double getWidth() {
        return this.width;
    }

    @Override
    public double getHeight() {
        return this.height;
    }

    @Override
    public Vector2D getPosition() {
        return this.position;
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }

    @Override
    public void up() {
        this.position.setY(this.position.getY() - speed);
    }

    @Override
    public void down() {
    }

    @Override
    public void left() { return; }

    @Override
    public void right() { return; }

    @Override
    public void takeDamage(double amount) {
        if(damage > 0)
            this.damage -= damage;
        else
            this.alive = false;
    }

    @Override
    public double getHealth() {
        return this.damage;
    }

    @Override
    public boolean isAlive() {
        return this.alive;
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        this.up();
        if(this.getPosition().getY() <= 0 && this.alive) {
            this.alive = false;
        }
    }
}
