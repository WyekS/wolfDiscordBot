package es.wolfteam.services.actions.impl;

import es.wolfteam.data.ContainerData;
import es.wolfteam.exceptions.ActionFilterException;
import es.wolfteam.filters.impl.ModsFilterMessage;
import es.wolfteam.services.actions.ActionService;
import es.wolfteam.utils.WBuilderUtils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.annotation.Nonnull;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * The type Slot action service.
 */
public class SetModsActionService extends ListenerAdapter implements ActionService
{
    private ContainerData containerData;
    private ModsFilterMessage filterMessage;

    public SetModsActionService()
    {
        // mandatory empty constructor
    }

    public SetModsActionService(final ContainerData containerData)
    {
        this.containerData = containerData;
    }

    @Override
    public TextChannel buildTargetMessage(final MessageReceivedEvent event, final ContainerData containerData) throws ActionFilterException
    {
        final MessageChannel channel = event.getChannel();
        channel.sendMessage("Adjunta el fichero html").queue();
        event.getJDA().addEventListener(new SetModsActionService(containerData));
        return event.getTextChannel();
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event)
    {
        if (event.getAuthor().isBot()) return; // don't respond to other bots
        if (event.getChannel().getIdLong() != containerData.getChannelId()) return; // ignore other channels

        final MessageChannel channel = event.getChannel();
        final String content = event.getMessage().getContentRaw();
        // since only one state is present you don't need a switch but that would be the concept if you had more than 1 interaction point in this protocol
        if (content.equals("STOP!"))
        {
            channel.sendMessage("Ok!").queue();
            event.getJDA().removeEventListener(this); // stop listening
        }
        else if (event.getAuthor().getIdLong() == containerData.getAuthorId())
        {
            final Set<Message.Attachment> files = new HashSet<>(event.getMessage().getAttachments());
            if (CollectionUtils.isNotEmpty(files))
            {
                for (Message.Attachment file : files)
                {

                    try
                    {
                        final String result = parseHtml(file.retrieveInputStream().get());
                        event.getChannel().sendMessage(WBuilderUtils.createMessage("Mods del html", result, Color.GREEN, null, null, true)).queue();
                    }
                    catch (InterruptedException | ExecutionException e)
                    {
                        e.printStackTrace();
                    }
                }
                event.getJDA().removeEventListener(this); // stop listening

            }
        }
        else
        {
            channel.sendMessage("Wait your turn " + event.getMember().getEffectiveName() + "!").queue();
        }

        super.onMessageReceived(event);
    }

    @Override
    public void instanciateFilters()
    {
        filterMessage = new ModsFilterMessage();
    }

    @Override
    public void runAction(final MessageReceivedEvent event, final ContainerData containerData)
    {
        // empty action
    }

    private String parseHtml(final InputStream inputStream)
    {
        final StringBuilder build = new StringBuilder();

        try
        {
            build.append(IOUtils.readLines(inputStream, "UTF-8"));

            final Document html = Jsoup.parse(build.toString());
            final Elements modDivs = html.body().getElementsByTag("tr");

            for (final Element modDiv : modDivs)
            {
                final String name = modDiv.getElementsByAttribute("ts[data-type]").text();
                System.out.println(name);
                final String link = modDiv.getElementsByTag("a").attr("href");
                System.out.println(link.substring(link.indexOf("?id=") + 7));
                System.out.println("#");
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return build.toString();
    }
}
