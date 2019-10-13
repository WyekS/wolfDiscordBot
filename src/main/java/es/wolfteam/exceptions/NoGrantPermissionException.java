package es.wolfteam.exceptions;

/**
 * The type Filter exception.
 */
public class NoGrantPermissionException extends ActionFailedException
{
    /**
     * Instantiates a new Filter exception.
     *
     * @param message the message to show
     */
    public NoGrantPermissionException(String message)
    {
        super(message);
    }

    /**
     * Instantiates a new Filter exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public NoGrantPermissionException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
