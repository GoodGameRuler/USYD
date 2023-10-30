package invaders.singleton;

public class Level4ConfigReader extends DifficultyConfigReader {
    private static Level4ConfigReader instance;

    static {
        instance = null;
    }

    private Level4ConfigReader() {
        this.level = 4;
        this.config = "src/main/resources/config_easy.json";
        this.start();
    }
    public static Level4ConfigReader getInstance() {
        if (instance == null) {
            instance = new Level4ConfigReader();
        }
        return instance;
    }
}
