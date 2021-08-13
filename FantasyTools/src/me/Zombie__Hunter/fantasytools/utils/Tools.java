package me.Zombie__Hunter.fantasytools.utils;

import org.bukkit.Material;

public enum Tools {
	AXE(new Material[] {Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE}),
	HOE(new Material[] {Material.WOODEN_HOE, Material.STONE_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.DIAMOND_HOE, Material.NETHERITE_HOE}),
	PICKAXE(new Material[] {Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE}),
	SHOVEL(new Material[] {Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.DIAMOND_SHOVEL, Material.NETHERITE_SHOVEL}),
	SWORD(new Material[] {Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD}),
	BOW(new Material[] {Material.BOW, Material.CROSSBOW}),
	FISHING_ROD(new Material[] {Material.FISHING_ROD}),
	TRIDENT(new Material[] {Material.TRIDENT}),
	STICK(new Material[] {Material.STICK, Material.BLAZE_ROD});
	
	private Material[] mats;
	
	private Tools(Material[] mats) {
		this.setMats(mats);
	}

	public Material[] getMats() {
		return mats;
	}

	public void setMats(Material[] mats) {
		this.mats = mats;
	}
}
