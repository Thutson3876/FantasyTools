package me.Zombie__Hunter.fantasytools.commands.commandexecutors;

import java.util.LinkedList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.Zombie__Hunter.fantasytools.FantasyTools;
import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.classtools.ClassTools;
import me.Zombie__Hunter.fantasytools.commands.AbstractCommand;
import me.Zombie__Hunter.fantasytools.utils.ChatUtils;

public class CommandExecutor_CreateClassTool extends AbstractCommand implements Listener {

	public CommandExecutor_CreateClassTool() {
		super("createclasstool", new String[] {"cct", "create"});
		//FantasyTools.getPlugin().getCommand("createclasstool").setExecutor(this);
	}

	@Override
	protected boolean onInternalCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!sender.isOp() || !(sender instanceof Player)) {
			sender.sendMessage(ChatUtils.chat("&4Must be an opped player to use this command."));
			return true;
		}
		if(args.length == 1) {
			Class<? extends AbstractClassTool> clazz = null;
			for(ClassTools tool : ClassTools.values()) {
				if(tool.name().equalsIgnoreCase(args[0])) {
					clazz = tool.getClazz();
					break;
				}
			}
			if(clazz == null) {
				sender.sendMessage(ChatUtils.chat("&4Class is null."));
				return false;
			}
			
			AbstractClassTool tool = ClassTools.generateToolFromClassAndPlayer(clazz, (Player)sender);
			FantasyTools.getPlugin().getToolManager().add(tool);
			((Player)sender).getInventory().addItem(tool.getItemStack());
			sender.sendMessage(ChatUtils.chat("&3" + tool.getName() + " has been added to your inventory!"));
			return true;
		}

		return false;
	}
	
	@Override
	  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		    List<String> values = new LinkedList<>();
		    if (!(sender instanceof Player))
		      return values; 
		    if (args.length == 1)
		      for (ClassTools tool : ClassTools.values()) {
		          values.add(tool.name().toLowerCase()); 
		      }  
		    return values;
	}
}
