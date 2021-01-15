package es.wolfteam.exceptions;

/**
 * The type Filter exception.
 */
public class FilterException extends Exception
{
    /**
     * Instantiates a new Filter exception.
     *
     * @param message the message to show
     */
    public FilterException(String message)
    {
        super(message);
    }

    /**
     * Instantiates a new Filter exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public FilterException(String message, Throwable cause) {
        super(message, cause);
    }
}
