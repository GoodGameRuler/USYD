package invaders.entityAbstractions;

import invaders.entities.Bunker;
import invaders.physics.Vector2D;

public interface BunkerBuilder {
    public BunkerBuilder setPosition(Vector2D position);
    public BunkerBuilder setWidth(double width);
    public BunkerBuilder setHeight(double height);

    public Bunker build();
}
