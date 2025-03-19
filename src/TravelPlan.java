import java.util.Properties;

/**
 * This class contains information relevant to a trip that is initiated between a Passenger and the Taxi
 *
 * @author SWEN20003 Teaching Staff
 */
public class TravelPlan {
    /**
     * X-coordinate of Passenger destination
     */
    private final int END_X;
    /**
     * Y distance that passenger must travel
     */
    private final int DISTANCE_Y;
    /**
     * Properties object that holds game properties
     */
    private final Properties GAME_PROP;
    /**
     * Y-coordinate of Passengerdestination
     */
    private int endY;
    /**
     * Current priority of passenger
     */
    private int currentPriority;
    /**
     * Initial priority of Passenger
     */
    private final int initPriority;
    /**
     * Boolean value indicating if coin is applied to trip (true) or not (false)
     */
    private boolean coinPowerApplied;

    /**
     * Constructor for TravelPlan objects
     *
     * @param gameProp  Properties object that holds game properties
     * @param endX      Final X-coordinate of trip
     * @param distanceY Y distance that passenger must travel
     * @param priority  Initial priority of passenger
     */
    public TravelPlan(Properties gameProp, int endX, int distanceY, int priority) {
        this.GAME_PROP = gameProp;
        this.END_X = endX;
        this.DISTANCE_Y = distanceY;
        this.currentPriority = priority;
        this.initPriority = priority;
    }

    /**
     * Gets X-coordinate of destination
     *
     * @return int value of destination X-coordinate for trip
     */
    public int getEndX() {
        return END_X;
    }

    /**
     * Gets current priority of passenger
     *
     * @return int value of Passenger's current priority
     */
    public int getPriority() {
        return currentPriority;
    }

    /**
     * Gets destination Y-coordinate of trip
     *
     * @return int value of destination Y-coordinate for trip
     */
    public int getEndY() {
        return endY;
    }

    /**
     * Calculates the destination Y-coordinate
     *
     * @param startY Starting Y-coordinate of trip
     */
    public void setStartY(int startY) {
        this.endY = startY - DISTANCE_Y;
    }

    /**
     * Changes priority of Passenger
     *
     * @param priority New priority level
     */
    public void setPriority(int priority) {
        this.currentPriority = priority;
    }

    /**
     * Sets coin activation state to true
     */
    public void setCoinPowerApplied() {
        this.coinPowerApplied = true;
    }

    /**
     * Gets coin activation state
     *
     * @return true if coin power is currently applied to trip, false otherwise
     */
    public boolean getCoinPowerApplied() {
        return this.coinPowerApplied;
    }

    /**
     * Get the expected fee of the trip based on the travel distance and priority.
     *
     * @return The expected fee of the trip.
     */
    public float getExpectedFee() {
        float ratePerY = Float.parseFloat(GAME_PROP.getProperty("trip.rate.perY"));
        float travelPlanDistanceFee = ratePerY * DISTANCE_Y;
        float travelPlanPriorityFee = currentPriority * Float.parseFloat(
                GAME_PROP.getProperty(String.format("trip.rate.priority%d", currentPriority)));

        return travelPlanDistanceFee + travelPlanPriorityFee;
    }
}
