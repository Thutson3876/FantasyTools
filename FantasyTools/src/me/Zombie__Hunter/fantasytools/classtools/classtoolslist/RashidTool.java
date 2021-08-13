package me.Zombie__Hunter.fantasytools.classtools.classtoolslist;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.classtools.ClassTools;
import me.Zombie__Hunter.fantasytools.traits.Trait;
import me.Zombie__Hunter.fantasytools.utils.Tools;

public class RashidTool extends AbstractClassTool {

private static final List<Class<? extends Trait>> traits = ClassTools.RASHID_TOOL.getDefaultTraits();
	
	public RashidTool(Player owner, ItemStack item) {
		super("Rashid Tool", owner, Tools.SWORD, item, traits);
	}
	
	public RashidTool(Player owner) {
		super("Rashid Tool", owner, Tools.SWORD, new ItemStack(Material.NETHERITE_SWORD), traits);
	}
	
	public RashidTool() {
		super(ClassTools.RASHID_TOOL);
	}

	@Override
	public String getDescription() {
		return "A nimble class that bounces around its target like the wind.";
	}
}
