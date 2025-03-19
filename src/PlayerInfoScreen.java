import bagel.Font;
import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.Properties;

/**
 * The screen class which allows the player to enter their name.
 *
 * @author SWEN20003 Teaching Staff, adapted by Kevin Tran
 */
public class PlayerInfoScreen extends Screen {
    /**
     * Image object that holds background image of player info screen
     */
    private final Image BG;
    /**
     * String holding Player Name header text
     */
    private final String PI_PLAYER_NAME;
    /**
     * String holding Start Game header text
     */
    private final String PI_START_GAME;
    /**
     * Font information for rendering text on player info screen
     */
    private final Font PI_FONT;
    /**
     * Y-coordinate of Player Name header
     */
    private final int PLAYER_NAME_Y;
    /**
     * Y-coordinate of input text
     */
    private final int PLAYER_NAME_INPUT_Y;
    /**
     * Y-coordinate of Start Game header
     */
    private final int START_GAME_Y;
    /**
     * String that will contain player input for name
     */
    private String playerName;

    /**
     * Constructor for player information screen as part of game loop. Reads user input for their name
     *
     * @param gameProp Properties object that holds game properties
     */
    public PlayerInfoScreen(Properties gameProp) {
        super(gameProp.getProperty("backgroundImage.playerInfo"));

        BG = new Image(getIMG_PATH());
        PI_PLAYER_NAME = getMsgProp().getProperty("playerInfo.playerName");
        PI_START_GAME = getMsgProp().getProperty("playerInfo.start");
        PI_FONT = new Font(getGameProp().getProperty("font"),
                Integer.parseInt(getGameProp().getProperty("playerInfo.fontSize")));
        PLAYER_NAME_Y = Integer.parseInt(getGameProp().getProperty("playerInfo.playerName.y"));
        PLAYER_NAME_INPUT_Y = Integer.parseInt(getGameProp().getProperty("playerInfo.playerNameInput.y"));
        START_GAME_Y = Integer.parseInt(getGameProp().getProperty("playerInfo.start.y"));

        playerName = "";
    }

    /**
     * Show the player info screen with the input entered for the player name and the start game message.
     *
     * @param input The current mouse/keyboard input.
     * @return true if ENTER key is pressed, false otherwise.
     */
    public boolean update(Input input) {
        render();

        String letter = MiscUtils.getKeyPress(input);
        if (letter != null && !(input.wasPressed(Keys.BACKSPACE) || input.wasPressed(Keys.DELETE))) {
            playerName += letter;
        } else if ((input.wasPressed(Keys.BACKSPACE) || input.wasPressed(Keys.DELETE)) && !playerName.isEmpty()) {
            playerName = playerName.substring(0, playerName.length() - 1);
        }

        return input.wasPressed(Keys.ENTER);
    }

    /**
     * Render background and text of player info screen
     */
    @Override
    public void render() {
        BG.drawFromTopLeft(0, 0);
        renderCentreText(PI_FONT, PI_PLAYER_NAME, getWinCentreX(), PLAYER_NAME_Y, getTextWhite());
        renderCentreText(PI_FONT, PI_START_GAME, getWinCentreX(), START_GAME_Y, getTextWhite());
        renderCentreText(PI_FONT, PI_PLAYER_NAME, getWinCentreX(), PLAYER_NAME_Y, getTextWhite());
        renderCentreText(PI_FONT, playerName, getWinCentreX(), PLAYER_NAME_INPUT_Y, getTextBlack());
    }

    /**
     * Gets the user inputted Player Name
     *
     * @return String holding player name
     */
    public String getPlayerName() {
        return playerName;
    }

}