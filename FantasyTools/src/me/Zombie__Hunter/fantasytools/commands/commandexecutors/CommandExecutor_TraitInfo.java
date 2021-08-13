package me.Zombie__Hunter.fantasytools.commands.commandexecutors;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.Zombie__Hunter.fantasytools.commands.AbstractCommand;
import me.Zombie__Hunter.fantasytools.traits.DefaultTraitsList;
import me.Zombie__Hunter.fantasytools.traits.Trait;
import me.Zombie__Hunter.fantasytools.utils.ChatUtils;

public class CommandExecutor_TraitInfo extends AbstractCommand implements Listener {
	
	private List<Trait> traitsList = DefaultTraitsList.getTraits();
	private List<String> infoTags = new LinkedList<>();
	
	public CommandExecutor_TraitInfo() {
		super("traits", new String[] {"traitinfo"});
		infoTags.add("description");
    	infoTags.add("maxlevel");
    	infoTags.add("cost");
    	infoTags.add("cooldown");
    	infoTags.add("activation");
	}

	@Override
	protected boolean onInternalCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length == 0) {
			sender.sendMessage(ChatUtils.traitListToString(this.traitsList));
			return true;
		}
		else if(args.length == 1) {
			Trait trait = isValidTrait(args[0]);
			if(trait == null) {
				sender.sendMessage(ChatUtils.chat("&4Trait not found"));
				return false;
			}
			
			sender.sendMessage(ChatUtils.chat("&6" + trait.getName() + "&3: "));
			sender.sendMessage(ChatUtils.chat("&6" + trait.getDescription()));
			sender.sendMessage(ChatUtils.chat("&3Cost to level up: &6" + trait.getSkillPointCost() + " &3skillpoints"));
			sender.sendMessage(ChatUtils.chat("&3Max Level: &6" + trait.getMaxLevel()));
			sender.sendMessage(ChatUtils.chat("&3Cooldown: &6" + (trait.getMaxCooldown() / 20) + " &3seconds"));
			sender.sendMessage(ChatUtils.chat("&Activation: &6" + trait.getActivation()));
			
			return true;
		}
		else if(args.length == 2) {
			Trait trait = isValidTrait(args[0]);
			if(trait == null) {
				sender.sendMessage(ChatUtils.chat("&4Trait not found"));
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
				  sender.sendMessage(ChatUtils.chat("&6" + trait.getDescription()));
				  return true;
			  case "maxlevel":
				  sender.sendMessage(ChatUtils.chat("&3Cost to level up: &6" + trait.getSkillPointCost() + " &3skillpoints"));
				  return true;
			  case "cost":
				  sender.sendMessage(ChatUtils.chat("&3Max Level: &6" + trait.getMaxLevel()));
				  return true;
			  case "cooldown":
				  sender.sendMessage(ChatUtils.chat("&3Cooldown: &6" + (trait.getMaxCooldown() / 20) + " &3seconds"));
				  return true;
			  case "activation":
				  sender.sendMessage(ChatUtils.chat("&Activation: &6" + trait.getActivation()));
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
		    	for (Trait t : this.traitsList) {
		    		values.add(t.getCommandName()); 
		    	} 
		    }
		       
		    else if(args.length == 2) {
		    	values = this.infoTags;
		    }
		    
		    return values;
	}
	
	private Trait isValidTrait(String arg) {
		Trait trait = null;
		for(Trait t : this.traitsList) {
			if(t.getCommandName().equalsIgnoreCase(arg)) {
				trait = t;
				break;
			}
		}
		return trait;
	}
}
