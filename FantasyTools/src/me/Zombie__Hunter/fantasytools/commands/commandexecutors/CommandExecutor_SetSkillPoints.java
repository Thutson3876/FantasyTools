package me.Zombie__Hunter.fantasytools.commands.commandexecutors;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.Zombie__Hunter.fantasytools.FantasyTools;
import me.Zombie__Hunter.fantasytools.commands.AbstractCommand;
import me.Zombie__Hunter.fantasytools.playermanagement.FantasyPlayer;
import me.Zombie__Hunter.fantasytools.utils.ChatUtils;

public class CommandExecutor_SetSkillPoints extends AbstractCommand implements Listener {

	public CommandExecutor_SetSkillPoints() {
		super("setskillpoints", new String[] {"ssp"});
		//FantasyTools.getPlugin().getCommand("createclasstool").setExecutor(this);
	}

	@Override
	protected boolean onInternalCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player) && !sender.isOp()) {
			sender.sendMessage(ChatUtils.chat("&4Must be an opped player to use this command."));
			return true;
		}
		
		if(args.length == 1) {
			try {
				int amt = Integer.parseInt(args[0]);
				Player player = (Player)sender;
				FantasyPlayer fantasyPlayer = FantasyTools.getPlugin().getPlayerManager().getPlayer(player);
				if(fantasyPlayer == null) {
					sender.sendMessage(ChatUtils.chat("&4You have not been registered."));
					return false;
				}
				fantasyPlayer.setSkillPoints(amt);
				
				sender.sendMessage(ChatUtils.chat("&3Your skillpoint amount has been set to " + amt));
				return true;
			} catch(NumberFormatException e) {
				sender.sendMessage(ChatUtils.chat("&4Incorrect Input. Try: /" + this.getCommandName() + " <amt>"));
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
		    	values.add("<amt>");
		    }
		        
		    return values;
	}
}
