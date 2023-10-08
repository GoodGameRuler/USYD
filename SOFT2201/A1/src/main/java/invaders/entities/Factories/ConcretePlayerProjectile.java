package invaders.entityAbstractions;

import invaders.entities.PlayerProjectile;
import invaders.entities.Projectile;
import invaders.physics.Vector2D;

public class ConcretePlayerProjectile extends ProjectileCreator {
    @Override
    public Projectile createProjectile(Vector2D position) {
        return new PlayerProjectile(position);
    }
}
