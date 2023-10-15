package invaders.entities.Factories;

import invaders.entities.Projectile;
import invaders.physics.Vector2D;

public interface ProjectileCreator {
    public Projectile createProjectile(Vector2D position);
}
