package es.wolfteam.filters.impl;

import es.wolfteam.data.FunctionData;
import es.wolfteam.data.types.ActionType;
import es.wolfteam.exceptions.FilterException;
import es.wolfteam.filters.FilterMessage;
import es.wolfteam.utils.WDefaultUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Set;

import static es.wolfteam.Constants.BASE;

/**
 * The type Default filter message.
 */
public class DefaultFilterMessage implements FilterMessage
{
    private Set<ActionType> actions;

    public DefaultFilterMessage(final Set<ActionType> actions)
    {
        this.actions = actions;
    }

    @Override
    public FunctionData filterMessages(final String ... messages) throws FilterException
    {
        final FunctionData functionData = new FunctionData(BASE, determineParams(messages[0]));
        final ActionType actionType = determineAction(functionData);
        functionData.setAction(actionType);
        return functionData;
    }

    /**
     * @param message message will be splitted
     * @return message parametrized
     */
    private List<String> determineParams(final String message)
    {
        return WDefaultUtils.splitParams(message);
    }

    /**
     * Determine the Action for the function, the action is the first param in list
     *
     * @param function {@link FunctionData} save the all information to action
     * @return ActionType filtered
     * @throws FilterException if the params is incorrect or not action is not found
     */
    private ActionType determineAction(final FunctionData function) throws FilterException
    {
        if (CollectionUtils.isEmpty(function.getParams()))
        {
            throw new FilterException("Params functions is null or is empty");
        }

        final int sizeParam = function.getParams().size();
        final String action = function.getParams().iterator().next();
        for (final ActionType actionType : actions)
        {
            if (actionType.getName().equals(action) && sizeParam <= actionType.getMaxParams())
            {
                return actionType;
            }
        }
        throw new FilterException("No action type had been found or max param not matches");
    }

    /**
     * Gets actions.
     *
     * @return the actions
     */
    public Set<ActionType> getActions()
    {
        return actions;
    }

    /**
     * Add actions to list
     *
     * @param actions {@link Set<ActionType>}  the actions to add
     */
    public void addActions(final Set<ActionType> actions)
    {
        this.actions.addAll(actions);
    }

    /**
     * Add an action to list
     *
     * @param action {@link ActionType} the action to add
     */
    public void addActions(final ActionType action)
    {
        this.actions.add(action);
    }
}
