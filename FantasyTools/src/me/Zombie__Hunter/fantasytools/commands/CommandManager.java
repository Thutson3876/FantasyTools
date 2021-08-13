package me.Zombie__Hunter.fantasytools.commands;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import me.Zombie__Hunter.fantasytools.FantasyTools;
import me.Zombie__Hunter.fantasytools.commands.commandexecutors.CommandExecutor_BlankTool;
import me.Zombie__Hunter.fantasytools.commands.commandexecutors.CommandExecutor_ClassInfo;
import me.Zombie__Hunter.fantasytools.commands.commandexecutors.CommandExecutor_ClearUnbreakables;
import me.Zombie__Hunter.fantasytools.commands.commandexecutors.CommandExecutor_CreateClassTool;
import me.Zombie__Hunter.fantasytools.commands.commandexecutors.CommandExecutor_GetSkillPoints;
import me.Zombie__Hunter.fantasytools.commands.commandexecutors.CommandExecutor_SetSkillPoints;
import me.Zombie__Hunter.fantasytools.commands.commandexecutors.CommandExecutor_TraitInfo;
import me.Zombie__Hunter.fantasytools.commands.commandexecutors.CommandExecutor_UnregisterAnyClassTools;
import me.Zombie__Hunter.fantasytools.commands.commandexecutors.CommandExecutor_UnregisterClassTool;
import me.Zombie__Hunter.fantasytools.commands.commandexecutors.CommandExecutor_LevelUpTrait;
import me.Zombie__Hunter.fantasytools.commands.commandexecutors.CommandExecutor_RegisterClassTool;

public class CommandManager {
	private List<AbstractCommand> commands = new LinkedList<>();
	
	public CommandManager() {
		commands.add(new CommandExecutor_CreateClassTool());
		commands.add(new CommandExecutor_SetSkillPoints());
		commands.add(new CommandExecutor_LevelUpTrait());
		commands.add(new CommandExecutor_RegisterClassTool());
		commands.add(new CommandExecutor_UnregisterClassTool());
		commands.add(new CommandExecutor_UnregisterAnyClassTools());
		commands.add(new CommandExecutor_GetSkillPoints());
		commands.add(new CommandExecutor_TraitInfo());
		commands.add(new CommandExecutor_ClassInfo());
		commands.add(new CommandExecutor_BlankTool());
		commands.add(new CommandExecutor_ClearUnbreakables());
		
		this.registerCommands();
	}
	
	
	public void registerCommands() {
		FantasyTools plugin = FantasyTools.getPlugin();
		for(AbstractCommand command : this.commands) {
			plugin.getCommand(command.getCommandName()).setDescription(command.getDescription());
			plugin.getCommand(command.getCommandName()).setAliases(Arrays.asList(command.getAliases()));
			plugin.getCommand(command.getCommandName()).setTabCompleter(command);
			plugin.getCommand(command.getCommandName()).setExecutor(command);
		}
	}
}
