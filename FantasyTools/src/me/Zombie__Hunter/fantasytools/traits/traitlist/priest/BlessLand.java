package me.Zombie__Hunter.fantasytools.traits.traitlist.priest;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;
import me.Zombie__Hunter.fantasytools.utils.TraitUtils;

public class BlessLand extends AbstractBasicTrait {

	private List<Material> cropTypes = TraitUtils.getCropMaterials();
	
	public BlessLand(AbstractClassTool tool) {
		super(tool);
		this.coolDowninTicks = 1 * 20;
		this.maximumLevel = 1;
		this.skillPointCost = 15;
	}

	@Override
	public boolean trigger(Event event) {
		if(event instanceof BlockBreakEvent) {
			BlockBreakEvent e = (BlockBreakEvent)event;
			Block block = e.getBlock();
			BlockData data = block.getBlockData();
			Material type = block.getType();
			
			Player p = e.getPlayer();
			if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
				return false;
			}
			if(!p.getInventory().getItemInMainHand().equals(this.tool.getItemStack()) && !p.getInventory().getItemInOffHand().equals(this.tool.getItemStack())) {
				return false;
			}
			
			if(!(data instanceof Ageable)) {
				return false;
			}
			Ageable ageable = (Ageable)data;
			if(ageable.getAge() < ageable.getMaximumAge()) {
				return false;
			}
			
			for(Material mat : this.cropTypes) {
				if(type.equals(mat)) {
					
					BukkitRunnable task = new BukkitRunnable() {

						@Override
						public void run() {
							if(block.getType().equals(Material.AIR)) {
								block.setType(type);
							}
						}
						
					};
					task.runTaskLater(plugin, 5);
					break;
				}
			}
			return true;
		}
		
		return false;
	}

	@Override
	public String getName() {
		return "Bless Land";
	}

	@Override
	public String getDescription() {
		return "Your class tool automatically blesses any crops it comes into contact with allowing them to regrow automatically.";
	}

	@Override
	public String getActivation() {
		return "Passive: on breaking fully aged crops with your class tool";
	}

	@Override
	public void levelModifier(int level) {}
	
	/*private boolean registerLand(Block block1, Block block2) {
		if(block1 == null || block2 == null) {
			sendErrorMessage("Blocks were not selected");
			return false;
		}
		
		int y1 = block1.getY();
		
		if(y1 != block2.getY()) {
			sendErrorMessage("Selected blocks have differing Y values.");
			return false;
		}
		
		World world = block1.getWorld();
		
		if(!world.equals(block2.getWorld())) {
			sendErrorMessage("Selected blocks are in different worlds.");
			return false;
		}
				
		
		
		List<Location> selectedLocations = new LinkedList<>();
		int maxX = Math.max(block1.getX(), block2.getX());
        int maxZ = Math.max(block1.getZ(), block2.getZ());
       
        int minX = Math.min(block1.getX(), block2.getX());
        int minZ = Math.min(block1.getZ(), block2.getZ());
        
        int xLength = Math.abs(maxX - minX);
		int zLength = Math.abs(maxZ - minZ);
		
		if(xLength*zLength > this.maxArea) {
			sendErrorMessage("Selected area is too large.");
			return false;
		}
       
        for(int x = minX; x <= maxX; x++){
            for(int z = minZ; z <= maxZ; z++){
            	selectedLocations.add(new Location(world, x, y1, z));
            }
        }
		
		if(this.locations.containsAll(selectedLocations)) {
			this.locations.removeAll(selectedLocations);
			sendErrorMessage("&3Removed area between [" + block1.getX() + ", " + y1 + ", " + block1.getZ() + "] and [" + block2.getX() + ", " + y1 + ", " + block2.getZ() + "]");
			return true;
		}
		
		for(Location loc : selectedLocations) {
			if(!this.locations.contains(loc)) {
				this.locations.add(loc);
			}
		}
		sendErrorMessage("&3Added area between [" + block1.getX() + ", " + y1 + ", " + block1.getZ() + "] and [" + block2.getX() + ", " + y1 + ", " + block2.getZ() + "]");
		return true;
	}
	
	private void sendErrorMessage(String message) {
		if(this.tool.getOwner() == null) {
			return;
		}
		
		this.tool.getOwner().sendMessage(ChatUtils.chat("&4" + message));
	}*/
}
