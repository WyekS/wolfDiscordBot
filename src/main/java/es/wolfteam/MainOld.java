package es.wolfteam;

import es.wolfteam.data.types.UserType;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static es.wolfteam.Constants.Ids.CHANNEL_BOT_SERVER;
import static es.wolfteam.Constants.Ids.CHANNEL_CALL_EVENTS;
import static es.wolfteam.Constants.REGEX;
import static es.wolfteam.Constants.Request.*;

/**
 * The type Main.
 *
 * @author <a href="mailto:wyeks@live.com">wyeks</a> Discord Bot Utils for Wolf Team channel administration
 */
public class MainOld extends ListenerAdapter
{
    private static final Logger LOG = LoggerFactory.getLogger(MainOld.class);

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws LoginException the login exception
     */
    public static void main(String[] args) throws LoginException
    {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        ///LOGIN DEL BOT
        builder.setToken(TOKEN);
        builder.addEventListener(new MainOld());
        builder.buildAsync();
        //END LOGIN BOT
    }

    public void onMessageReceived(final MessageReceivedEvent event)
    {


        if (!event.getTextChannel().getId().equals(CHANNEL_BOT_SERVER))
        {
            return;
        }

        LOG.info("We received a message from " +
                event.getAuthor().getName() + ": " +
                event.getMessage().getContentDisplay()
        );

        final String request = event.getMessage().getContentRaw();

        final Pattern p = Pattern.compile(REGEX);
        final Matcher matcher = p.matcher(request);
        if (!matcher.matches())
        {
            LOG.info("Not matches pattern!");
            return;
        }

        String user;
        final String command = matcher.group(2);

        String message = "", alias;
        Boolean result = null;
        try
        {
            switch (command)
            {
                case Constants.Commands.HELP:
                    LOG.info("HELP Executed");
                    message = buildHelpingsMessage();
                    result = true;
                    break;

                case Constants.Commands.START:
                    LOG.info("START Executed");

                    alias = getSecondArgumentFromRequest(matcher);
                    user = getUserFromAlias(alias);
                    result = executeCommand(Constants.Paths.ROOT.concat(Constants.Paths.STOP), user);
                    if (result)
                    {
                        result = executeCommand(Constants.Paths.ROOT.concat(Constants.Paths.START), user, alias);
                        if (result)
                        {
                            message = alias + " iniciándose...\n" + "Ha sido iniciado por " + event.getAuthor().getName();
                        }
                        else
                        {
                            message = " Error al iniciar " + alias;
                        }
                    }
                    else
                    {
                        message = "Error al parar el anterior servidor. No es posible continuar con el inicio de " + alias;
                    }

                    break;

                case Constants.Commands.STOP:
                    LOG.info("STOP Executed");

                    alias = getSecondArgumentFromRequest(matcher);
                    user = getUserFromAlias(alias);

                    result = executeCommand(Constants.Paths.ROOT.concat(Constants.Paths.STOP), user);

                    if (result)
                        message = alias + " parándose...\n" + "Ha sido parado por " + event.getAuthor().getName();
                    break;

                case Constants.Commands.RESTART:
                    LOG.info("RESTART Executed");

                    alias = getSecondArgumentFromRequest(matcher);
                    user = getUserFromAlias(alias);

                    result = StringUtils.isNotBlank(user)
                            && executeCommand(Constants.Paths.ROOT.concat(Constants.Paths.STOP), user)
                            && executeCommand(Constants.Paths.ROOT.concat(Constants.Paths.START), user, alias);

                    if (result)
                        message = alias + " reiniciándose...\n" + "Ha sido reiniciado por " + event.getAuthor().getName();
                    break;

               /* case Constants.Commands.NUCLEAR:
                    LOG.info("RESTART ALL Executed");
                    result = allServerExecution(Constants.Alias.ROOT.concat(Constants.Alias.STOP))
                            && allServerExecution(Constants.Alias.ROOT.concat(Constants.Alias.START));

                    if (result)
                        message = "El usuario " + event.getAuthor().getName() + " ha lanzado una bomba nuclear en Altis " +
                                ":bomb:+ \nSe ha producido un parado e inicio de todos los servidores de Wolf Team";
                    break;*/

                case Constants.Commands.STATUS:
                    LOG.info("Status");
                    requestStatusServerArma(event.getChannel());
                    break;

                case Constants.Commands.SLOT:
                    LOG.info("Slot");
                    // Another way to mention a message
                    // event.getTextChannel().sendMessage("Prueba con mention" + event.getGuild().getRoleById("564470299370848280")
                    //         .getAsMention()).complete();
                    final String places = matcher.group(4);
                    final String specialization = matcher.group(6);

                    // Send message on specific channel
                    final TextChannel textChannel = event.getGuild().getTextChannelById(CHANNEL_CALL_EVENTS);
                    textChannel.sendMessage(buildCardPlacesMessage(specialization, places)).queue();
                    break;

                case Constants.Commands.UPDATE:
                    LOG.info("UPDATE Executed");
                    // TODO: falta hacerlo ASYNC
                    // Send start message
                    event.getChannel().sendMessage("Va a comenzar la actualización, en breve será informado").queue();
                    String argument = getSecondArgumentFromRequest(matcher);

                    // TODO: refactorizar esto
                    if (argument.startsWith("all"))
                    {
                        argument = "1";
                    }
                    else if (argument.startsWith("servers"))
                    {
                        argument = "2";
                    }
                    else if (argument.startsWith("mods"))
                    {
                        argument = "3";
                    }
                    else
                    {
                        break;
                    }

                    boolean res = executeCommand("/opt/update_arma/execute.sh", argument);

                    if (res)
                    {
                        message = "Actualización finalizada con éxito: ";
                    }
                    else
                    {
                        message = "Ha habido errores en la actualización";
                    }

                    // Send result message
                    event.getChannel().sendMessage(message).queue();

                    final Message file_attachment = new MessageBuilder().append("LOG de la actualización").build();
                    try
                    {
                        event.getChannel().sendFile(new File("/opt/update_arma/log_update.txt"), file_attachment).queue();
                    }
                    catch (final NullPointerException npe)
                    {
                        LOG.error("Error to read file log from update process");
                        event.getChannel().sendMessage("No se ha generado ningún LOG sobre la actualización").queue();
                    }

                    break;

                default:
                    break;
            }
        }
        catch (final Exception e) // catch generic Exception because we want to show error for any unusual situation
        {
            result = false;
            LOG.error("Trace: " + " " + e.getMessage());
        }

        if (BooleanUtils.isTrue(result))
        {
            event.getChannel().sendMessage(":wolf~1: " + message).queue();
            LOG.info("Finishing request");
        }
        else if (null != result && !result)
        {
            LOG.error("An error ocurred in the request bot");
            event.getChannel().sendMessage("Ha ocurrido un error, lo sentimos :frowning:").queue();
        }
    }

