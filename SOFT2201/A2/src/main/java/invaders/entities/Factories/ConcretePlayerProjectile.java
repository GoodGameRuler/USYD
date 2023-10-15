package invaders.entities.Factories;

import invaders.entities.Player;
import invaders.entities.PlayerProjectile;
import invaders.entities.Projectile;
import invaders.physics.Vector2D;

public class ConcretePlayerProjectile implements ProjectileCreator {
    @Override
    public Projectile createProjectile(Vector2D position) {
        return new PlayerProjectile(position);
    }
}
