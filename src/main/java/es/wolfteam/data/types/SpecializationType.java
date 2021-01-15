package es.wolfteam.data.types;

public enum SpecializationType
{
    ametrallador("Ametrallador",
            "https://cdn.discordapp.com/attachments/544493063372931080/556418623996952586/Ametrallador.jpg"),

    ataa("AT-AA",
            "https://cdn.discordapp.com/attachments/544493087729123328/556416879632187403/At.jpg"),

    dmr("Tirador Selecto",
            "https://cdn.discordapp.com/attachments/544485350307921931/556417715368361994/Dmw.jpg"),

    falcon("Falcon",
            "https://cdn.discordapp.com/attachments/544485160196898816/556418554136625153/Falcon.jpg"),

    francotirador("Francotirador",
            "https://cdn.discordapp.com/attachments/563264854194913280/564840607122456576/Francotirador.jpg"),

    granadero("Granadero",
            "https://cdn.discordapp.com/attachments/563268757007368192/564840497512972298/Granadero.jpg"),

    ingeniero("Ingeniero",
            "https://cdn.discordapp.com/attachments/544485121143865344/556417236282245120/Ingeniero.jpg"),

    medico("Médico",
            "https://cdn.discordapp.com/attachments/544485012188430336/556417073862017024/Medico.jpg"),

    martillo("Martillo",
            "https://cdn.discordapp.com/attachments/544485384009023538/556418499375923221/Hammer.jpg"),

    radio("Operador de Radio",
            "https://cdn.discordapp.com/attachments/544492936079867905/556418413841612820/Radiooperador.jpg"),

    triton("Triton",
            "https://media.discordapp.net/attachments/563268809570648075/564840398535655425/Triton.jpg"),

    valquiria("Valquiria",
            "https://cdn.discordapp.com/attachments/544485234251399169/556417423096676352/Valkiria.jpg");

    private String name;
    private String image;

    SpecializationType(final String name, final String image)
    {
        this.name = name;
        this.image = image;
    }

    public String getName()
    {
        return name;
    }

    public String getImage()
    {
        return image;
    }
}