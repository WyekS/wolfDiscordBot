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
        final String alias = containerData.getFunctionData().getParams().get(1);
        filterMessage.filterMessages(alias);
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
        final String alias = containerData.getFunctionData().getParams().get(1);
        final String defaultUser = WConfig.getParameter("user.default");
        final String stopServer = WConfig.getParameter("path.stop_server");

        LOG.info("Executing command...");
        boolean result;
        if ("all".equals(alias))
        {
            result = WSystemUtils.executeCommand(stopServer, defaultUser, "delta")
                    && WSystemUtils.executeCommand(stopServer, defaultUser, "tango")
                    && WSystemUtils.executeCommand(stopServer, defaultUser, "nono");
        }
        else
        {
            result = WSystemUtils.executeCommand(stopServer, defaultUser, alias);
        }
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
