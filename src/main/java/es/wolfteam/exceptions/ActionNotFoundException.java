package es.wolfteam.exceptions;

/**
 * The type Action not found exception.
 */
public class ActionNotFoundException extends ActionFailedException
{
    /**
     * Instantiates a new Action not found exception.
     *
     * @param message the message to show
     */
    public ActionNotFoundException(String message)
    {
        super(message);
    }

    /**
     * Instantiates a new Action not found exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public ActionNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
