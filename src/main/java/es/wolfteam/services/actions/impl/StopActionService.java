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

/**
 * The type Stop action service.
 */
public class StopActionService implements ActionService
{
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
        event.getJDA().getPresence().setActivity(Activity.playing(""));

        final String alias = containerData.getFunctionData().getParams().get(1);
        final String defaultUser = WConfig.getParameter("user.default");
        final String stopServer = WConfig.getParameter("path.start_server");

        final boolean result = WSystemUtils.executeCommand(stopServer, defaultUser)
                && WSystemUtils.executeCommand(stopServer, defaultUser, alias);

        if (result)
        {
            event.getTextChannel().sendMessage(WBuilderUtils.buildStopSucessMessage(alias)).queue();
            return;
        }
        event.getTextChannel().sendMessage(WBuilderUtils.buildStopErrorMessage(alias)).queue();
    }
}
