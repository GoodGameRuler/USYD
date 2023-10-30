package invaders.strategy;

import invaders.factory.Projectile;

public class FastProjectileStrategy implements ProjectileStrategy{

    @Override
    public void update(Projectile p) {
        double newYPos = p.getPosition().getY() + 3;
        p.getPosition().setY(newYPos);
    }

    @Override
    public int projectilePoints() {
        return 2;
    }

    @Override
    public int creatingEntityPoints() {
        return 4;
    }
}
