package es.wolfteam.filters.impl;

import es.wolfteam.exceptions.ActionFilterException;
import es.wolfteam.filters.FilterMessage;

import java.util.regex.Pattern;

public class SlotFilterMessage implements FilterMessage
{
    private static final String REGEX_PLACES = "([0-9])?";
    private static final String REGEX_SPEC = "(ametrallador|ataa|dmr|falcon|francotirador|granadero|ingeniero|medico|martillo|radio|triton|valquiria)?";

    public SlotFilterMessage()
    {
        // empty
    }

    @Override
    public Boolean filterMessages(final String... messages) throws ActionFilterException
    {
        Pattern pattern = Pattern.compile(REGEX_PLACES);
        if (!pattern.matcher(messages[0]).matches())
        {
            throw new ActionFilterException("Malformed parameter to Slot action: " + messages[0]);
        }

        pattern = Pattern.compile(REGEX_SPEC);
        if (!pattern.matcher(messages[1]).matches())
        {
            throw new ActionFilterException("Malformed parameter to Slot action: " + messages[0]);
        }

        return true;
    }
}
