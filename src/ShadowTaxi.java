import bagel.AbstractGame;
import bagel.Input;
import bagel.Keys;
import bagel.Window;

import java.util.Properties;

/**
 * ShadowTaxi class that holds game logic
 *
 * @author SWEN20003 Teaching Staff, adapted by Kevin Tran
 */
public class ShadowTaxi extends AbstractGame {
    /**
     * Properties object that holds game properties
     */
    private final Properties GAME_PROP;
    /**
     * Properties object that holds message properties
     */
    private final Properties MSG_PROP;
    /**
     * Home screen object of Shadow Taxi game
     */
    private final HomeScreen homeScreen;
    /**
     * Player information screen object of Shadow Taxi game
     */
    private PlayerInfoScreen playerInfoScreen;
    /**
     * Game screen object of Shadow Taxi game
     */
    private GameScreen gamePlayScreen;
    /**
     * End screen object of Shadow Taxi game
     */
    private GameEndScreen gameEndScreen;

    /**
     * Constructor for Shadow Taxi game
     *
     * @param gameProps    Properties object that holds game properties
     * @param messageProps Properties object that holds message properties
     */
    public ShadowTaxi(Properties gameProps, Properties messageProps) {
        super(Integer.parseInt(gameProps.getProperty("window.width")),
                Integer.parseInt(gameProps.getProperty("window.height")),
                messageProps.getProperty("home.title"));

        this.GAME_PROP = gameProps;
        this.MSG_PROP = messageProps;

        homeScreen = new HomeScreen(GAME_PROP);
    }

    /**
     * Render the relevant screen based on the keyboard input given by the user and the status of the game play.
     *
     * @param input The current mouse/keyboard input.
     */
    @Override
    protected void update(Input input) {
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        // render the home screen
        if (gamePlayScreen == null && playerInfoScreen == null && gameEndScreen == null) {
            // if the user click ENTER button when in the Home Screen, generate the player info screen
            if (homeScreen.update(input)) {
                playerInfoScreen = new PlayerInfoScreen(GAME_PROP);
            }
        } else if (playerInfoScreen != null && gamePlayScreen == null && gameEndScreen == null) {
            // if the user selects to start the game, generate a new game play screen
            if (playerInfoScreen.update(input)) {
                gamePlayScreen = new GameScreen(GAME_PROP, playerInfoScreen.getPlayerName());
                playerInfoScreen = null;
            }
        } else if (playerInfoScreen == null && gamePlayScreen != null && gameEndScreen == null) {
            // if the game is over or the level is completed, generate new game end screen
            if (gamePlayScreen.update(input)) {
                boolean isWon = gamePlayScreen.isLevelCompleted();

                gameEndScreen = new GameEndScreen(GAME_PROP);
                gameEndScreen.setIsWon(isWon);

                gamePlayScreen = null;
            }
        } else if (playerInfoScreen == null && gamePlayScreen == null && gameEndScreen != null) {
            if (gameEndScreen.update(input)) {
                gamePlayScreen = null;
                playerInfoScreen = null;
                gameEndScreen = null;
            }
        }
    }

    /**
     * Main function of Shadow Taxi game
     *
     * @param args Arguments passed to main
     */
    public static void main(String[] args) {
        Properties game_props = IOUtils.readPropertiesFile("res/app.properties");
        Properties message_props = IOUtils.readPropertiesFile("res/message_en.properties");
        ShadowTaxi game = new ShadowTaxi(game_props, message_props);
        game.run();
    }
}
