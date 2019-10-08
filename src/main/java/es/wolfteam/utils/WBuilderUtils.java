package es.wolfteam.utils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import es.wolfteam.MainOld;
import es.wolfteam.data.types.SpecializationType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpHeaders;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static es.wolfteam.Constants.Files.HELP_MESSAGE_FILE;
import static es.wolfteam.Constants.Request.FRONT_END_HTTPS;
import static es.wolfteam.Constants.Urls.FORM_EVENT;

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
    private static void requestAsyncArmaUnitStatus(int attempts, final String url,
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
                        messageChannel.sendMessage(buildStatusServerMessage(name,
                                String.valueOf(jsonObject.getBoolean("status")),
                                jsonObject.getString("name"),
                                String.valueOf(jsonObject.getInt("players")))).queue();
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
    private static MessageEmbed buildCardPlacesMessage(final String specialization, final String placesStr)
    {
        Validate.notNull(specialization, "Parameter specialization cannot be null");
        Validate.notNull(placesStr, "Parameter places cannot be null");

        final int places = Integer.parseInt(placesStr);
        final String title = "Nuevas vacantes para ".concat(SpecializationType.valueOf(specialization).getName());
        final StringBuilder description = new StringBuilder();
        description.append("<@&536857111368433675> Se necesita personal para la especialización de ")
                .append(SpecializationType.valueOf(specialization).getName())
                .append(". \n\nLos interesados deberán rellenar este formulario: \n")
                .append(FORM_EVENT)
                .append("\n\nEl instructor se pondrá en contacto con los seleccionados. *(Rango mínimo de Soldado)*");

        HashMap<String, String> fieldsValues = new HashMap<>();
        fieldsValues.put("Plazas disponibles", String.valueOf(places));

        return createMessage(title,
                description.toString(), Color.red, fieldsValues, SpecializationType.valueOf(specialization).getImage());
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
    private static MessageEmbed buildStatusServerMessage(final String server, final String status,
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
        ClassLoader classLoader = MainOld.class.getClassLoader();
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
    private static MessageEmbed createMessage(final String title, final String description, final Color color,
                                       final HashMap<String, String> fieldsValue, final String image)
    {
        // Create the EmbedBuilder instance
        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle(title, null);

        if (null == color)
        {
            eb.setColor(Color.red);
        }
        else
        {
            eb.setColor(color);
        }

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
        }
        eb.addBlankField(true); // Does the card more width
        // eb.addBlankField(false); // Separation before image


         eb.setAuthor("", null, null);

        // eb.setFooter("__por Wolf Team__", null);
        eb.setTimestamp(Instant.now());

        if (null != image)
        {
            eb.setImage(image);
        }

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
    private static MessageEmbed createMessageStatus(final String title, final Color color,
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
}
