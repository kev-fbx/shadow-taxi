import java.util.Properties;

/**
 * This class holds methods and attributes for Star objects that are rendered on GameScreen
 *
 * @author Kevin Tran
 */
public class Star extends Collectable {
    /**
     * Constructor for Star objects
     * @param gameProp    Properties object that holds game properties
     * @param x           Initial X-coordinate
     * @param y           Initial Y-coordinate
     */
    public Star(Properties gameProp, int x, int y) {
        super(x, y, gameProp.getProperty("gameObjects.invinciblePower.image"),
                Float.parseFloat(gameProp.getProperty("gameObjects.invinciblePower.radius")),
                Integer.parseInt(gameProp.getProperty("gameObjects.invinciblePower.maxFrames")));
    }

    /**
     * Checks collision state between `this` Star and the current Taxi
     *
     * @param object Empowerable object in game screen that collected Star
     */
    public void collide(Empowerable object) {
        if (isCollided((Entity) object)) {
            object.collectStar(this);
            setCollectableCollided(true);
        }
    }
}
