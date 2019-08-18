package es.wolfteam;

public enum UserEnum
{
    // alpha("arma302", "2302"), unused
    nono("arma3hc", "2320"),
    // foxtrot("arma3hc", "2330"),
    tango("arma3hc", "2320"),
    delta("arma3hc", "2320");

    private String name;
    private String port;

    UserEnum(final String name, final String port) {
        this.name = name;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public String getPort()
    {
        return port;
    }
}