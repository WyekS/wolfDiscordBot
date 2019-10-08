package es.wolfteam.data.types;

public enum ActionType
{
    HELP("help", 1),
    START("start", 2),
    STOP("stop", 1),
    RESTART("restart", 1),
    STATUS("status", 1),
    UPDATE("update", 2),
    NUCLEAR("nuclear", 1),
    SLOT("slot", 3);

    private String name;
    private int maxParams;

    ActionType(final String name, int maxParams)
    {
        this.name = name;
        this.maxParams = maxParams;
    }

    public String getName()
    {
        return name;
    }

    public int getMaxParams()
    {
        return maxParams;
    }
}
