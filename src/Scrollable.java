import java.util.Properties;

/**
 * Interface for implementing the vertical movement effect on the GameScreen
 *
 * @author Kevin Tran
 */
public interface Scrollable {
    /**
     * Properties file that contains game information
     */
    Properties GAME_PROP = IOUtils.readPropertiesFile("res/app.properties");
    /**
     * Number of pixels to move game objects when corresponding input is entered
     */
    int SCROLL_SPEED = Integer.parseInt(GAME_PROP.getProperty("gameObjects.taxi.speedY"));
}
