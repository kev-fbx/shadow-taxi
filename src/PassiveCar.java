import bagel.Input;

import java.util.Properties;

/**
 * This class holds attributes and methods for passive car objects that appear in GameScreen
 *
 * @author Kevin Tran
 */
public class PassiveCar extends Car {
    /**
     * Passive Car constructor to instantiate passives
     *
     * @param gameProp Properties file holding game properties
     * @param x        Initial X-coordinate
     * @param y        Initial Y-coordinate
     */
    public PassiveCar(Properties gameProp, int x, int y) {
        super(gameProp, x, y,
                String.format(gameProp.getProperty("gameObjects.otherCar.image"), MiscUtils.getRandomInt(1, 3)),
                (int) (100 * Double.parseDouble(gameProp.getProperty("gameObjects.otherCar.health"))),
                (int) (100 * Double.parseDouble(gameProp.getProperty("gameObjects.otherCar.damage"))),
                Integer.parseInt(gameProp.getProperty("gameObjects.otherCar.minSpeedY")),
                Integer.parseInt(gameProp.getProperty("gameObjects.otherCar.maxSpeedY")),
                Float.parseFloat(gameProp.getProperty("gameObjects.otherCar.radius")));
    }

    /**
     * Updating logic for passive car objects
     *
     * @param input The current mouse/keyboard input.
     */
    public void update(Input input) {
        render();
        if (input != null) {
            adjustToInputMovement(input);
        }

        if (isDead()) {
            setSpeedY(0);
        }

        if (isWasHit()) {
            pauseMovement();
        }

        moveRelative();
    }
}