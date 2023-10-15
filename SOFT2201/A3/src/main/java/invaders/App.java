package invaders;

import invaders.state.GameStateClass;
import invaders.state.MenuScreen;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import invaders.engine.GameEngine;
import invaders.engine.GameWindow;

import java.util.ArrayList;

public class App extends Application {

    private Stage primaryStage;
    private GameEngine model;
    private GameWindow window;
    private GameStateClass currentGameState;
    private ArrayList<GameStateClass> gameStates;

    public static void main(String[] args) {
        launch(args);
    }

    public App() {
        this.model = new GameEngine("src/main/resources/config_easy.json");
        this.window = new GameWindow(model);
        this.gameStates = new ArrayList<>();
        MenuScreen ms = new MenuScreen();

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(12);

        HBox hbButtons = new HBox();
        hbButtons.setSpacing(10.0);

        Button btnLv1 = new Button("Level 1");
        Button btnLv2 = new Button("Level 2");
        Button btnLv3 = new Button("Level 3");
        Button btnLv4 = new Button("Level 4");

        EventHandler<ActionEvent> lv1 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                nextScene();
            }
        };
        EventHandler<ActionEvent> lv2 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
            }
        };

        EventHandler<ActionEvent> lv3 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
            }
        };

        EventHandler<ActionEvent> lv4 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
            }
        };

        btnLv1.setOnAction(lv1);
        btnLv2.setOnAction(lv2);
        btnLv3.setOnAction(lv3);
        btnLv4.setOnAction(lv4);

        hbButtons.getChildren().addAll(btnLv1, btnLv2, btnLv3, btnLv4);
        grid.add(hbButtons, 0, 2, 2, 1);

        ms.getRootPane().getChildren().add(grid);

        this.gameStates.add(ms);
        this.gameStates.add(this.window);
    }

    @Override
    public void start(Stage primaryStage) {
        currentGameState = this.gameStates.remove(0);
        currentGameState.run();

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Space Invaders");
        this.primaryStage.setScene(currentGameState.getScene());
        this.primaryStage.show();

        currentGameState.run();
    }

    public void setGameState(GameStateClass gs) {
        currentGameState = gs;
        currentGameState.run();
        this.primaryStage.setScene(gs.getScene());
        this.primaryStage.show();
        currentGameState.run();
    }

    public void nextScene() {
        if(this.gameStates.isEmpty()) {
            System.out.println("Game Over!");
            return;
        }
        this.setGameState(this.gameStates.remove(0));
    }
}
