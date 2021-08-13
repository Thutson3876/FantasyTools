package me.Zombie__Hunter.fantasytools.commands.commandexecutors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.commands.AbstractCommand;
import me.Zombie__Hunter.fantasytools.utils.ChatUtils;

public class CommandExecutor_UnregisterClassTool extends AbstractCommand implements Listener {

	public CommandExecutor_UnregisterClassTool() {
		super("unregister", new String[] {"unregisterclasstool"});
	}

	@Override
	protected boolean onInternalCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length == 0 && sender instanceof Player) {
			Player player = (Player)sender;
			ItemStack item = player.getInventory().getItemInMainHand();
			AbstractClassTool tool = plugin.getToolManager().getTool(player);
			
			if(tool == null) {
				sender.sendMessage(ChatUtils.chat("&4You do not currently have any registered class tools."));
				return false;
			}
			if(!tool.getItemStack().equals(item)) {
				sender.sendMessage(ChatUtils.chat("&4Item is not a registered class tool. Make sure to hold the tool that you wish to unregister in your main hand."));
				return false;
			}
			
			
			plugin.getToolManager().remove(tool);
			player.getInventory().setItemInMainHand(tool.disable());
			sender.sendMessage(ChatUtils.chat("&3Item has been unregistered."));
			return true;
		}
		
		
		return false;
	}
}
