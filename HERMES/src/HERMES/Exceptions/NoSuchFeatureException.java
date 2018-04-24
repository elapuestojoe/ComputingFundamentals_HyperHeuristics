package HERMES.Exceptions;

/**
 * Defines an exception for handling events where a feature of a problem is not defined.
 * <p>
 * @author Jose Carlos Ortiz Bayliss (jcobayliss@gmail.com)
 * @version 1.0
 */
public final class NoSuchFeatureException extends Exception {

    /**
     * Creates a new instance of <code>NoSuchFeatureException</code>.
     * <p>
     * @param message The message to describe the exception.
     */
    public NoSuchFeatureException(String message) {
        super(message);
    }

}
