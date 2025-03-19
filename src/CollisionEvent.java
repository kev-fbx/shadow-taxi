import java.util.ArrayList;

/**
 * This class contains the generic collision logic for entity interactions in the game
 *
 * @author Kevin Tran
 */
public class CollisionEvent {

    /**
     * Calculates Euclidean distance between 2 entities
     *
     * @param e1 First entity
     * @param e2 Second entity
     * @return Distance (double) between the entities
     */
    private static double calcEuclid(Entity e1, Entity e2) {
        double xDist = e1.getX() - e2.getX();
        double yDist = e1.getY() - e2.getY();
        return Math.hypot(xDist, yDist);
    }

    /**
     * Calculates if 2 entities have (approximately) the same position.
     *
     * @param e1 First entity
     * @param e2 Second entity
     * @return Boolean value whether 2 entities have the same position
     */
    private static boolean coordinatesEqual(Entity e1, Entity e2) {
        double epsilon = 1e-5;
        return Math.abs(e1.getX() - e2.getX()) < epsilon && Math.abs(e1.getY() - e2.getY()) < epsilon;
    }

    /**
     * Checks if entities are collided based on Euclidean distance or equal coordinates
     *
     * @param e1 First entity
     * @param e2 Second Entity
     * @return true if Euclidean distance is less than sum of radii, or coordinate are equal
     */
    private static boolean isCollided(Entity e1, Entity e2) {
        return calcEuclid(e1, e2) < e1.getRad() + e2.getRad() || coordinatesEqual(e1, e2);
    }

    /**
     * Iterates over all game entities and checks for collision events
     *
     * @param entities Game entities ArrayList
     */
    public static void checkCollisions(ArrayList<Entity> entities) {
        for (int i = 0; i < entities.size(); i++) {
            Entity e1 = entities.get(i);
            for (int j = 0; j < entities.size(); j++) {
                Entity e2 = entities.get(j);
                // First check if Driver is in Taxi
                if (e1 instanceof Driver driver && e2 instanceof Taxi taxi) {
                    if (calcEuclid(driver, taxi) < driver.getDetectRad()) {
                        handleDriverEnterTaxi(driver, taxi);
                    }
                }
                if (e1 != e2 && isCollided(e1, e2)) {
                    handleCollision(e1, e2);
                }
            }
        }
    }

    /**
     * This method contains the different collision cases that may occur between 2 collided entities
     *
     * @param e1 First entity
     * @param e2 Second entity
     */
    private static void handleCollision(Entity e1, Entity e2) {
        if (e1 instanceof Taxi taxi) {
            if (e2 instanceof Car car) {
                handleTaxiCrash(taxi, car);
            }
        }

        if (e1 instanceof Fireball fireball) {
            if (e2 instanceof Damageable damageable) {
                handleFireballHit(fireball, damageable);
            }
        }

        if (e1 instanceof Car c1 && e2 instanceof Car c2) {
            handleCarCrash(c1, c2);
        }

        if (e1 instanceof Empowerable empowerable && e2 instanceof Collectable collectable) {
            handleCollectablePickup(empowerable, collectable);
        }

        if (e1 instanceof Driver driver) {
            if (e2 instanceof Car car) {
                handleDriverCrash(driver, car);
            }
        }
    }

    /**
     * Handles car-car collisions
     *
     * @param c1 First car
     * @param c2 Second car
     */
    private static void handleCarCrash(Car c1, Car c2) {
        c1.takeDamage(c2.getDmg());
        c2.takeDamage(c1.getDmg());
    }

    /**
     * Handles driver-taxi interaction
     *
     * @param d Driver
     * @param t Current taxi
     */
    private static void handleDriverEnterTaxi(Driver d, Taxi t) {
        d.setOutOfTaxi(false);
        t.setHasDriver(true);
    }

    /**
     * Handles pickup of Collectbable objects by Empowerable objects
     *
     * @param e Object that picked up object
     * @param c Object that is picked up
     */
    private static void handleCollectablePickup(Empowerable e, Collectable c) {
        if (c instanceof Coin) {
            e.collectCoin((Coin) c);
            ((Coin) c).collide(e);
        }

        if (c instanceof Star) {
            e.collectStar((Star) c);
            ((Star) c).collide(e);
        }
    }

    /**
     * Handles collision between driver and passive or enemy car
     *
     * @param d Driver
     * @param c Collided car
     */
    private static void handleDriverCrash(Driver d, Car c) {
        d.takeDamage(c.getDmg());
    }

    /**
     * Handles interactions between emitted fireballs and Damageable entities
     *
     * @param f Fireball
     * @param d Hit entity
     */
    private static void handleFireballHit(Fireball f, Damageable d) {
        f.collide(d);
        d.takeDamage(f.getDmg());
    }

    /**
     * Handles interaction between current taxi and car
     *
     * @param t Current taxi
     * @param c Collided car
     */
    private static void handleTaxiCrash(Taxi t, Car c) {
        t.takeDamage(c.getDmg());
        if (!t.isTaxiDead()) {
            c.takeDamage(t.getDmg());
        }
    }
}
