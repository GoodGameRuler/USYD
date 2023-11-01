package invaders.memento;

import invaders.entities.Player;
import invaders.gameobject.GameObject;
import invaders.gameobject.ScoreCollectable;
import invaders.rendering.Renderable;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void undo() {
        this.gameOriginator.setPlayer(player);
        this.gameOriginator.setLeft(left);
        this.gameOriginator.setRight(right);
    }
}
