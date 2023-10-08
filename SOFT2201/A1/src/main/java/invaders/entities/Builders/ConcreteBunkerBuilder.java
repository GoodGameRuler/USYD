package invaders.entityAbstractions;

import invaders.entities.Bunker;
import invaders.physics.Vector2D;

public class ConcreteBunkerBuilder implements BunkerBuilder {

    private Vector2D position;
    private double width;
    private double height;

    @Override
    public BunkerBuilder setPosition(Vector2D position) {
        this.position = position;
        return this;
    }

    @Override
    public BunkerBuilder setWidth(double width) {
        this.width = width;
        return this;
    }

    @Override
    public BunkerBuilder setHeight(double height) {
        this.height = height;
        return this;
    }

    @Override
    public Bunker build() {
        return new Bunker(position, height, width);
    }
}
