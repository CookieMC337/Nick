
package listener;

import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import com.mycompany.nick.Nick;
import org.bukkit.event.Listener;

public class PlayerJoinListener implements Listener
{
    private Nick plugin;
    
    public PlayerJoinListener(final Nick plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }
    
    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player p = event.getPlayer();
    }
}
