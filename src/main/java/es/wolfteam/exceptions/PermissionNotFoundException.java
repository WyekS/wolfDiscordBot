package es.wolfteam.exceptions;

/**
 * The type Action not found exception.
 */
public class PermissionNotFoundException extends RuntimeException
{
    /**
     * Instantiates a new Action not found exception.
     *
     * @param message the message to show
     */
    public PermissionNotFoundException(String message)
    {
        super(message);
    }

    /**
     * Instantiates a new Action not found exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public PermissionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
