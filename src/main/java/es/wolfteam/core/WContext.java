package es.wolfteam.core;

import es.wolfteam.Bot;
import es.wolfteam.data.ContainerData;
import es.wolfteam.data.types.ActionType;
import es.wolfteam.exceptions.ActionFailedException;
import es.wolfteam.exceptions.ActionFilterException;
import es.wolfteam.exceptions.ActionNotFoundException;
import es.wolfteam.exceptions.NoGrantPermissionException;
import es.wolfteam.services.actions.ActionService;
import es.wolfteam.services.permissions.PermissionService;
import es.wolfteam.services.permissions.impl.DefaultPermissionService;
import es.wolfteam.services.permissions.impl.MockedPermissionService;
import es.wolfteam.utils.WBuilderUtils;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * The type Wolf context action.
 */
public class WContext
{
    private static final Logger LOG = JDALogger.getLog(WContext.class);
    private final Set<ActionService> actions = new HashSet<>();
    private PermissionService permissionService;

    /**
     * Instantiates a new context action in the application.
     */
    public WContext()
    {
        initializeActions();
        initializePermissions();
    }

    public TextChannel executeAction(final MessageReceivedEvent event, final ContainerData container) throws ActionFailedException
    {
        final ActionType action = container.getFunctionData().getAction();

        if (!permissionService.hasPermissionForAction(event.getMember(), action))
        {
            container.setResultMessage(WBuilderUtils.buildErrorPermissionMessage());
            throw new NoGrantPermissionException("This user have not have permission to execute this action");
        }

        ActionService currentAction;
        try
        {
            currentAction = getActionService(action);
            LOG.info("Action to execute " + action.getName());
            final TextChannel resultChannel = currentAction.buildTargetMessage(event, container);
            currentAction.runAction(event, container);
            return resultChannel;
        }
        catch (final ActionNotFoundException | ActionFilterException exception)
        {
            container.setResultMessage(WBuilderUtils.buildErrorCommandNotFoundMessage());
            throw new ActionFailedException("Action failed to catch service");
        }
    }

    /**
     * Returns the Action Service that matches the parameterized type.
     *
     * @param type the type Class to match
     * @return {@link ActionService} implementation
     * @throws ActionNotFoundException when the Action is not found in the list Actions loaded
     */
    private ActionService getActionService(final ActionType type) throws ActionNotFoundException
    {
        return actions.stream().filter(action -> type.getActionService().isInstance(action))
                .findFirst().orElseThrow(() -> new ActionNotFoundException("No actions have been found in the initial list loaded"));
    }

    private void initializeActions()
    {
        try
        {
            for (final ActionType value : ActionType.values())
            {
                final Class<?> clazz = value.getActionService();
                final ActionService newInstance = (ActionService) clazz.newInstance();
                newInstance.instanciateFilters();
                if (!isContainsAction(newInstance))
                {
                    actions.add(newInstance);
                }
            }
        }
        catch (final IllegalAccessException | InstantiationException exception)
        {
            LOG.error("An error ocurred when an action was adding to list actions");
        }
    }

    /**
     * Implemented here new PermissionsService if it's necessary to improve the hierarchy
     */
    private void initializePermissions()
    {
        if (Bot.botMocked)
        {
            permissionService = new MockedPermissionService();
        } else
        {
            permissionService = new DefaultPermissionService();
        }
    }

    private boolean isContainsAction(final ActionService actionService)
    {
        return actions.stream().anyMatch(action -> action.getClass().isInstance(actionService));
    }

    public PermissionService getPermissionService()
    {
        return permissionService;
    }
}
