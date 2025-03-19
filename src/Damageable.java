/**
 * Interface providing the method relevant to damage logic among Entities
 *
 * @author Kevin Tran
 */
public interface Damageable {
    /**
     * Decrements Entity health by dmg
     *
     * @param dmg Integer value to decrement Entity health
     */
    void takeDamage(int dmg);
}
