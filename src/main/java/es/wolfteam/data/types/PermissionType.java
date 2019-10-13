package es.wolfteam.data.types;

public enum PermissionType
{
    ADMIN("admin", 524007569438539797L, 559009681553752080L, 0),
    OFFICER("officer", 570765709173456896L, 632980438679420930L, 10),
    WOLFTEAM("wolfteam", 536857111368433675L, 564470299370848280L, 20);

    private String name;
    private long id;
    private long idMocked;
    private int level;

    PermissionType(String name, long id, long idMocked, int level)
    {
        this.name = name;
        this.id = id;
        this.idMocked = idMocked;
        this.level = level;
    }

    public String getName()
    {
        return name;
    }

    public long getId()
    {
        return id;
    }

    public long getIdMocked()
    {
        return idMocked;
    }

    public int getLevel()
    {
        return level;
    }
}
