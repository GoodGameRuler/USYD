package invaders.entities.States;

import javafx.scene.image.Image;

import java.io.File;

public class RedBunkerState implements BunkerColorState {

    private Image image;

    public RedBunkerState(double height, double width) {
        this.image = new Image(new File("src/main/resources/bunker_red.png").toURI().toString(), width, height, true, true);
    }

    @Override
    public Image returnImage() {
        return this.image;
    }
}
