package me.Zombie__Hunter.fantasytools.classtools;

import java.util.Set;

import me.Zombie__Hunter.fantasytools.traits.Trait;
import me.Zombie__Hunter.fantasytools.utils.Tools;

public interface ClassTool {
	Tools getToolType();
	
	Set<Trait> getTraits();
	
	Set<String> getLore();
	
	void setLore();
	
	
}
