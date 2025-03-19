import java.util.Properties;

/**
 * Abstract class that describes common attributes shared by Car entities
 *
 * @author Kevin Tran
 */
public abstract class Car extends Entity implements Damageable, Flammable, Crashable {
    /**
     * Health of the car entity
     */
    private int health;
    /**
     * Amount of damage car can deal to other Damageable entities
     */
    private final int DMG;
    /**
     * Vertical speed of car object
     */
    private int SPEED_Y;
    /**
     * Current frame count for rendering smoke
     */
    private int currSmokeFrame;
    /**
     * Current frame count for rendering fire
     */
    private int currFireFrame;
    /**
     * Boolean value representing if Taxi is emitting smoke
     */
    private boolean isSmoking;
    /**
     * Boolean value representing if Taxi is emitting flames
     */
    private boolean isBurning;
    /**
     * Boolean value representing living or dead state of Car
     */
    private boolean isDead;
    /**
     * Boolean value tracking if Car was just hit with another collidable object
     */
    private boolean wasHit;

    /**
     * Car constructor to instantiate cars
     *
     * @param gameProp Properties file holding game properties
     * @param x        Initial X-coordinate
     * @param y        Initial Y-coordinate
     * @param imgPath  File path for image to be rendered
     * @param health   Health of car
     * @param minY     Minimum vertical speed possible
     * @param maxY     Maximum vertical speed possible
     * @param rad      Radius of Entity
     */
    public Car(Properties gameProp, int x, int y, String imgPath, int health, int dmg, int minY, int maxY, float rad) {
        super(x, y, imgPath, rad);

        this.health = health;
        this.DMG = dmg;
        this.SPEED_Y = MiscUtils.getRandomInt(minY, maxY + 1);
        this.currSmokeFrame = 0;
        this.currFireFrame = 0;
        this.isSmoking = false;
        this.isBurning = false;
        this.isDead = false;
        this.wasHit = false;
    }

    /**
     * Render logic for cars. Displays smoke when hit, and also fire when dead
     */
    @Override
    public void render() {
        if (isSmoking) {
            if (currSmokeFrame < SMOKE_LT) {
                emitSmoke();
                currSmokeFrame++;
            }
            if (currSmokeFrame >= SMOKE_LT) {
                currSmokeFrame = 0;
                this.isSmoking = false;
            }
        }

        if (isBurning) {
            if (currFireFrame < FIRE_LT) {
                burn();
                currFireFrame++;
            }
            if (currFireFrame >= FIRE_LT) {
                currFireFrame = 0;
                this.isBurning = false;
            }
        }
        if (!isDead) {
            getSprite().draw(getX(), getY());
        }
    }

    /**
     * Decrements car health by specified damage amount
     *
     * @param dmg Integer value to decrement Entity health
     */
    @Override
    public void takeDamage(int dmg) {
        this.wasHit = true;
        health -= dmg;
        this.isSmoking = true;
        if (health <= 0 && !isDead) {
            this.isDead = true;
        }
    }

    /**
     * Draws fire image when car dies (i.e., health reaches 0 for first time)
     */
    @Override
    public void burn() {
        if (health > 0) {
            fire.draw(getX(), getY());
        }
    }

    /**
     * Renders smoke over passive car during collision
     */
    @Override
    public void emitSmoke() {
        if (health > 0) {
            smoke.draw(getX(), getY());
        }
    }

    /**
     * Moves entity along Y axis based on input
     */
    @Override
    public void moveRelative() {
        setY(getY() + SCROLL_SPEED * getMoveY() - SPEED_Y);
    }

    /**
     * Halts movement momentarily and initiates invincibility when hit
     */
    @Override
    public void pauseMovement() {

    }

    /**
     * Gets car's damage value
     *
     * @return Integer value of car's damage
     */
    public int getDmg() {
        return DMG;
    }

    /**
     * Gets death status of car
     * @return true if Car is dead, false otherwise
     */
    public boolean isDead() {
        return isDead;
    }

    /**
     * Sets vertical speed to specified value
     * @param SPEED_Y New vertical speed of car
     */
    public void setSpeedY(int SPEED_Y) {
        this.SPEED_Y = SPEED_Y;
    }

    /**
     * Gets hit status of car to handle collisions
     * @return true if car was hit, false otherwise
     */
    public boolean isWasHit() {
        return wasHit;
    }

}
