package online.gun.pseudo.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import online.gun.pseudo.PseudoServerMain;

public class CommandManager implements CommandExecutor {
	PseudoServerMain plugin;
	public CommandManager(PseudoServerMain plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			if (command.getName().equals("pgs")) {
				if (args.length==1) {
				}
				else if (args.length==2) {
					if (args[0].equals("get")) {
						plugin.giveWeapon((Player)sender,args[1]);
						return true;
					}
				}
				else if (args.length==3) {
					if (args[0].equals("write")) {
						plugin.writeWeapon((Player)sender,args[1],args[2]);
						return true;
					}
				}
			}
		}
		return false;
	}
}
