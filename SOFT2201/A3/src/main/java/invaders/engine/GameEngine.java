package invaders.engine;

import java.util.ArrayList;
import java.util.List;

import invaders.App;
import invaders.ConfigReader;
import invaders.builder.BunkerBuilder;
import invaders.builder.Director;
import invaders.builder.EnemyBuilder;
import invaders.factory.Projectile;
import invaders.gameobject.Bunker;
import invaders.gameobject.Enemy;
import invaders.gameobject.GameObject;
import invaders.entities.Player;
import invaders.gameobject.ScoreCollectable;
import invaders.memento.BasicGameMemento;
import invaders.memento.GameMemento;
import invaders.memento.GameOriginator;
import invaders.observer.Score;
import invaders.observer.Timer;
import invaders.rendering.Renderable;
import invaders.singleton.DifficultyConfigReader;
import invaders.state.GameStateClass;
import invaders.state.LoseScreen;
import invaders.state.WinScreen;
import org.json.simple.JSONObject;

/**
 * This class manages the main loop and logic of the game
 */
public class GameEngine implements GameOriginator {
	private List<GameObject> gameObjects = new ArrayList<>(); // A list of game objects that gets updated each frame
	private List<GameObject> pendingToAddGameObject = new ArrayList<>();
	private List<GameObject> pendingToRemoveGameObject = new ArrayList<>();

	private List<Renderable> pendingToAddRenderable = new ArrayList<>();
	private List<Renderable> pendingToRemoveRenderable = new ArrayList<>();

	private List<Renderable> renderables =  new ArrayList<>();
	private List<ScoreCollectable> collectables = new ArrayList<>();
	private Timer clock = new Timer();
	private Score score = new Score();

	private Player player;

	private boolean left;
	private boolean right;
	private int gameWidth;
	private int gameHeight;
	private int timer = 45;
	private App app;

	public GameEngine(){

	}

	public Timer getTimer() {
		return this.clock;
	}

	public Score getScore() {
		return this.score;
	}

	public List<ScoreCollectable> getCollectables() {
		return this.collectables;
	}

	public void setDifficulty(DifficultyConfigReader dcr){
		// Get game width and height
		gameWidth = dcr.getGameWidth();
		gameHeight = dcr.getGameHeight();

		renderables.clear();
		gameObjects.clear();

		//Get player info
		this.player = new Player(dcr.getPlayerInfo());
		renderables.add(player);


		Director director = new Director();
		BunkerBuilder bunkerBuilder = new BunkerBuilder();
		//Get Bunkers info
		for(Object eachBunkerInfo : dcr.getBunkerInfo()){
			Bunker bunker = director.constructBunker(bunkerBuilder, (JSONObject) eachBunkerInfo);
			gameObjects.add(bunker);
			renderables.add(bunker);
		}


		EnemyBuilder enemyBuilder = new EnemyBuilder();
		//Get Enemy info
		for(Object eachEnemyInfo: dcr.getEnemiesInfo()){
			Enemy enemy = director.constructEnemy(this,enemyBuilder,(JSONObject)eachEnemyInfo);
			this.collectables.add(enemy);
			gameObjects.add(enemy);
			renderables.add(enemy);
		}
	}

	public void setApp(App app) {
		this.app = app;
	}

	public void changeGameState(GameStateClass gs) {
		this.app.setGameState(gs);
	}


