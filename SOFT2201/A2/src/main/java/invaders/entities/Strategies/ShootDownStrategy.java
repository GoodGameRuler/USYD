package invaders.entities.Strategies;

import invaders.physics.Vector2D;

public class ShootDownStrategy implements EnemyProjectileStrategy {

    int speed;

    public ShootDownStrategy(int speed) {
        this.speed = speed;
    }

    @Override
    public Vector2D behaviour(Vector2D position) {
        return position.clone(0, speed);
    }
}
