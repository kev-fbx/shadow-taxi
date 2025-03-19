import bagel.*;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Represents the gamePlay screen in the game.
 *
 * @author SWEN20003 Teaching Staff, adapted by Kevin Tran
 */
public class GameScreen extends Screen {
    /**
     * Earnings of current game
     */
    private float totalEarnings;
    /**
     * Number of frames where coin power is active
     */
    private float coinFramesActive;
    /**
     * Number of frames where star power is active
     */
    private float starFramesActive;
    /**
     * Integer that tracks game time
     */
    private int currFrame = 0;
    /**
     * ArrayList of String[] that holds gameObjects.csv
     */
    private final ArrayList<String[]> GAME_OBJECTS;
    /**
     * Taxi object of game
     */
    private Taxi taxi;
    /**
     * Driver object of game
     */
    private Driver driver;
    /**
     * ArrayList of passengers in game
     */
    private final ArrayList<Passenger> PASSENGERS;
    /**
     * ArrayList of coins in game
     */
    private final ArrayList<Coin> COINS;
    /**
     * ArrayList of stars in game
     */
    private final ArrayList<Star> STARS;
    /**
     * ArrayList of passive and enemy cars in game
     */
    private final ArrayList<Car> CARS;
    /**
     * ArrayList holding all game entities
     */
    private final ArrayList<Entity> ENTITIES;
    /**
     * Array of each lane's X-coordinates
     */
    private final int[] LANES;
    /**
     * Array of possible Y-coordinates for car spawning
     */
    private final int[] Y_SPAWNS;
    /**
     * Static spawn rate of passive cars
     */
    private final static int PASSIVE_SPAWN_RATE = 200;
    /**
     * Static spawn rate of enemy cars
     */
    private final static int ENEMY_SPAWN_RATE = 400;
    /**
     * CSV file containing frame ranges of weather states
     */
    private final ArrayList<String[]> weatherFile;
    /**
     * Target number for earnings
     */
    private final float TARGET;
    /**
     * Maximum frames game can run
     */
    private final int MAX_FRAMES;
    /**
     * Different images to render based on weather state
     */
    private final Image BG_SUN, BG_RAIN;
    /**
     * Current images to be rendered
     */
    private Image bgCurr1, bgCurr2;
    /**
     * Current Y-coordinate of rendered images
     */
    private double currY1, currY2;
    /**
     * Initial Y-coordinate value for first screen
     */
    private final double BG_INIT_Y = Window.getHeight() / 2.0;
    /**
     * Player name to store in score.csv
     */
    private final String PLAYER_NAME;
    /**
     * State indicating if data was written to file
     */
    private boolean savedData;
    /**
     * Font object for rendering text
     */
    private final Font INFO_FONT;
    /**
     * X and Y coordinates of earnings text
     */
    private final int EARNINGS_X, EARNINGS_Y;
    /**
     * X and Y coordinates of coin information text
     */
    private final int COIN_X, COIN_Y;
    /**
     * X and Y coordinates of target earnings text
     */
    private final int TARGET_X, TARGET_Y;
    /**
     * X and Y coordinates of max frames text
     */
    private final int MAX_FRAMES_X, MAX_FRAMES_Y;
    /**
     * X and Y coordinates of taxi health text
     */
    private final int TAXI_HEALTH_X, TAXI_HEALTH_Y;
    /**
     * X and Y coordinates of passenger health text
     */
    private final int PASSENGER_HEALTH_X, PASSENGER_HEALTH_Y;
    /**
     * X and Y coordinates of driver health text
     */
    private final int DRIVER_HEALTH_X, DRIVER_HEALTH_Y;
    /**
     * X and Y coordinates of trip information
     */
    private final int TRIP_INFO_X, TRIP_INFO_Y;
    /**
     * First trip information text offset
     */
    private final int TRIP_INFO_OFFSET_1;
    /**
     * Second trip information text offset
     */
    private final int TRIP_INFO_OFFSET_2;
    /**
     * Third trip information text offset
     */
    private final int TRIP_INFO_OFFSET_3;

