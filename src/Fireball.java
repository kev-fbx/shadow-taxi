import bagel.Input;

import java.util.Properties;

/**
 * This class contains attributes and methods relevant to Fireball objects that are rendered in GameScreen
 *
 * @author Kevin Tran
 */
public class Fireball extends Entity {
    /**
     * Vertical speed of fireball object
     */
    private final int SPEED_Y;
    /**
     * Damage value of Fireball objects
     */
    private final int DMG;
    /**
     * Boolean value tracking if fireball has collided with Taxi
     */
    private boolean hitObject;

    /**
     * Fireball constructor to instantiate fireball game objects
     *
     * @param gameProp Properties file holding game properties
     * @param x        Initial X-coordinate
     * @param y        Initial Y-coordinate
     */
    public Fireball(Properties gameProp, int x, int y) {
        super(x, y, gameProp.getProperty("gameObjects.fireball.image"),
                Float.parseFloat(gameProp.getProperty("gameObjects.fireball.radius")));
        this.SPEED_Y = Integer.parseInt(getGameProp().getProperty("gameObjects.fireball.shootSpeedY"));
        this.DMG = (int) (100 * Double.parseDouble(gameProp.getProperty("gameObjects.fireball.damage")));
        this.hitObject = false;
    }

    /**
     * Render logic of fireball objects
     */
    @Override
    public void render() {
        if (!hitObject) {
            getSprite().draw(getX(), getY());
        }
    }

    /**
     * Frame update logic of fireballs
     *
     * @param input The current mouse/keyboard input.
     */
    @Override
    public void update(Input input) {
        if (input != null) {
            adjustToInputMovement(input);
        }
        moveRelative();
        render();
    }

    /**
     * Relative movement of fireball
     */
    @Override
    public void moveRelative() {
        setY(getY() + SCROLL_SPEED * getMoveY() - SPEED_Y);
    }

    /**
     * Collision checking logic between `this` fireball and current Taxi object
     *
     * @param object Current object in GameScreen
     */
    public void collide(Damageable object) {
        if (!hitObject) {
            object.takeDamage(getDmg());
            setHitObject(true);
        }
    }

    /**
     * Gets damage value of fireball
     *
     * @return Integer value of fireball damage
     */
    public int getDmg() {
        return DMG;
    }

    /**
     * Sets hitTaxi to true when `this` fireball collides with current Taxi
     */
    public void setHitObject(boolean hitObject) {
        this.hitObject = hitObject;
    }
}
