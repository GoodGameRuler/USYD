package invaders.entities.Strategies;

import invaders.entities.Factories.ConcreteEnemyProjectile;
import invaders.entities.Factories.ProjectileCreator;
import invaders.entities.Projectile;
import invaders.physics.Vector2D;

import java.util.ArrayList;

public class BasicProjectileCreationStrategy implements EnemyProjectileCreationStrategy {

    private ArrayList<Projectile> projectiles;
    private ProjectileCreator pc;

    public BasicProjectileCreationStrategy() {
        this.pc = new ConcreteEnemyProjectile();
    }

    @Override
    public Projectile createProjectile(Vector2D position) {
        if(this.projectiles.size() < 3) {
            Projectile p = this.pc.createProjectile(position);
            this.projectiles.add(p);
            return p;
        }
        return null;
    }
}
