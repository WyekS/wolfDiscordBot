package es.wolfteam;

public interface Constants
{
    String BASE = "/wolf";
    String REGEX_ALIAS = "(delta|nono|tango)";
    String REGEX_ALIAS_ALL = "(delta|nono|tango|all)";
    String REGEX = "(/wolf)\\s(help|start|stop|restart|nuclear|status|update|slot)(\\s(all|servers|mods|delta|tango|nono|[0-9]))?" +
            "(\\s(ametrallador|ataa|dmr|falcon|francotirador|granadero|ingeniero|medico|martillo|radio|triton|valquiria))?";

    interface Files
    {
        String HELP_MESSAGE_FILE = "messages/help_message.md";
        String MODS_MESSAGE_FILE = "/home/arma3hc/serverfiles/arma3server_%s.sh";
    }

    interface Ids
    {
        interface MockedIds
        {
            long CHANNEL_CALL_EVENTS_MOCKED = 571065967942041699L;
        }

        long CHANNEL_CALL_EVENTS = 563696160817283102L;
        String CHANNEL_BOT_SERVER = "562266299657486338";
    }

    interface Urls
    {
        String FORM_EVENT =
                "https://docs.google.com/forms/d/e/1FAIpQLSfFN6Q3Rke8v7bhYmmSkbabLxdZlJibwx2Hv6V05s-7jyUEkQ/viewform?usp=sf_link";
    }

    interface Request
    {
        String OUR_IP = "82.223.29.198";
        String REQUEST_STATUS = "https://units.arma3.com/resource/server?ip_address=%1$s&port=%2$s";
        String FRONT_END_HTTPS = "Front-End-Https";
    }
}