package es.wolfteam.services.actions.impl;

import es.wolfteam.core.WConfig;
import es.wolfteam.data.ContainerData;
import es.wolfteam.exceptions.ActionFilterException;
import es.wolfteam.filters.impl.StopFilterMessage;
import es.wolfteam.services.actions.ActionService;
import es.wolfteam.utils.WBuilderUtils;
import es.wolfteam.utils.WSystemUtils;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;

/**
 * The type Stop action service.
 */
public class StopActionService implements ActionService
{
    private static final Logger LOG = JDALogger.getLog(StopActionService.class);
    private StopFilterMessage filterMessage;

    @Override
    public TextChannel buildTargetMessage(final MessageReceivedEvent event, final ContainerData containerData) throws ActionFilterException
    {
        // implementation is not necessary
        filterMessage.filterMessages("");
        return event.getTextChannel();
    }

    @Override
    public void instanciateFilters()
    {
        filterMessage = new StopFilterMessage();
    }

    @Override
    public void runAction(final MessageReceivedEvent event, final ContainerData containerData)
    {

        final String defaultUser = WConfig.getParameter("user.default");
        LOG.info("stopserver");
        final String stopServer = WConfig.getParameter("path.stop_server");

        LOG.info("Executing command...");
        final boolean result = WSystemUtils.executeCommand(stopServer, defaultUser);
        LOG.info("Result " + result);

        if (result)
        {
            event.getJDA().getPresence().setActivity(Activity.playing("Waiting"));
            event.getTextChannel().sendMessage(WBuilderUtils.buildStopSucessMessage()).queue();
            return;
        }
        event.getTextChannel().sendMessage(WBuilderUtils.buildStopErrorMessage()).queue();
    }
}
