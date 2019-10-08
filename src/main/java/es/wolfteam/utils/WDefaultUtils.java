package es.wolfteam.utils;

import es.wolfteam.data.types.ActionType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static es.wolfteam.Constants.BASE;

public class WDefaultUtils
{
    private static int BASE_POSITION = 0;

    public static Set<ActionType> getDefaultCommands()
    {
        final Set<ActionType> actions = new HashSet<>();
        actions.add(ActionType.HELP);
        actions.add(ActionType.START);
        actions.add(ActionType.STOP);
        actions.add(ActionType.RESTART);
        actions.add(ActionType.STATUS);
        actions.add(ActionType.UPDATE);
        actions.add(ActionType.NUCLEAR);
        actions.add(ActionType.SLOT);
        return actions;
    }

    public static Set<String> splitParams(final String message)
    {
        final String[] splitting = message.split("\\s+");
        if (BASE.equals(splitting[BASE_POSITION]))
        {
            return new HashSet<>();
        }
        return new HashSet<>(Arrays.asList(splitting).subList(1, splitting.length));
    }
}
