package server.game;

/**
 * Represents a single card in the concentration game.  A card knows its
 * position in the board, its letter, and whether it is hidden or not.
 *
 * @author RIT CS
 */
public class ConcentrationCard {
    /** when hidden the card should be displayed as a dot */
    public final static char HIDDEN = '.';

    /** the row on the board */
    private int row;
    /** the column on the board */
    private int col;
    /** is the card hidden or revealed? */
    private boolean hidden;
    /** the letter for this card */
    private char letter;

    /**
     * Create a new card that is initially hidden.
     *
     * @param row the row
     * @param col the column
     * @param letter the letter
     */
    public ConcentrationCard(int row, int col, char letter) {
        this.row = row;
        this.col = col;
        this.letter = letter;
        this.hidden = false;
    }

    /**
     * Is the card hidden?
     *
     * @return true or false
     */
    public boolean isHidden() {
        return this.hidden;
    }

    /** Revealing the card makes it not hidden. */
    public void reveal() {
        this.hidden = false;
    }

    /** Hide the card. */
    public void hide() {
        this.hidden = true;
    }

    /**
     * Get the card's row.
     *
     * @return the row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Get the card's column.
     *
     * @return the column
     */
    public int getCol() {
        return this.col;
    }

    /**
     * Get the letter of the card when not hidden.
     *
     * @return the letter
     */
    public char getLetter() {
        return this.letter;
    }

    /**
     * Return a string representation of the card.  This should be used for
     * debugging purposes only as it shows a hidden card's letter.
     *
     * @return a string message, e.g. "(0, 1)=A, hidden=true"
     */
    @Override
    public String toString() {
        return "(" + this.row + ", " + this.col + ")=" + this.letter + ", hidden=" + this.hidden;
    }

    /**
     * Two cards are equal if they have the same letter.
     *
     * @param other the other card
     * @return whether they are equal or not
     */
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof ConcentrationCard) {
            ConcentrationCard c = (ConcentrationCard)other;
            result = this.letter == c.letter;
        }
        return result;
    }
}
