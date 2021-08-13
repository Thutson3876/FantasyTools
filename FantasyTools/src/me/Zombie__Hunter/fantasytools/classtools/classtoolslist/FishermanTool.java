package me.Zombie__Hunter.fantasytools.classtools.classtoolslist;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.classtools.ClassTools;
import me.Zombie__Hunter.fantasytools.traits.Trait;
import me.Zombie__Hunter.fantasytools.utils.Tools;

public class FishermanTool extends AbstractClassTool {

	private static final List<Class<? extends Trait>> traits = ClassTools.FISHERMAN_TOOL.getDefaultTraits();
	
	public FishermanTool(Player owner, ItemStack item) {
		super("Fisherman Tool", owner, Tools.FISHING_ROD, item, traits);
	}
	
	public FishermanTool(Player owner) {
		super("Fisherman Tool", owner, Tools.FISHING_ROD, new ItemStack(Material.FISHING_ROD), traits);
	}
	
	public FishermanTool() {
		super(ClassTools.FISHERMAN_TOOL);
	}
	
	public String getDescription() {
		return "A class of simplicity based around providing plenty.";
	}
}
