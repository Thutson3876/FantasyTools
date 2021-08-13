package me.Zombie__Hunter.fantasytools.classtools.classtoolslist;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.classtools.ClassTools;
import me.Zombie__Hunter.fantasytools.utils.Tools;

public class BlankTool extends AbstractClassTool {
	
	public BlankTool(Player owner) {
		super("Blank Tool", owner, Tools.STICK, new ItemStack(Material.STICK), null);
	}
	
	public BlankTool(Player owner, ItemStack item) {
		super("Blank Tool", owner, Tools.STICK, item, null);
	}
	
	public BlankTool() {
		super(ClassTools.BLANK_TOOL);
	}

	@Override
	public String getDescription() {
		return "A template of a class tool that can hold any traits.";
	}
}
