package common;

/**
 * The Concentration game network protocol.  It is a string based protocol
 * the contains the messaged passed between the client and server in order
 * to play the game.
 *
 * @author RIT CS
 */
public interface ConcentrationProtocol {
    /*
     * SERVER -> CLIENT MESSAGE HEADERS
     */

    /** get the square dimensions of the board */
    String BOARD_DIM = "BOARD_DIM";
    /** an error has occurred during game play */
    String ERROR = "ERROR";
    /** details of the card that requested to be revealed by the client */
    String CARD = "CARD";
    /** indicates a match of the last two cards revealed was made */
    String MATCH = "MATCH";
    /** indicates a match of the lst two cards revealed was NOT made */
    String MISMATCH = "MISMATCH";
    /** the game is over */
    String GAME_OVER = "GAME_OVER";

    /*
     * SERVER -> CLIENT FULL MESSAGE FORMAT STRINGS
     */

    /** the full board dimension message, e.g. "BOARD_DIM 4" */
    String BOARD_DIM_MSG = BOARD_DIM + " %d";
    /** an error occurred, e.g. "ERROR Invalid coordinates" */
    String ERROR_MSG = ERROR + " %s";
    /** the card detail message, e.g. "CARD 0 1 A" */
    String CARD_MSG = CARD + " %d %d %s";

    /**
     * the card match message with the coordinates of the two cards:
     * e.g. "MATCH 0 1 3 2"
     */
    String MATCH_MSG = MATCH + " %d %d %d %d";

    /**
     * the card mismatch message with the coordinates of the two cards:
     * e.g. "MISMATCH 0 1 3 2"
     */
    String MISMATCH_MSG = MISMATCH + " %d %d %d %d";

    /** the game over message, e.g. "GAME_OVER" */
    String GAME_OVER_MSG = GAME_OVER;

    /*
     * SERVER -> CLIENT MESSAGE HEADERS
     */

    /** the message to have the server reveal a card */
    String REVEAL = "REVEAL";

    /** the reveal message with the card coordinate, e.g. "REVEAL 0 2" */
    String REVEAL_MSG = REVEAL + " %d %d";
}
