package es.wolfteam;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpHeaders;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static es.wolfteam.Constants.Files.HELP_MESSAGE_FILE;
import static es.wolfteam.Constants.Ids.CHANNEL_BOT_SERVER;
import static es.wolfteam.Constants.Ids.CHANNEL_CALL_EVENTS;
import static es.wolfteam.Constants.REGEX;
import static es.wolfteam.Constants.Request.*;
import static es.wolfteam.Constants.Urls.FORM_EVENT;

/**
 * The type Main.
 *
 * @author <a href="mailto:wyeks@live.com">wyeks</a> Discord Bot Utils for Wolf Team channel administration
 */
public class Main extends ListenerAdapter
{
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

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
        builder.addEventListener(new Main());
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
                    LOG.info("HELP finished");
                    result = true;
                    break;

                case Constants.Commands.START:
                    LOG.info("START Executed");

                    alias = getSecondArgumentFromRequest(matcher);
                    user = getUserFromAlias(alias);

                    result = executeCommand(Constants.Alias.ROOT.concat(Constants.Alias.START), user);
                    if (result)
                        message = alias + " iniciándose...\n" + "Ha sido iniciado por " + event.getAuthor().getName();
                    break;

                case Constants.Commands.STOP:
                    LOG.info("STOP Executed");

                    alias = getSecondArgumentFromRequest(matcher);
                    user = getUserFromAlias(alias);

                    result = executeCommand(Constants.Alias.ROOT.concat(Constants.Alias.STOP), user);

                    if (result)
                        message = alias + " parándose...\n" + "Ha sido parado por " + event.getAuthor().getName();
                    break;

                case Constants.Commands.RESTART:
                    LOG.info("RESTART Executed");

                    alias = getSecondArgumentFromRequest(matcher);
                    user = getUserFromAlias(alias);

                    result = StringUtils.isNotBlank(user)
                            && executeCommand(Constants.Alias.ROOT.concat(Constants.Alias.STOP), user)
                            && executeCommand(Constants.Alias.ROOT.concat(Constants.Alias.START), user);

                    if (result)
                        message = alias + " reiniciándose...\n" + "Ha sido reiniciado por " + event.getAuthor().getName();
                    break;

                case Constants.Commands.NUCLEAR:
                    LOG.info("RESTART ALL Executed");
                    result = allServerExecution(Constants.Alias.ROOT.concat(Constants.Alias.STOP))
                            && allServerExecution(Constants.Alias.ROOT.concat(Constants.Alias.START));

