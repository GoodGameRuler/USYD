package invaders.entities.States;

import javafx.scene.image.Image;

import java.io.File;

public class GreenBunkerState implements BunkerColorState {

    private Image image;

    public GreenBunkerState(double height, double width) {
        this.image = new Image(new File("src/main/resources/red_green.png").toURI().toString(), width, height, true, true);
    }

    @Override
    public Image returnImage() {
        return this.image;
    }
}
