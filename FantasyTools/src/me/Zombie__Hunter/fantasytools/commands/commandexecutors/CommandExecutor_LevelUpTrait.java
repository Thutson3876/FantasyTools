package me.Zombie__Hunter.fantasytools.commands.commandexecutors;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.Zombie__Hunter.fantasytools.FantasyTools;
import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.commands.AbstractCommand;
import me.Zombie__Hunter.fantasytools.traits.Trait;
import me.Zombie__Hunter.fantasytools.utils.ChatUtils;

public class CommandExecutor_LevelUpTrait extends AbstractCommand implements Listener {
	
	
	public CommandExecutor_LevelUpTrait() {
		super("leveluptrait", new String[] {"uptrait"});
	}

	@Override
	protected boolean onInternalCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			return false;
		}
		if(args.length < 1 || args.length > 2) {
			return false;
		}
		
		Player player = (Player)sender;
		int amt = 1;
		if(args.length == 2) {
			try {
				amt = Integer.parseInt(args[1]);
			} catch(NumberFormatException e) {
				return false;
			}
		}	
			
		AbstractClassTool tool = FantasyTools.getPlugin().getToolManager().getTool(player);
		Trait desiredTrait = null;
	    if(tool == null) {
	    	sender.sendMessage(ChatUtils.chat("&4You don't own a registered class tool."));
	    	return false;
	    }
	    for(Trait trait : tool.getTraits()) {
	    	if(trait.getCommandName().equalsIgnoreCase(args[0])) {
	    		desiredTrait = trait;
	    		break;
	    	}
	    }
	    if(desiredTrait == null) {
	    	sender.sendMessage(ChatUtils.chat("&4" + args[0] + " isn't a trait on your registered class tool."));
	    	return false;
	    }
	    
		if(desiredTrait.getTool().levelUpTrait(desiredTrait, amt)) {
			sender.sendMessage(ChatUtils.chat("&3Successfully upgraded " + desiredTrait.getName() + " to level " + desiredTrait.getCurrentLevel()));
			return true;
		}
		sender.sendMessage(ChatUtils.chat("&4Something went wrong."));
		return false;
	}
	
	@Override
	  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		    List<String> values = new LinkedList<>();
		    if (!(sender instanceof Player))
		      return values; 
		    Player player = (Player)sender;
		    
		    if (args.length == 1) {
		    	values = toolTraitNames(player);
		    }
		    else if(args.length == 2) {
		    	values.add("<amt>");
		    }
		    return values;
	}
	
	private static List<String> toolTraitNames(Player p){
		List<String> values = new LinkedList<>();
	    
	    AbstractClassTool tool = FantasyTools.getPlugin().getToolManager().getTool(p);
	    if(tool == null) {
	    	return values;
	    }
	    for(Trait trait : tool.getTraits()) {
	    	values.add(trait.getCommandName());
	    }
	    return values;
	}
	
}
