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
public class ModsActionService implements ActionService {
    private static final String CHANNEL_CALL_EVENTS = "channel.call_events";
    private static final String CHANNEL_CALL_EVENTS_MOCKED = "channel.call_events_mocked";

    private ModsFilterMessage filterMessage;

    @Override
    public TextChannel buildTargetMessage(final MessageReceivedEvent event, final ContainerData containerData) throws ActionFilterException {
        // Another way to mention a message
        // event.getTextChannel().sendMessage("Prueba con mention" + event.getGuild().getRoleById("564470299370848280")
        //         .getAsMention()).complete();
        final String alias = containerData.getFunctionData().getParams().get(1);
        final String function = containerData.getFunctionData().getParams().get(2);
        filterMessage.filterMessages(alias, function);

        final String modsList = WBuilderUtils.buildModsMessage();
        final MessageEmbed finalMessage =
                WBuilderUtils.createMessage("Lista de Mods cargadas en el mapa nono", modsList, Color.BLUE, null, null, true);
        containerData.setResultMessage(finalMessage);
        return event.getTextChannel();
    }

    @Override
    public void instanciateFilters() {
        filterMessage = new ModsFilterMessage();
    }

    @Override
    public void runAction(final MessageReceivedEvent event, final ContainerData containerData) {
        // empty action
    }
}
