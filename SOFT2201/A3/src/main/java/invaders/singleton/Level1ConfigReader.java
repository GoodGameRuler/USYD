package invaders.singleton;

public class Level1ConfigReader extends DifficultyConfigReader {

    private static Level1ConfigReader instance;

    static {
        instance = null;
    }

    private Level1ConfigReader() {
        this.level = 1;
        this.config = "src/main/resources/config_easy.json";
        this.start();
    }
    public static Level1ConfigReader getInstance() {
        if (instance == null) {
            instance = new Level1ConfigReader();
        }
        return instance;
    }
}
