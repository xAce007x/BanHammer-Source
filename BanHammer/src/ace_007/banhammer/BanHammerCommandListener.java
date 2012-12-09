package ace_007.banhammer;

import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class BanHammerCommandListener implements Listener, CommandExecutor {
	
	public final static Logger LOG = Logger.getLogger("Minecraft");
	private Plugin bhPlugin;
	private final static Server SERVER = Bukkit.getServer();
	private int banRunning = 0;
	private static int messagePos = 0;
	private static int pid;
	private static String prefix = ChatColor.RED + "[BanHammer] " + ChatColor.RESET;
	
	
	// Constructor
	public BanHammerCommandListener(Plugin plugin) {
		bhPlugin = plugin;
	}
	
	// Executes when someone Ban Hammers someone else!
	private static void banExecute(Player sender, Player target, boolean console) throws Exception {
		String targetPlayer = target.getName();
		String attackingPlayer = sender.getName();
		
		String[] messages = {
				attackingPlayer + " wields the " + ChatColor.RED + "BanHammer!" + ChatColor.RESET, 
				ChatColor.GREEN + sender.getName() + ChatColor.RESET + " begins hunting " + ChatColor.YELLOW + targetPlayer + ChatColor.RESET + "!",
				prefix + attackingPlayer +  " has struck " + targetPlayer + " with the " + ChatColor.RED + "Ban Hammer." + ChatColor.RESET,
				ChatColor.YELLOW + targetPlayer + ChatColor.RESET + " crumbles to dust.",
				"Noticing the weight of the " + ChatColor.RED + "Ban Hammer" + ChatColor.RESET +  ", " + ChatColor.GREEN + attackingPlayer + ChatColor.RESET + " puts it down."};
		if(!console) {
			if (messagePos >= messages.length) {
				messagePos = 0;
				SERVER.getScheduler().cancelTask(pid);
			} else {
				for (Player onlinePlayer : SERVER.getOnlinePlayers()) {
					onlinePlayer.sendMessage(messages[messagePos]);
				}
				if (messagePos == messages.length-3) {
					target.kickPlayer("BanHammer!");
					LOG.info("[BanHammer] " + targetPlayer + " was kicked from the server!");
				}
				messagePos++;
			}
		} else {
			for (Player onlinePlayer : SERVER.getOnlinePlayers()) {
				onlinePlayer.sendMessage(
						ChatColor.RED + "[BanHammer] " + ChatColor.WHITE + "The " + ChatColor.LIGHT_PURPLE + "server " +
						ChatColor.WHITE + "smashes " + ChatColor.YELLOW + targetPlayer + ChatColor.WHITE + " with the " +
						ChatColor.RED + "BanHammer" + ChatColor.WHITE + "!");
				target.kickPlayer("BanHammer!");
				LOG.info("Player kicked!");
			}
		}

}
	
	// Executed when someone Fake Ban Hammers someone.
	private void banFake(Player sender, Player target) {
		
	}
	
	
	// Command Listeners
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if(commandLabel.equalsIgnoreCase("banhammer") || commandLabel.equalsIgnoreCase("bh")) { // BanHammer listener
			if(!sender.getName().equalsIgnoreCase("console")) {
				final Player PLAYER = (Player) sender;
				if (sender.isOp()) {
					if(args.length < 1) {
						PLAYER.sendMessage(prefix + "You didn't enter anything doofus!");
						this.LOG.info(PLAYER.getName() + " has used the BanHammer!");
					} else {
						final Player target = SERVER.getPlayer(args[0]);
						if (target != null) {
							// This is where the scheduler schedules the task to run so that you can print
							// messages to the server on a time based system.
							pid = SERVER.getScheduler().scheduleSyncRepeatingTask(bhPlugin, new Runnable() {
									public void run() {
										try {
											banExecute(PLAYER, target, false);
										} catch (Exception e) {
											LOG.info("This error was given!");
											e.printStackTrace();
										}
									}
							}, 0, 80);
					
							// Sends the message to the sender with the parsed target info.
							PLAYER.sendMessage(prefix + "You wanted to smite " + target.getName() + "!");
							this.LOG.info(PLAYER.getName() + " has used the BanHammer on " + target.getName());
						} else {
							sender.sendMessage(prefix + "It appears that person isn't online!");
							sender.sendMessage(prefix + "Use the normal /ban command.");
						}
					}
				} else {
					sender.sendMessage(ChatColor.RED + "You do not have access to this command!");
				}
				} else {  // Code for when the console enters the banhammer command (or bh)
					if (SERVER.getPlayer("args[0]") != null) {
						try {
							banExecute((Player) sender, SERVER.getPlayer(args[0]), true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						LOG.info("That player isn't online!");
					}
				}
			}
		
		if(commandLabel.equalsIgnoreCase("smite")) { // Smite listener
			if(!sender.getName().equalsIgnoreCase("console")) {
				final Player PLAYER = (Player) sender;
				World world = PLAYER.getWorld();
				if(args.length < 1) { // Dealing with too few or too many arguments.
					PLAYER.sendMessage(prefix + "you must supply a PLAYER to smite");
				
				} else if (args.length > 1) { // Too many arguments.
					PLAYER.sendMessage(ChatColor.RED + "[BanHammer]" + ChatColor.RESET + " Godly power can only be used upon one player at a time.");
				
				} else { // Arguments just right.
					if(SERVER.getPlayer(args[0]) != null) { // Player is online and will be smited!
						Player target = SERVER.getPlayer(args[0]);  // Getting player object for the target
						Location targetLocation = target.getLocation();  // Getting the location of the target player object
						int[] targetLocXYZ = {
								(int) targetLocation.getX(),
								(int) targetLocation.getY(),
								(int) targetLocation.getZ()
								};
						sender.sendMessage(prefix + "Smiting " + PLAYER.getName()
								+ " at [" + targetLocXYZ[0] + ", " + targetLocXYZ[1] + ", " + targetLocXYZ[2] +  "]!");
						world.strikeLightningEffect(targetLocation);
						target.setHealth(1);
						target.sendMessage(prefix + "You were smited by " + sender.getName() + ", consider this a warning!");
						
						for (Player onlinePlayer : SERVER.getOnlinePlayers()) {
							onlinePlayer.sendMessage(prefix + target.getName() + " was smited by " + sender.getName() + " for being a dumbass!");
						}
						LOG.info("[BanHammer] " + sender.getName() + " has smitted " + target.getName() + ".");
					
					} else { // Player is not online (or not found).
						sender.sendMessage(prefix + "You can't smite someone who isn't online. (DUH)");
					
					}
					
				}
			} else { // How to handle if the server smites someone!
				LOG.info("[BanHammer] Smite not setup for console yet.");
			}
		} // End of smite listener
		return false;
	} // End of onCommand()
} // End of BanHammerCommandListener class
