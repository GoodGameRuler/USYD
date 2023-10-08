package invaders.entities;

import invaders.GameObject;
import invaders.entities.States.BunkerColorState;
import invaders.entities.States.GreenBunkerState;
import invaders.entities.States.RedBunkerState;
import invaders.entities.States.YellowBunkerState;
import invaders.logic.Damagable;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import javafx.scene.image.Image;

import java.io.File;
import java.util.ArrayList;

public class Bunker implements Renderable, Damagable, GameObject {

    private Vector2D position;
    private double health;
    private double height;
    private double width;
    private Image image;
    private ArrayList<BunkerColorState> bunkerStates;
    private int stateIndex;
    private boolean alive;

    public Bunker(Vector2D position, double height, double width) {
        this.health = 3;
        this.position = position;
        this.height = height;
        this.width = width;
        this.image = new Image(new File("src/main/resources/bunker_green.png").toURI().toString(), width, height, true, true);
        this.bunkerStates = new ArrayList<BunkerColorState>();
        this.bunkerStates.add(new GreenBunkerState(height, width));
        this.bunkerStates.add(new YellowBunkerState(height, width));
        this.bunkerStates.add(new RedBunkerState(height, width));
        this.stateIndex = 0;
        this.alive = true;
    }

    @Override
    public void takeDamage(double amount) {
        if(this.health > 0) {
            this.health -= amount;
            this.changeState();
        } else
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
        return this.bunkerStates.get(this.stateIndex).returnImage();
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

    public void changeState() {
        if(this.stateIndex >= this.bunkerStates.size()) {
            stateIndex++;
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {

    }
}
