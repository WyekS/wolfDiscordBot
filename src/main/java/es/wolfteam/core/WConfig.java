package es.wolfteam.core;

import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class WConfig
{
    private static final Logger LOG = JDALogger.getLog(WConfig.class);
    private static Properties properties = null;

    public static void loadProperties()
    {
        try (final InputStream input = new FileInputStream("/home/wyeks/Proyectos/arma/wolfDiscordBot/src/main/config/config.properties"))
        {
            properties = new Properties();
            properties.load(input);
        }
        catch (final IOException ex)
        {
            LOG.error("Error when properties was loading");
        }
    }

    public static String getParameter(final String property)
    {
        return properties.getProperty(property);
    }
}
