package client.model;

/**
 * An interface representing any class whose objects get notified when
 * the objects they are observing update them.
 *
 * @param <Subject> the type of object an implementor of this interface
 *                is observing
 * @author Shivani Singh ,ss5243@rit.edu
 * @author Ronit Boddu, rb1209@g.rit.edu
 */
public interface Observer<Subject>{
    /**
     * The observed subject calls this method on each observer that has
     *      * previously registered with it.
     * @param subject
     * @param card
     */
    void update(Subject subject,ConcentrationModel.CardUpdate card);
}