    /**
     * Constructor for GameScreen that instantiates the gameplay of Shadow Taxi
     *
     * @param gameProp   Properties object that holds game properties
     * @param playerName String input from PlayerInfoScreen of the player's name
     */
    public GameScreen(Properties gameProp, String playerName) {
        super(gameProp.getProperty("backgroundImage"));

        // read game objects from file and weather file and populate the game objects and weather conditions
        GAME_OBJECTS = IOUtils.readCommaSeparatedFile(getGameProp().getProperty("gamePlay.objectsFile"));
        weatherFile = IOUtils.readCommaSeparatedFile(getGameProp().getProperty("gamePlay.weatherFile"));
        this.PASSENGERS = new ArrayList<>();
        this.COINS = new ArrayList<>();
        this.STARS = new ArrayList<>();
        this.CARS = new ArrayList<>();
        this.ENTITIES = new ArrayList<>();
        initObjects();

        this.LANES = new int[]{
                Integer.parseInt(getGameProp().getProperty("roadLaneCenter1")),
                Integer.parseInt(getGameProp().getProperty("roadLaneCenter2")),
                Integer.parseInt(getGameProp().getProperty("roadLaneCenter3"))
        };

        this.Y_SPAWNS = new int[]{-50, 768};

        this.TARGET = Float.parseFloat(getGameProp().getProperty("gamePlay.target"));
        this.MAX_FRAMES = Integer.parseInt(getGameProp().getProperty("gamePlay.maxFrames"));

        this.BG_SUN = new Image(getGameProp().getProperty("backgroundImage.sunny"));
        this.BG_RAIN = new Image(getGameProp().getProperty("backgroundImage.raining"));
        this.currY1 = BG_INIT_Y;
        this.currY2 = -BG_INIT_Y;

        // display text vars
        INFO_FONT = new Font(getGameProp().getProperty("font"), Integer.parseInt(
                getGameProp().getProperty("gamePlay.info.fontSize")));
        EARNINGS_Y = Integer.parseInt(getGameProp().getProperty("gamePlay.earnings.y"));
        EARNINGS_X = Integer.parseInt(getGameProp().getProperty("gamePlay.earnings.x"));
        COIN_X = Integer.parseInt(getGameProp().getProperty("gameplay.coin.x"));
        COIN_Y = Integer.parseInt(getGameProp().getProperty("gameplay.coin.y"));
        TARGET_X = Integer.parseInt(getGameProp().getProperty("gamePlay.target.x"));
        TARGET_Y = Integer.parseInt(getGameProp().getProperty("gamePlay.target.y"));
        MAX_FRAMES_X = Integer.parseInt(getGameProp().getProperty("gamePlay.maxFrames.x"));
        MAX_FRAMES_Y = Integer.parseInt(getGameProp().getProperty("gamePlay.maxFrames.y"));

        TAXI_HEALTH_X = Integer.parseInt(getGameProp().getProperty("gamePlay.taxiHealth.x"));
        TAXI_HEALTH_Y = Integer.parseInt(getGameProp().getProperty("gamePlay.taxiHealth.y"));
        PASSENGER_HEALTH_X = Integer.parseInt(getGameProp().getProperty("gamePlay.passengerHealth.x"));
        PASSENGER_HEALTH_Y = Integer.parseInt(getGameProp().getProperty("gamePlay.passengerHealth.y"));
        DRIVER_HEALTH_X = Integer.parseInt(getGameProp().getProperty("gamePlay.driverHealth.x"));
        DRIVER_HEALTH_Y = Integer.parseInt(getGameProp().getProperty("gamePlay.driverHealth.y"));

        // current trip info vars
        TRIP_INFO_X = Integer.parseInt(getGameProp().getProperty("gamePlay.tripInfo.x"));
        TRIP_INFO_Y = Integer.parseInt(getGameProp().getProperty("gamePlay.tripInfo.y"));
        TRIP_INFO_OFFSET_1 = 30;
        TRIP_INFO_OFFSET_2 = 60;
        TRIP_INFO_OFFSET_3 = 90;

        this.PLAYER_NAME = playerName;
    }

    /**
     * Changes the current background images based on the weather file frame ranges
     * by parsing through weatherFile
     */
    private void changeWeather() {
        int state = 0, start = 1, end = 2;

        for (String[] currLine : weatherFile) {
            if (currFrame >= Integer.parseInt(currLine[start]) &&
                    currFrame <= Integer.parseInt(currLine[end])) {
                if (currLine[state].equals("SUNNY")) {
                    bgCurr1 = bgCurr2 = BG_SUN;
                }
                if (currLine[state].equals("RAINING")) {
                    bgCurr1 = bgCurr2 = BG_RAIN;
                }
            }
        }
    }

