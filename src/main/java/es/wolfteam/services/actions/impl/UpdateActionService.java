package es.wolfteam.services.actions.impl;

import es.wolfteam.Bot;
import es.wolfteam.data.ContainerData;
import es.wolfteam.exceptions.ActionFilterException;
import es.wolfteam.filters.impl.UpdateFilterMessage;
import es.wolfteam.services.actions.ActionService;
import es.wolfteam.utils.WBuilderUtils;
import es.wolfteam.utils.WSystemUtils;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.AttachmentOption;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.util.Date;

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


                final String mode = convertToScriptParam(containerData);
                if (mode == null)
                {
                    return;
                }

                boolean res = WSystemUtils.executeCommand("/opt/update_arma/execute.sh", mode);
                // Send result message
                if (res)
                {
                    event.getTextChannel().sendMessage(WBuilderUtils.buildUpdateFinishMessage()).queue();
                    try
                    {
                        event.getChannel().sendFile(
                                new File("/opt/update_arma/log_update.txt"),
                                "update_arma" + new Date() + ".log", AttachmentOption.SPOILER).queue();
                    }
                    catch (final IllegalArgumentException iae)
                    {
                        event.getTextChannel().sendMessage(WBuilderUtils.buildUpdateErrorMessage()).queue();
                    }
                }
                else
                {
                    event.getTextChannel().sendMessage(WBuilderUtils.buildUpdateErrorMessage()).queue();
                }
            }
            catch (InterruptedException e)
            {
                LOG.error("Serious execution error");
            }
        }
        event.getJDA().getPresence().setActivity(Activity.playing(""));

    }

    @Nullable
    private String convertToScriptParam(ContainerData containerData)
    {
        String mode = containerData.getFunctionData().getParams().get(1);
        // TODO: refactorizar esto
        if (mode.startsWith("all"))
        {
            mode = "1";
        }
        else if (mode.startsWith("servers"))
        {
            mode = "2";
        }
        else if (mode.startsWith("mods"))
        {
            mode = "3";
        }
        else
        {
            return null;
        }
        return mode;
    }
}
