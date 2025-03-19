/**
 * Interface grouping methods relevant to empowering an Entity
 *
 * @author Kevin Tran
 */
public interface Empowerable {
    /**
     * Abstract method that will implement coin capture logic
     *
     * @param coin Coin object that is collected by Driver or Taxi
     */
    void collectCoin(Coin coin);

    /**
     * Abstract method that will implement star capture logic
     *
     * @param star Star object that is collected by Driver or Taxi
     */
    void collectStar(Star star);
}
