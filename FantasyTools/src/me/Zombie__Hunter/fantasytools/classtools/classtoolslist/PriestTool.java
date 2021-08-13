package me.Zombie__Hunter.fantasytools.classtools.classtoolslist;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.classtools.ClassTools;
import me.Zombie__Hunter.fantasytools.traits.Trait;
import me.Zombie__Hunter.fantasytools.utils.Tools;

public class PriestTool extends AbstractClassTool {
	private static final List<Class<? extends Trait>> traits = ClassTools.PRIEST_TOOL.getDefaultTraits();
	
	public PriestTool(Player owner, ItemStack item) {
		super("Priest Tool", owner, Tools.SHOVEL, item, traits);
	}
	
	public PriestTool(Player owner) {
		super("Priest Tool", owner, Tools.SHOVEL, new ItemStack(Material.NETHERITE_SHOVEL), traits);
	}
	
	public PriestTool() {
		super(ClassTools.PRIEST_TOOL);
	}
	
	public String getDescription() {
		return "A passive peace-maker focused on growth and prosperity.";
	}
}
