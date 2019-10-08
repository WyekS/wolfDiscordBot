package es.wolfteam;

import es.wolfteam.core.WMaster;
import es.wolfteam.filters.FilterMessage;
import es.wolfteam.filters.impl.DefaultFilterMessage;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

import static es.wolfteam.Constants.Request.TOKEN;

/**
 * The type Main.
 *
 * @author <a href="mailto:wyeks@live.com">wyeks</a> Discord Bot Utils for Wolf Team channel administration
 */
public class Main
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
        final FilterMessage filter = new DefaultFilterMessage();
        final JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(TOKEN);
        builder.addEventListener(new WMaster(filter));
        builder.buildAsync();
    }
}