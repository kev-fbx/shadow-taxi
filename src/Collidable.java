/**
 * Interface that holds method relevant to checking collisions between Entities
 *
 * @author Kevin Tran
 */
public interface Collidable {
    /**
     * Invincibility frames (i-Frames) after collision
     */
    int TIMEOUT_FRAMES = 200;

    /**
     * Abstract method that checks if `this` entity is collided with `e`
     *
     * @param e The other entity
     * @return true if `this` is collided with `e`, false otherwise
     */
    boolean isCollided(Entity e);
}
