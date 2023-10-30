package invaders;

import invaders.singleton.DifficultyConfigReader;
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
        this.model = new GameEngine();
        this.gameStates = new ArrayList<>();
        MenuScreen ms = new MenuScreen(this);
        this.gameStates.add(ms);
    }

    public void setLevel(DifficultyConfigReader dcr) {
        model.setDifficulty(dcr);
        this.window = new GameWindow(model);
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
