import java.util.Properties;

/**
 * A class representing the trip in the game play.
 * It contains the passenger, driver, taxi, trip end flag and other relevant details.
 * It will calculate the fee of the trip and update the status of the trip.
 *
 * @author SWEN20003 Teaching Staff
 */
public class Trip {
    /**
     * Pasenger object of trip
     */
    private final Passenger PASSENGER;
    /**
     * Properties object that holds game properties
     */
    private final Properties GAME_PROP;
    /**
     * Flag object of trip
     */
    private final Flag FLAG;
    /**
     * Taxi that is conducting trip
     */
    private final Taxi TAXI;
    /**
     * Boolean value indicating if trip is completed
     */
    private boolean isComplete;
    /**
     * Fee to increment Driver payment by
     */
    private float fee;
    /**
     * Penalty rate that decrements Driver pay
     */
    private float penalty;

    /**
     * Constructor for Trip objects
     *
     * @param gameProp  Properties object that holds game properties
     * @param passenger Current Passenger object for trip
     * @param taxi      Current Taxi object that is completing trip
     */
    public Trip(Properties gameProp, Passenger passenger, Taxi taxi) {
        this.PASSENGER = passenger;
        this.TAXI = taxi;
        this.FLAG = new Flag(gameProp, passenger.getTravelPlan().getEndX(), passenger.getTravelPlan().getEndY());
        this.GAME_PROP = gameProp;
    }

    /**
     * Gets Passenger of trip
     *
     * @return Current passenger in trip
     */
    public Passenger getPassenger() {
        return PASSENGER;
    }

    /**
     * Gets completion state of trip
     *
     * @return true if trip is completed, false otherwise
     */
    public boolean isComplete() {
        return isComplete;
    }

    /**
     * Gets Flag object of trip
     *
     * @return Flag object of trip
     */
    public Flag getFlag() {
        return FLAG;
    }

    /**
     * Fee value of trip upon completion
     *
     * @return float value of fee payment
     */
    public float getFee() {
        return fee;
    }

    /**
     * Expected payment to Driver based on current trip state
     *
     * @return int value of expected pay for trip
     */
    public int getExpectedFee() {
        return PASSENGER.getTravelPlan().getPriority();
    }

    /**
     * Gets penalty value of the trip to deduct from final pay
     *
     * @return float value of penalty fee
     */
    public float getPenalty() {
        return penalty;
    }

    /**
     * Check if the trip has reached the end point based on several criteria.
     *
     * @return true if the trip has reached the end point, false otherwise.
     */
    public boolean hasReachedEnd() {
        // Taxi is stopped when it is not moving in any direction and has health > 0.
        boolean isTaxiStopped = !TAXI.isMovingY() && !TAXI.isMovingX();
        float currDistance = getCurrentDistance();
        boolean passedDropOff = hasPassedDropOff();

        // The trip is considered as reached end if the taxi is stopped and the distance between the passenger
        // and the drop-off point is less than the radius of the drop-off point.
        // Or if the passenger has passed the drop-off point and the taxi is stopped.
        return (currDistance <= FLAG.getRad() && isTaxiStopped) || (passedDropOff && isTaxiStopped);
    }

    /**
     * Check if the passenger has passed the drop-off point.
     *
     * @return true if the passenger has passed the drop-off point, false otherwise.
     */
    private boolean hasPassedDropOff() {
        return PASSENGER.getY() < FLAG.getY() && getCurrentDistance() > FLAG.getRad();
    }

    /**
     * Calculate the current distance (Euclidean) between the passenger and the drop-off point.
     *
     * @return The current distance between the passenger and the drop-off point.
     */
    private float getCurrentDistance() {
        return (float) Math.sqrt(Math.pow(FLAG.getX() - PASSENGER.getX(), 2) +
                Math.pow(FLAG.getY() - PASSENGER.getY(), 2));
    }

    /**
     * End the trip (update relevant status) and calculate the fee.
     */
    public void end() {
        isComplete = true;
        PASSENGER.setIsGetInTaxi(null);
        TAXI.setTrip(null);
        calculateFee();
    }

    /**
     * Calculate the fee of the trip based on the travel plan details, rate and penalty if applicable.
     */
    private void calculateFee() {
        float initialFee = PASSENGER.getTravelPlan().getExpectedFee();

        // If the passenger has passed the drop-off point, a penalty will be applied to the fee.
        if (hasPassedDropOff()) {
            float penalty = Float.parseFloat(GAME_PROP.getProperty("trip.penalty.perY")) *
                    (FLAG.getY() - PASSENGER.getY());
            initialFee -= penalty;
            this.penalty = penalty;
        }

        // Fee cannot be negative.
        if (fee < 0) {
            initialFee = 0;
        }

        this.fee = initialFee;
    }
}
