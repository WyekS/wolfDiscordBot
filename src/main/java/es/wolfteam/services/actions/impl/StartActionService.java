package es.wolfteam.services.actions.impl;

import es.wolfteam.core.WConfig;
import es.wolfteam.data.ContainerData;
import es.wolfteam.exceptions.ActionFilterException;
import es.wolfteam.filters.impl.StartFilterMessage;
import es.wolfteam.services.actions.ActionService;
import es.wolfteam.utils.WBuilderUtils;
import es.wolfteam.utils.WSystemUtils;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;

/**
 * The type Start action service.
 */
public class StartActionService implements ActionService
{
    private static final Logger LOG = JDALogger.getLog(StartActionService.class);

    private StartFilterMessage filterMessage;

    @Override
    public TextChannel buildTargetMessage(final MessageReceivedEvent event, final ContainerData containerData)
            throws ActionFilterException
    {
        final String alias = containerData.getFunctionData().getParams().get(1);
        filterMessage.filterMessages(alias);
        // Send message on specific channel
        return event.getTextChannel();
    }

    @Override
    public void instanciateFilters()
    {
        filterMessage = new StartFilterMessage();
    }

    @Override
    public void runAction(final MessageReceivedEvent event, final ContainerData containerData)
    {

        final String alias = containerData.getFunctionData().getParams().get(1);
        // TODO user string for severals server instances, Script on server is prepared for this.
        final String defaultUser = WConfig.getParameter("user.default");
        final String startServer = WConfig.getParameter("path.start_server");
        final String stopServer = WConfig.getParameter("path.stop_server");

        LOG.info("Executing command...");
        final boolean result = WSystemUtils.executeCommand(stopServer, defaultUser, alias)
                && WSystemUtils.executeCommand(startServer, defaultUser, alias);
        LOG.info("Result " + result);

        if (result)
        {
            event.getJDA().getPresence().setActivity(Activity.playing(alias + " on Arma 3"));
            event.getTextChannel().sendMessage(WBuilderUtils.buildStartSucessMessage(alias)).queue();
            return;
        }
        event.getTextChannel().sendMessage(WBuilderUtils.buildStartErrorMessage(alias)).queue();
    }
}
