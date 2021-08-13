package me.Zombie__Hunter.fantasytools.classtools.classtoolslist;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.classtools.ClassTools;
import me.Zombie__Hunter.fantasytools.traits.Trait;
import me.Zombie__Hunter.fantasytools.utils.Tools;

public class BrawlerTool extends AbstractClassTool {

private static final List<Class<? extends Trait>> traits = ClassTools.BRAWLER_TOOL.getDefaultTraits();
	
	public BrawlerTool(Player owner, ItemStack item) {
		super("Brawler Tool", owner, Tools.PICKAXE, item, traits);
	}
	
	public BrawlerTool(Player owner) {
		super("Brawler Tool", owner, Tools.PICKAXE, new ItemStack(Material.NETHERITE_PICKAXE), traits);
	}
	
	public BrawlerTool() {
		super(ClassTools.BRAWLER_TOOL);
	}
	
	public String getDescription() {
		return "A class thats great at pummeling people into pulp.";
	}
}
