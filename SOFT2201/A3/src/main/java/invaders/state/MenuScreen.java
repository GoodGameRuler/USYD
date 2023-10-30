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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.regex.PatternSyntaxException;

public class MenuScreen extends GameStateClass {
    public MenuScreen(App app) {
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

        Button btnLv1 = new Button("Easy");
        Button btnLv2 = new Button("Medium");
        Button btnLv3 = new Button("Hard");
        Button btnLv4 = new Button("Hardcore");
        btnLv4.setDisable(true);

        btnLv1.defaultButtonProperty();

        EventHandler<ActionEvent> lv1 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                app.setLevel(Level1ConfigReader.getInstance());
                app.nextScene();
            }
        };
        EventHandler<ActionEvent> lv2 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                app.setLevel(Level2ConfigReader.getInstance());
                app.nextScene();
            }
        };

        EventHandler<ActionEvent> lv3 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                app.setLevel(Level3ConfigReader.getInstance());
                app.nextScene();
            }
        };

        EventHandler<ActionEvent> lv4 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                app.setLevel(Level4ConfigReader.getInstance());
                app.nextScene();
            }
        };

        btnLv1.setOnAction(lv1);
        btnLv2.setOnAction(lv2);
        btnLv3.setOnAction(lv3);
        btnLv4.setOnAction(lv4);

        Label title = new Label("Choose Difficulty");
        title.setAlignment(Pos.TOP_CENTER);

        Label info = new Label("Press enter to select default level");
        info.setAlignment(Pos.BOTTOM_CENTER);

        grid.add(title, 1, 0, 2, 1);
        grid.add(btnLv1, 1, 1, 2, 1);
        grid.add(btnLv2, 1, 2, 2, 1);
        grid.add(btnLv3, 1, 3, 2, 1);
        grid.add(btnLv4, 1, 4, 2, 1);
        grid.add(info, 1, 7, 2, 1);

        pane.getChildren().add(grid);

    }
}
