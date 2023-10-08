package invaders.entityAbstractions;

import invaders.entities.Alien;
import invaders.physics.Vector2D;

public class ConcreteAlienBuilder implements AlienBuilder {

    private Alien alien;
    private Vector2D position;
    private double height;
    private double width;

    @Override
    public AlienBuilder setPosition(Vector2D position) {
        this.position = position;
        return this;
    }

    @Override
    public AlienBuilder setWidth(double width) {
        this.width = width;
        return this;
    }

    @Override
    public AlienBuilder setHeight(double height) {
        this.height = height;
        return this;
    }

    @Override
    public Alien build() {
        return new Alien(position, width, height);
    }
}
