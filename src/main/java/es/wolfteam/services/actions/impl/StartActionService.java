package es.wolfteam.services.actions.impl;

import es.wolfteam.data.ContainerData;
import es.wolfteam.services.actions.ActionService;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * The type Start action service.
 */
public class StartActionService implements ActionService
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

    }

    @Override
    public void runAction(final MessageReceivedEvent event, final ContainerData containerData)
    {

    }
}
