import bagel.Input;

/**
 * Interface that groups methods relevant to Entities that can move
 *
 * @author Kevin Tran
 */
public interface Movable extends Scrollable {
    /**
     * Abstract method that will provide logic for detecting user input
     *
     * @param input The current mouse/keyboard input.
     */
    void adjustToInputMovement(Input input);

    /**
     * Abstract method that will implement relative movement logic
     */
    void moveRelative();
}