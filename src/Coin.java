import java.util.Properties;

/**
 * Class representing coins in the game. Coins can be collected by either the player or the taxi.
 * It will set one level higher priority for the passengers that are waiting to get-in or already in the taxi.
 *
 * @author SWEN20003 Teaching Staff, adapted by Kevin Tran
 */
public class Coin extends Collectable {
    /**
     * Constructor for Coin objects
     *
     * @param gameProp Properties file that holds all game properties
     * @param x        Initial X-coordinate of Coin object
     * @param y        Initial Y-coordinate of Coin object
     */
    public Coin(Properties gameProp, int x, int y) {
        super(x, y, gameProp.getProperty("gameObjects.coin.image"),
                Float.parseFloat(gameProp.getProperty("gameObjects.coin.radius")),
                Integer.parseInt(gameProp.getProperty("gameObjects.coin.maxFrames")));
    }

    /**
     * Apply the effect of the coin on the priority of the passenger.
     *
     * @param priority The current priority of the passenger.
     * @return The new priority of the passenger.
     */
    public Integer applyEffect(Integer priority) {
        if (getFramesActive() <= this.getMaxFrames() && priority > 1) {
            priority -= 1;
        }
        return priority;
    }

    /**
     * Check if the coin has collided with any PowerCollectable objects, and power will be collected by PowerCollectable
     * object that is collided with.
     */
    public void collide(Empowerable object) {
        if (isCollided((Entity) object)) {
            object.collectCoin(this);
            setCollectableCollided(true);
        }
    }

    /**
     * Overwritten method of collision method that holds logic for coin collisions
     *
     * @param e Other entity
     * @return true if distance between object and coin is less than the sun of their radii
     */
    @Override
    public boolean isCollided(Entity e) {
        // if the distance between the two objects is less than the sum of their radius, they are collided
        float collisionDistance = getRad() + e.getRad();
        float currDistance = (float) Math.sqrt(Math.pow(getX() - e.getX(), 2) + Math.pow(getY() - e.getY(), 2));
        return currDistance <= collisionDistance;
    }
}
