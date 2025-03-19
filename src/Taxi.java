import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.Properties;

/**
 * The class representing the taxis in the game play
 *
 * @author SWEN20003 Teaching Staff, adapted by Kevin Tran
 */
public class Taxi extends Entity implements Damageable, Empowerable, Flammable {
    /**
     * Image of living Taxi object
     */
    private final Image LIVING_IMG;
    /**
     * Image of dead Taxi object
     */
    private final Image DEAD_IMG;
    /**
     * Horizontal speed of taxi
     */
    private final int SPEED_X;
    /**
     * Trips that are conducted by a Taxi
     */
    private final Trip[] TRIPS;
    /**
     * Buddy variable that tracks how many trips are completed
     */
    private int tripCount;
    /**
     * Current frame count for rendering smoke
     */
    private int currSmokeFrame;
    /**
     * Current frame count for rendering fire
     */
    private int currFireFrame;
    /**
     * Boolean value representing if vertically moving
     */
    private boolean isMovingY;
    /**
     * Boolean value representing if horizontally moving
     */
    private boolean isMovingX;
    /**
     * Boolean value representing if Taxi object has a Driver
     */
    private boolean hasDriver;
    /**
     * Boolean value representing if Taxi has collected a Star
     */
    private boolean isInvincible;
    /**
     * Boolean value representing if Taxi is emitting smoke
     */
    private boolean smoking;
    /**
     * Boolean value representing if Taxi is emitting flames
     */
    private boolean burning;
    /**
     * Boolean value representing if Taxi is dead or not
     */
    private boolean dead;
    /**
     * Current coin collected by Taxi
     */
    private Coin coinPower;
    /**
     * Current star collected by Taxi
     */
    private Star starPower;
    /**
     * Current trip of Taxi
     */
    private Trip trip;
    /**
     * Health of Taxi
     */
    private int health;
    /**
     * Damage that Taxi can deal
     */
    private final int DMG;

    /**
     * Constructor for Taxi objects
     *
     * @param gameProp     Properties object that holds game properties
     * @param x            Initial X-coordinate of Taxi
     * @param y            Initial Y-coordinate of Taxi
     * @param maxTripCount Maximum possible trips a Taxi can have
     */
    public Taxi(Properties gameProp, int x, int y, int maxTripCount) {
        super(x, y, gameProp.getProperty("gameObjects.taxi.image"),
                Float.parseFloat(gameProp.getProperty("gameObjects.taxi.radius")));
        TRIPS = new Trip[maxTripCount];

        this.SPEED_X = Integer.parseInt(getGameProp().getProperty("gameObjects.taxi.speedX"));
        this.LIVING_IMG = new Image(getGameProp().getProperty("gameObjects.taxi.image"));
        this.DEAD_IMG = new Image(getGameProp().getProperty("gameObjects.taxi.damagedImage"));
        this.health = (int) (100 * Double.parseDouble(getGameProp().getProperty("gameObjects.taxi.health")));
        this.DMG = (int) (100 * Double.parseDouble(getGameProp().getProperty("gameObjects.taxi.damage")));
        this.currSmokeFrame = 0;
        this.currFireFrame = 0;
        this.isInvincible = false;
        this.smoking = false;
        this.burning = false;
        this.dead = false;
    }

    /**
     * Rendering logic for living or dead Taxi, as well as smoke and fire emitting
     */
    @Override
    public void render() {
        if (health > 0) {
            LIVING_IMG.draw(getX(), getY());
        } else {
            DEAD_IMG.draw(getX(), getY());
        }

        if (smoking) {
            emitSmoke();
        }

        if (burning && !dead) {
            burn();
        }
    }

    /**
     * Checks if taxi is moving vertically
     *
     * @return true if taxi is moving vertically, false otherwise
     */
    public boolean isMovingY() {
        return isMovingY;
    }

    /**
     * Checks if taxi is moving horizontally
     *
     * @return true if taxi is moving horizontally, false otherwise
     */
    public boolean isMovingX() {
        return isMovingX;
    }

    /**
     * If it's a new trip, it will add to the list of trips.
     *
     * @param trip trip object
     */
    public void setTrip(Trip trip) {
        this.trip = trip;
        if (trip != null) {
            this.TRIPS[tripCount] = trip;
            tripCount++;
        }
    }

    /**
     * Gets current trip of Taxi
     *
     * @return Current Trip object of Taxi
     */
    public Trip getTrip() {
        return this.trip;
    }

    /**
     * Get the last trip from the list of trips.
     *
     * @return Trip object
     */
    public Trip getLastTrip() {
        if (tripCount == 0) {
            return null;
        }
        return TRIPS[tripCount - 1];
    }

