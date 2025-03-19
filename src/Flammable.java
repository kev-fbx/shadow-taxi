import bagel.Image;

/**
 * Interface grouping attributes and methods relevant to burning Entities
 *
 * @author Kevin Tran
 */
public interface Flammable extends VFX {
    /**
     * Image attribute holding the fire image to be rendered
     */
    Image fire = new Image(GAME_PROP.getProperty("gameObjects.fire.image"));
    /**
     * Image attribute holding the smoke image to be rendered
     */
    Image smoke = new Image(GAME_PROP.getProperty("gameObjects.smoke.image"));

    /**
     * Abstract method that will implement fire rendering logic
     */
    void burn();

    /**
     * Abstract method that will implement smoke rendering logic
     */
    void emitSmoke();
}
