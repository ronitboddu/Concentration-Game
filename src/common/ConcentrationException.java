package common;

/**
 * A class to represent abnormal situations that can arise from playing the
 * network game.
 *
 * @author RIT CS
 */
public class ConcentrationException extends Exception {
    /**
     * Construct with a helpful message.
     *
     * @param msg exception message
     */
    public ConcentrationException(String msg) {
        super(msg);
    }

    /**
     * Construct with a Throwable
     * @param cause the Throwable cause
     */
    public ConcentrationException(Throwable cause) {
        super(cause);
    }
}
