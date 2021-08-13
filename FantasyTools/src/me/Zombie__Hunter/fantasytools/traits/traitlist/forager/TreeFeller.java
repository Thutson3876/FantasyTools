package me.Zombie__Hunter.fantasytools.traits.traitlist.forager;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class TreeFeller extends AbstractBasicTrait {

	private static final int MAXDISTANCE = 4;
	//private static final int MAXBLOCKCOUNT = 1200;
	
	private Block startingBlock = null;
	//private int currentBlockCount = 0;
	
	private static final Set<Material> logMaterials = new HashSet<>(Arrays.asList(new Material[] {Material.ACACIA_LOG, Material.BIRCH_LOG, Material.DARK_OAK_LOG, Material.JUNGLE_LOG,
																				Material.OAK_LOG, Material.SPRUCE_LOG}));	
	private static final Set<Material> leafMaterials = new HashSet<>(Arrays.asList(new Material[] {Material.ACACIA_LEAVES, Material.AZALEA_LEAVES, Material.BIRCH_LEAVES, Material.DARK_OAK_LEAVES,
																				Material.FLOWERING_AZALEA_LEAVES, Material.JUNGLE_LEAVES, Material.OAK_LEAVES, Material.SPRUCE_LEAVES}));
	public TreeFeller(AbstractClassTool tool) {
		super(tool);
		this.coolDowninTicks = 3 * 20;
		this.maximumLevel = 3;
		this.skillPointCost = 8;
	}

	@Override
	public boolean trigger(Event event) {
		if(!(event instanceof BlockBreakEvent)) {
			return false;
		}
		BlockBreakEvent e = (BlockBreakEvent)event;
		Player p = e.getPlayer();
		
		if(!logMaterials.contains(e.getBlock().getType())) {
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
		
		this.startingBlock = e.getBlock();
		//this.currentBlockCount = 0;
		breakTree(this.startingBlock);
		return true;
	}

	@Override
	public String getName() {
		return "Tree Feller";
	}

	@Override
	public String getDescription() {
		return "Your experience among the trees allows you to fell a tree in a single strike.";
	}

	@Override
	public String getActivation() {
		return "Chop a tree with your class tool";
	}

	@Override
	public void levelModifier(int level) {
		this.coolDowninTicks = (4 - level) * 20;
	}
	
	private void breakTree(Block tree){
		   /*if(this.currentBlockCount > MAXBLOCKCOUNT) {
			   return;
		   }*/
		   if(Math.abs(this.startingBlock.getX() - tree.getX()) > MAXDISTANCE || Math.abs(this.startingBlock.getZ() - tree.getZ()) > MAXDISTANCE) {
			   return;
		   }
		   
		   if(!logMaterials.contains(tree.getType()) && !leafMaterials.contains(tree.getType())) return;
		   tree.breakNaturally(this.tool.getItemStack());
		   Bukkit.getPluginManager().callEvent(new BlockBreakEvent(tree, this.tool.getOwner()));
		   for(BlockFace face : BlockFace.values()) {
			   breakTree(tree.getRelative(face));
			   breakTree(tree.getRelative(face).getRelative(BlockFace.UP));
			   //this.currentBlockCount++;
		   }  
	}
	
}
