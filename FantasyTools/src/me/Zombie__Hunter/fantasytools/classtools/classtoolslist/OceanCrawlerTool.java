package me.Zombie__Hunter.fantasytools.classtools.classtoolslist;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.classtools.ClassTools;
import me.Zombie__Hunter.fantasytools.traits.Trait;
import me.Zombie__Hunter.fantasytools.utils.Tools;

public class OceanCrawlerTool extends AbstractClassTool {

private static final List<Class<? extends Trait>> traits = ClassTools.OCEANCRAWLER_TOOL.getDefaultTraits();
	
	public OceanCrawlerTool(Player owner, ItemStack item) {
		super("Ocean Crawler Tool", owner, Tools.TRIDENT, item, traits);
	}
	
	public OceanCrawlerTool(Player owner) {
		super("Ocean Crawler Tool", owner, Tools.TRIDENT, new ItemStack(Material.TRIDENT), traits);
	}
	
	public OceanCrawlerTool() {
		super(ClassTools.OCEANCRAWLER_TOOL);
	}
	
	public String getDescription() {
		return "A delver of the deep ocean that thrives in the water.";
	}
}
