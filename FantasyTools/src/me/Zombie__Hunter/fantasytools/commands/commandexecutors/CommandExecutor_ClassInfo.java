package me.Zombie__Hunter.fantasytools.commands.commandexecutors;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.classtools.ClassTools;
import me.Zombie__Hunter.fantasytools.commands.AbstractCommand;
import me.Zombie__Hunter.fantasytools.utils.ChatUtils;

public class CommandExecutor_ClassInfo extends AbstractCommand implements Listener {

	private List<AbstractClassTool> classList = ClassTools.getDefaultClassTools();
	private List<String> infoTags = new LinkedList<>();
	
	public CommandExecutor_ClassInfo() {
		super("classes", new String[] {"classinfo"});
		infoTags.add("description");
    	infoTags.add("tooltype");
    	infoTags.add("traits");
	}

	@Override
	protected boolean onInternalCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length == 0) {
			String toolNames = "";
			for(AbstractClassTool t : this.classList) {
				toolNames += "&6" + t.getCommandName() + "&3, ";
			}
			StringUtils.chop(toolNames);
			StringUtils.chop(toolNames);
			
			sender.sendMessage(ChatUtils.chat("&3Classes: " + toolNames));
			return true;
		}
		else if(args.length == 1) {
			AbstractClassTool tool = null;
			for(AbstractClassTool t : this.classList) {
				if(t.getCommandName().equalsIgnoreCase(args[0])) {
					tool = t;
					break;
				}
			}
			if(tool == null) {
				sender.sendMessage(ChatUtils.chat("&4Class not found"));
				return false;
			}
			
			sender.sendMessage(ChatUtils.chat("&6" + tool.getName() + ": "));
			sender.sendMessage(ChatUtils.chat("&6" + tool.getDescription()));
			sender.sendMessage(ChatUtils.chat("&3Tool Type: &6" + tool.getToolType().name()));
			sender.sendMessage(ChatUtils.traitListToString(tool.getTraits()));
			
			return true;
		}
		else if(args.length == 2) {
			AbstractClassTool tool = isValidClass(args[0]);
			if(tool == null) {
				sender.sendMessage(ChatUtils.chat("&4Class not found"));
				return false;
			}
			
			String info = null;
			for(String s : this.infoTags) {
				if(s.equalsIgnoreCase(args[1])) {
					info = s;
					break;
				}
			}
			if(info == null) {
				return false;
			}
			
			switch(info) {
			  case "description":
				  sender.sendMessage(ChatUtils.chat("&6" + tool.getDescription()));
				  return true;
			  case "tooltype":
				  sender.sendMessage(ChatUtils.chat("&3Tool Type: &6" + tool.getToolType().name()));
				  return true;
			  case "traits":
				  sender.sendMessage(ChatUtils.traitListToString(tool.getTraits()));
				  return true;
			  default:
			      return false;
			}
		}
		
		return false;
	}
	
	@Override
	  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		    List<String> values = new LinkedList<>();
		    if (!(sender instanceof Player))
		      return values; 
		    
		    if (args.length == 1) {
		    	for (AbstractClassTool t : this.classList) {
		    		values.add(t.getCommandName()); 
		    	} 
		    }
		       
		    else if(args.length == 2) {
		    	values = this.infoTags;
		    }
		    
		    return values;
	}
	
	private AbstractClassTool isValidClass(String arg) {
		AbstractClassTool tool = null;
		for(AbstractClassTool t : this.classList) {
			if(t.getCommandName().equalsIgnoreCase(arg)) {
				tool = t;
				break;
			}
		}
		return tool;
	}
}
