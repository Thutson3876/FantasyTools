package me.Zombie__Hunter.fantasytools.traits.traitlist.miner;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;
import me.Zombie__Hunter.fantasytools.utils.Cuboid;

public class Excavation extends AbstractBasicTrait {

	private static final List<Material> breakableMaterials = getMaterialList();
	
	public Excavation(AbstractClassTool tool) {
		super(tool);
		this.skillPointCost = 7;
		this.maximumLevel = 5;
		this.coolDowninTicks = 10 * 20;
	}

	@Override
	public boolean trigger(Event event) {
		if(!(event instanceof BlockBreakEvent)) {
			return false;
		}
		BlockBreakEvent e = (BlockBreakEvent)event;
		Player p = e.getPlayer();
		
		if(!p.isSneaking()) {
			return false;
		}
		
		if(!breakableMaterials.contains(e.getBlock().getType())) {
			return false;
		}
		
		if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
			return false;
		}
		
		if(this.isOnCooldown()) {
			return false;
		}
		
		if(!p.getInventory().getItemInMainHand().equals(this.tool.getItemStack()) && !p.getInventory().getItemInOffHand().equals(this.tool.getItemStack())) {
			return false;
		}
		
		return breakArea(e);
	}

	@Override
	public String getName() {
		return "Excavation";
	}

	@Override
	public String getDescription() {
		return "Dig with impressive strength and carve out a 3x3 cube in stone.";
	}

	@Override
	public String getActivation() {
		return "Dig stone while crouching.";
	}

	@Override
	public void levelModifier(int level) {
		this.coolDowninTicks = ((10 - (2 * level)) * 20) + 4;
	}
	
	private boolean breakArea(BlockBreakEvent e) {
		Block broken = e.getBlock();
		boolean wrongType = false;
		for(Material mat : breakableMaterials) {
			if(broken.getType().equals(mat)) {
				break;
			}
		}
		if(wrongType) {
			return false;
		}
		
		List<Block> surroundingBlocks = new LinkedList<>();
		Cuboid cube = Cuboid.createFromLocationRadius(broken.getLocation(), 1);
		
		for(Block block : cube) {
			if(breakableMaterials.contains(block.getType())) {
				surroundingBlocks.add(block);
			}
		}
		if(surroundingBlocks.isEmpty()) {
			return false;
		}
		
		for(Block b : surroundingBlocks) {
			b.breakNaturally(this.tool.getItemStack());
		}
		
		return true;
	}
	
	private static List<Material> getMaterialList(){
		List<Material> materials = new LinkedList<>();
		materials.add(Material.BLACKSTONE);
		materials.add(Material.COBBLESTONE);
		materials.add(Material.END_STONE);
		materials.add(Material.MOSSY_COBBLESTONE);
		materials.add(Material.SANDSTONE);
		materials.add(Material.STONE);
		materials.add(Material.NETHERRACK);
		materials.add(Material.BASALT);
		materials.add(Material.DIORITE);
		materials.add(Material.GRANITE);
		materials.add(Material.ANDESITE);
		materials.add(Material.COBBLED_DEEPSLATE);
		materials.add(Material.DEEPSLATE);
		
		return materials;
	}
}
