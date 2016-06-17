package bot.commands.sound;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public enum Sound
{

    QUACK("quack", new File("C:\\Users\\wiize\\Downloads\\quack.mp3"), "duck"),
    BOOTY("booty", new File("C:\\Users\\wiize\\Downloads\\booty.mp3"), "(͡°͜ʖ͡°)"),
    HIGHNOON("highnoon", new File("C:\\Users\\wiize\\Downloads\\highnoon.mp3"), "whattimeisit"),
    LOL("lol", new File("C:\\Users\\wiize\\Downloads\\lol.mp3")),
    SOGOOD("sogood", new File("C:\\Users\\wiize\\Downloads\\sogood.mp3")),
    ALLAHU("allahuakbar", new File("C:\\Users\\wiize\\Downloads\\allahuakbar.mp3"), "jihad", "allahu"),
    //
    YEAH("raymanyeah", new File("../raymanyeah.mp3")),
    DUMMY("dummy", new File("../dummy.mp3")),
    BALLSOFSTEEL("ballsofsteel", new File("../ballsofsteel.mp3")),
    BALLS("balls", new File("../balls.mp3")),
    MACHO1("circle", new File("../macho_alert16.mp3")),
    MACHO2("getyou", new File("../macho_alert13.mp3")),
    ALIVE("alive", new File("../alive.mp3")),
    BUBBERDUCK("bubberduck", new File("../bubberduck.mp3")),
    BONETROUSLE("bones", new File("../bonetrousle.mp3")),
    BONES("bonel", new File("../bones.mp3"));

    private String name;
    private String[] aliases;

    private String url;
    private File path;

    Sound(String name, String url, String... aliases)
    {
        this.name = name;
        this.url = url;
        this.aliases = aliases;
    }

    @Deprecated
    Sound(String name, File path, String... aliases)
    {
        this.name = name;
        this.aliases = aliases;
        this.path = path;
    }

    public static Sound get(String name)
    {
        for (Sound s : values())
        {
            if (s.getName().equalsIgnoreCase(name)) return s;
            for (String a : s.aliases)
            {
                if (a.equalsIgnoreCase(name)) return s;
            }
        }
        return null;
    }

    public String getName()
    {
        return this.name;
    }

    public String[] getAliases()
    {
        return this.aliases;
    }

    public URL getUrl() throws MalformedURLException
    {
        return new URL(this.url);
    }

    @Deprecated
    public File getPath()
    {
        return this.path;
    }
}
