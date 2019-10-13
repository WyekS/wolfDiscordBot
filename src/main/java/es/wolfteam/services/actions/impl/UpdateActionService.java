package es.wolfteam.services.actions.impl;

import es.wolfteam.Bot;
import es.wolfteam.data.ContainerData;
import es.wolfteam.exceptions.ActionFilterException;
import es.wolfteam.filters.impl.UpdateFilterMessage;
import es.wolfteam.services.actions.ActionService;
import es.wolfteam.utils.WBuilderUtils;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.AttachmentOption;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;

import java.io.File;

/**
 * The type Start action service.
 */
public class UpdateActionService implements ActionService
{
    private static final Logger LOG = JDALogger.getLog(UpdateActionService.class);
    private UpdateFilterMessage filterMessage;

    @Override
    public TextChannel buildTargetMessage(final MessageReceivedEvent event, final ContainerData containerData) throws ActionFilterException
    {
        final String mode = containerData.getFunctionData().getParams().get(1);
        filterMessage.filterMessages(mode);
        event.getTextChannel().sendMessage(WBuilderUtils.buildUpdateInProcessMessage()).queue();
        // Send message on specific channel
        return event.getTextChannel();
    }

    @Override
    public void instanciateFilters()
    {
        filterMessage = new UpdateFilterMessage();
    }

    @Override
    public void runAction(final MessageReceivedEvent event, final ContainerData containerData)
    {
        event.getJDA().getPresence().setActivity(Activity.playing("Updating Arma 3"));
        if (Bot.botMocked)
        {
            try
            {
                Thread.sleep(5000);
                // todo llamar update -> mode = containerData.getFunctionData().getParams().get(1);
                event.getTextChannel().sendMessage(WBuilderUtils.buildUpdateFinishMessage()).queue();
                try
                {
                    event.getChannel().sendFile(
                            new File("/home/wyeks/Proyectos/arma/wolfDiscordBot/src/main/resources/messages/ink"), "LogUpdate.txt", AttachmentOption.SPOILER)
                            .queue();
                }
                catch (final NullPointerException npe)
                {
                    LOG.error("Error to read file log from update process");
                    event.getChannel().sendMessage("No se ha generado ningún LOG sobre la actualización").queue();
                }
            }
            catch (InterruptedException e)
            {
                LOG.error("Serious execution error");
            }
        }
        event.getJDA().getPresence().setActivity(Activity.playing(""));

    }
}
