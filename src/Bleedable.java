import bagel.Image;

/**
 * Interface that groups relevant attributes and methods for bleeding
 *
 * @author Kevin Tran
 */
public interface Bleedable extends VFX {
    /**
     * Image attribute that holds the blood image to be rendered
     */
    Image blood = new Image(GAME_PROP.getProperty("gameObjects.blood.image"));

    /**
     * Abstract method to draw the blood image
     */
    void bleed();
}
