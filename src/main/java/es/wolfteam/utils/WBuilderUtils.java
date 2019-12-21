package es.wolfteam.utils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import es.wolfteam.Bot;
import es.wolfteam.core.WConfig;
import es.wolfteam.data.types.SpecializationType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpHeaders;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static es.wolfteam.Constants.Files.HELP_MESSAGE_FILE;
import static es.wolfteam.Constants.Request.FRONT_END_HTTPS;

public class WBuilderUtils
{
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
    public static void requestAsyncArmaUnitStatus(int attempts, final String url,
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
                        // LOG.info("Def +" + response.getBody().toString());
                        try
                        {
                            messageChannel.sendMessage(buildStatusServerMessage(name,
                                    String.valueOf(jsonObject.getBoolean("status")),
                                    jsonObject.getString("name"),
                                    String.valueOf(jsonObject.getInt("players")))).queue();
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace(); // todo change to log
                        }
                    }

                    @Override
                    public void failed(UnirestException e)
                    {
                        // LOG.error("=== Trying again! Attemps: " + finalAttempts);
                        if (finalAttempts < 2)
                        {
                            requestAsyncArmaUnitStatus(finalAttempts, url, messageChannel, name);
                        }
                    }

                    @Override
                    public void cancelled()
                    {
                        // Cancelled task
                        // LOG.error("=== Cancelled Async task to getting url from Unit Arma");
                    }
                });
    }

    /**
     * Returns a message card to publish a specialization event
     * <p/>
     *
     * @param specialization name of specialization  {@link SpecializationType name attribute}
     * @param placesStr      Number places for this event {@link String}
     * @return {@link MessageEmbed} a builded card for chat bot
     */
    public static MessageEmbed buildCardPlacesMessage(final String specialization, final String placesStr)
    {
        Validate.notNull(specialization, "Parameter specialization cannot be null");
        Validate.notNull(placesStr, "Parameter places cannot be null");

        final int places = Integer.parseInt(placesStr);
        final String title = "Nuevas vacantes para ".concat(SpecializationType.valueOf(specialization).getName());
        final StringBuilder description = new StringBuilder();
        description.append("<@&536857111368433675> Se necesita personal para la especialización de ")
                .append(SpecializationType.valueOf(specialization).getName())
                .append(". \n\nLos interesados deberán rellenar este formulario: \n")
                .append(WConfig.getParameter("url.form_event"))
                .append("\n\nEl instructor se pondrá en contacto con los seleccionados. *(Rango mínimo de Soldado)*");

        HashMap<String, String> fieldsValues = new HashMap<>();
        fieldsValues.put("Plazas disponibles", String.valueOf(places));

        return createMessage(title,
                description.toString(), Color.red, fieldsValues, SpecializationType.valueOf(specialization).getImage(), true);
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
    public static MessageEmbed buildStatusServerMessage(final String server, final String status,
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
    public static String buildHelpingsMessage()
    {
        String result = "";
        ClassLoader classLoader = Bot.class.getClassLoader();
        try
        {
            result = IOUtils.toString(
                    Objects.requireNonNull(classLoader.getResourceAsStream(HELP_MESSAGE_FILE)), StandardCharsets.UTF_8);
        }
        catch (final IOException ioe)
        {
            // LOG.error("Error I/O when the file help_message.md was being read");
        }

        return result;
    }

    public static String buildInk()
    {
        String result = "";
        ClassLoader classLoader = Bot.class.getClassLoader();
        try
        {
            result = IOUtils.toString(
                    Objects.requireNonNull(classLoader.getResourceAsStream("messages/ink")), StandardCharsets.UTF_8);
        }
        catch (final IOException ioe)
        {
            // LOG.error("Error I/O when the file help_message.md was being read");
        }

        return result;
    }

    public static MessageEmbed buildErrorPermissionMessage()
    {
        return createMessage("Error", "No tienes permisos para ejecutar este comando", null, null, null, false);
    }

    public static MessageEmbed buildErrorCommandNotFoundMessage()
    {
        return createMessage("Error", "Ese comando no existe", null, null, null, false);
    }

    public static MessageEmbed buildUpdateInProcessMessage()
    {
        return createMessage("Update Arma 3", "La actualización está en proceso...", Color.orange, null, null, false);
    }

    public static MessageEmbed buildUpdateErrorMessage()
    {
        return createMessage("Error", "No ningún LOG sobre la actualización", null, null, null, false);
    }

    public static MessageEmbed buildUpdateFinishMessage()
    {
        return createMessage("Update Arma 3", "La actualización ha finalizado", Color.green, null, null, false);
    }


    public static MessageEmbed buildStartSucessMessage(final String alias)
    {
        return createMessage("Mapa " + alias, "Se está iniciado el mapa " + alias + ", tardará unos minutos", Color.green, null, null, false);
    }

    public static MessageEmbed buildStartErrorMessage(final String alias)
    {
        return createMessage("Mapa " + alias, "Hay ocurrido un error al iniciar " + alias, Color.red, null, null, false);
    }

    public static MessageEmbed buildStopSucessMessage()
    {
        return createMessage("Stop Arma 3", "Servidores parados con éxito", Color.green, null, null, false);
    }

    public static MessageEmbed buildStopErrorMessage()
    {
        return createMessage("Stop Arma 3" , "Hay ocurrido un error al parar los servidores ", Color.red, null, null, false);
    }

    public static MessageEmbed buildOperationEventMessage()
    {
        return createMessage("Operación", "Se solitita intervención de las fuerzas de @Wolfteam a las 22.00h",
                Color.green, null, null, false);
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
    public static MessageEmbed createMessage(final String title, final String description, final Color color,
                                             final HashMap<String, String> fieldsValue, final String image, final boolean enableFooter)
    {
        // Create the EmbedBuilder instance
        final EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle(title, null);

        eb.setColor(null == color ? Color.red : color);

        if (null != description)
        {
            eb.setDescription(description);
        }

        if (MapUtils.isNotEmpty(fieldsValue))
        {
            for (final Map.Entry<String, String> entry : fieldsValue.entrySet())
            {
                eb.addField(entry.getKey(), entry.getValue(), true);
            }
            eb.addBlankField(true); // Separation before image
        }

        if (enableFooter)
        {
            eb.addBlankField(true); // Does the card more width
            eb.setAuthor("", null, null);
            eb.setFooter("__por Wolf Team__", "https://media.discordapp.net/attachments/564454752705052683/571084650202791938/wolf_logo_sm.png");
            eb.setTimestamp(Instant.now());
        }

        if (null != image)
        {
            eb.setImage(image);
        }

        //eb.setThumbnail("https://media.discordapp.net/attachments/564454752705052683/571084650202791938/wolf_logo_sm.png");

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
    public static MessageEmbed createMessageStatus(final String title, final Color color,
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
        // eb.addBlankField(true); // Does the card more width
        // eb.setTimestamp(Instant.now());

        if (null != image)
        {
            eb.setImage(image);
        }

        eb.setThumbnail("https://media.discordapp.net/attachments/564454752705052683/571084650202791938/wolf_logo_sm.png");

        return eb.build();
    }

    private MessageEmbed generate(double tps)
    {
        EmbedBuilder eb = new EmbedBuilder();
        double lagPercentage = Math.round((1.0D - tps / 20.0D) * 100.0D);
        eb.addField("TPS:", "`" + new DecimalFormat("#.####").format(tps) + "`", true);
        eb.addField("Lag Percentage: ", lagPercentage + "%", true);
        eb.addField("Free RAM: ", Runtime.getRuntime().freeMemory() / 1024L / 1024L + "mb", true);
        eb.addField("Total Memory: ", Runtime.getRuntime().totalMemory() / 1024L / 1024L + "mb", true);
        eb.addField("Allocated Memory: ", Runtime.getRuntime().totalMemory() / 1024L / 1024L + "mb", true);
        eb.addBlankField(true);
        return eb.build();
    }

}
