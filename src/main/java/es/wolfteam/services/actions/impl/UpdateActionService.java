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
        event.getTextChannel().sendMessage(WBuilderUtils.buildUpdateInProcessMessage()).queue();

        if (Bot.botMocked)
        {
            updateMocked(event);
        }
        else
        {
            update(event, containerData);
        }

        try
        {
            Thread.sleep(10000);
            event.getJDA().getPresence().setActivity(Activity.playing(""));
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private void update(MessageReceivedEvent event, ContainerData containerData)
    {
        final int mode = convertToScriptParam(containerData);
        if (mode == 0)
        {
            return;
        }
        boolean res = WSystemUtils.executeCommand("/opt/update_arma/execute.sh", String.valueOf(mode));
        // Send result message
        if (res)
        {
            event.getTextChannel().sendMessage(WBuilderUtils.buildUpdateFinishMessage()).queue();
            event.getChannel().sendFile(
                    new File("/opt/update_arma/update.log"),
                    "update_" + mode + "_" + new Date() + ".log").queue();
        }
        else
        {
            event.getTextChannel().sendMessage(WBuilderUtils.buildUpdateErrorMessage()).queue();
        }
    }

    private void updateMocked(MessageReceivedEvent event)
    {
        try
        {
            // Emulate an update process from core/mods Arma
            Thread.sleep(5000);
            event.getTextChannel().sendMessage(WBuilderUtils.buildUpdateFinishMessage()).queue();
            event.getChannel().sendFile(
                    new File("/home/wyeks/Proyectos/arma/wolfDiscordBot/src/main/resources/messages/ink"), "LogUpdate.txt", AttachmentOption.SPOILER)
                    .queue();

        }
        catch (final InterruptedException | NullPointerException ex)
        {
            LOG.error("Serious execution error");
            event.getTextChannel().sendMessage(WBuilderUtils.buildUpdateErrorMessage()).queue();
        }
    }

    private int convertToScriptParam(ContainerData containerData)
    {
        String mode = containerData.getFunctionData().getParams().get(1);
        // TODO: refactorizar esto
        switch (mode)
        {
            case "all":
                return 1;
            case "servers":
                return 2;
            case "mods":
                return 3;
            default:
                return 0;
        }
    }
}
