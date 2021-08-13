package me.Zombie__Hunter.fantasytools.classtools.classtoolslist;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.classtools.ClassTools;
import me.Zombie__Hunter.fantasytools.traits.Trait;
import me.Zombie__Hunter.fantasytools.utils.Tools;

public class SindoreiTool extends AbstractClassTool {
	
	private static final List<Class<? extends Trait>> traits = ClassTools.SINDOREI_TOOL.getDefaultTraits();
	
	public SindoreiTool(Player owner, ItemStack item) {
		super("Sindorei Tool", owner, Tools.SWORD, item, traits);
	}

	public SindoreiTool(Player owner) {
		super("Sindorei Tool", owner, Tools.SWORD, new ItemStack(Material.NETHERITE_SWORD), traits);
	}
	
	public SindoreiTool() {
		super(ClassTools.SINDOREI_TOOL);
	}
	
	@Override
	public String getDescription() {
		return "A class of crimson crusaders who see great value in life; especially in manipulating it.";
	}
	
}
