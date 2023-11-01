package invaders.state;

import invaders.App;
import invaders.singleton.Level1ConfigReader;
import invaders.singleton.Level2ConfigReader;
import invaders.singleton.Level3ConfigReader;
import invaders.singleton.Level4ConfigReader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class WinScreen extends GameStateClass {
    public WinScreen(App app) {
        Pane pane = this.getRootPane();
        Scene scene = this.getScene();

        GridPane grid = new GridPane();
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);
        grid.setPrefSize(600, 400);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(25);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHalignment(HPos.CENTER);
        col2.setPercentWidth(50);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(25);
        grid.getColumnConstraints().addAll(col1,col2,col3);


        Label title = new Label("You Have Won!!");

        Label info = new Label("Next level implementation in progress");
        info.setAlignment(Pos.BOTTOM_CENTER);

        grid.add(title, 1, 0, 2, 1);

        pane.getChildren().add(grid);
    }

    @Override
    public void stop() {

    }
}
