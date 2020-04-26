package es.wolfteam.filters.impl;

import es.wolfteam.Constants;
import es.wolfteam.exceptions.ActionFilterException;
import es.wolfteam.filters.FilterMessage;

import java.util.regex.Pattern;

public class StopFilterMessage implements FilterMessage
{
    public StopFilterMessage()
    {
        // empty
    }

    @Override
    public Boolean filterMessages(final String... messages) throws ActionFilterException
    {
        final Pattern pattern = Pattern.compile(Constants.REGEX_ALIAS_ALL);
        if (!pattern.matcher(messages[0]).matches())
        {
            throw new ActionFilterException("Malformed parameter to Slot action: " + messages[0]);
        }

        return true;
    }
}
