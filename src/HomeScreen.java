import bagel.Font;
import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.Properties;

/**
 * A class representing the home screen of the game.
 *
 * @author SWEN20003 Teaching Staff, adapted by Kevin Tran
 */
public class HomeScreen extends Screen {
    /**
     * Image object that holds background image of home screen
     */
    private final Image BG;
    /**
     * String holding game title
     */
    private final String TITLE;
    /**
     * String holding home screen instructions
     */
    private final String PROMPT;
    /**
     * Font information for title rendering
     */
    private final Font TITLE_FONT;
    /**
     * Font information for home screen instruction rendering
     */
    private final Font PROMPT_FONT;
    /**
     * Y-coordinate of title text
     */
    private final int TITLE_Y;
    /**
     * Y-coordinate of instruction text
     */
    private final int PROMPT_Y;

    /**
     * Constructor for home screen that displays title at beginning of game loop
     *
     * @param gameProp Properties object that holds game properties
     */
    public HomeScreen(Properties gameProp) {
        super(gameProp.getProperty("backgroundImage"));
        BG = new Image(getGameProp().getProperty("backgroundImage.home"));
        TITLE = getMsgProp().getProperty("home.title");
        TITLE_FONT = new Font(getGameProp().getProperty("font"),
                Integer.parseInt(getGameProp().getProperty("home.title.fontSize")));
        TITLE_Y = Integer.parseInt(getGameProp().getProperty("home.title.y"));

        PROMPT = getMsgProp().getProperty("home.instruction");
        PROMPT_FONT = new Font(getGameProp().getProperty("font"),
                Integer.parseInt(getGameProp().getProperty("home.instruction.fontSize")));
        PROMPT_Y = Integer.parseInt(getGameProp().getProperty("home.instruction.y"));
    }

    /**
     * Show the home screen with the title and the background.
     *
     * @param input The current mouse/keyboard input.
     * @return true if ENTER key is pressed, false otherwise.
     */
    public boolean update(Input input) {
        render();
        return input.wasPressed(Keys.ENTER);
    }

    /**
     * Text and image rendering of home screen
     */
    @Override
    public void render() {
        BG.drawFromTopLeft(0, 0);
        renderCentreText(TITLE_FONT, TITLE, getWinCentreX(), TITLE_Y, getTextWhite());
        renderCentreText(PROMPT_FONT, PROMPT, getWinCentreX(), PROMPT_Y, getTextWhite());
    }
}
