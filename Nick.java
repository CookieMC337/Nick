

package Nick;

import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import listener.PlayerJoinListener;
import api.NickManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Nick extends JavaPlugin
{
    FileConfiguration config;
    public static NickManager nicknamer;
    private PlayerJoinListener playerjoin;
    public static String Namelist;
    public static ArrayList<String> Names;
    static ArrayList<String> Names1;
    public static Nick instance;
    
    public Nick() {
        this.config = this.getConfig();
    }
    
    public void onEnable() {
        final List<String> Namess = new ArrayList<String>(Arrays.asList(Nick.Namelist.split(", ")));
        for (final String s : Namess) {
            Nick.Names.add(s);
            Nick.Names1.add(s);
        }
        (Nick.instance = this).registerListener();
        this.registerCommands();
        this.loadConfig();
        this.init();
    }
    
    private void init() {
    }
    
    private void registerListener() {
        this.playerjoin = new PlayerJoinListener(this);
    }
    
    private void registerCommands() {
    }
    
    public static Nick getInstance() {
        return Nick.instance;
    }
    
    public void loadConfig() {
        final FileConfiguration cfg = this.getConfig();
        cfg.addDefault("Nickmessage", (Object)"&8[&5Nick&8] &7Dein Aktueller Nickname ist §8» &e ");
        cfg.options().copyDefaults(true);
        this.saveConfig();
    }
    
    public static NickManager getNicknamer() {
        return Nick.nicknamer;
    }
    
    public static String replaceColors(final String string) {
        return string.replaceAll("(?i)&([a-f0-9])", "§$1");
    }
    
    static {
        Nick.nicknamer = new NickManager();
        Nick.Namelist = "nflboy717, Vault48, cody4209, Yourself, MidgetCo, mult, Runn3R, Tim_K, Mulk, mvit, Verace, Mark42, ophelia123, verado, drunkster, DemmyPlays, TheRevNetwork, Baki9610, fratesi, Orepros, john7067, Connor, Jamesy, Luvitus, Black, cheatinghater, twomach33, porty101, talaknor, tnt240, swagswagswag, fredddi, flyers418, pa55word, jtmf26, vortex308, Maximus, YouAintSTRiNG, slam10000bob, pipola, superfes, azgoo, WilliamBoo, Budder, NoWayItsTrevor, Lauren, KohdWing, Fletchps, Solidarity, 1johnclaude, Fanta, rukbukus, corndog10997, XenoCraftLp, ebag9101002, diablo, ttreg123, LlamasATA, immortalHD, Erak606, Kyletiv7, DraaxLP, Hawkb, creepabusster, Fletch, wildii, Fernandooo, ShadowShak, Btrpo, Jolly, 3agle, banana10";
        Nick.Names = new ArrayList<String>();
        Nick.Names1 = new ArrayList<String>();
    }
}