    /**
     * Initialises game objects by parsing through gameObjects
     */
    private void initObjects() {
        int type = 0, initX = 1, initY = 2, priority = 3, endX = 4, endY = 5, umbrellaState = 6;
        int passengerCount = 0;
        for (String[] obj : GAME_OBJECTS) {
            if (obj[type].equals("PASSENGER")) {
                passengerCount++;
            }
        }
        for (String[] obj : GAME_OBJECTS) {
            switch (obj[type]) {
                case "DRIVER":
                    this.driver = new Driver(getGameProp(), Integer.parseInt(obj[initX]), Integer.parseInt(obj[initY]));
                    getEntities().add(driver);
                    break;
                case "COIN":
                    this.COINS.add(new Coin(getGameProp(), Integer.parseInt(obj[initX]), Integer.parseInt(obj[initY])));
                    getEntities().add(COINS.get(COINS.size() - 1));
                    break;
                case "PASSENGER":
                    this.PASSENGERS.add(new Passenger(getGameProp(), Integer.parseInt(obj[initX]),
                            Integer.parseInt(obj[initY]),
                            Integer.parseInt(obj[priority]),
                            Integer.parseInt(obj[endX]),
                            Integer.parseInt(obj[endY]),
                            Integer.parseInt(obj[umbrellaState])));
                    getEntities().add(PASSENGERS.get(PASSENGERS.size() - 1));
                    break;
                case "INVINCIBLE_POWER":
                    this.STARS.add(new Star(getGameProp(), Integer.parseInt(obj[initX]), Integer.parseInt(obj[initY])));
                    getEntities().add(STARS.get(STARS.size() - 1));
                    break;
                case "TAXI":
                    this.taxi = new Taxi(getGameProp(), Integer.parseInt(obj[initX]), Integer.parseInt(obj[initY]), passengerCount);
                    getEntities().add(taxi);
                    break;
            }
        }
    }

    /**
     * Randomly spawns passive and enemy type cars
     */
    private void spawnCars() {
        if (MiscUtils.canSpawn(ENEMY_SPAWN_RATE)) {
            int laneIdx = MiscUtils.getRandomInt(0, LANES.length);
            int yIdx = MiscUtils.getRandomInt(0, Y_SPAWNS.length);
            this.CARS.add(new Enemy(getGameProp(), LANES[laneIdx], Y_SPAWNS[yIdx]));
            getEntities().add(CARS.get(CARS.size() - 1));
        }
        if (MiscUtils.canSpawn(PASSIVE_SPAWN_RATE)) {
            int laneIdx = MiscUtils.getRandomInt(0, LANES.length);
            int yIdx = MiscUtils.getRandomInt(0, Y_SPAWNS.length);
            this.CARS.add(new PassiveCar(getGameProp(), LANES[laneIdx], Y_SPAWNS[yIdx]));
            getEntities().add(CARS.get(CARS.size() - 1));
        }
    }

    /**
     * Update the states of the game objects based on the keyboard input.
     * Handle the spawning of other cars in random intervals
     * Change the background image and change priorities based on the weather condition
     * Handle collision between game objects
     * Spawn new taxi if the active taxi is destroyed
     *
     * @param input The current mouse/keyboard input.
     * @return boolean value corresponding to win/loss state
     */
    @Override
    public boolean update(Input input) {
        currFrame++;
        changeWeather();
        render();

        if (input.isDown(Keys.UP)) {
            currY1 += SCROLL_SPEED;
            currY2 += SCROLL_SPEED;

            if (currY1 >= 1.5 * Window.getHeight()) {
                currY1 = BG_INIT_Y;
                currY2 = -BG_INIT_Y;
            }
            if (currY2 >= 1.5 * Window.getHeight()) {
                currY2 = BG_INIT_Y;
                currY1 = -BG_INIT_Y;
            }
        }

        spawnCars();

        // Entity update calls here
        driver.update(input);
        taxi.update(input);
        totalEarnings = taxi.calculateTotalEarnings();

        CollisionEvent.checkCollisions(getEntities());

        if (taxi.isTaxiDead()) {
            driver.eject();
        }

        for (Passenger passenger : PASSENGERS) {
            passenger.updatePassenger(input, taxi);
        }

        for (Car car : CARS) {
            car.update(input);
        }

        if (!STARS.isEmpty()) {
            int minFramesActive = STARS.get(0).getMaxFrames();
            for (Star starPower : STARS) {
                starPower.update(input);

                if (starPower.getFramesActive() > starPower.getMaxFrames()) {
                    taxi.setNotInvincible();
                }

                int framesActive = starPower.getFramesActive();
                if (starPower.getIsActive() && minFramesActive > framesActive) {
                    minFramesActive = framesActive;
                }
            }
            starFramesActive = minFramesActive;
        }

        if (!COINS.isEmpty()) {
            int minFramesActive = COINS.get(0).getMaxFrames();
            for (Coin coinPower : COINS) {
                coinPower.update(input);

                // check if there's active coin and finding the coin with maximum ttl
                int framesActive = coinPower.getFramesActive();
                if (coinPower.getIsActive() && minFramesActive > framesActive) {
                    minFramesActive = framesActive;
                }
            }
            coinFramesActive = minFramesActive;
        }

        displayInfo();
        return isGameOver() || isLevelCompleted();
    }

    /**
     * Handles rendering of background images
     */
    @Override
    public void render() {
        bgCurr1.draw(getWinCentreX(), currY1);
        bgCurr2.draw(getWinCentreX(), currY2);
    }

