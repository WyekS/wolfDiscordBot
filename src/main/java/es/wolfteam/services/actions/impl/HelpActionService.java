package es.wolfteam.services.actions.impl;

import es.wolfteam.data.ContainerData;
import es.wolfteam.services.actions.ActionService;
import es.wolfteam.utils.WBuilderUtils;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

/**
 * The type Help action service.
 */
public class HelpActionService implements ActionService
{
    @Override
    public TextChannel buildTargetMessage(final MessageReceivedEvent event, final ContainerData containerData)
    {
        final String helpMessage = WBuilderUtils.buildHelpingsMessage();
        final MessageEmbed finalMessage =
                WBuilderUtils.createMessage("Ayuda WolfMasterBot", helpMessage, Color.BLUE, null, null, true);
        containerData.setResultMessage(finalMessage);
        return event.getTextChannel();
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
