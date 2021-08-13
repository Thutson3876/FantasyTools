package me.Zombie__Hunter.fantasytools.classtools.classtoolslist;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.classtools.ClassTools;
import me.Zombie__Hunter.fantasytools.traits.Trait;
import me.Zombie__Hunter.fantasytools.utils.Tools;

public class ArcherTool extends AbstractClassTool {
	
	private static final List<Class<? extends Trait>> traits = ClassTools.ARCHER_TOOL.getDefaultTraits();
	
	public ArcherTool(Player owner, ItemStack item) {
		super("Archer Tool", owner, Tools.BOW, item, traits);
	}
	
	public ArcherTool(Player owner) {
		super("Archer Tool", owner, Tools.BOW, new ItemStack(Material.BOW), traits);
	}
	
	public ArcherTool() {
		super(ClassTools.ARCHER_TOOL);
	}

	@Override
	public String getDescription() {
		return "A nimble and calculated class that prefers to fight from a range.";
	}
}
