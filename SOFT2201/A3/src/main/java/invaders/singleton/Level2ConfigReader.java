package invaders.singleton;

public class Level2ConfigReader extends DifficultyConfigReader {
    private static Level2ConfigReader instance;

    static {
        instance = null;
    }

    private Level2ConfigReader() {
        this.level = 2;
        this.config = "src/main/resources/config_medium.json";
        this.start();
    }
    public static Level2ConfigReader getInstance() {
        if (instance == null) {
            instance = new Level2ConfigReader();
        }
        return instance;
    }
}
