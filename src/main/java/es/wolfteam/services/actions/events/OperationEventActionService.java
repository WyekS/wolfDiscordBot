package es.wolfteam.services.actions.events;

import es.wolfteam.core.WConfig;
import es.wolfteam.data.ContainerData;
import es.wolfteam.exceptions.ActionFilterException;
import es.wolfteam.services.actions.ActionService;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.HashMap;

import static es.wolfteam.utils.WBuilderUtils.createMessage;

/**
 * The type Slot action service.
 */
public class OperationEventActionService implements ActionService
{
    private static final String CHANNEL_MISSIONS_EVENTS = "channel.missions_events";
    private static final String CHANNEL_MISSIONS_EVENTS_MOCKED = "channel.missions_events_mocked";

    @Override
    public TextChannel buildTargetMessage(final MessageReceivedEvent event, final ContainerData containerData) throws ActionFilterException
    {
        final HashMap<String, String> fieldsValues = new HashMap<>();
        fieldsValues.put("Mínimo de jugadores", "10");
        fieldsValues.put("Duración", "~4h");
        fieldsValues.put("Fechas", "Votación vía Doodle: https://doodle.com/poll/q3auiph7b7pupm6p");
        fieldsValues.put("Descripción", "Un pelotón del 3er regimiento de marines desembarca en las blancas playas de Guadalcanal, " +
                "en las islas Salomón. Refuerzan la posición de los exhaustos marines defendiendo el aeródromo Henderson. Enfrentados " +
                "la temeraria infantería japonesa en lo más profundo de la jungla, deberán jugarse la vida como un equipo para evitar que la isla caiga.");


        final MessageEmbed finalMessage = createMessage("Operación" + "Watchtower", "Creada por Cbo.Mike <@97105829521526784> ",
                Color.magenta, fieldsValues, "https://cdn.discordapp.com/attachments/564454752705052683/635924825805946910/watchtower_pic.png",
                true);

        //Objects.requireNonNull(event.getGuild().getRoleById("564470299370848280")).getAsMention()

        final TextChannel operationChannel = event.getGuild().getTextChannelById(WConfig.getParameter(CHANNEL_MISSIONS_EVENTS));
        if (null != operationChannel)
        {
            //operationChannel.sendMessage(finalMessage).queue(message -> message.addReaction("minn:572501636170121227").queue());
            operationChannel.sendMessage(finalMessage).queue(message ->
            {
                message.addReaction("U+2705").queue();
                message.addReaction("U+274C").queue();
            });
        }
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
