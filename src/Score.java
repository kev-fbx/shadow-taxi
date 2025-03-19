/**
 * Score class that stores the player's name and score.
 *
 * @author SWEN20003 Teaching Staff
 */
public class Score {
    /**
     * String holding player name of score
     */
    private final String PLAYER_NAME;
    /**
     * Value of score achieved in game
     */
    private final double SCORE;

    /**
     * Constructor for a score object that holds a score value and the owner
     *
     * @param playerName String value of Player's name
     * @param score      Double value of Player's score achieved
     */
    public Score(String playerName, double score) {
        this.PLAYER_NAME = playerName;
        this.SCORE = score;
    }

    /**
     * Gets name of Player
     *
     * @return String value of Player's name
     */
    public String getPlayerName() {
        return PLAYER_NAME;
    }

    /**
     * Gets score of Player
     *
     * @return Double value of Player's score
     */
    public double getScore() {
        return SCORE;
    }
}
