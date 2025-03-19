import bagel.Input;

import java.util.ArrayList;
import java.util.Properties;

/**
 * This class contains the methods and attributes relevant for Enemy entities that are rendered in GameScreen
 *
 * @author Kevin Tran
 */
public class Enemy extends Car {
    /**
     * ArrayList holding all fireballs that `this` Enemy shoots
     */
    private final ArrayList<Fireball> FIREBALLS;
    /**
     * Static spawn rate of fireballs
     */
    private final static int FIREBALL_SPAWN_RATE = 300;

    /**
     * Enemy constructor to instantiate enemy cars
     *
     * @param gameProp Properties file holding game properties
     * @param x        Initial X-coordinate
     * @param y        Initial Y-coordinate
     */
    public Enemy(Properties gameProp, int x, int y) {
        super(gameProp, x, y,
                gameProp.getProperty("gameObjects.enemyCar.image"),
                (int) (100 * Double.parseDouble(gameProp.getProperty("gameObjects.enemyCar.health"))),
                (int) (100 * Double.parseDouble(gameProp.getProperty("gameObjects.enemyCar.damage"))),
                Integer.parseInt(gameProp.getProperty("gameObjects.enemyCar.minSpeedY")),
                Integer.parseInt(gameProp.getProperty("gameObjects.enemyCar.maxSpeedY")),
                Float.parseFloat(gameProp.getProperty("gameObjects.enemyCar.radius")));
        this.FIREBALLS = new ArrayList<>();
    }

    /**
     * Instantiates new Fireball object
     */
    public void shoot() {
        FIREBALLS.add(new Fireball(getGameProp(), getX(), getY()));
    }

    /**
     * Updating logic of Enemy objects.
     * Randomly creates new fireballs to shoot
     *
     * @param input The current mouse/keyboard input.
     */
    @Override
    public void update(Input input) {
        render();
        if (MiscUtils.canSpawn(FIREBALL_SPAWN_RATE) && !isDead()) {
            shoot();
        }

        for (Fireball fireball : FIREBALLS) {
            fireball.update(input);
        }

        if (input != null) {
            adjustToInputMovement(input);
        }

        if (isWasHit()) {
            pauseMovement();
        }

        if (isDead()) {
            setSpeedY(0);
        }

        moveRelative();
    }
}
