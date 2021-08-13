package me.Zombie__Hunter.fantasytools.commands.commandexecutors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.commands.AbstractCommand;
import me.Zombie__Hunter.fantasytools.utils.ChatUtils;

public class CommandExecutor_UnregisterAnyClassTools extends AbstractCommand implements Listener {
	public CommandExecutor_UnregisterAnyClassTools() {
		super("unregisterall", new String[] {"unregisterallclasstools", "unregisteranyclasstools"});
	}

	@Override
	protected boolean onInternalCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length == 0 && sender instanceof Player) {
			Player player = (Player)sender;
			AbstractClassTool tool = plugin.getToolManager().getTool(player);
			
			if(tool == null) {
				sender.sendMessage(ChatUtils.chat("&4You do not currently have any registered class tools."));
				return false;
			}
			
			plugin.getToolManager().remove(tool);
			sender.sendMessage(ChatUtils.chat("&3Your tool has been unregistered."));
			return true;
		}
		
		return false;
	}

}
