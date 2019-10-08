package es.wolfteam;

public interface Constants
{
    String BASE = "/wolf";
    String REGEX = "(/wolf)\\s(help|start|stop|restart|nuclear|status|update|slot)(\\s(all|servers|mods|delta|tango|nono|[0-9]))?" +
            "(\\s(ametrallador|ataa|dmr|falcon|francotirador|granadero|ingeniero|medico|martillo|radio|triton|valquiria))?";

    interface Paths
    {
        String START = "start_server.sh";
        String STOP = "stop_server.sh";
        String ROOT = "/opt/wolf_server/";
    }

    interface Commands
    {
        String HELP = "help";
        String START = "start";
        String STOP = "stop";
        String RESTART = "restart";
        String UPDATE = "update";
        String NUCLEAR = "nuclear";
        String STATUS = "status";
        String SLOT = "slot";
    }

    interface Files
    {
        String HELP_MESSAGE_FILE = "help_message.md";
    }

    interface Ids
    {
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
        String TOKEN = "NTU5MDI3MzYyMjkzODc0Njg5.D3feiw.Ef1yFusAJoCBay9UhTFZ-TAPhW4";
        String FRONT_END_HTTPS= "Front-End-Https";
    }
}