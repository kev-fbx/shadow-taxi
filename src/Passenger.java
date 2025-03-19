import bagel.Font;
import bagel.Image;
import bagel.Input;

import java.util.Properties;

/**
 * This class contains methods and attributes of Passenger objects that are rendered in GameScreen
 *
 * @author SWEN20003 Teaching Staff, adapted by Kevin Tran
 */
public class Passenger extends Entity implements Damageable, Bleedable {
    /**
     * Detection radius for a taxi
     */
    private final int TAXI_DETECT_RADIUS;
    /**
     * TravelPlan that contains information of the trip
     */
    private final TravelPlan TRAVEL_PLAN;
    /**
     * Horizontal movement speed
     */
    private final int WALK_SPEED_X;
    /**
     * Vertical movement speed
     */
    private final int WALK_SPEED_Y;
    /**
     * Text offset for priority
     */
    private final int PRIORITY_OFFSET;
    /**
     * Text offset for expected fee
     */
    private final int EXPECTED_FEE_OFFSET;
    /**
     * Boolean indicating if passenger has umbrella or not
     */
    private final boolean hasUmbrella;
    /**
     * Horizontal direction that passenger walks in
     */
    private int walkDirectionX;
    /**
     * Vertical direction that passenger walks in
     */
    private int walkDirectionY;
    /**
     * Boolean value indicating if passenger is in a taxi
     */
    private boolean isGetInTaxi;
    /**
     * Current Trip object of the passenger
     */
    private Trip trip;
    /**
     * Boolean value indicating if `this` passenger has reached it's end Flag
     */
    private boolean reachedFlag;
    /**
     * Image object holding standard Passenger image
     */
    private final Image P_IMG;
    /**
     * Image object holding umbrella passenger image
     */
    private final Image P_IMG_U;
    /**
     * Health attribute of `this` Passenger
     */
    private int health;

    /**
     * Constructor method for Passenger objects
     *
     * @param gameProp    Properties object that holds game properties
     * @param x           Initial X-coordinate
     * @param y           Initial Y-coordinate
     * @param priority    Priority level of passenger
     * @param endX        X-coordinate of passenger's destination
     * @param distanceY   Y distance that passenger must travel
     * @param hasUmbrella Integer value indicating if passenger has umbrella (1) or doesn't (0)
     */
    public Passenger(Properties gameProp, int x, int y, int priority, int endX, int distanceY, int hasUmbrella) {
        super(x, y, gameProp.getProperty("gameObjects.passenger.image"), Float.parseFloat(gameProp.getProperty("gameObjects.passenger.radius")));

        this.hasUmbrella = (hasUmbrella == 1);

        P_IMG = getSprite();
        P_IMG_U = new Image(getGameProp().getProperty("gameObjects.passenger.Umbrella.image"));

        this.WALK_SPEED_X = Integer.parseInt(getGameProp().getProperty("gameObjects.passenger.walkSpeedX"));
        this.WALK_SPEED_Y = Integer.parseInt(getGameProp().getProperty("gameObjects.passenger.walkSpeedY"));

        this.TRAVEL_PLAN = new TravelPlan(getGameProp(), endX, distanceY, priority);
        this.TAXI_DETECT_RADIUS = Integer.parseInt(getGameProp().getProperty("gameObjects.passenger.taxiDetectRadius"));

        setMoveY(0);
        this.PRIORITY_OFFSET = 30;
        this.EXPECTED_FEE_OFFSET = 100;

        this.health = (int) (100 * Double.parseDouble(getGameProp().getProperty("gameObjects.passenger.health")));
    }

    /**
     * Renders different passenger image based on umbrella state
     */
    @Override
    public void render() {
        if (hasUmbrella) {
            P_IMG.draw(getX(), getY());
        } else {
            P_IMG_U.draw(getX(), getY());
        }
    }

    /**
     * Gets the passenger's TravelPlan object
     *
     * @return TravelPlan object of passenger
     */
    public TravelPlan getTravelPlan() {
        return TRAVEL_PLAN;
    }

    /**
     * Update the passenger status, move according to the input, active taxi and trip status.
     * Initiate the trip if the passenger is in the taxi.
     * See move method below to understand the movement of the passenger better.
     *
     * @param input The current mouse/keyboard input.
     * @param taxi  The active taxi in the game play.
     */
    public void updatePassenger(Input input, Taxi taxi) {
        update(input);

        // if the passenger is not in the taxi and there's no trip initiated, draw the priority number on the passenger.
        if (!isGetInTaxi && trip == null) {
            drawPriority();
        }

        if (adjacentToObject(taxi) && !isGetInTaxi && trip == null) {
            // if the passenger has not started the trip yet,
            // Taxi must be stopped in passenger's vicinity and not having another trip.
            setIsGetInTaxi(taxi);
            move(taxi);
        } else if (isGetInTaxi) {
            // if the passenger is in the taxi, initiate the trip and move the passenger along with the taxi.
            if (trip == null) {
                //Create new trip
                getTravelPlan().setStartY(getY());
                trip = new Trip(getGameProp(), this, taxi);
                taxi.setTrip(trip);
            }
            move(taxi);
        } else if (trip != null && trip.isComplete()) {
            move(taxi);
        }
    }

