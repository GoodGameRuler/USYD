package invaders.entities.Strategies;

import invaders.entities.Projectile;
import invaders.physics.Vector2D;

public interface EnemyProjectileCreationStrategy {
    public Projectile createProjectile(Vector2D position);
}
