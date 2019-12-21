package es.wolfteam.filters.impl;

import es.wolfteam.exceptions.ActionFilterException;
import es.wolfteam.filters.FilterMessage;

import java.util.regex.Pattern;

public class StartFilterMessage implements FilterMessage
{
    private static final String REGEX_ALIAS = "(delta|nono|tango)";

    public StartFilterMessage()
    {
        // empty
    }

    @Override
    public Boolean filterMessages(final String... messages) throws ActionFilterException
    {
        Pattern pattern = Pattern.compile(REGEX_ALIAS);
        if (!pattern.matcher(messages[0]).matches())
        {
            throw new ActionFilterException("Malformed parameter to Slot action: " + messages[0]);
        }

        return true;
    }
}
