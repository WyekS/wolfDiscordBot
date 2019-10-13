package es.wolfteam.exceptions;

/**
 * The type Action not found exception.
 */
public class ActionFailedException extends FilterException
{
    /**
     * Instantiates a new Action not found exception.
     *
     * @param message the message to show
     */
    public ActionFailedException(String message)
    {
        super(message);
    }

    /**
     * Instantiates a new Action not found exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public ActionFailedException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
