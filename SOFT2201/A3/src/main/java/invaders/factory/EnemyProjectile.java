package invaders.factory;

import invaders.engine.GameEngine;
import invaders.physics.Vector2D;
import invaders.strategy.ProjectileStrategy;
import javafx.scene.image.Image;

public class EnemyProjectile extends Projectile{
    private ProjectileStrategy strategy;

    public EnemyProjectile(Vector2D position, ProjectileStrategy strategy, Image image) {
        super(position,image);
        this.strategy = strategy;
    }

    @Override
    public void update(GameEngine model) {
        strategy.update(this);

        if(this.getPosition().getY()>= model.getGameHeight() - this.getImage().getHeight()){
            this.setDestroyed();
            this.takeDamage(1);
        }

    }
    @Override
    public String getRenderableObjectName() {
        return "EnemyProjectile";
    }

    @Override
    public int getScore() {
        return this.strategy.projectilePoints();
    }

    @Override
    public EnemyProjectile clone() {
        EnemyProjectile ep = new EnemyProjectile(this.getPosition().clone(), strategy, getImage());
        ep.setDestroyed(this.getDestroyed());
        ep.setHealth((int) this.getHealth());
        return ep;
    }
}
