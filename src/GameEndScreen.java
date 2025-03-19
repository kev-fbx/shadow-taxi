import bagel.Font;
import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

/**
 * This class contains attributes and methods relevant to the end screen of the Shadow Taxi game loop
 *
 * @author SWEN20003 Teaching Staff, adapted by Kevin Tran
 */
public class GameEndScreen extends Screen {
    /**
     * Image object that holds background image of end screen
     */
    private final Image BG;
    /**
     * String containing winner message
     */
    private final String GAME_WON_TXT;
    /**
     * String containing loser message
     */
    private final String GAME_LOST_TXT;
    /**
     * String containing leaderboard header
     */
    private final String HIGHEST_SCORE_TXT;
    /**
     * Font object for status text
     */
    private final Font STATUS_FONT;
    /**
     * Font object for leaderboard text
     */
    private final Font SCORES_FONT;
    /**
     * Y-coordinate of status text
     */
    private final int STATUS_Y;
    /**
     * Y-coordinate of leaderboard
     */
    private final int SCORES_Y;
    /**
     * Array of top scores
     */
    private final Score[] TOP_SCORES;
    /**
     * Array of scores
     */
    private Score[] scores;
    /**
     * Boolean value determining if game was won or not
     */
    private boolean isWon;

    /**
     * Constructor for the ending screen of Shadow Taxi after a game is completed
     *
     * @param gameProp Properties object that holds game properties
     */
    public GameEndScreen(Properties gameProp) {
        super(gameProp.getProperty("backgroundImage.gameEnd"));

        BG = new Image(getIMG_PATH());

        GAME_WON_TXT = getMsgProp().getProperty("gameEnd.won");
        GAME_LOST_TXT = getMsgProp().getProperty("gameEnd.lost");
        HIGHEST_SCORE_TXT = getMsgProp().getProperty("gameEnd.highestScores");

        STATUS_Y = Integer.parseInt(getGameProp().getProperty("gameEnd.status.y"));
        SCORES_Y = Integer.parseInt(getGameProp().getProperty("gameEnd.scores.y"));

        String fontFile = getGameProp().getProperty("font");
        STATUS_FONT = new Font(fontFile, Integer.parseInt(getGameProp().getProperty("gameEnd.status.fontSize")));
        SCORES_FONT = new Font(fontFile, Integer.parseInt(getGameProp().getProperty("gameEnd.scores.fontSize")));

        //get top 5 scores
        populateScores(getGameProp().getProperty("gameEnd.scoresFile"));
        TOP_SCORES = getTopScores();
    }

    /**
     * Sets win status to true or false
     *
     * @param isWon Boolean value indicating status of game end
     */
    public void setIsWon(boolean isWon) {
        this.isWon = isWon;
    }

    /**
     * Show whether the game is won or lost and the top 5 scores.
     *
     * @param input The current mouse/keyboard input.
     * @return true if SPACE key is pressed, false otherwise.
     */
    @Override
    public boolean update(Input input) {
        render();
        return input.wasPressed(Keys.SPACE);
    }

    /**
     * Text and background rendering logic of the end screen
     */
    @Override
    public void render() {
        BG.drawFromTopLeft(0, 0);
        if (isWon) {
            renderCentreText(STATUS_FONT, GAME_WON_TXT, getWinCentreX(), STATUS_Y, getTextWhite());
        } else {
            renderCentreText(STATUS_FONT, GAME_LOST_TXT, getWinCentreX(), STATUS_Y, getTextWhite());
        }

        renderCentreText(SCORES_FONT, HIGHEST_SCORE_TXT, getWinCentreX(), SCORES_Y, getTextWhite());

        int scoreIdx = 0;
        for (Score score : TOP_SCORES) {
            if (score != null) {
                String text = score.getPlayerName() + " - " + String.format("%.02f", score.getScore());
                double y = SCORES_Y + 40 * (scoreIdx + 1);
                renderCentreText(SCORES_FONT, text, getWinCentreX(), y, getTextWhite());
            }

            scoreIdx++;
        }
    }

    /**
     * Populate the scores from a file. The file used to store the scores of previous game plays.
     *
     * @param filename The name of the file to read the scores from.
     */
    private void populateScores(String filename) {
        ArrayList<String[]> data = IOUtils.readCommaSeparatedFile(filename);
        scores = new Score[data.size()];

        int scoreIdx = 0;
        for (String[] line : data) {
            String username = line[0];
            double score = Double.parseDouble(line[1]);
            scores[scoreIdx] = new Score(username, score);
            scoreIdx++;
        }
    }

    /**
     * Sort the score in descending order and return the top 5 scores.
     *
     * @return array of Score objects representing the top 5 scores.
     */
    public Score[] getTopScores() {
        Arrays.sort(scores, (a, b) -> Double.compare(b.getScore(), a.getScore()));
        return Arrays.copyOfRange(scores, 0, 5);
    }
}
