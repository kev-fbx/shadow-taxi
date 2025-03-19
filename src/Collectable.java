import bagel.Input;

/**
 * This abstract class contains common logic, attributes and methods of collectable items in Shadow Taxi
 *
 * @author Kevin Tran
 */
public abstract class Collectable extends Entity {
    /**
     * Maximum frames that a collectable object can empower an Entity for
     */
    private final int MAX_FRAMES;
    /**
     * Boolean value indicating if collectable has collided with other Entity
     */
    private boolean isCollided;
    /**
     * Current frames active for the collectable object
     */
    private int framesActive;

    /**
     * Entity constructor to instantiate game entities
     *
     * @param x          Initial X-coordinate
     * @param y          Initial Y-coordinate
     * @param spritePath File path to png file
     * @param rad        Radius of entity
     */
    public Collectable(int x, int y, String spritePath, Float rad, int maxFrames) {
        super(x, y, spritePath, rad);
        this.MAX_FRAMES = maxFrames;
        this.isCollided = false;
        this.framesActive = 0;
    }

    /**
     * Draws image of collectable object
     */
    @Override
    public void render() {
        getSprite().draw(getX(), getY());
    }

    /**
     * Move the object in y direction according to the keyboard input, and render the collectable image,
     * before collision happens with collectable objects.
     * Once the collision happens, the active time will be increased.
     *
     * @param input The current mouse/keyboard input.
     */
    @Override
    public void update(Input input) {
        if (isCollided) {
            framesActive++;
        } else {
            if (input != null) {
                adjustToInputMovement(input);
            }
            render();
            moveRelative();
        }
    }

    /**
     * Gets maximum frames the Collectable can be active for
     *
     * @return int value of maximum activation frames
     */
    public int getMaxFrames() {
        return MAX_FRAMES;
    }

    /**
     * Sets collided state to given value
     *
     * @param collided boolean value to set collision state to
     */
    public void setCollectableCollided(boolean collided) {
        isCollided = collided;
    }

    /**
     * Gets current frame of activated collectable
     *
     * @return int value of current frame of activated collectable
     */
    public int getFramesActive() {
        return framesActive;
    }

    /**
     * Gets activation state of Star
     *
     * @return true if Star is activated from collision, false otherwise
     */
    public boolean getIsActive() {
        return isCollided && framesActive <= MAX_FRAMES && framesActive > 0;
    }
}
