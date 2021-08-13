package me.Zombie__Hunter.fantasytools.traits.traitlist.forager;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;
import me.Zombie__Hunter.fantasytools.utils.ChatUtils;

public class HuntingTrap extends AbstractBasicTrait {

	private Material trapType = Material.JUNGLE_PRESSURE_PLATE;
	private int tickDuration = 5 * 20;
	private int amp = 2;
	private double dmg = 10.0;
	private Block trap = null;
	
	public HuntingTrap(AbstractClassTool tool) {
		super(tool);
		this.skillPointCost = 6;
		this.maximumLevel = 3;
		this.coolDowninTicks = 16 * 20;
	}

	@Override
	public boolean trigger(Event event) {
		if(event instanceof PlayerInteractEvent) {
			PlayerInteractEvent e = (PlayerInteractEvent)event;
			Player p = e.getPlayer();
			Action action = e.getAction();
			
			if(action.equals(Action.PHYSICAL)) {
				if(e.getClickedBlock().equals(this.trap)) {
					trapTriggered(e.getPlayer());
					return false;
				}
			}
			
			if(!p.isSneaking()) {
				return false;
			}
			
			if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
				return false;
			}
			
			if(!p.getInventory().getItemInMainHand().equals(this.tool.getItemStack()) && !p.getInventory().getItemInOffHand().equals(this.tool.getItemStack())) {
				return false;
			}
			
			if(this.isOnCooldown()) {
				return false;
			}
			
			if(action.equals(Action.LEFT_CLICK_BLOCK)) {
				if(this.trap != null) {
					this.trap.getLocation().getBlock().setType(Material.AIR);
				}
				
				Block block = e.getClickedBlock();
				BlockFace face = e.getBlockFace();
				if(block.getRelative(face).getType().equals(Material.AIR)) {
					if(!block.getRelative(face).getRelative(BlockFace.DOWN).isPassable()) {
						
						spawnTrap(block.getRelative(face, 1));
						return true;
					}
				}
			}
		}
		else if(event instanceof EntityInteractEvent) {
			EntityInteractEvent e = (EntityInteractEvent)event;
			if(e.getBlock().equals(this.trap)) {
				trapTriggered(e.getEntity());
			}
		}
		
		return false;
	}

	@Override
	public String getName() {
		return "Hunting Trap";
	}

	@Override
	public String getDescription() {
		return "Set a trap for an unsuspecting victim to step into.";
	}

	@Override
	public String getActivation() {
		return "Left click a block while crouching to set the trap. It dissapears when another trap is placed.";
	}

	@Override
	public void levelModifier(int level) {
		this.tickDuration = (4 + level) * 20;
		this.amp = 1 + level;
		this.dmg = 9.0 + (1.0 * level);
	}
	
	private void spawnTrap(Block b) {
		b.setType(this.trapType);
		this.trap = b;
	}
	
	private void trapTriggered(Entity e) {
		if(this.trap == null) {
			return;
		}
		this.trap.getLocation().getBlock().setType(Material.AIR);
		this.trap = null;
		this.tool.getOwner().sendMessage(ChatUtils.chat("&6Trap Triggered!"));
		if(!(e instanceof LivingEntity)) {
			return;
		}
		
		LivingEntity entity = (LivingEntity)e;
		entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, this.tickDuration, this.amp));
		entity.damage(this.dmg, this.tool.getOwner());
		entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, this.tickDuration, this.amp));
	}
}
