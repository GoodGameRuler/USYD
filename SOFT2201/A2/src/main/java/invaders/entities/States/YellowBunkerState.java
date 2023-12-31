package invaders.entities.States;

import javafx.scene.image.Image;

import java.io.File;

public class YellowBunkerState implements BunkerColorState {

    private Image image;

    public YellowBunkerState(double height, double width) {
        this.image = new Image(new File("src/main/resources/bunker_yellow.png").toURI().toString(), width, height, true, true);
    }

    @Override
    public Image returnImage() {
        return this.image;
    }
}
