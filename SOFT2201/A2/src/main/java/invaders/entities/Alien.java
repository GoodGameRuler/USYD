package invaders.entities;

import invaders.GameObject;
import invaders.entities.Strategies.EnemyProjectileCreationStrategy;
import invaders.entities.Strategies.EnemyProjectileStrategy;
import invaders.logic.Damagable;
import invaders.physics.Moveable;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import javafx.scene.image.Image;
import java.io.File;

public class Alien implements Renderable, Damagable, Moveable, GameObject {

    private int health = 50;
    private double width = 25;
    private double height = 30;
    private Image image;
    private Vector2D position;
    private boolean alive;
    private EnemyProjectileCreationStrategy projectileCreationStrategy;

    public Alien(Vector2D position, double width, double height) {
        this.image = new Image(new File("src/main/resources/enemy.png").toURI().toString(), width, height, true, true);
        this.position = position;
        this.width = width;
        this.height = height;
        this.alive = true;
    }

    @Override
    public void takeDamage(double amount) {
        if(this.health > 0)
            this.health -= amount;
        else
            this.alive = false;
    }

    @Override
    public double getHealth() {
        return this.health;
    }

    @Override
    public boolean isAlive() {
        return this.alive;
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

    }

    @Override
    public void down() {

    }

    @Override
    public void left() {

    }

    @Override
    public void right() {

    }

    @Override
    public void start() {

    }

    @Override
    public void update() {

    }

    // TODO change position to relative to the group
    public Projectile shoot() {
        return this.projectileCreationStrategy.createProjectile(this.position.clone());
    }

    public Alien setProjectileCreationStrategy(EnemyProjectileCreationStrategy epcs) {
        this.projectileCreationStrategy = epcs;
        return this;
    }
}
