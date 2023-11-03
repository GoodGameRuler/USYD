package invaders.memento;

import invaders.entities.Player;
import invaders.factory.EnemyProjectile;
import invaders.factory.PlayerProjectile;
import invaders.gameobject.Enemy;
import invaders.gameobject.GameObject;
import invaders.gameobject.ScoreCollectable;
import invaders.observer.Score;
import invaders.observer.Timer;
import invaders.rendering.Renderable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

public class BasicGameMemento implements GameMemento {

    private List<GameObject> gameObjects = new ArrayList<>(); // A list of game objects that gets updated each frame
    private List<GameObject> pendingToAddGameObject = new ArrayList<>();
    private List<GameObject> pendingToRemoveGameObject = new ArrayList<>();

    private List<Renderable> pendingToAddRenderable = new ArrayList<>();
    private List<Renderable> pendingToRemoveRenderable = new ArrayList<>();

    private List<Renderable> renderables =  new ArrayList<>();
    private List<ScoreCollectable> collectables = new ArrayList<>();

    private Player player;

    private boolean left;
    private boolean right;
    private int timer = 45;
    private GameOriginator gameOriginator;
    private int frames;
    private int score;

    public BasicGameMemento(GameOriginator go) {
        this.gameOriginator = go;
    }

    public GameMemento setLeft(boolean left) {
        this.left = left;
        return this;
    }

    public GameMemento setRight(boolean right) {
        this.right = right;
        return this;
    }

    public GameMemento setPlayer(Player player) {
        this.player = player.clone();
        return this;
    }

    public void setTimer(Timer timer) {
        this.frames = timer.getFrames();
    }

    public void setScore(Score score) {
        this.score = score.getScore();
    }

    @Override
    public void undo() {
        this.gameOriginator.setPlayer(player);
        this.gameOriginator.setLeft(left);
        this.gameOriginator.setRight(right);
        this.gameOriginator.setScore(score);
        this.gameOriginator.setTimer(frames);
        this.gameOriginator.setGameObjects(renderables, gameObjects);
    }

    public void setGameObjects(List<Renderable> renderables, List<GameObject> gameObjects) {
        this.renderables = new ArrayList<Renderable>();
        this.gameObjects = new ArrayList<GameObject>();

//        enemies
        List<Renderable> enemies = renderables.stream().filter(renderable -> renderable.getRenderableObjectName().equals("Enemy")).toList();;
        List<Renderable> enemyProjectiles = renderables.stream().filter(renderable -> renderable.getRenderableObjectName().equals("EnemyProjectile")).toList();;
        List<Renderable> Bunkers = renderables.stream().filter(renderable -> renderable.getRenderableObjectName().equals("Bunker")).filter(Renderable::isAlive).toList();;

        List<Renderable> playerProjectiles = renderables.stream().filter(renderable -> renderable.getRenderableObjectName().equals("PlayerProjectile")).toList();;

        for(Renderable enemy : enemies) {
            Enemy castEnemy = (Enemy) enemy;

            Enemy enemyClone = castEnemy.clone();

            this.renderables.add(enemyClone);
            this.gameObjects.add(enemyClone);
        }

        for(Renderable enemyProjectile : enemyProjectiles) {
            EnemyProjectile castEnemyProjectile = (EnemyProjectile) enemyProjectile;
            PlayerProjectile projectileClone = castEnemyProjectile.clone();

            this.renderables.add(projectileClone);
            this.gameObjects.add(projectileClone);
        }

        for(Renderable playerProjectile : playerProjectiles) {
            PlayerProjectile castPlayerProjectile = (PlayerProjectile) playerProjectile;
            PlayerProjectile projectileClone = castPlayerProjectile.clone();

            this.renderables.add(projectileClone);
            this.gameObjects.add(projectileClone);
        }
    }
}
