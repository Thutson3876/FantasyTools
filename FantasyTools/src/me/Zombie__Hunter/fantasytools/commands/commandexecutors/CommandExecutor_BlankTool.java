package me.Zombie__Hunter.fantasytools.commands.commandexecutors;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.Zombie__Hunter.fantasytools.FantasyTools;
import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.classtools.classtoolslist.BlankTool;
import me.Zombie__Hunter.fantasytools.commands.AbstractCommand;
import me.Zombie__Hunter.fantasytools.traits.DefaultTraitsList;
import me.Zombie__Hunter.fantasytools.traits.Trait;
import me.Zombie__Hunter.fantasytools.utils.ChatUtils;

public class CommandExecutor_BlankTool extends AbstractCommand implements Listener {
	
	private List<Trait> traitsList = DefaultTraitsList.getTraits();
	
	public CommandExecutor_BlankTool() {
		super("blanktool", new String[] {"blank"});
	}

	@Override
	protected boolean onInternalCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!sender.isOp() || !(sender instanceof Player)) {
			sender.sendMessage(ChatUtils.chat("&4You must be an opped player to use this command."));
			return false;
		}
		
		Player player = (Player)sender;
		
		if(args.length == 1) {
			if(!args[0].equalsIgnoreCase("create")) {
				return false;
			}
			
			BlankTool tool = new BlankTool(player);
			FantasyTools.getPlugin().getToolManager().add(tool);
			player.getInventory().addItem(tool.getItemStack());
			sender.sendMessage(ChatUtils.chat("&3" + tool.getName() + " has been added to your inventory!"));
			return true;
		}
		else if(args.length == 2) {
			AbstractClassTool tool = FantasyTools.getPlugin().getToolManager().getTool(player);
			if(tool == null) {
				sender.sendMessage(ChatUtils.chat("&4You don't own a registered class tool."));
				return false;
			}
			Class<? extends Trait> clazz = null;
			for(Trait trait : traitsList) {
				if(trait.getCommandName().equalsIgnoreCase(args[1])) {
					clazz = trait.getClass();
				}
			}
			if(clazz == null) {
				sender.sendMessage(ChatUtils.chat("&4Invalid trait name."));
				return false;
			}
			
			if(args[0].equalsIgnoreCase("addtrait")) {
				boolean success = tool.addTrait(clazz);
				if(success) {
					sender.sendMessage(ChatUtils.chat("&3Successfully added trait."));
				}
				
				return success;
			}
			else if(args[0].equalsIgnoreCase("removetrait")){
				boolean success = tool.removeTrait(clazz);
				if(success) {
					sender.sendMessage(ChatUtils.chat("&3Successfully removed trait."));
				}
				
				return success;
			}
		}

		return false;
	}
	
	@Override
	  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		    List<String> values = new LinkedList<>();
		    if (!(sender instanceof Player))
		      return values; 
		    Player p = (Player)sender;
		    
		    if(args.length == 1) {
		    	values.add("create");
		    	values.add("addtrait");
		    	values.add("removetrait");
		    }
		    else if (args.length == 2) {
		    	
		    	if(args[0].equalsIgnoreCase("removetrait")) {
		    		AbstractClassTool tool = FantasyTools.getPlugin().getToolManager().getTool(p);
		    		if(tool == null) {
		    			return values;
		    		}
		    		for(Trait trait : tool.getTraits()) {
		    			values.add(trait.getCommandName());
		    		}
		    	}
		    	else {
		    		for (Trait t : this.traitsList) {
		    			values.add(t.getCommandName()); 
		    		} 
		    	}
		    }
		    
		    return values;
	}
}
