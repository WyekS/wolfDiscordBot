package es.wolfteam.filters.impl;

import es.wolfteam.exceptions.ActionFilterException;
import es.wolfteam.filters.FilterMessage;

import java.util.regex.Pattern;

public class UpdateFilterMessage implements FilterMessage
{
    private static final String REGEX_MODE = "(all|servers|mods)?";

    public UpdateFilterMessage()
    {
        // empty
    }

    @Override
    public Boolean filterMessages(final String... messages) throws ActionFilterException
    {
        Pattern pattern = Pattern.compile(REGEX_MODE);
        if (!pattern.matcher(messages[0]).matches())
        {
            throw new ActionFilterException("Malformed parameter to Update action: " + messages[0]);
        }

        return true;
    }
}
