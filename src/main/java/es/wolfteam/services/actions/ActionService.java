package es.wolfteam.services.actions;

import es.wolfteam.data.ContainerData;
import es.wolfteam.data.types.ActionType;
import es.wolfteam.exceptions.ActionFilterException;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * The interface Action service.
 * <p/>
 * > Add a new implementation of ActionService when you need a new command for Wolf Bot and override the method \
 * {@link this.buildTargetMessage} with the new built message.
 * <p/>
 * > NOTE: you should to declare the new command in the list {@link ActionType}
 *
 * @author <a href="mailto:wyeks@live.com">wyeks</a>
 */
public interface ActionService
{
    /**
     * Build here the custom message for the <code>Action</code>.
     * Important: Return at most one channel where send message.
     *
     * @param event         the event
     * @param containerData the container data
     * @return the text channel
     */
    TextChannel buildTargetMessage(MessageReceivedEvent event, ContainerData containerData) throws ActionFilterException;

    /**
     * Generate filters.
     */
    void instanciateFilters();

    /**
     * Run action.
     *
     * @param containerData the container data
     */
    void runAction(MessageReceivedEvent event, ContainerData containerData);
}
