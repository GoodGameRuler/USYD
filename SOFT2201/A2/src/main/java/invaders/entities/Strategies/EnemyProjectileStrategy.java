package invaders.entities.Strategies;

import invaders.physics.Vector2D;

public interface EnemyProjectileStrategy {
    public Vector2D behaviour(Vector2D position);
}
