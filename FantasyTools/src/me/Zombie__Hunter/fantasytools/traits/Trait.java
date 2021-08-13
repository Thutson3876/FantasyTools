package me.Zombie__Hunter.fantasytools.traits;

import org.bukkit.event.Event;
import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;

public interface Trait{
	
	AbstractClassTool getTool();
	
	public void init();
	
	public void deInit();
	
	boolean trigger(Event event);
	
	String getName();
	
	String getCommandName();
	
	String getDescription();
	
	String getActivation();
	
	void levelModifier(int level);

	void triggerCooldown();
	
	int getMaxCooldown();

	int getMaxLevel();
	
	int getCurrentLevel();
	
	void setCurrentLevel(int newLevel);
	
	int getSkillPointCost();
	
	void particles();
}
