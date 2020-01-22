package es.wolfteam.core;

import es.wolfteam.data.ContainerData;
import es.wolfteam.data.FunctionData;
import es.wolfteam.exceptions.ActionFailedException;
import es.wolfteam.exceptions.FilterException;
import es.wolfteam.filters.FilterMessage;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The type Wolf Master.
 * It contains the main flow bot
 */
public class WMaster extends ListenerAdapter
{
    private static final Logger LOG = JDALogger.getLog(WMaster.class);
    private FilterMessage filter;
    private WContext contextAction;

    /**
     * Instantiates a new W master.
     *
     * @param filter        the filter
     * @param contextAction the context action
     */
    public WMaster(final FilterMessage filter, final WContext contextAction)
    {
        this.filter = filter;
        this.contextAction = contextAction;
    }

    public void onMessageReceived(final MessageReceivedEvent event)
    {
        try
        {
            final Message messageEvent = event.getMessage();
            LOG.info(messageEvent.getContentRaw());
            final ContainerData containerData = new ContainerData();
            containerData.setSourceMessage(messageEvent);
            containerData.setFunctionData((FunctionData) filter.filterMessages(messageEvent.getContentRaw()));
            containerData.setAuthorId(event.getAuthor().getIdLong());
            containerData.setChannelId(event.getChannel().getIdLong());
            executeProcess(event, containerData);
        }
        catch (final FilterException fe)
        {
            LOG.info("This message does not meet the filter requirements!!. Cause: " + fe.getMessage());
        }
    }

    private void executeProcess(final MessageReceivedEvent event, final ContainerData container)
    {
        // probar con completableFuture
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() ->
        {
            processMessage(event, container);
            LOG.info("End thread " + Thread.currentThread().getName());
        });
        executor.shutdown();
    }

    private void processMessage(final MessageReceivedEvent event, final ContainerData container)
    {
        try
        {
            LOG.info("Filter passed. Generating an action...");
            final TextChannel senderChannel = contextAction.executeAction(event, container);
            senderChannel.sendMessage(container.getResultMessage()).queue();
        }
        catch (final ActionFailedException e)
        {
            LOG.error("Something didn't work properly to process action");
            LOG.error("Returns this message: " + container.getResultMessage().toData().toString());
            event.getChannel().sendMessage(container.getResultMessage()).queue();
        }
    }

    /**
     * Gets filter.
     *
     * @return An filter assigned to this entity
     */
    public FilterMessage getFilter()
    {
        return this.filter;
    }

    /**
     * Gets context action.
     *
     * @return the context action
     */
    public WContext getContextAction()
    {
        return contextAction;
    }

}
