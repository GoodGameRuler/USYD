package invaders.entities;

import javafx.scene.Node;
import invaders.rendering.Renderable;

public interface EntityView {
    void update(double xViewportOffset, double yViewportOffset);

    boolean matchesEntity(Renderable entity);

    void markForDelete();

    Node getNode();

    boolean isMarkedForDelete();
}
package invaders.entities;

import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import javafx.scene.Node;
import javafx.scene.image.ImageView;

public class EntityViewImpl implements EntityView {
    private Renderable entity;
    private Vector2D position;
    private boolean delete = false;
    private ImageView node;

    public EntityViewImpl(Renderable entity) {
        this.entity = entity;
        this.position = entity.getPosition();
        node = new ImageView(entity.getImage());
        node.setViewOrder(getViewOrder(entity.getLayer()));
        update(0.0, 0.0);
    }

    private static double getViewOrder(Renderable.Layer layer) {
        switch (layer) {
            case BACKGROUND: return 100.0;
            case FOREGROUND: return 50.0;
            case EFFECT: return 25.0;
            default: throw new IllegalStateException("Javac doesn't understand how enums work so now I have to exist");
        }
    }

    @Override
    public void update(double xViewportOffset, double yViewportOffset) {
        if (!node.getImage().equals(entity.getImage())) {
            node.setImage(entity.getImage());
        }
        node.setX(position.getX() - xViewportOffset);
        node.setY(position.getY() - yViewportOffset);
        node.setFitHeight(entity.getHeight());
        node.setFitWidth(entity.getWidth());
        node.setPreserveRatio(true);
        delete = false;
    }

    @Override
    public boolean matchesEntity(Renderable entity) {
        return this.entity.equals(entity);
    }

    @Override
    public void markForDelete() {
        delete = true;
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public boolean isMarkedForDelete() {
        return delete;
    }
}
package invaders.entities;

import invaders.factory.PlayerProjectile;
import invaders.factory.PlayerProjectileFactory;
import invaders.factory.Projectile;
import invaders.factory.ProjectileFactory;
import invaders.physics.Collider;
import invaders.physics.Moveable;
import invaders.physics.Vector2D;
import invaders.rendering.Animator;
import invaders.rendering.Renderable;

import invaders.strategy.NormalProjectileStrategy;
import javafx.scene.image.Image;
import org.json.simple.JSONObject;

import java.io.File;

public class Player implements Moveable, Renderable {

    private final Vector2D position;
    private double health;
    private double velocity;

    private final double width = 20;
    private final double height = 20;
    private final Image image;
    private ProjectileFactory playerProjectileFactory = new PlayerProjectileFactory();

    private JSONObject playerInfo;


    public Player(JSONObject playerInfo){
        int x = ((Long)((JSONObject)(playerInfo.get("position"))).get("x")).intValue();
        int y = ((Long)((JSONObject)(playerInfo.get("position"))).get("y")).intValue();

        this.image = new Image(new File("src/main/resources/player.png").toURI().toString(), width, height, true, true);
        this.position = new Vector2D(x,y);
        this.health = ((Long) playerInfo.get("lives")).intValue();
        this.velocity = ((Long) playerInfo.get("speed")).intValue();
    }

    private Player(Image image, Vector2D position, Double health, Double velocity) {
        this.image = image;
        this.position = position;
        this.health = health;
        this.velocity = velocity;
    }

    @Override
    public void takeDamage(double amount) {
        this.health -= amount;
    }

    @Override
    public double getHealth() {
        return this.health;
    }

    @Override
    public boolean isAlive() {
        return this.health > 0;
    }

    @Override
    public void up() {
        return;
    }

    @Override
    public void down() {
        return;
    }

    @Override
    public void left() {
        this.position.setX(this.position.getX() - this.velocity);
    }

    @Override
    public void right() {
        this.position.setX(this.position.getX() + this.velocity);
    }

    public Projectile shoot(){
        return playerProjectileFactory.createProjectile(new Vector2D(this.position.getX() + 5 ,this.position.getY() - 10),new NormalProjectileStrategy(),null);
    }

