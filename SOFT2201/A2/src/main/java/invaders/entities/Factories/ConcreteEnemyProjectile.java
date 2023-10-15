package invaders.entities.Factories;

import invaders.entities.EnemyProjectile;
import invaders.entities.Projectile;
import invaders.physics.Vector2D;

public class ConcreteEnemyProjectile implements ProjectileCreator {
    @Override
    public Projectile createProjectile(Vector2D position) {
        return new EnemyProjectile(position, new Vector2D(1 , 1), 10, 10);
    }
}
