package me.Zombie__Hunter.fantasytools.classtools.classtoolslist;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.classtools.ClassTools;
import me.Zombie__Hunter.fantasytools.traits.Trait;
import me.Zombie__Hunter.fantasytools.utils.Tools;

public class MinerTool extends AbstractClassTool {

	private static final List<Class<? extends Trait>> traits = ClassTools.MINER_TOOL.getDefaultTraits();
	
	public MinerTool(Player owner, ItemStack item) {
		super("Miner Tool", owner, Tools.PICKAXE, item, traits);
	}

	public MinerTool(Player owner) {
		super("Miner Tool", owner, Tools.PICKAXE, new ItemStack(Material.NETHERITE_PICKAXE), traits);
	}
	
	public MinerTool() {
		super(ClassTools.MINER_TOOL);
	}
	
	@Override
	public String getDescription() {
		return "A class of depth divers that prefer the company of feldspar over friends.";
	}
}
