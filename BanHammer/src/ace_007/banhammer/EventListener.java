package ace_007.banhammer;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventListener implements Listener {
	public final Logger LOG = Logger.getLogger("Minecraft");
	
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		String name = player.getName();
		player.sendMessage(ChatColor.RED + "[BanHammer] " + ChatColor.RESET + "Welcome " + player.getName() + "!");
		player.sendMessage(ChatColor.RED + "[BanHammer] " + ChatColor.RESET + "This Server is protected with the BanHammer!");
	}
}
