package invaders.entities.Factories;

import invaders.entities.Projectile;
import invaders.physics.Vector2D;

public abstract class ProjectileCreator {
    public abstract Projectile createProjectile(Vector2D position);
}
