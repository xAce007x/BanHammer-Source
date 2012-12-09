package ace_007.banhammer;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;

public class banHammer extends JavaPlugin {
	public static Plugin plugin;
	private final Logger LOGGER = Logger.getLogger("Minecraft");
	private final EventListener EVENTLISTENER = new EventListener();
	private final Server SERVER = Bukkit.getServer();
	private int tid = 1;
	private int messagePos = 1;

	
/* 
	private boolean createDataDirectory() {
		File file = this.getDataFolder();
		if (!file.isDirectory())
		{
			if (!file.mkdirs())
			{
				// Couldn't make directories needed so failure.
				return false;
			}
		}
		return true;
	}
	
	private File getDataFile(String filename, boolean mustAlreadyExist) {
		if (createDataDirectory()) {
			File file = new File(this.getDataFolder(), filename);
			if (mustAlreadyExist) {
				if (file.exists()) {
					return file;
				}
			} else {
				return file;
			}
		}
		return null;
	}
*/
	
	
	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		PluginDescriptionFile pdfFile = this.getDescription();
		this.LOGGER.info(pdfFile.getName() + " " + pdfFile.getVersion() + " is now enabled");
		getCommand("banhammer").setExecutor(new BanHammerCommandListener(this));
		getCommand("bh").setExecutor(new BanHammerCommandListener(this));
		getCommand("smite").setExecutor(new BanHammerCommandListener(this));
		pm.registerEvents(this.EVENTLISTENER, this);
		SERVER.broadcastMessage(ChatColor.RED + "[BanHammer] " + ChatColor.RESET + "The Ban Hammer has been enabled.  Use caution.");
	}
	
	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		SERVER.broadcastMessage(ChatColor.RED + "[BanHammer] " + ChatColor.RESET + "The Ban Hammer has been disabled.");
		SERVER.broadcastMessage(ChatColor.RED + "[BanHammer] " + ChatColor.RESET +  "You're on your own.");
		this.LOGGER.info(pdfFile.getName() + " " + pdfFile.getVersion() + " is now disabled");
	}

}
