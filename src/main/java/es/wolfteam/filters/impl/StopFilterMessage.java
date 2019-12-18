package es.wolfteam.filters.impl;

import es.wolfteam.exceptions.ActionFilterException;
import es.wolfteam.filters.FilterMessage;

public class StopFilterMessage implements FilterMessage
{
    public StopFilterMessage()
    {
        // empty
    }

    @Override
    public Boolean filterMessages(final String... messages) throws ActionFilterException
    {
        return true;
    }
}
