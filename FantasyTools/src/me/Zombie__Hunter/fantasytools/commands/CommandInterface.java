package me.Zombie__Hunter.fantasytools.commands;

import org.bukkit.command.TabExecutor;

public interface CommandInterface extends TabExecutor {
	  String getCommandName();
	  
	  String[] getAliases();
	  
	  boolean hasAliases();
	  
	  String getDescription();
}