    /**
     * Draw the priority number on the passenger.
     */
    private void drawPriority() {
        Font font = new Font(getGameProp().getProperty("font"),
                Integer.parseInt(getGameProp().getProperty("gameObjects.passenger.fontSize")));
        font.drawString(String.valueOf(TRAVEL_PLAN.getPriority()), getX() - PRIORITY_OFFSET, getY());
        font.drawString(String.valueOf(TRAVEL_PLAN.getExpectedFee()), getX() - EXPECTED_FEE_OFFSET, getY());
    }

    /**
     * Move in relevant to the taxi and passenger's status.
     *
     * @param taxi active taxi
     */
    private void move(Taxi taxi) {
        if (isGetInTaxi) {
            // if the passenger is in the taxi, move the passenger along with the taxi.
            followEntity(taxi);
        } else if (trip != null && trip.isComplete()) {
            //walk towards end flag if the trip is completed and not in the taxi.
            if (!hasReachedFlag()) {
                Flag flag = trip.getFlag();
                walkXDirectionObj(flag.getX());
                walkYDirectionObj(flag.getY());
                walk();
            }
        } else {
            // Walk towards the taxi if other conditions are not met.
            // (That is when taxi is stopped with not having a trip and adjacent to the passenger and the passenger
            // hasn't initiated the trip yet.)
            walkXDirectionObj(taxi.getX());
            walkYDirectionObj(taxi.getY());
            walk();
        }
    }

    /**
     * Walk the people object based on the walk direction and speed.
     */
    private void walk() {
        setX(getX() + WALK_SPEED_X * walkDirectionX);
        setY(getY() + WALK_SPEED_Y * walkDirectionY);
    }

    /**
     * Determine the walk direction in x-axis of the passenger based on the x direction of the object.
     */
    private void walkXDirectionObj(int otherX) {
        walkDirectionX = Integer.compare(otherX, getX());
    }

    /**
     * Determine the walk direction in y-axis of the passenger based on the x direction of the object.
     */
    private void walkYDirectionObj(int otherY) {
        walkDirectionY = Integer.compare(otherY, getY());
    }

    /**
     * Check if the passenger has reached the end flag of the trip.
     *
     * @return a boolean value indicating if the passenger has reached the end flag.
     */
    public boolean hasReachedFlag() {
        if (trip != null) {
            Flag tef = trip.getFlag();
            if (tef.getX() == getX() && tef.getY() == getY()) {
                reachedFlag = true;
            }
            return reachedFlag;
        }
        return false;
    }

    /**
     * Check if the taxi is adjacent to the passenger. This is evaluated based on multiple criteria.
     *
     * @param taxi The active taxi in the game play.
     * @return a boolean value indicating if the taxi is adjacent to the passenger.
     */
    private boolean adjacentToObject(Taxi taxi) {
        // Check if Taxi is stopped and health > 0
        boolean taxiStopped = !taxi.isMovingX() && !taxi.isMovingY();
        // Check if Taxi is in the passenger's detect radius
        float currDistance = (float) Math.sqrt(Math.pow(taxi.getX() - getX(), 2) + Math.pow(taxi.getY() - getY(), 2));
        // Check if Taxi is not having another trip
        boolean isHavingAnotherTrip = taxi.getTrip() != null && taxi.getTrip().getPassenger() != this;

        return currDistance <= TAXI_DETECT_RADIUS && taxiStopped && !isHavingAnotherTrip;
    }

    /**
     * Set the get in taxi status of the people object.
     * This is used to set an indication to check whether the people object is in the taxi or not.
     *
     * @param taxi The taxi object to be checked. If it is null, the people object is not in a taxi at the moment in
     *             the game play.
     */
    public void setIsGetInTaxi(Taxi taxi) {
        if (taxi == null) {
            isGetInTaxi = false;
        } else if ((float) Math.sqrt(Math.pow(taxi.getX() - getX(), 2) + Math.pow(taxi.getY() - getY(), 2)) <= 1) {
            isGetInTaxi = true;
        }
    }

    /**
     * Unused update method of Passenger
     *
     * @param input The current mouse/keyboard input.
     */
    @Override
    public void update(Input input) {
        // if the passenger is not in the taxi or the trip is completed, update the passenger status based on keyboard
        // input. This means the passenger is go down when taxi moves up.
        if (!isGetInTaxi) {
            render();
        }
        if (!isGetInTaxi || (trip != null && trip.isComplete())) {
            if (input != null) {
                adjustToInputMovement(input);
            }
            moveRelative();
        }

        if (health <= 0) {
            bleed();
        }
    }

    /**
     * Decrements passenger health based on damage dealt
     *
     * @param dmg Integer value to decrement Entity health
     */
    @Override
    public void takeDamage(int dmg) {
        if (!isGetInTaxi) {
            health -= dmg;
        }

        if (health < 0) {
            this.health = 0;
        }
    }

    /**
     * Render logic for passenger bleeding
     */
    @Override
    public void bleed() {
        blood.draw(getX(), getY());
    }

    /**
     * Gets current passenger health
     *
     * @return current int value of passenger health
     */
    public int getHealth() {
        return health;
    }
}