    /**
     * Generate a message in the Channel param
     * <p/>
     *
     * @param messageChannel {@link MessageChannel}
     */
    private void requestStatusServerArma(final MessageChannel messageChannel)
    {
        for (final UserType userType : UserType.values())
        {
            int attempts;
            for (attempts = 0; attempts < 3; attempts++)
            {
                LOG.info("@@@@ Attemp: " + attempts + " for server " + userType.getName());
                try
                {
                    final String url = String.format(REQUEST_STATUS, OUR_IP, userType.getPort());
                    LOG.info(url);
                    requestAsyncArmaUnitStatus(attempts, url, messageChannel, userType.getName());
                    Thread.sleep(1000);
                    break;
                }
                catch (final JSONException je)
                {
                    LOG.error("Not found a json object in URL from server " + userType.getName() + ":" + userType.getPort() +
                            " ### Another request will be attempted (Attempts: " + attempts + "/5)");
                }
                catch (final Exception e)
                {
                    LOG.error("An error ocurred when get status from server " + userType.getName() + ":" + userType.getPort() +
                            " ### Another request will be attempted (Attempts: " + attempts + "/)");
                    LOG.error(e.getMessage());
                }
            }
        }
    }




    ////////


    private String getSecondArgumentFromRequest(final Matcher matcher)
    {
        return matcher.group(4);
    }

    private String getUserFromAlias(final String alias)
    {
        return UserType.valueOf(alias).getName();
    }
}
