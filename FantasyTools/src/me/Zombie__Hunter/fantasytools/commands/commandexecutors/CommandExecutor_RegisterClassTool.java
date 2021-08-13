package me.Zombie__Hunter.fantasytools.commands.commandexecutors;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import me.Zombie__Hunter.fantasytools.FantasyTools;
import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.classtools.ClassTools;
import me.Zombie__Hunter.fantasytools.commands.AbstractCommand;
import me.Zombie__Hunter.fantasytools.playermanagement.FantasyPlayer;
import me.Zombie__Hunter.fantasytools.utils.ChatUtils;

public class CommandExecutor_RegisterClassTool extends AbstractCommand implements Listener {

	public CommandExecutor_RegisterClassTool() {
		super("register", new String[] {"registerclasstool"});
	}

	@Override
	protected boolean onInternalCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			return false;
		}
		if(args.length == 0) {
			Player player = (Player)sender;
			ItemStack item = player.getInventory().getItemInMainHand();
			AbstractClassTool tool = plugin.getToolManager().getTool(player);
			
			if(tool != null) {
				sender.sendMessage(ChatUtils.chat("&4Item was unable to be registered. You currently have another class tool already registered."));
				return false;
			}
			
			tool = AbstractClassTool.generateFromItem(item, player);
			
			if(tool == null) {
				tool = AbstractClassTool.generateFromMat(item, player);
				sender.sendMessage(ChatUtils.chat("&4Item was registered from only material."));
			}
			
			if(tool == null) {
				sender.sendMessage(ChatUtils.chat("&4Item was unable to be registered. Make sure to hold the tool that you wish to register in your main hand."));
				return false;
			}
			
			int cost = tool.totalSkillpointCost();
			FantasyPlayer fPlayer = FantasyTools.getPlugin().getPlayerManager().getPlayer(player);
			
			if(cost > fPlayer.getSkillPoints()) {
				sender.sendMessage(ChatUtils.chat("&4Item was unable to be registered. You don't have enough skillpoints to register this item."));
				return false;
			}
			
			fPlayer.spendSkillPoints(cost);
			plugin.getToolManager().add(tool);
			tool.replaceCurrentItem(item);
			sender.sendMessage(ChatUtils.chat("&3Item registered"));
			return true;
		}
		else if(args.length == 1) {
			Player player = (Player)sender;
			ItemStack item = player.getInventory().getItemInMainHand();
			AbstractClassTool tool = plugin.getToolManager().getTool(player);
			
			if(tool != null) {
				sender.sendMessage(ChatUtils.chat("&4Item was unable to be registered. You currently have another class tool already registered."));
				return false;
			}
			ClassTools classTool = null;
			for(ClassTools t : ClassTools.values()) {
				if(args[0].equalsIgnoreCase(t.name().toLowerCase())) {
					classTool = t;
					break;
				}
			}
			if(classTool == null) {
				sender.sendMessage(ChatUtils.chat("&4Item was unable to be registered. Invalid class tool name."));
				return false;
			}
			
			boolean matchingType = false;
			for(Material mat : classTool.getToolType().getMats()) {
				if(mat.equals(item.getType())) {
					matchingType = true;
					break;
				}
			}
			if(!matchingType) {
				sender.sendMessage(ChatUtils.chat("&4Item was unable to be registered. Item material doesn't match class tool type."));
				return true;
			}
			
			tool = AbstractClassTool.generateFromClassToolAndItem(player, classTool, item);
			
			if(tool == null) {
				tool = AbstractClassTool.generateFromClassToolAndMat(player, classTool, item);
				sender.sendMessage(ChatUtils.chat("&4Item was registered from only material."));
			}
			
			if(tool == null) {
				sender.sendMessage(ChatUtils.chat("&4Item was unable to be registered. Make sure to hold the tool that you wish to register in your main hand."));
				return false;
			}
			
			int cost = tool.totalSkillpointCost();
			FantasyPlayer fPlayer = FantasyTools.getPlugin().getPlayerManager().getPlayer(player);
			
			if(cost > fPlayer.getSkillPoints()) {
				sender.sendMessage(ChatUtils.chat("&4Item was unable to be registered. You don't have enough skillpoints to register this item."));
				return false;
			}
			
			fPlayer.spendSkillPoints(cost);
			plugin.getToolManager().add(tool);
			tool.replaceCurrentItem(item);
			sender.sendMessage(ChatUtils.chat("&3Item registered"));
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
