package me.Zombie__Hunter.fantasytools.commands.commandexecutors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.Zombie__Hunter.fantasytools.FantasyTools;
import me.Zombie__Hunter.fantasytools.commands.AbstractCommand;
import me.Zombie__Hunter.fantasytools.playermanagement.FantasyPlayer;
import me.Zombie__Hunter.fantasytools.utils.ChatUtils;

public class CommandExecutor_GetSkillPoints extends AbstractCommand implements Listener {
	public CommandExecutor_GetSkillPoints() {
		super("skillpoints", new String[] {"getskillpoints"});
	}

	@Override
	protected boolean onInternalCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length == 0 && sender instanceof Player) {
			Player player = (Player)sender;
			FantasyPlayer fPlayer = FantasyTools.getPlugin().getPlayerManager().getPlayer(player);
			if(fPlayer == null) {
				sender.sendMessage(ChatUtils.chat("&4You don't have any skillpoints as you aren't registered."));
				return false;
			}
			
			sender.sendMessage(ChatUtils.chat("&3Your skillpoint amount is currently " + fPlayer.getSkillPoints()));
			return true;
		}
		
		return false;
	}
}
