package me.Zombie__Hunter.fantasytools.traits.traitlist.miner;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class HotHands extends AbstractBasicTrait {

	private static final Map<Material, Material> SMELTABLES = smeltables();
	
	public HotHands(AbstractClassTool tool) {
		super(tool);
		this.skillPointCost = 12;
		this.maximumLevel = 1;
		this.coolDowninTicks = 0 * 20;
	}

	@Override
	public boolean trigger(Event event) {
		if(event instanceof BlockBreakEvent) {
			BlockBreakEvent e = (BlockBreakEvent)event;
			Player p = e.getPlayer();
			
			if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
				return false;
			}
			if(!p.getInventory().getItemInMainHand().equals(this.tool.getItemStack()) && !p.getInventory().getItemInOffHand().equals(this.tool.getItemStack())) {
				return false;
			}
			
			return smeltItem(e);
		}
		else if(event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)event;
			if(!(e.getDamager() instanceof Player)) {
				return false;
			}
			if(!(e.getEntity() instanceof LivingEntity)) {
				return false;
			}
			LivingEntity entity = (LivingEntity)e.getEntity();
			Player p = (Player)e.getDamager();
			
			if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
				return false;
			}
			if(!p.getInventory().getItemInMainHand().equals(this.tool.getItemStack()) && !p.getInventory().getItemInOffHand().equals(this.tool.getItemStack())) {
				return false;
			}
			
			return setTargetOnFire(entity);
		}
		
		return false;
	}

	@Override
	public String getName() {
		return "Hot Hands";
	}

	@Override
	public String getDescription() {
		return "Your tool irradiates so much heat that you smelt ores upon mining them and cause enemies you hit to catch fire.";
	}

	@Override
	public String getActivation() {
		return "Passive: when you break a smeltable block or hit an entity";
	}

	@Override
	public void levelModifier(int level) {}
	
	private boolean smeltItem(BlockBreakEvent e) {
		Block block = e.getBlock();
		Collection<ItemStack> drops = block.getDrops(this.tool.getItemStack(), this.tool.getOwner());
		if(drops == null || drops.isEmpty()) {
			return false;
		}
		
		List<ItemStack> toRemove = new LinkedList<>();
		
		for(ItemStack item : drops) {
			if(SMELTABLES.containsKey(item.getType())) {
				toRemove.add(item);
			}
		}
		if(toRemove.isEmpty()) {
			return false;
		}
		
		for(ItemStack item : toRemove) {
			drops.remove(item);
			drops.add(new ItemStack(SMELTABLES.get(item.getType()), item.getAmount()));
		}
		
		for(ItemStack item : drops) {
			block.getWorld().dropItemNaturally(block.getLocation(), item);
		}
		e.setDropItems(false);
		return true;
	}
	
	private boolean setTargetOnFire(LivingEntity entity) {
		entity.setFireTicks(4*20);
		return true;
	}
	
	private static Map<Material, Material> smeltables(){
		Map<Material, Material> materials = new HashMap<>();
		materials.put(Material.COBBLESTONE, Material.STONE);
		materials.put(Material.RAW_COPPER, Material.COPPER_INGOT);
		materials.put(Material.RAW_IRON, Material.IRON_INGOT);
		materials.put(Material.RAW_GOLD, Material.GOLD_INGOT);
		materials.put(Material.COBBLED_DEEPSLATE, Material.DEEPSLATE);
		materials.put(Material.GILDED_BLACKSTONE, Material.GOLD_INGOT);
		materials.put(Material.GOLD_NUGGET, Material.GOLD_INGOT);
		materials.put(Material.SAND, Material.GLASS);
		materials.put(Material.RED_SAND, Material.GLASS);
		materials.put(Material.SANDSTONE, Material.SMOOTH_SANDSTONE);
		materials.put(Material.RED_SANDSTONE, Material.SMOOTH_RED_SANDSTONE);
		materials.put(Material.BASALT, Material.SMOOTH_BASALT);
		materials.put(Material.NETHER_GOLD_ORE, Material.GOLD_INGOT);
		materials.put(Material.SANDSTONE, Material.SMOOTH_SANDSTONE);
		materials.put(Material.CLAY, Material.TERRACOTTA);
		materials.put(Material.CLAY_BALL, Material.TERRACOTTA);
		materials.put(Material.CACTUS, Material.GREEN_DYE);
		materials.put(Material.ACACIA_LOG, Material.CHARCOAL);
		materials.put(Material.BIRCH_LOG, Material.CHARCOAL);
		materials.put(Material.DARK_OAK_LOG, Material.CHARCOAL);
		materials.put(Material.JUNGLE_LOG, Material.CHARCOAL);
		materials.put(Material.OAK_LOG, Material.CHARCOAL);
		materials.put(Material.SPRUCE_LOG, Material.CHARCOAL);
		materials.put(Material.STRIPPED_ACACIA_LOG, Material.CHARCOAL);
		materials.put(Material.STRIPPED_BIRCH_LOG, Material.CHARCOAL);
		materials.put(Material.STRIPPED_DARK_OAK_LOG, Material.CHARCOAL);
		materials.put(Material.STRIPPED_JUNGLE_LOG, Material.CHARCOAL);
		materials.put(Material.STRIPPED_OAK_LOG, Material.CHARCOAL);
		materials.put(Material.STRIPPED_SPRUCE_LOG, Material.CHARCOAL);
		materials.put(Material.WET_SPONGE, Material.SPONGE);
		materials.put(Material.SEA_PICKLE, Material.LIME_DYE);
		
		return materials;
	}
}