    @Override
    public Image getImage() {
        return this.image;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public Vector2D getPosition() {
        return position;
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }

    @Override
    public String getRenderableObjectName() {
        return "Player";
    }

    @Override
    public Player clone() {
        return new Player(this.image, this.position.clone(), this.health, this.velocity);
    }

}package invaders.entities;

import invaders.engine.GameEngine;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class SpaceBackground implements Renderable {
	private Rectangle space;
	private Pane pane;
    private GameEngine model;

	public SpaceBackground(GameEngine engine, Pane pane){
		double width = pane.getWidth();
		double height = pane.getHeight();
		space = new Rectangle(0, 0, width, height);
		space.setFill(Paint.valueOf("BLACK"));
		space.setViewOrder(1000.0);

		pane.getChildren().add(space);
	}

	public Image getImage() {
		return null;
	}

	@Override
	public double getWidth() {
		return 0;
	}

	@Override
	public double getHeight() {
		return 0;
	}

	@Override
	public Vector2D getPosition() {
		return null;
	}

	@Override
	public Layer getLayer() {
		return Layer.BACKGROUND;
	}

	@Override
	public boolean isAlive() {
		return true;
	}

	@Override
	public void takeDamage(double amount) {}

	@Override
	public double getHealth() {
		return 0;
	}

	@Override
	public String getRenderableObjectName() {
		return "background";
	}
}
package invaders.rendering;

import javafx.scene.image.Image;

public interface Animation {
    public String getName();
    public Image getCurrentFrame();
    public void next();
}
package invaders.rendering;

import java.util.List;

public class Animator {
	private final List<Animation> animations;
	private Animation state;

	public Animator(List<Animation> animations){
		if(animations.isEmpty()){
			throw new IllegalArgumentException("Animations list must contain at least 1 animation!");
		}
		
		this.animations = animations;
		this.state = animations.get(0);
	}

	public void setState(String name){
		this.state = animations.stream().filter(a -> a.getName().equals(name)).findFirst().orElse(animations.get(0));
	}

	public Animation getState(){
		return this.state;
	}

}
package invaders.rendering;

import invaders.physics.Collider;
import invaders.physics.Vector2D;
import javafx.scene.image.Image;

/**
 * Represents something that can be rendered
 */
public interface Renderable {

    public Image getImage();

    public double getWidth();
    public double getHeight();

    public Vector2D getPosition();

    public Renderable.Layer getLayer();

    public boolean isAlive();
    public void takeDamage(double amount);

    public double getHealth();

    /**
     * The set of available layers
     */
    public static enum Layer {
        BACKGROUND, FOREGROUND, EFFECT
    }

    public default boolean isColliding(Renderable col) {
        double minX1 = this.getPosition().getX();
        double maxX1 = this.getPosition().getX() + this.getWidth();
        double minY1 = this.getPosition().getY();
        double maxY1 = this.getPosition().getY() + this.getHeight();

        double minX2 = col.getPosition().getX();
        double maxX2 = col.getPosition().getX() + col.getWidth();
        double minY2 = col.getPosition().getY();
        double maxY2 = col.getPosition().getY() + col.getHeight();

        if (maxX1 < minX2 || maxX2 < minX1) {
            return false; // No overlap in the x-axis
        }

        if (maxY1 < minY2 || maxY2 < minY1) {
            return false; // No overlap in the y-axis
        }

        return true; // Overlap in both x-axis and y-axis
    }

    public String getRenderableObjectName();
}
package invaders.physics;

public class BoxCollider implements Collider {

    private double width;
    private double height;
    private Vector2D position;

    public BoxCollider(double width, double height, Vector2D position){
        this.height = height;
        this.width = width;
        this.position = position;
    }

    @Override
    public double getWidth() {
        return this.width;
    }

    @Override
    public double getHeight() {
        return this.height;
    }

    @Override
    public Vector2D getPosition(){
        return this.position;
    }
}
package invaders.physics;

public interface Collider {

    public double getWidth();

    public double getHeight();

    public Vector2D getPosition();

    public default boolean isColliding(Collider col) {
        double minX1 = this.getPosition().getX();
        double maxX1 = this.getPosition().getX() + this.getWidth();
        double minY1 = this.getPosition().getY();
        double maxY1 = this.getPosition().getY() + this.getHeight();

        double minX2 = col.getPosition().getX();
        double maxX2 = col.getPosition().getX() + col.getWidth();
        double minY2 = col.getPosition().getY();
        double maxY2 = col.getPosition().getY() + col.getHeight();

        if (maxX1 < minX2 || maxX2 < minX1) {
            return false; // No overlap in the x-axis
        }

        if (maxY1 < minY2 || maxY2 < minY1) {
            return false; // No overlap in the y-axis
        }

        return true; // Overlap in both x-axis and y-axis
    }
}package invaders.physics;

// represents something that can move up, down, left, right
public interface Moveable {

	public void up();

	public void down();

	public void left();

	public void right();
}
package invaders.physics;

/**
 * A utility class for storing position information
 */
public class Vector2D {

	private double x;
	private double y;

	public Vector2D(double x, double y){
		this.x = x;
		this.y = y;
	}

	public double getX(){
		return this.x;
	}

	public double getY(){
		return this.y;
	}

	public void setX(double x){
		this.x = x;
	}

	public void setY(double y){
		this.y = y;
	}

	@Override
	public Vector2D clone() {
		return new Vector2D(x, y);
	}
}
