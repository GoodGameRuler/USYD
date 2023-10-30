package invaders.singleton;

public class Level3ConfigReader extends DifficultyConfigReader {
    private static Level3ConfigReader instance;

    static {
        instance = null;
    }

    private Level3ConfigReader() {
        this.level = 3;
        this.config = "src/main/resources/config_hard.json";
        this.start();
    }
    public static Level3ConfigReader getInstance() {
        if (instance == null) {
            instance = new Level3ConfigReader();
        }
        return instance;
    }
}
