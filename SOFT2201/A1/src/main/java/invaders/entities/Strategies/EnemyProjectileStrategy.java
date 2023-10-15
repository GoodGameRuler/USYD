package invaders.entities.Strategies;

import invaders.physics.Vector2D;

public interface EnemyProjectileStartegy {
    public Vector2D behaviour(Vector2D position);
}
