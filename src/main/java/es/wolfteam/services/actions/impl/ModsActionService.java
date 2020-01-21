package es.wolfteam.services.actions.impl;

import es.wolfteam.data.ContainerData;
import es.wolfteam.exceptions.ActionFilterException;
import es.wolfteam.filters.impl.ModsFilterMessage;
import es.wolfteam.services.actions.ActionService;
import es.wolfteam.utils.WBuilderUtils;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

/**
 * The type Slot action service.
 */
public class ModsActionService implements ActionService
{
    private ModsFilterMessage filterMessage;

    @Override
    public TextChannel buildTargetMessage(final MessageReceivedEvent event, final ContainerData containerData) throws ActionFilterException
    {
        final String alias = containerData.getFunctionData().getParams().get(1);
        final String function = containerData.getFunctionData().getParams().get(2);
        filterMessage.filterMessages(alias, function);

        final String modsList = WBuilderUtils.buildModsMessage(alias);
        final MessageEmbed finalMessage =
                WBuilderUtils.createMessage("MODS | **Mapa " + alias + "**", modsList, Color.BLUE, null, null, false);
        containerData.setResultMessage(finalMessage);
        return event.getTextChannel();
    }

    @Override
    public void instanciateFilters()
    {
        filterMessage = new ModsFilterMessage();
    }

    @Override
    public void runAction(final MessageReceivedEvent event, final ContainerData containerData)
    {
        // empty action
    }
}
