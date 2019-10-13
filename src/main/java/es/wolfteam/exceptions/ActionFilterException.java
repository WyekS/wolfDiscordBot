package es.wolfteam.exceptions;

/**
 * The type Action not found exception.
 */
public class ActionFilterException extends ActionFailedException
{
    /**
     * Instantiates a new Action not found exception.
     *
     * @param message the message to show
     */
    public ActionFilterException(String message)
    {
        super(message);
    }

    /**
     * Instantiates a new Action not found exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public ActionFilterException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
