import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.Properties;

/**
 * Abstract class that describes the common attributes and useful methods for Shadow Taxi entities.
 *
 * @author Kevin Tran
 */
public abstract class Entity implements Collidable, Movable {
    /**
     * Game properties file that contains Entity information
     */
    private final Properties GAME_PROP = IOUtils.readPropertiesFile("res/app.properties");
    /**
     * X and Y coordinates of Entity
     */
    private int x, y;
    /**
     * Integer to indicate state of Y axis movement
     */
    private int moveY;
    /**
     * Image of Entity to be rendered
     */
    private final Image SPRITE;
    /**
     * Radius of entity for collision logic
     */
    private final Float RADIUS;
    /**
     * Buddy variable to keep track of i-Frames
     */
    private int currTimeOutFrame;

    /**
     * Entity constructor to instantiate game entities
     *
     * @param x           Initial X-coordinate
     * @param y           Initial Y-coordinate
     * @param SPRITE_PATH File path to png file
     * @param RADIUS      Radius of entity
     */
    public Entity(int x, int y, String SPRITE_PATH, Float RADIUS) {
        this.x = x;
        this.y = y;
        this.currTimeOutFrame = 0;
        this.moveY = 0;
        this.SPRITE = new Image(SPRITE_PATH);
        this.RADIUS = RADIUS;
    }

    /**
     * Calculates Euclidean distance between 2 entities
     *
     * @param e1 First entity
     * @param e2 Second entity
     * @return Distance (double) between the entities
     */
    private double calcEuclid(Entity e1, Entity e2) {
        double xDist = e1.getX() - e2.getX();
        double yDist = e1.getY() - e2.getY();
        return Math.hypot(xDist, yDist);
    }

    /**
     * Sets this Entity to follow the coordinates of another Entity
     *
     * @param e Other entity
     */
    public void followEntity(Entity e) {
        this.x = e.getX();
        this.y = e.getY();
    }

    /**
     * Calculates if 2 entities have (approximately) the same position.
     *
     * @param e1 First entity
     * @param e2 Second entity
     * @return Boolean value whether 2 entities have the same position
     */
    private boolean coordinatesEqual(Entity e1, Entity e2) {
        double epsilon = 1e-5;
        return Math.abs(e1.getX() - e2.getX()) < epsilon &&
                Math.abs(e1.getY() - e2.getY()) < epsilon;
    }

    /**
     * Determines collision state between this entity and another.
     *
     * @param e Other entity
     * @return Boolean value indicating collision state
     */
    @Override
    public boolean isCollided(Entity e) {
        return calcEuclid(this, e) < this.getRad() + e.getRad() || coordinatesEqual(this, e);
    }

    /**
     * Adjust the movement direction in y-axis of the GameObject based on the keyboard input.
     *
     * @param input The current mouse/keyboard input.
     */
    @Override
    public void adjustToInputMovement(Input input) {
        if (input.wasPressed(Keys.UP)) {
            moveY = 1;
        } else if (input.wasReleased(Keys.UP)) {
            moveY = 0;
        }
    }

    /**
     * Moves entity along Y axis based on input
     */
    @Override
    public void moveRelative() {
        setY(getY() + SCROLL_SPEED * moveY);
    }

    /**
     * Sets the moveY factor to given value
     *
     * @param moveY integer factor to assign to moveY
     */
    public void setMoveY(int moveY) {
        this.moveY = moveY;
    }

    /**
     * Gets current moveY value
     *
     * @return Integer value of moveY
     */
    public int getMoveY() {
        return moveY;
    }

    /**
     * Sets X-coordinate value
     *
     * @param x X-coordinate of Entity
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets Y-coordinate value
     *
     * @param y Y-coordinate of Entity
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gets X-coordinate of Entity
     *
     * @return int value of X-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets Y-coordinate of Entity
     *
     * @return int value of Y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Gets Image of Entity
     *
     * @return Image of Entity
     */
    public Image getSprite() {
        return SPRITE;
    }

    /**
     * Gets radius of Entity
     *
     * @return Float value of Entity radius
     */
    public float getRad() {
        return RADIUS;
    }

    /**
     * Gets game properties file
     *
     * @return Property object that holds game properties file
     */
    public Properties getGameProp() {
        return GAME_PROP;
    }

    /**
     * Gets current i-Frame count for collided Entity
     *
     * @return int value of current i-Frame
     */
    public int getCurrTimeOutFrame() {
        return currTimeOutFrame;
    }

    /**
     * Increments the current i-Frame counter
     */
    public void incrementTimeOutFrame() {
        this.currTimeOutFrame++;
    }

    /**
     * Abstract method for rendering logic of Entities
     */
    public abstract void render();

    /**
     * Abstract method for the updating logic of Entities
     *
     * @param input The current mouse/keyboard input.
     */
    public abstract void update(Input input);
}
