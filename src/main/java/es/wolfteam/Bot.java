package es.wolfteam;

import es.wolfteam.core.WConfig;
import es.wolfteam.core.WContext;
import es.wolfteam.core.WMaster;
import es.wolfteam.filters.FilterMessage;
import es.wolfteam.filters.impl.DefaultFilterMessage;
import es.wolfteam.utils.WDefaultUtils;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;

import javax.security.auth.login.LoginException;

import static es.wolfteam.core.WConfig.loadProperties;
import static es.wolfteam.utils.WBuilderUtils.buildInk;

/**
 * The type Bot.
 *
 * @author <a href="mailto:wyeks@live.com">wyeks</a> Discord Bot Utils for Wolf Team channel administration
 */
public class Bot
{
    private static final Logger LOG = JDALogger.getLog(Bot.class);
    private static final String MOCKED = "mocked";
    private static final String TOKEN = "token_bot";
    public static boolean botMocked;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws LoginException the login exception
     */
    public static void main(String[] args) throws LoginException
    {
        // Start Props
        loadProperties();
        botMocked = Boolean.parseBoolean(WConfig.getParameter(MOCKED));
        LOG.info("Start Discord Wolf Bot by Wolf TEAM. Bot Mocked: " + botMocked);
        LOG.info(buildInk());

        // Bot Application
        final FilterMessage filter = new DefaultFilterMessage(WDefaultUtils.getDefaultCommands());
        final WContext context = new WContext();

        // JDA Functions
        final JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(WConfig.getParameter(TOKEN));
        builder.addEventListeners(new WMaster(filter, context));
        builder.build();
    }
}