    /**
     * Update the GameObject object's movement states based on the input.
     * Render the game object into the screen.
     *
     * @param input The current mouse/keyboard input.
     */
    public void update(Input input) {

        if (smoking) {
            if (currSmokeFrame < SMOKE_LT) {
                currSmokeFrame++;
            }
            if (currSmokeFrame >= SMOKE_LT) {
                currSmokeFrame = 0;
                this.smoking = false;
            }
        }

        if (burning) {
            if (currFireFrame < FIRE_LT) {
                currFireFrame++;
            }
            if (currFireFrame >= FIRE_LT) {
                currFireFrame = 0;
                this.burning = false;
                this.dead = true;
            }
        }
        // if the taxi has coin power, apply the effect of the coin on the priority of the passenger
        // (See the logic in TravelPlan class)
        if (trip != null && coinPower != null) {
            TravelPlan tp = trip.getPassenger().getTravelPlan();
            int newPriority = tp.getPriority();
            if (!tp.getCoinPowerApplied()) {
                newPriority = coinPower.applyEffect(tp.getPriority());
            }
            if (newPriority < tp.getPriority()) {
                tp.setCoinPowerApplied();
            }
            tp.setPriority(newPriority);
        }

        if (input != null && hasDriver) {
            adjustToInputMovement(input);
        }

        if (isTaxiDead()) {
            moveRelative();
        }

        if (trip != null && trip.hasReachedEnd()) {
            getTrip().end();
        }

        render();

        // the flag of the current trip renders to the screen
        if (tripCount > 0) {
            Trip lastTrip = TRIPS[tripCount - 1];
            if (!lastTrip.getPassenger().hasReachedFlag()) {
                lastTrip.getFlag().update(input);
            }
        }
    }

    /**
     * Adjust the movement of the taxi based on the keyboard input.
     * If the taxi has a driver, and taxi has health>0 the taxi can only move left and right (fixed in y direction).
     * If the taxi does not have a driver, the taxi can move in all directions.
     *
     * @param input The current mouse/keyboard input.
     */
    @Override
    public void adjustToInputMovement(Input input) {
        if (input.wasPressed(Keys.UP)) {
            setMoveY(1);
            isMovingY = true;
        } else if (input.wasReleased(Keys.UP)) {
            setMoveY(0);
            isMovingY = false;
        } else if (input.isDown(Keys.LEFT)) {
            if (health > 0) {
                setX(getX() - SPEED_X);
                isMovingX = true;
            }
        } else if (input.isDown(Keys.RIGHT)) {
            if (health > 0) {
                setX(getX() + SPEED_X);
                isMovingX = true;
            }
        } else if (input.wasReleased(Keys.LEFT) || input.wasReleased(Keys.RIGHT)) {
            isMovingX = false;
        }
    }

    /**
     * Assigns Coin object to Taxi upon collision
     *
     * @param coin Coin object that is collected by Driver or Taxi
     */
    @Override
    public void collectCoin(Coin coin) {
        coinPower = coin;
    }

    /**
     * Assigns Star object to Taxi upon collision
     *
     * @param star Star object that is collected by Driver or Taxi
     */
    @Override
    public void collectStar(Star star) {
        starPower = star;
        setIsInvincible();
        isInvincible = true;
    }

    /**
     * Decrements taxi's health if it does not have a Star activated
     *
     * @param dmg Integer value to decrement Entity health
     */
    @Override
    public void takeDamage(int dmg) {
        if (!isInvincible) {
            health -= dmg;
            this.smoking = true;
            if (health <= 0 && !dead) {
                this.burning = true; // WRONG placement should only be set true once when taxi dies and never again
            }
        }

        if (health < 0) {
            this.health = 0;
        }
    }

    /**
     * Calculate total earnings. (See how fee is calculated for each trip in Trip class.)
     *
     * @return int, total earnings
     */
    public float calculateTotalEarnings() {
        float totalEarnings = 0;
        for (Trip trip : TRIPS) {
            if (trip != null) {
                totalEarnings += trip.getFee();
            }
        }
        return totalEarnings;
    }

    /**
     * Gets current health of Taxi
     *
     * @return Integer value of current taxi health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Sets empowered state of Taxi to true
     */
    public void setIsInvincible() {
        this.isInvincible = true;
    }

    /**
     * Sets empowered state of Taxi to false
     */
    public void setNotInvincible() {
        this.isInvincible = false;
    }

    /**
     * Rendering logic for fire image upon death
     */
    @Override
    public void burn() {
        fire.draw(getX(), getY());
    }

    /**
     * Rendering logic for smoke image upon collision
     */
    @Override
    public void emitSmoke() {
        smoke.draw(getX(), getY());
    }

    /**
     * Gets death state of Taxi
     *
     * @return true if Taxi is dead (health <= 0), false otherwise
     */
    public boolean isTaxiDead() {
        return (health <= 0);
    }

    /**
     * Gets damage value of Taxi object
     *
     * @return int value that Taxi can deal in damage
     */
    public int getDmg() {
        return DMG;
    }

    /**
     * Sets whether Taxi has driver or not
     *
     * @param hasDriver true when taxi has a driver, false if not
     */
    public void setHasDriver(boolean hasDriver) {
        this.hasDriver = hasDriver;
    }
}
