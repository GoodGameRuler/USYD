package invaders.engine;

import java.util.List;
import java.util.ArrayList;

import invaders.entities.EntityViewImpl;
import invaders.entities.SpaceBackground;
import invaders.factory.EnemyProjectile;
import invaders.gameobject.Enemy;
import invaders.gameobject.ScoreCollectable;
import invaders.memento.GameCareTaker;
import invaders.memento.GameMemento;
import invaders.observer.ScoreObserver;
import invaders.observer.Timer;
import invaders.observer.Score;
import invaders.observer.TimerObserver;
import invaders.singleton.Level3ConfigReader;
import invaders.singleton.Level4ConfigReader;
import invaders.state.GameStateClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import invaders.entities.EntityView;
import invaders.rendering.Renderable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class GameWindow extends GameStateClass implements GameCareTaker {
	private final int width;
    private final int height;
	private Scene scene;
    private Pane pane;
    private GameEngine model;
    private List<EntityView> entityViews =  new ArrayList<EntityView>();
    private Renderable background;

    private double xViewportOffset = 0.0;
    private double yViewportOffset = 0.0;
    private Timeline timeline;
    // private static final double VIEWPORT_MARGIN = 280.0;
    private GameMemento prevCheckpoint = null;
    private ScoreObserver so;
    private TimerObserver to;

	public GameWindow(GameEngine model){
        this.model = model;
		this.width =  model.getGameWidth();
        this.height = model.getGameHeight();

        pane = new Pane();
        scene = new Scene(pane, width, height);
        this.background = new SpaceBackground(model, pane);

        KeyboardInputHandler keyboardInputHandler = new KeyboardInputHandler(this.model);

        scene.setOnKeyPressed(keyboardInputHandler::handlePressed);
        scene.setOnKeyReleased(keyboardInputHandler::handleReleased);


        so = new ScoreObserver(model.getScore());
        to = new TimerObserver(model.getTimer());

        model.getTimer().attach(to);
        model.getScore().attach(so);

        for (ScoreCollectable sc : model.getCollectables()) {
            sc.setScoreCollector(model.getScore());
        }


        HBox hb = new HBox();
        hb.setSpacing(20);
        hb.setAlignment(Pos.TOP_CENTER);

        Button save = new Button("Save");
        Button undo = new Button("Undo");
        Button cheatFA = new Button("Cheat FA");
        Button cheatSA = new Button("Cheat SA");
        Button cheatFP = new Button("Cheat FP");
        Button cheatSP = new Button("Cheat SP");

        save.setFocusTraversable(false);
        undo.setFocusTraversable(false);
        cheatFA.setFocusTraversable(false);
        cheatSA.setFocusTraversable(false);
        cheatFP.setFocusTraversable(false);
        cheatSP.setFocusTraversable(false);

        ArrayList<Renderable> allRendarables = new ArrayList<>();
        allRendarables.addAll(model.getRenderables());
        allRendarables.addAll(model.getPendingToAddRenderable());
        EventHandler<ActionEvent> saveEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                save();
            }
        };

        EventHandler<ActionEvent> undoEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                undo();
            }
        };

        EventHandler<ActionEvent> cheatFAEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                for(Renderable rend : allRendarables) {
                    if(rend.getRenderableObjectName().equals("Enemy")) {
                        if(((Enemy) rend).getScore() == 4) {
                            rend.takeDamage(rend.getHealth());
                        }

                    }
                }
            }
        };

        EventHandler<ActionEvent> cheatSAEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                for(Renderable rend : allRendarables) {
                    if(rend.getRenderableObjectName().equals("Enemy")) {
                        if(((Enemy) rend).getScore() == 3) {
                            rend.takeDamage(rend.getHealth());
                        }

                    }
                }
            }
        };

        EventHandler<ActionEvent> cheatFPEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                for(Renderable rend : allRendarables) {
                    if(rend.getRenderableObjectName().equals("Enemy")) {
                        Enemy enemy = (Enemy) rend;
                        if(enemy.getScore() == 4) {
                            enemy.getEnemyProjectile().forEach(ep -> {
                                ep.takeDamage(ep.getHealth());
                            });
                        }
                    }
                }
            }
        };

        EventHandler<ActionEvent> cheatSPEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                for(Renderable rend : allRendarables) {
                    if(rend.getRenderableObjectName().equals("Enemy")) {
                        Enemy enemy = (Enemy) rend;
                        if(enemy.getScore() == 3) {
                            enemy.getEnemyProjectile().forEach(ep -> {
                                ep.takeDamage(ep.getHealth());
                            });
                        }
                    }
                }
            }
        };
        save.setOnAction(saveEvent);
        undo.setOnAction(undoEvent);

        cheatFA.setOnAction(cheatFAEvent);
        cheatSA.setOnAction(cheatSAEvent);
        cheatSP.setOnAction(cheatSPEvent);
        cheatFP.setOnAction(cheatFPEvent);

        hb.getChildren().addAll(so.getLabel(), to.getLabel(), save, undo, cheatFA, cheatSA, cheatFP, cheatSP);
        pane.getChildren().add(hb);

    }

	public void run() {
         this.timeline = new Timeline(new KeyFrame(Duration.millis(17), t -> this.draw()));

         timeline.setCycleCount(Timeline.INDEFINITE);
         timeline.play();
    }

    @Override
    public void stop() {
        this.timeline.stop();
    }


    private void draw(){
        model.update();

        model.getTimer().incrementTime();
        model.getTimer().informObservers();

        List<Renderable> renderables = model.getRenderables();
        for (Renderable entity : renderables) {
            boolean notFound = true;
            for (EntityView view : entityViews) {
                if (view.matchesEntity(entity)) {
                    notFound = false;
                    view.update(xViewportOffset, yViewportOffset);
                    break;
                }
            }
            if (notFound) {
                EntityView entityView = new EntityViewImpl(entity);
                entityViews.add(entityView);
                pane.getChildren().add(entityView.getNode());
            }
        }

        for (Renderable entity : renderables){
            if (!entity.isAlive()){
                for (EntityView entityView : entityViews){
                    if (entityView.matchesEntity(entity)){
                        entityView.markForDelete();
                    }
                }

                // Also remove dead objects if not already to remove
                if(!model.getPendingToRemoveRenderable().contains(entity))
                    model.getPendingToRemoveRenderable().add(entity);
            }
        }

        for (EntityView entityView : entityViews) {
            if (entityView.isMarkedForDelete()) {
                pane.getChildren().remove(entityView.getNode());
            }
        }


        model.getGameObjects().removeAll(model.getPendingToRemoveGameObject());
        model.getGameObjects().addAll(model.getPendingToAddGameObject());
        model.getRenderables().removeAll(model.getPendingToRemoveRenderable());
        model.getRenderables().addAll(model.getPendingToAddRenderable());

        model.getPendingToAddGameObject().clear();
        model.getPendingToRemoveGameObject().clear();
        model.getPendingToAddRenderable().clear();
        model.getPendingToRemoveRenderable().clear();

        entityViews.removeIf(EntityView::isMarkedForDelete);

    }

	public Scene getScene() {
        return scene;
    }

    @Override
    public void undo() {
        if(this.prevCheckpoint != null) {
            this.prevCheckpoint.undo();
            this.prevCheckpoint = null;
        }
    }

    @Override
    public void save() {
        this.prevCheckpoint = this.model.save();

    }
}
