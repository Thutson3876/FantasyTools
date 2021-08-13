package me.Zombie__Hunter.fantasytools.classtools.classtoolslist;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.classtools.ClassTools;
import me.Zombie__Hunter.fantasytools.traits.Trait;
import me.Zombie__Hunter.fantasytools.utils.Tools;

public class RogueTool extends AbstractClassTool {

private static final List<Class<? extends Trait>> traits = ClassTools.ROGUE_TOOL.getDefaultTraits();
	
	public RogueTool(Player owner, ItemStack item) {
		super("Rogue Tool", owner, Tools.SWORD, item, traits);
	}
	
	public RogueTool(Player owner) {
		super("Rogue Tool", owner, Tools.SWORD, new ItemStack(Material.NETHERITE_SWORD), traits);
	}
	
	public RogueTool() {
		super(ClassTools.ROGUE_TOOL);
	}
	
	public String getDescription() {
		return "A sly class based around dealing heavy amounts of damage and escaping quickly thus-after.";
	}
}
