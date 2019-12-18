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

/**
 * The type Start action service.
 */
public class StartActionService implements ActionService
{
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
        event.getJDA().getPresence().setActivity(Activity.playing("Arma 3"));

        final String alias = containerData.getFunctionData().getParams().get(1);
        // TODO user string for severals server instances, Script on server is prepared for this.
        final String defaultUser = WConfig.getParameter("user.default");
        final String startServer = WConfig.getParameter("path.stop_server");
        final String stopServer = WConfig.getParameter("path.start_server");

        final boolean result = WSystemUtils.executeCommand(stopServer, defaultUser)
                && WSystemUtils.executeCommand(startServer, defaultUser, alias);

        if (result)
        {
            event.getTextChannel().sendMessage(WBuilderUtils.buildStartSucessMessage(alias)).queue();
            return;
        }
        event.getTextChannel().sendMessage(WBuilderUtils.buildStartErrorMessage(alias)).queue();
    }
}
