package me.Zombie__Hunter.fantasytools.classtools;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;

import me.Zombie__Hunter.fantasytools.FantasyTools;
import me.Zombie__Hunter.fantasytools.playermanagement.FantasyPlayer;
import me.Zombie__Hunter.fantasytools.traits.Trait;

public class ClassToolManager {
	private Set<AbstractClassTool> classTools;
	
	public ClassToolManager() {
		this.classTools = new HashSet<>();
	}
	
	public boolean add(AbstractClassTool tool) {
		if(tool == null) {
			return false;
		}
		
		this.classTools.add(tool);
		//FantasyTools.getPlugin().getParticleManager().add(tool.getTraits());
		FantasyTools.getPlugin().log(tool.getName() + ", owned by " + tool.getOwner() + ", has been added to the manager.");
		return true;
	}
	
	public void remove(AbstractClassTool tool) {
		refundSkillPoints(tool);
		
		tool.deInit();
		tool.replaceCurrentWithDisabled(tool.getItemStack());
		//FantasyTools.getPlugin().getParticleManager().remove(tool.getTraits());
		this.classTools.remove(tool);
	}
	
	private static void refundSkillPoints(AbstractClassTool tool) {
		FantasyPlayer player = FantasyTools.getPlugin().getPlayerManager().getPlayer(tool.getOwner());
		for(Map.Entry<Trait, Integer> entry: tool.getSkillMap().entrySet()) {
			player.addSkillPoints(entry.getValue() * entry.getKey().getSkillPointCost());
		}
	}
	
	public boolean contains(AbstractClassTool tool) {
		return this.classTools.contains(tool);
	}
	
	public Set<AbstractClassTool> getRegisteredTools(){
		return this.classTools;
	}
	
	public AbstractClassTool getTool(Player p) {
		for(AbstractClassTool tool : this.classTools) {
			if(tool.getOwner().equals(p)) {
				return tool;
			}
		}
		return null;
	}

	public void deInit() {
		for(AbstractClassTool tool : classTools) {
			tool.deInit();
		}
	}
}
