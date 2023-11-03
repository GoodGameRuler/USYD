package invaders.memento;

import invaders.entities.Player;
import invaders.gameobject.GameObject;
import invaders.rendering.Renderable;
import java.util.List;

import java.util.ArrayList;

public interface GameOriginator {
    public GameMemento save();
    public void setPlayer(Player player);
    public void setLeft(Boolean left);
    public void setRight(Boolean right);
    public void setScore(int score);
    public void setTimer(int frames);
    public void setGameObjects(List<Renderable> renderables, List<GameObject> gameObjects);
}