    /**
     * Display the game information on the screen.
     */
    private void displayInfo() {
        renderText(INFO_FONT, getMsgProp().getProperty("gamePlay.earnings") + getTotalEarnings(), EARNINGS_X, EARNINGS_Y);
        renderText(INFO_FONT, getMsgProp().getProperty("gamePlay.target") + String.format("%.02f", TARGET), TARGET_X, TARGET_Y);
        renderText(INFO_FONT, getMsgProp().getProperty("gamePlay.remFrames") + (MAX_FRAMES - currFrame), MAX_FRAMES_X, MAX_FRAMES_Y);

        renderText(INFO_FONT, getMsgProp().getProperty("gamePlay.taxiHealth") + String.format("%d.00", taxi.getHealth()), TAXI_HEALTH_X, TAXI_HEALTH_Y);
        renderText(INFO_FONT, getMsgProp().getProperty("gamePlay.driverHealth") + String.format("%d.00", driver.getHealth()), DRIVER_HEALTH_X, DRIVER_HEALTH_Y);

        if (taxi.getTrip() != null) {
            int pHealth = taxi.getTrip().getPassenger().getHealth();
            renderText(INFO_FONT, getMsgProp().getProperty("gamePlay.passengerHealth") + String.format("%d.00", pHealth), PASSENGER_HEALTH_X, PASSENGER_HEALTH_Y);
        }

        if (!COINS.isEmpty() && COINS.get(0).getMaxFrames() != coinFramesActive) {
            renderText(INFO_FONT, String.valueOf(Math.round(coinFramesActive)), COIN_X, COIN_Y);
        }

        if (!STARS.isEmpty() && STARS.get(0).getMaxFrames() != starFramesActive) {
            renderText(INFO_FONT, String.valueOf(Math.round(starFramesActive)), COIN_X - 100, COIN_Y);
        }

        Trip lastTrip = taxi.getLastTrip();
        if (lastTrip != null) {
            if (lastTrip.isComplete()) {
                renderText(INFO_FONT, getMsgProp().getProperty("gamePlay.completedTrip.title"), TRIP_INFO_X, TRIP_INFO_Y);
            } else {
                renderText(INFO_FONT, getMsgProp().getProperty("gamePlay.onGoingTrip.title"), TRIP_INFO_X, TRIP_INFO_Y);
            }
            renderText(INFO_FONT, getMsgProp().getProperty("gamePlay.trip.expectedEarning")
                    + lastTrip.getPassenger().getTravelPlan().getExpectedFee(), TRIP_INFO_X, TRIP_INFO_Y
                    + TRIP_INFO_OFFSET_1);
            renderText(INFO_FONT, getMsgProp().getProperty("gamePlay.trip.priority")
                    + lastTrip.getPassenger().getTravelPlan().getPriority(), TRIP_INFO_X, TRIP_INFO_Y
                    + TRIP_INFO_OFFSET_2);

            if (lastTrip.isComplete()) {
                renderText(INFO_FONT, getMsgProp().getProperty("gamePlay.trip.penalty") + String.format("%.02f",
                        lastTrip.getPenalty()), TRIP_INFO_X, TRIP_INFO_Y + TRIP_INFO_OFFSET_3);
            }
        }
    }

    /**
     * Gets the total earnings of a trip
     *
     * @return Formatted string of the trip total earnings
     */
    public String getTotalEarnings() {
        return String.format("%.02f", totalEarnings);
    }

    /**
     * Check if the game is over. If the game is over and not saved the score, save the score.
     *
     * @return true if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        // Game is over if the current frame is greater than the max frames or driver has health < 0
        boolean isGameOver = currFrame >= MAX_FRAMES || !(driver.getHealth() > 0);

        if (currFrame >= MAX_FRAMES && !savedData) {
            savedData = true;
            IOUtils.writeLineToFile(getGameProp().getProperty("gameEnd.scoresFile"), PLAYER_NAME + "," + totalEarnings);
        }
        return isGameOver;
    }

    /**
     * Check if the level is completed. If the level is completed and not saved the score, save the score.
     *
     * @return true if the level is completed, false otherwise.
     */
    public boolean isLevelCompleted() {
        // Level is completed if the total earnings is greater than or equal to the target earnings
        boolean isLevelCompleted = totalEarnings >= TARGET;
        if (isLevelCompleted && !savedData) {
            savedData = true;
            IOUtils.writeLineToFile(getGameProp().getProperty("gameEnd.scoresFile"), PLAYER_NAME + "," + totalEarnings);
        }
        return isLevelCompleted;
    }

    private ArrayList<Entity> getEntities() {
        return this.ENTITIES;
    }
}
