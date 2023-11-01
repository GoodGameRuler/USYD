package invaders.memento;

import invaders.entities.Player;

public interface GameOriginator {
    public GameMemento save();
    public void setPlayer(Player player);
    public void setLeft(Boolean left);
    public void setRight(Boolean right);
}