	/**
	 * Updates the game/simulation
	 */
	public void update(){
		timer+=1;

		movePlayer();

		boolean foundEnemy = false;

		for(Renderable rend : renderables) {
			if(rend.getRenderableObjectName().equals("Enemy") && rend.isAlive()) {
				foundEnemy = true;				
			}
		}
		
		if(!foundEnemy) {
			this.changeGameState(new WinScreen(this.app));
			return;
			
		} else if (!player.isAlive()) {
			this.changeGameState(new LoseScreen(this.app));
			return;
		}


		for(GameObject go: gameObjects){
			go.update(this);
		}

		for (int i = 0; i < renderables.size(); i++) {
			Renderable renderableA = renderables.get(i);
			for (int j = i+1; j < renderables.size(); j++) {
				Renderable renderableB = renderables.get(j);

				if((renderableA.getRenderableObjectName().equals("Enemy") && renderableB.getRenderableObjectName().equals("EnemyProjectile"))
						||(renderableA.getRenderableObjectName().equals("EnemyProjectile") && renderableB.getRenderableObjectName().equals("Enemy"))||
						(renderableA.getRenderableObjectName().equals("EnemyProjectile") && renderableB.getRenderableObjectName().equals("EnemyProjectile"))){
				}else{
					if(renderableA.isColliding(renderableB) && (renderableA.getHealth()>0 && renderableB.getHealth()>0)) {

						if(renderableA.getRenderableObjectName().equals("EnemyProjectile") && !renderableB.getRenderableObjectName().equals("PlayerProjectile")) {
							((Projectile) renderableA).setDestroyed();

						} else if(renderableB.getRenderableObjectName().equals("EnemyProjectile") && !renderableA.getRenderableObjectName().equals("PlayerProjectile")) {
							((Projectile) renderableB).setDestroyed();

						}

						renderableA.takeDamage(1);
						renderableB.takeDamage(1);
					}
				}
			}
		}


		// ensure that renderable foreground objects don't go off-screen
		int offset = 1;
		for(Renderable ro: renderables){
			if(!ro.getLayer().equals(Renderable.Layer.FOREGROUND)){
				continue;
			}
			if(ro.getPosition().getX() + ro.getWidth() >= gameWidth) {
				ro.getPosition().setX((gameWidth - offset) -ro.getWidth());
			}

			if(ro.getPosition().getX() <= 0) {
				ro.getPosition().setX(offset);
			}

			if(ro.getPosition().getY() + ro.getHeight() >= gameHeight) {
				ro.getPosition().setY((gameHeight - offset) -ro.getHeight());
			}

			if(ro.getPosition().getY() <= 0) {
				ro.getPosition().setY(offset);
			}
		}

	}

	public List<Renderable> getRenderables(){
		return renderables;
	}

	public List<GameObject> getGameObjects() {
		return gameObjects;
	}
	public List<GameObject> getPendingToAddGameObject() {
		return pendingToAddGameObject;
	}

	public List<GameObject> getPendingToRemoveGameObject() {
		return pendingToRemoveGameObject;
	}

	public List<Renderable> getPendingToAddRenderable() {
		return pendingToAddRenderable;
	}

	public List<Renderable> getPendingToRemoveRenderable() {
		return pendingToRemoveRenderable;
	}


	public void leftReleased() {
		this.left = false;
	}

	public void rightReleased(){
		this.right = false;
	}

	public void leftPressed() {
		this.left = true;
	}
	public void rightPressed(){
		this.right = true;
	}

	public boolean shootPressed(){
		if(timer>45 && player.isAlive()){
			Projectile projectile = player.shoot();
			gameObjects.add(projectile);
			renderables.add(projectile);
			timer=0;
			return true;
		}
		return false;
	}

	private void movePlayer(){
		if(left){
			player.left();
		}

		if(right){
			player.right();
		}
	}

	public int getGameWidth() {
		return gameWidth;
	}

	public int getGameHeight() {
		return gameHeight;
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public GameMemento save() {
		BasicGameMemento gameMemento = new BasicGameMemento(this);
		gameMemento.setLeft(left);
		gameMemento.setRight(right);
		gameMemento.setPlayer(player);
		gameMemento.setTimer(clock);
		gameMemento.setScore(score);
		gameMemento.setGameObjects(renderables, gameObjects);
		return gameMemento;
	}

	public void setGameObjects(List<Renderable> renderables, List<GameObject> gameObjects) {
		for(Renderable rend : this.renderables) {
			if(!rend.equals(player)) {
				rend.takeDamage(rend.getHealth());
			}
		}
		this.renderables.addAll(renderables);
		this.gameObjects.addAll(gameObjects);
	}

	@Override
	public void setPlayer(Player player) {
		renderables.add(player);
		this.player.takeDamage(this.player.getHealth());
		this.player.isAlive();
		this.player = player;
	}

	@Override
	public void setLeft(Boolean left) {
		this.left = left;

	}

	@Override
	public void setRight(Boolean right) {
		this.right = right;

	}

	@Override
	public void setScore(int score) {
		this.score.setScore(score);
		this.score.informObservers();
	}

	@Override
	public void setTimer(int frames) {
		this.clock.setFrames(frames);
		this.clock.informObservers();
	}

	public void setEngineState(GameMemento gm) {

	}
}
