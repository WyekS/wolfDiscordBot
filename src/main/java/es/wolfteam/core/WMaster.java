package es.wolfteam.core;

import es.wolfteam.data.ContainerData;
import es.wolfteam.exceptions.FilterException;
import es.wolfteam.filters.FilterMessage;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WMaster extends ListenerAdapter
{
    private FilterMessage filter;

    public WMaster(final FilterMessage filter)
    {
        this.filter = filter;
    }

    public void onMessageReceived(final MessageReceivedEvent event)
    {
        // entry filter - throw exception - control
        // Future & ExecutorService ----
        try
        {
            final Message messageEvent = event.getMessage();
            final ContainerData containerData = new ContainerData();
            containerData.setSourceMessage(messageEvent);
            containerData.setFunctionData(filter.filterMessage(messageEvent.getContentRaw()));

            processMessage(event, containerData);
        }
        catch (final FilterException fe)
        {
            System.out.println(fe.getMessage()); // todo change to LOG
            // LOG.debug("This message does not passed!!"
        }
    }

    private void processMessage(final MessageReceivedEvent event, final ContainerData container)
    {
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() ->
        {
            //todo crear WAction en base al action, crear clases de los diferentes actions
            event.getChannel().sendMessage(container.getResultMessage()).submit();
            String threadName = Thread.currentThread().getName();
            System.out.println("Hello " + threadName);
        });
        executor.shutdown();
    }

    /**
     * @return An filter assigned to this entity
     */
    public FilterMessage getFilter()
    {
        return this.filter;
    }
}
