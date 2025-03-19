import java.util.Properties;

/**
 * Interface that groups relevant VFX rendering attributes
 *
 * @author Kevin Tran
 */
public interface VFX {
    /**
     * Game properties file that hold game information including VFX lifetimes
     */
    Properties GAME_PROP = IOUtils.readPropertiesFile("res/app.properties");
    /**
     * Fire image render lifetime
     */
    int FIRE_LT = Integer.parseInt(GAME_PROP.getProperty("gameObjects.fire.ttl"));
    /**
     * Smoke image render lifetime
     */
    int SMOKE_LT = Integer.parseInt(GAME_PROP.getProperty("gameObjects.smoke.ttl"));
    /**
     * Blood image render lifetime
     */
    int BLOOD_LT = Integer.parseInt(GAME_PROP.getProperty("gameObjects.blood.ttl"));
}
