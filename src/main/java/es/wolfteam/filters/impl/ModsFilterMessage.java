package es.wolfteam.filters.impl;

import es.wolfteam.Constants;
import es.wolfteam.exceptions.ActionFilterException;
import es.wolfteam.filters.FilterMessage;

import java.util.regex.Pattern;

public class ModsFilterMessage implements FilterMessage<Boolean>
{
    private static final String REGEX_FUNC = "(list)?";

    public ModsFilterMessage()
    {
        // empty
    }

    @Override
    public Boolean filterMessages(final String... messages) throws ActionFilterException
    {
        Pattern pattern = Pattern.compile(Constants.REGEX_ALIAS);
        if (!pattern.matcher(messages[0]).matches())
        {
            throw new ActionFilterException("Malformed parameter to Mods ALIAS!: " + messages[0]);
        }

        pattern = Pattern.compile(REGEX_FUNC);
        if (!pattern.matcher(messages[1]).matches())
        {
            throw new ActionFilterException("Malformed parameter to Mods FUNCTION!: " + messages[1]);
        }

        return true;
    }
}
