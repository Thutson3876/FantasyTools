package me.Zombie__Hunter.fantasytools.commands.commandexecutors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.Zombie__Hunter.fantasytools.commands.AbstractCommand;
import me.Zombie__Hunter.fantasytools.playermanagement.PlayerManager;

public class CommandExecutor_ClearUnbreakables extends AbstractCommand implements Listener {

	public CommandExecutor_ClearUnbreakables() {
		super("clearunbreakables", new String[] {"unbreakables"});
	}

	@Override
	protected boolean onInternalCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!sender.isOp()) {
			sender.sendMessage("You don't have permission to use this command.");
			return true;
		}
		String playerList = "";
		
		if(args.length == 0) {
			
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(PlayerManager.removeAllUnbreakablesButTool(p)) {
					playerList += p.getDisplayName() + "; ";
				}
			}
			
			if(playerList.equals("")) {
				sender.sendMessage("No one is scum");
				return true;
			}
			
			sender.sendMessage("Scum: " + playerList);
			return true;
		}
		else if(args.length == 1) {
			Player scum = null;
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(args[0].equalsIgnoreCase(p.getDisplayName())) {
					scum = p;
					break;
				}
			}
			
			if(scum == null) {
				sender.sendMessage("Player not online.");
				return true;
			}
			
			if(!PlayerManager.removeAllUnbreakablesButTool(scum)) {
				sender.sendMessage("Player had no unbreakables.");
				return true;
			}
			
			sender.sendMessage("Player had unbreakables.");
			return true;
		}
		return false;
	}
	
	
}
