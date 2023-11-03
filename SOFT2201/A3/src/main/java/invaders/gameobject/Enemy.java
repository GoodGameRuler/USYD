package invaders.gameobject;

import invaders.engine.GameEngine;
import invaders.factory.EnemyProjectile;
import invaders.factory.EnemyProjectileFactory;
import invaders.factory.Projectile;
import invaders.factory.ProjectileFactory;
import invaders.observer.Score;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import invaders.strategy.ProjectileStrategy;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Enemy implements GameObject, Renderable, ScoreCollectable {
    private Vector2D position;
    private int lives = 1;
    private Image image;
    private int xVel = -1;

    private ArrayList<Projectile> enemyProjectile;
    private ArrayList<Projectile> pendingToDeleteEnemyProjectile;
    private ProjectileStrategy projectileStrategy;
    private ProjectileFactory projectileFactory;
    private Image projectileImage;
    private Random random = new Random();
    private Score scoreCollector;

    public Enemy(Vector2D position) {
        this.position = position;
        this.projectileFactory = new EnemyProjectileFactory();
        this.enemyProjectile = new ArrayList<>();
        this.pendingToDeleteEnemyProjectile = new ArrayList<>();
    }

    @Override
    public void start() {}

    @Override
    public void update(GameEngine engine) {
        if(enemyProjectile.size()<3){
            if(this.isAlive() &&  random.nextInt(120)==20){
                Projectile p = projectileFactory.createProjectile(new Vector2D(position.getX() + this.image.getWidth() / 2, position.getY() + image.getHeight() + 2),projectileStrategy, projectileImage);
                p.setScoreCollector(this.scoreCollector);
                enemyProjectile.add(p);
                engine.getPendingToAddGameObject().add(p);
                engine.getPendingToAddRenderable().add(p);
            }
        }else{
            pendingToDeleteEnemyProjectile.clear();
            for(Projectile p : enemyProjectile){
                if(!p.isAlive()){
                    engine.getPendingToRemoveGameObject().add(p);
                    engine.getPendingToRemoveRenderable().add(p);
                    pendingToDeleteEnemyProjectile.add(p);
                }
            }

            for(Projectile p: pendingToDeleteEnemyProjectile){
                enemyProjectile.remove(p);
            }
        }

        if(this.position.getX()<=this.image.getWidth() || this.position.getX()>=(engine.getGameWidth()-this.image.getWidth()-1)){
            this.position.setY(this.position.getY()+25);
            xVel*=-1;
        }

        this.position.setX(this.position.getX() + xVel);

        if((this.position.getY()+this.image.getHeight())>=engine.getPlayer().getPosition().getY()){
            engine.getPlayer().takeDamage(Integer.MAX_VALUE);
        }

        /*
        Logic TBD
         */

    }

    @Override
    public Image getImage() {
        return this.image;
    }

    @Override
    public double getWidth() {
        return this.image.getWidth();
    }

    @Override
    public double getHeight() {
       return this.image.getHeight();
    }

    @Override
    public Vector2D getPosition() {
        return this.position;
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setProjectileImage(Image projectileImage) {
        this.projectileImage = projectileImage;
    }

    @Override
    public void takeDamage(double amount) {
        this.lives-=1;
        if(this.lives <= 0) {
            this.incrementCollector();
            this.scoreCollector.informObservers();
        }
    }

    @Override
    public double getHealth() {
        return this.lives;
    }

    @Override
    public String getRenderableObjectName() {
        return "Enemy";
    }

    @Override
    public boolean isAlive() {
        return this.lives>0;
    }

    public void setProjectileStrategy(ProjectileStrategy projectileStrategy) {
        this.projectileStrategy = projectileStrategy;
    }

    @Override
    public void setScoreCollector(Score score) {
        this.scoreCollector = score;

    }

    @Override
    public void incrementCollector() {
        this.scoreCollector.incrementScore(this.getScore());

    }

    @Override
    public Enemy clone() {
        Enemy newEnemy = new Enemy(position.clone());

        newEnemy.setImage(image);
        newEnemy.setScoreCollector(scoreCollector);
        newEnemy.setLives(lives);
        newEnemy.setProjectileStrategy(projectileStrategy);
        newEnemy.setProjectileImage(projectileImage);
        newEnemy.setEnemyProjectileFactory(projectileFactory);
        newEnemy.setXVel(xVel);
        return newEnemy;
    }

    protected void setXVel(int xVel) {
        this.xVel = xVel;
    }

    protected void setEnemyProjectileFactory(ProjectileFactory projectileFactory) {
        this.projectileFactory = projectileFactory;
    }

    public List<Projectile> getEnemyProjectile() {
        return this.enemyProjectile;
    }

    public List<Projectile> getPendingToDeleteProjectiles() {
        return this.pendingToDeleteEnemyProjectile;
    }
    @Override
    public int getScore() {
        return this.projectileStrategy.creatingEntityPoints();
    }
}
