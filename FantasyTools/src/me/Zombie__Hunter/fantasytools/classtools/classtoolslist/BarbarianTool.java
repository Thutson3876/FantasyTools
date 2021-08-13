package me.Zombie__Hunter.fantasytools.classtools.classtoolslist;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.classtools.ClassTools;
import me.Zombie__Hunter.fantasytools.traits.Trait;
import me.Zombie__Hunter.fantasytools.utils.Tools;

public class BarbarianTool extends AbstractClassTool {
	
	private static final List<Class<? extends Trait>> traits = ClassTools.BARBARIAN_TOOL.getDefaultTraits();
	
	public BarbarianTool(Player owner, ItemStack item) {
		super("Barbarian Tool", owner, Tools.AXE, item, traits);
	}
	
	public BarbarianTool(Player owner) {
		super("Barbarian Tool", owner, Tools.AXE, new ItemStack(Material.NETHERITE_AXE), traits);
	}
	
	public BarbarianTool() {
		super(ClassTools.BARBARIAN_TOOL);
	}
	
	public String getDescription() {
		return "A versatile class well suited for combat and protecting their allies.";
	}
}
