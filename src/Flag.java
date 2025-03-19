import bagel.Input;

import java.util.Properties;

/**
 * A class representing the trip end flag in the game play.
 * Objects of this class will only move up and down based on the keyboard input. No other functionalities needed.
 *
 * @author SWEN20003 Teaching Staff, adapted by Kevin Tran
 */
public class Flag extends Entity {

    /**
     * Constructor for Flag objects
     *
     * @param gameProp Properties file that holds all game properties
     * @param x        Initial X-coordinate of Driver object
     * @param y        Initial Y-coordinate of Driver object
     */
    public Flag(Properties gameProp, int x, int y) {
        super(x, y, gameProp.getProperty("gameObjects.tripEndFlag.image"),
                Float.parseFloat(gameProp.getProperty("gameObjects.tripEndFlag.radius")));
    }

    /**
     * Rendering logic for Flag objects
     */
    @Override
    public void render() {
        getSprite().draw(getX(), getY());
    }

    /**
     * Move the object in y direction according to the keyboard input, and render the trip flag image.
     *
     * @param input The current mouse/keyboard input.
     */
    public void update(Input input) {
        if (input != null) {
            adjustToInputMovement(input);
        }
        moveRelative();
        render();
    }
}
