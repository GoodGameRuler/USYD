package invaders.state;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public abstract class GameStateClass {
    private int width;
    private int height;
    private Scene scene;
    private Pane pane;

    public GameStateClass(){
        this.width = 720;
        this.height = 540;

        pane = new Pane();
        scene = new Scene(pane, width, height);
    }

    public Scene getScene() {
        return this.scene;
    }

    public Pane getRootPane() {
        return this.pane;
    }

    public void run() {
        return;
    }
}
