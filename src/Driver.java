import bagel.Input;
import bagel.Keys;

import java.util.Properties;

/**
 * This class contains the methods and attributes for the Driver entity that is rendered in GameScreen
 *
 * @author Kevin Tran
 */
public class Driver extends Entity implements Damageable, Empowerable, Bleedable {
    /**
     * Health of the driver object
     */
    private int health;
    /**
     * Horizontal movement speed of Driver
     */
    private final int SPEED_X;
    /**
     * Vertical movement speed of Driver
     */
    private final int SPEED_Y;
    /**
     * Detection radius for taxi interaction
     */
    private final float DETECT_RAD;
    /**
     * Boolean value tracking if Driver is ejected from Taxi
     */
    private boolean outOfTaxi;
    /**
     * Invincibility status of Driver when star is collected
     */
    private boolean isInvincible;
    /**
     * Current coin of driver
     */
    private Coin coinPower;
    /**
     * Current star of driver
     */
    private Star starPower;

    /**
     * Constructor for Driver object
     *
     * @param gameProp Properties file that holds all game properties
     * @param x        Initial X-coordinate of Driver object
     * @param y        Initial Y-coordinate of Driver object
     */
    public Driver(Properties gameProp, int x, int y) {
        super(x, y, gameProp.getProperty("gameObjects.driver.image"),
                Float.parseFloat(gameProp.getProperty("gameObjects.driver.radius")));
        this.health = (int) (100 * Double.parseDouble(getGameProp().getProperty("gameObjects.driver.health")));
        this.DETECT_RAD = Float.parseFloat(getGameProp().getProperty("gameObjects.driver.taxiGetInRadius"));
        this.SPEED_X = Integer.parseInt(getGameProp().getProperty("gameObjects.driver.walkSpeedX"));
        this.SPEED_Y = Integer.parseInt(getGameProp().getProperty("gameObjects.driver.walkSpeedY"));
        this.outOfTaxi = false;
    }

    /**
     * Rendering logic for Driver object
     */
    @Override
    public void render() {
        getSprite().draw(getX(), getY());
    }

    /**
     * Updating logic for Driver object
     *
     * @param input The current mouse/keyboard input.
     */
    @Override
    public void update(Input input) {
        render();

        if (input != null) {
            adjustToInputMovement(input);
        }
    }

    /**
     * Detects user input and adjusts attributes according to keyboard input
     *
     * @param input The current mouse/keyboard input.
     */
    @Override
    public void adjustToInputMovement(Input input) {
        if (input.isDown(Keys.LEFT)) {
            setX(getX() - SPEED_X);
        } else if (input.isDown(Keys.RIGHT)) {
            setX(getX() + SPEED_X);
        }
        if (outOfTaxi) {
            if (input.isDown(Keys.UP)) {
                setY(getY() - SPEED_Y);
            } else if (input.isDown(Keys.DOWN)) {
                setY(getY() + SPEED_Y);
            }
        }
    }

    /**
     * Offsets Driver object from current taxi for ejection
     */
    public void eject() {
        if (!outOfTaxi) {
            setX(getX() - 50);
        }
        this.outOfTaxi = true;
    }

    /**
     * Decrements Driver health based on specified damage
     *
     * @param dmg Integer value to decrement Entity health
     */
    @Override
    public void takeDamage(int dmg) {
        if (outOfTaxi && !isInvincible) {
            if (health > 0) {
                health -= dmg;
            } else {
                health = 0;
            }
        }
    }

    /**
     * Coin collection logic for Driver
     *
     * @param coin Coin object that is collected by Driver or Taxi
     */
    @Override
    public void collectCoin(Coin coin) {
        if (outOfTaxi) {
            coinPower = coin;
        }
    }

    /**
     * Star collection logic for Driver
     *
     * @param star Star object that is collected by Driver or Taxi
     */
    @Override
    public void collectStar(Star star) {
        if (outOfTaxi) {
            starPower = star;
            isInvincible = true;
        }
    }

    /**
     * Gets current health of Driver object
     *
     * @return Current integer value of Driver health attribute
     */
    public int getHealth() {
        return health;
    }

    /**
     * Renders blood over Driver when dead
     */
    @Override
    public void bleed() {
        blood.draw(getX(), getY());
    }

    /**
     * Gets detection radius of driver for taxi interaction
     *
     * @return float value of driver's detection radius
     */
    public float getDetectRad() {
        return DETECT_RAD;
    }

    /**
     * Sets driver state to in or out of taxi
     *
     * @param outOfTaxi boolean value depicting new driver state
     */
    public void setOutOfTaxi(boolean outOfTaxi) {
        this.outOfTaxi = outOfTaxi;
    }
}