                    if (result)
                        message = "El usuario " + event.getAuthor().getName() + " ha lanzado una bomba nuclear en Altis " +
                                ":bomb:+ \nSe ha producido un parado e inicio de todos los servidores de Wolf Team";
                    break;

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
        for (final UserEnum userEnum : UserEnum.values())
        {
            int attempts;
            for (attempts = 0; attempts < 3; attempts++)
            {
                LOG.info("@@@@ Attemp: " + attempts + " for server " + userEnum.getName());
                try
                {
                    final String url = String.format(REQUEST_STATUS, OUR_IP, userEnum.getPort());
                    LOG.info(url);
                    requestAsyncArmaUnitStatus(attempts, url, messageChannel, userEnum.getName());
                    Thread.sleep(1000);
                    break;
                }
                catch (final JSONException je)
                {
                    LOG.error("Not found a json object in URL from server " + userEnum.getName() + ":" + userEnum.getPort() +
                            " ### Another request will be attempted (Attempts: " + attempts + "/5)");
                }
                catch (final Exception e)
                {
                    LOG.error("An error ocurred when get status from server " + userEnum.getName() + ":" + userEnum.getPort() +
                            " ### Another request will be attempted (Attempts: " + attempts + "/)");
                    LOG.error(e.getMessage());
                }
            }
        }
    }

    /**
     * Generate a request to Units Servers from Arma 3 <a href="https://units.arma3.com/">Arma 3 Units</a> to retrieve
     * the information servers. It is not a API from Arma 3, we only attack URL.
     * <p/>
     *
     * @param attempts       {@link int}
     * @param url            {@link String}
     * @param messageChannel {@link MessageChannel}
     * @param name           {@link String}
     */
    private void requestAsyncArmaUnitStatus(int attempts, final String url,
                                            final MessageChannel messageChannel, final String name)
    {
        final int finalAttempts = attempts + 1;
        Unirest.get(url)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.CONTENT_ENCODING, "gzip")
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .header(FRONT_END_HTTPS, "off")
                // .header("Postman-Token", "2e545e67-5873-417e-aada-2fcd08c5965a")
                .asJsonAsync(new Callback<JsonNode>()
                {
                    @Override
                    public void completed(HttpResponse<JsonNode> response)
                    {
                        final JSONObject jsonObject = response.getBody().getObject();
                        LOG.info("Def +" + response.getBody().toString());
                        messageChannel.sendMessage(buildStatusServerMessage(name,
                                String.valueOf(jsonObject.getBoolean("status")),
                                jsonObject.getString("name"),
                                String.valueOf(jsonObject.getInt("players")))).queue();
                    }

                    @Override
                    public void failed(UnirestException e)
                    {
                        LOG.error("=== Trying again! Attemps: " + finalAttempts);
                        if (finalAttempts < 2)
                        {
                            requestAsyncArmaUnitStatus(finalAttempts, url, messageChannel, name);
                        }
                    }

                    @Override
                    public void cancelled()
                    {
                        // Cancelled task
                        LOG.error("=== Cancelled Async task to getting url from Unit Arma");
                    }
                });
    }

    /**
     * Returns a message card to publish a specialization event
     * <p/>
     *
     * @param specialization name of specialization  {@link SpecializationEnum name attribute}
     * @param placesStr      Number places for this event {@link String}
     * @return {@link MessageEmbed} a builded card for chat bot
     */
    private MessageEmbed buildCardPlacesMessage(final String specialization, final String placesStr)
    {
        Validate.notNull(specialization, "Parameter specialization cannot be null");
        Validate.notNull(placesStr, "Parameter places cannot be null");

        final int places = Integer.parseInt(placesStr);
        final String title = "Nuevas vacantes para ".concat(SpecializationEnum.valueOf(specialization).getName());
        final StringBuilder description = new StringBuilder();
        description.append("<@&536857111368433675> Se necesita personal para la especialización de ")
                .append(SpecializationEnum.valueOf(specialization).getName())
                .append(". \n\nLos interesados deberán rellenar este formulario: \n")
                .append(FORM_EVENT)
                .append("\n\nEl instructor se pondrá en contacto con los seleccionados. *(Rango mínimo de Soldado)*");

        HashMap<String, String> fieldsValues = new HashMap<>();
        fieldsValues.put("Plazas disponibles", String.valueOf(places));

        return createMessage(title,
                description.toString(), Color.red, fieldsValues, SpecializationEnum.valueOf(specialization).getImage());
    }

    /**
     * Build a message status of all servers with values parameters info
     * <p/>
     *
     * @param server  {@link String}
     * @param status  {@link String}
     * @param name    {@link String}
     * @param players {@link String}
     * @return {@link MessageEmbed} a message status of all servers
     */
    private MessageEmbed buildStatusServerMessage(final String server, final String status,
                                                  final String name, final String players)
    {
        final boolean active = status.startsWith("true");
        final HashMap<String, String> fieldsValues = new HashMap<>();
        fieldsValues.put("Nombre", name);
        fieldsValues.put("Estado", active ? "Activo" : "Inactivo");
        fieldsValues.put("Jugadores activos", players);

        final Color color = active ? Color.green : Color.red;

        return createMessageStatus("Estado del servidor " + server, color, fieldsValues, null);
    }

    /**
     * Read and return file <a href="file:../resources/help_message.md">/resources/help_message.md</a>
     * <p/>
     *
     * @return {@link String} a help message to discord
     */
    private static String buildHelpingsMessage()
    {
        String result = "";
        ClassLoader classLoader = Main.class.getClassLoader();
        try
        {
            result = IOUtils.toString(
                    Objects.requireNonNull(classLoader.getResourceAsStream(HELP_MESSAGE_FILE)), StandardCharsets.UTF_8);
        }
        catch (final IOException ioe)
        {
            LOG.error("Error I/O when the file help_message.md was being read");
        }

        return result;
    }

    /**
     * Execute a command in all user from UserEnum
     * <p/>
     *
     * @param command {@link String} command to execute
     * @return <code>true<code/> if all commands were executed correctly
     */
    private boolean allServerExecution(final String command)
    {
        boolean result = true;
        for (UserEnum userEnum : UserEnum.values())
        {
            if (!executeCommand(command, userEnum.getName()))
            {
                result = false;
            }
        }
        return result;
    }

    /**
     * A lower level method than before <strong>allServerExecution</strong
     * Execute a command in the system. Control I/O exception
     * <p/>
     *
     * @param arguments to generate command(0) + arguments(1-X) ProcessBuilder
     * @return <code>true<code/> if all commands were executed correctly
     */
    private boolean executeCommand(final String... arguments)
    {
        Process process;
        try
        {
            LOG.info("Command to execute: " + Arrays.toString(arguments) + "; Argument: ");
            process = new ProcessBuilder(arguments).start();
        }
        catch (final Exception ex) // catch generic Exception because we want to show error for any unusual situation
        {
            LOG.error("Error to execute this command in the system. Info command:\n" + Arrays.toString(arguments) + "; ");
            return false;
        }
        return processErrorHandler(process);
    }

    /**
     * Handle the process to show an error when the result not be expected
     * <p/>
     *
     * @param process to calculate
     * @return <code>true</code> it's finalized or not <code>false</code>
     */
    private boolean processErrorHandler(Process process)
    {
        try
        {
            int result = process.waitFor();
            InputStream inputStream = result == 0 ? process.getInputStream() : process.getErrorStream();
            if (result != 0)
            {
                LOG.error(new BufferedReader(new InputStreamReader(inputStream))
                        .lines().parallel().collect(Collectors.joining("\n")));
                return false;
            }
            return true;
        }
        catch (final InterruptedException e)
        {
            LOG.error("InterruptedException to execute command");
            return false;
        }
    }

    /**
     * Create a Message object from API Discord
     * <p/>
     *
     * @param title       {@link String}
     * @param description {@link String}
     * @param color       {@link Color}
     * @param fieldsValue {@link HashMap}
     * @param image       {@link String}
     * @return {@link MessageEmbed}
     */
    private MessageEmbed createMessage(final String title, final String description, final Color color,
                                       final HashMap<String, String> fieldsValue, final String image)
    {
        // Create the EmbedBuilder instance
        EmbedBuilder eb = new EmbedBuilder();

        /*
            Set the title:
            1. Arg: title as string
            2. Arg: URL as string or could also be null
         */
        eb.setTitle(title, null);

        /*
            Set the color
         */
        if (null == color)
        {
            eb.setColor(Color.red);
        }
        else
        {
            eb.setColor(color);
        }
        // eb.setColor(new Color(0xF40C0C));
        // eb.setColor(new Color(255, 0, 54));

        /*
            Set the text of the Embed:
            Arg: text as string
         */
        if (null != description)
        {
            eb.setDescription(description);
        }

        /*
            Add fields to embed:
            1. Arg: title as string
            2. Arg: text as string
            3. Arg: inline mode true / false
         */
        if (MapUtils.isNotEmpty(fieldsValue))
        {
            for (final Map.Entry<String, String> entry : fieldsValue.entrySet())
            {
                eb.addField(entry.getKey(), entry.getValue(), true);
            }
        }

        /*
            Add spacer like field
            Arg: inline mode true / false
         */
        eb.addBlankField(true); // Does the card more width
//        eb.addBlankField(false); // Separation before image

        /*
            Add embed author:
            1. Arg: name as string
            2. Arg: url as string (can be null)
            3. Arg: icon url as string (can be null)
         */
        // eb.setAuthor("", null, null);

        /*
            Set footer:
            1. Arg: text as string
            2. icon url as string (can be null)
         */
        // eb.setFooter("__por Wolf Team__", null);
        eb.setTimestamp(Instant.now());
        /*
            Set image:
            Arg: image url as string
         */
        if (null != image)
        {
            eb.setImage(image);
        }

        /*
            Set thumbnail image:
            Arg: image url as string
         */
        eb.setThumbnail("https://media.discordapp.net/attachments/564454752705052683/571084650202791938/wolf_logo_sm.png");

        return eb.build();
    }

    /**
     * TODO: Refactor
     *
     * @param title
     * @param color
     * @param fieldsValue
     * @param image
     * @return
     */
    private MessageEmbed createMessageStatus(final String title, final Color color,
                                             final HashMap<String, String> fieldsValue, final String image)
    {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(title, null);
        eb.setColor(color);

        if (MapUtils.isNotEmpty(fieldsValue))
        {
            for (final Map.Entry<String, String> entry : fieldsValue.entrySet())
            {
                if (entry.getKey().equals("Nombre"))
                {
                    eb.addField(entry.getKey(), entry.getValue(), false);
                    eb.addBlankField(false);
                }
                else
                {
                    eb.addField(entry.getKey(), entry.getValue(), true);
                }
            }
        }
//        eb.addBlankField(true); // Does the card more width

//        eb.setTimestamp(Instant.now());

        if (null != image)
        {
            eb.setImage(image);
        }

        eb.setThumbnail("https://media.discordapp.net/attachments/564454752705052683/571084650202791938/wolf_logo_sm.png");

        return eb.build();
    }

    private String getSecondArgumentFromRequest(final Matcher matcher)
    {
        return matcher.group(4);
    }

    private String getUserFromAlias(final String alias)
    {
        return UserEnum.valueOf(alias).getName();
    }
}
