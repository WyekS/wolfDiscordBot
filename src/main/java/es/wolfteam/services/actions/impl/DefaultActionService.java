package es.wolfteam.services.actions.impl;

import es.wolfteam.data.ContainerData;
import es.wolfteam.services.actions.ActionService;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * The type Default action service.
 */
public class DefaultActionService implements ActionService
{
    @Override
    public TextChannel buildTargetMessage(final MessageReceivedEvent event, final ContainerData containerData)
    {
        // todo not implement yet
        return null;
    }

    @Override
    public void instanciateFilters()
    {
        // empty, no necessary for this Action
    }

    @Override
    public void runAction(final MessageReceivedEvent event, final ContainerData containerData)
    {
        // empty, no necessary for this Action
    }
}
