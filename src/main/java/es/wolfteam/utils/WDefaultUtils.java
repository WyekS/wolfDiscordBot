package es.wolfteam.utils;

import es.wolfteam.data.types.ActionType;

import java.util.*;

import static es.wolfteam.Constants.BASE;

/**
 * Wolf default utils for actions
 */
public class WDefaultUtils
{
    private static int BASE_POSITION = 0;

    /**
     * Gets default commands loaded like a datasource.
     * <p/>
     * > Add here the actions you want
     *
     * @return the default commands
     */
    public static Set<ActionType> getDefaultCommands()
    {
        return new HashSet<>(Arrays.asList(ActionType.values()));
    }

    /**
     * Split params list.
     * <p/>
     * Example: "/wolf update mods" -> {"update", "mods"} no "/wolf" is added in the list
     *
     * @param message the message
     * @return the list
     */
    public static List<String> splitParams(final String message)
    {
        final String[] splitting = message.split("\\s+");
        if (BASE.equals(splitting[BASE_POSITION]))
        {
            return new ArrayList<>(Arrays.asList(splitting).subList(1, splitting.length));
        }
        return new ArrayList<>();
    }

    /**
     * Gets base position.
     *
     * @return the base position
     */
    public static int getBasePosition()
    {
        return BASE_POSITION;
    }
}
