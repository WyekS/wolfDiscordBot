package es.wolfteam.services.actions.impl;

import es.wolfteam.data.ContainerData;
import es.wolfteam.exceptions.ActionFilterException;
import es.wolfteam.filters.impl.SlotFilterMessage;
import es.wolfteam.services.actions.ActionService;
import es.wolfteam.utils.WBuilderUtils;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static es.wolfteam.Bot.botMocked;

/**
 * The type Slot action service.
 */
public class SlotActionService implements ActionService
{
    private static final String CHANNEL_CALL_EVENTS = "channel.call_events";
    private static final String CHANNEL_CALL_EVENTS_MOCKED = "channel.call_events_mocked";

    private SlotFilterMessage filterMessage;

    @Override
    public TextChannel buildTargetMessage(final MessageReceivedEvent event, final ContainerData containerData) throws ActionFilterException
    {
        // Another way to mention a message
        // event.getTextChannel().sendMessage("Prueba con mention" + event.getGuild().getRoleById("564470299370848280")
        //         .getAsMention()).complete();
        final String places = containerData.getFunctionData().getParams().get(1);
        final String specialization = containerData.getFunctionData().getParams().get(2);
        filterMessage.filterMessages(places, specialization);
        final MessageEmbed finalMessage = WBuilderUtils.buildCardPlacesMessage(specialization, places);
        containerData.setResultMessage(finalMessage);
        // Send message on specific channel
        if (botMocked)
        {
            return event.getGuild().getTextChannelById(CHANNEL_CALL_EVENTS_MOCKED);
        }
        return event.getGuild().getTextChannelById(CHANNEL_CALL_EVENTS);
    }

    @Override
    public void instanciateFilters()
    {
        filterMessage = new SlotFilterMessage();
    }

    @Override
    public void runAction(final MessageReceivedEvent event, final ContainerData containerData)
    {
        // empty action
    }
}
