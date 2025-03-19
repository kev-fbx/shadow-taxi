import bagel.DrawOptions;
import bagel.Font;
import bagel.Input;
import bagel.Window;
import bagel.util.Colour;

import java.util.Properties;

/**
 * Code for abstract Screen class. Contains common properties
 * and methods of all screens in Shadow Taxi.
 *
 * @author Kevin Tran
 */
public abstract class Screen implements Scrollable {
    /**
     * Properties attribute that holds game properties from file
     */
    private final Properties GAME_PROP = IOUtils.readPropertiesFile("res/app.properties");
    /**
     * Properties attribute that holds message properties from file
     */
    private final Properties MSG_PROP = IOUtils.readPropertiesFile("res/message_en.properties");
    /**
     * DrawOptions attribute for rendering white text
     */
    private static final DrawOptions TEXT_WHITE = new DrawOptions().setBlendColour(Colour.WHITE);
    /**
     * DrawOptions attribute for rendering black text
     */
    private static final DrawOptions TEXT_BLACK = new DrawOptions().setBlendColour(Colour.BLACK);
    /**
     * Centre X-coordinates of the game window for text rendering
     */
    private static final double WIN_CENTRE_X = Window.getWidth() / 2.0;
    /**
     * X-coordinate of screen
     */
    private final double X;
    /**
     * Y-coordinate of screen
     */
    private final double Y;
    /**
     * File path to screen background
     */
    private final String IMG_PATH;
    /**
     * File path to font used for text rendering
     */
    private final String FONT_PATH;

    /**
     * Screen constructor that takes in properties to be passed onto
     * subclasses and initialises common attributes.
     *
     * @param IMG_PATH Relative path of background image file
     */
    public Screen(String IMG_PATH) {
        this.X = Integer.parseInt(GAME_PROP.getProperty("window.width"));
        this.Y = Integer.parseInt(GAME_PROP.getProperty("window.height"));
        this.IMG_PATH = IMG_PATH;
        this.FONT_PATH = GAME_PROP.getProperty("font");
    }

    /**
     * Renders text onto screen. Positions the text based on the centre of the X-coordinate.
     *
     * @param font    Font object that contains text font path and text font size
     * @param text    String content to be rendered
     * @param y       Y-coordinate of text position
     * @param options DrawOptions that alter shown text, such as text colour
     */
    protected void renderCentreText(Font font, String text, double x, double y, DrawOptions options) {
        font.drawString(text, getWinCentreX() - font.getWidth(text) / 2.0, y, options);
    }

    /**
     * Renders text onto screen. Positions the text based on the left-most X-coordinate.
     *
     * @param font Font object that contains text font path and text font size
     * @param text String content to be rendered
     * @param x    X-coordinate of text position
     * @param y    Y-coordinate of text position
     */
    protected void renderText(Font font, String text, double x, double y) {
        font.drawString(text, x, y);
    }

    /**
     * Gets the DrawOptions for white text
     *
     * @return DrawOptions object that holds white text
     */
    public DrawOptions getTextWhite() {
        return TEXT_WHITE;
    }

    /**
     * Gets the DrawOptions for black text
     *
     * @return DrawOptions object that holds black text
     */
    public DrawOptions getTextBlack() {
        return TEXT_BLACK;
    }

    /**
     * Gets Y-coordinate of screen
     *
     * @return Double value of screen's Y-coordinate
     */
    public double getY() {
        return Y;
    }

    /**
     * Gets file path of screen background image
     *
     * @return String value of file path to screen background image
     */
    public String getIMG_PATH() {
        return IMG_PATH;
    }

    /**
     * Gets game properties file
     *
     * @return Properties attribute containing all game properties
     */
    public Properties getGameProp() {
        return GAME_PROP;
    }

    /**
     * Gets message properties file
     *
     * @return Properties attribute containing all message properties
     */
    public Properties getMsgProp() {
        return MSG_PROP;
    }

    /**
     * Gets centre X-coordinate of the game window
     *
     * @return Static double value of game window centre X-coordinate
     */
    public static double getWinCentreX() {
        return WIN_CENTRE_X;
    }

    /**
     * Abstract method for screen updating logic
     *
     * @param input The current mouse/keyboard input.
     * @return boolean value indicating if input is being read on the current screen
     */
    public abstract boolean update(Input input);

    /**
     * Abstract method for screen rendering logic
     */
    public abstract void render();
}
