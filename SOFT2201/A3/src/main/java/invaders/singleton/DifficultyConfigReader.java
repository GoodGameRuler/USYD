package invaders.singleton;

import invaders.ConfigReader;
import invaders.builder.BunkerBuilder;
import invaders.builder.Director;
import invaders.builder.EnemyBuilder;
import invaders.entities.Player;
import invaders.gameobject.Bunker;
import invaders.gameobject.Enemy;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public abstract class DifficultyConfigReader {

    protected int level;
    protected int gameWidth;
    protected int gameHeight;
    protected JSONArray bunkerInfo;
    protected JSONArray enemiesInfo;
    protected JSONObject playerInfo;
    protected String config;

    protected void start() {

        if(config == null)
            throw new NullPointerException();

        ConfigReader.parse(config);

        // Get game width and height
        gameWidth = ((Long)((JSONObject) ConfigReader.getGameInfo().get("size")).get("x")).intValue();
        gameHeight = ((Long)((JSONObject) ConfigReader.getGameInfo().get("size")).get("y")).intValue();

        //Get player info
        this.playerInfo = ConfigReader.getPlayerInfo();
        this.bunkerInfo = ConfigReader.getBunkersInfo();
        this.enemiesInfo = ConfigReader.getEnemiesInfo();

    }

    public int getLevel() {
       return this.level;
    }
    public int getGameWidth() {
        return this.gameWidth;
    }
    public int getGameHeight() {
        return this.gameHeight;
    }
    public JSONArray getBunkerInfo() {
        return this.bunkerInfo;
    }
    public JSONArray getEnemiesInfo() {
        return this.enemiesInfo;
    }

    public JSONObject getPlayerInfo() {
        return this.playerInfo;

    }
}
