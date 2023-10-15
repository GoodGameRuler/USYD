package invaders.entities.Builders;

import invaders.entities.Alien;
import invaders.physics.Vector2D;

public interface AlienBuilder {

    public AlienBuilder setPosition(Vector2D position);
    public AlienBuilder setWidth(double width);
    public AlienBuilder setHeight(double height);

    public Alien build();

}
