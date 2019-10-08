package es.wolfteam.filters.impl;

import es.wolfteam.data.ActionDataList;
import es.wolfteam.data.FunctionData;
import es.wolfteam.data.types.ActionType;
import es.wolfteam.exceptions.FilterException;
import es.wolfteam.filters.FilterMessage;
import es.wolfteam.utils.WDefaultUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Filter;

import static es.wolfteam.Constants.BASE;

public class DefaultFilterMessage implements FilterMessage
{
    private ActionDataList actionDataList;

    public DefaultFilterMessage()
    {
        actionDataList = new ActionDataList(WDefaultUtils.getDefaultCommands());
    }

    @Override
    public FunctionData filterMessage(final String message) throws FilterException
    {
        final FunctionData functionData = new FunctionData(BASE, determineParams(message));

        final ActionType actionType = determineAction(functionData.getParams().iterator().next());
        if (actionType.getMaxParams() != functionData.getParams().size())
        {
            throw new FilterException(); // build message
        }
        functionData.setAction(determineAction(functionData.getParams().iterator().next()));
        // filter and get Action, fill functionData and return it
        return functionData;
    }

    // filter function
    private Set<String> determineParams(final String message)
    {
        return WDefaultUtils.splitParams(message);
    }

    private ActionType determineAction(final String action) throws FilterException
    {
        for (ActionType actionType : actionDataList.getCommands())
        {
            if (actionType.getName().equals(action))
            {
                return actionType;
            }
        }
        throw new FilterException();
    }
}
