package HERMES.Exceptions;

/**
 * Defines an exception for handling events where a heuristic for a problem is not defined.
 * <p>
 * @author Jose Carlos Ortiz Bayliss (jcobayliss@gmail.com)
 * @version 1.0
 */
public final class NoSuchHeuristicException extends Exception {

    /**
     * Creates a new instance of <code>NoSuchHeuristicException</code>.
     * <p>
     * @param message The message to describe the exception.
     */
    public NoSuchHeuristicException(String message) {
        super(message);
    }

}
