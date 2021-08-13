package me.Zombie__Hunter.fantasytools.classtools.classtoolslist;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.classtools.ClassTools;
import me.Zombie__Hunter.fantasytools.traits.Trait;
import me.Zombie__Hunter.fantasytools.utils.Tools;

public class ForagerTool extends AbstractClassTool {

private static final List<Class<? extends Trait>> traits = ClassTools.FORAGER_TOOL.getDefaultTraits();
	
	public ForagerTool(Player owner, ItemStack item) {
		super("Forager Tool", owner, Tools.HOE, item, traits);
	}
	
	public ForagerTool(Player owner) {
		super("Forager Tool", owner, Tools.HOE, new ItemStack(Material.NETHERITE_HOE), traits);
	}
	
	public ForagerTool() {
		super(ClassTools.FORAGER_TOOL);
	}

	@Override
	public String getDescription() {
		return "A survivalist at its core, a forager knows how to survive off the land with ease.";
	}
}
