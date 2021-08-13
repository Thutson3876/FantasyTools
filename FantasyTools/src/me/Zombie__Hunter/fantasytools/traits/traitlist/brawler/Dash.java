package me.Zombie__Hunter.fantasytools.traits.traitlist.brawler;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class Dash extends AbstractBasicTrait {

	private double range = 50;
	private int hitWindow = 20;
	private PotionEffect haste;
	private Vector lastMove = null;
	
	public Dash(AbstractClassTool tool) {
		super(tool);
		this.skillPointCost = 8;
		this.maximumLevel = 3;
		this.coolDowninTicks = 5 * 20;
	}

	@Override
	public boolean trigger(Event event) {
		if(event instanceof PlayerInteractEvent) {
			PlayerInteractEvent e = (PlayerInteractEvent)event;
			Player p = e.getPlayer();
			if(!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
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
			
			executeDash(p);
			return true;
		}
		else if(event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)event;
			reset(e);
		}
		else if(event instanceof ProjectileHitEvent) {
			ProjectileHitEvent e = (ProjectileHitEvent)event;
			Block block = e.getHitBlock();
			if(block == null) {
				return false;
			}
			if(!(e.getEntity().getShooter() instanceof Player)) {
				return false;
			}
			
			Player p = (Player)e.getEntity().getShooter();
			
			if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
				return false;
			}
			
			if(this.isOnCooldown()) {
				return false;
			}
			
			return teleport(p, block.getRelative(e.getHitBlockFace()).getLocation());
		}
		else if(event instanceof PlayerMoveEvent) {
			PlayerMoveEvent e = (PlayerMoveEvent)event;
			this.lastMove = e.getFrom().add(e.getTo()).getDirection().setY(0.0).multiply(0.5).normalize().setY(0.2);
		}
		return false;
	}

	@Override
	public String getName() {
		return "Dash";
	}

	@Override
	public String getDescription() {
		return "You dash forward and gain a short haste buff. If you hit an enemy while it is active, this ability's cooldown is reset. "
				+ "Firing an arrow will cause you to teleport to the block it lands in if this is off cooldown.";
	}

	@Override
	public String getActivation() {
		return "Right click with your class tool in hand";
	}

	@Override
	public void levelModifier(int level) {
		this.coolDowninTicks = (6 - level) * 20;
		this.hitWindow = 20 + (6 * level);
		this.haste = new PotionEffect(PotionEffectType.FAST_DIGGING, this.hitWindow, 1);
	}
	
	private boolean reset(EntityDamageByEntityEvent e) {
		if(!(e.getDamager() instanceof Player)) {
			return false;
		}
		if(!(e.getEntity() instanceof LivingEntity)) {
			return false;
		}
		Player p = (Player)e.getDamager();
		
		if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
			return false;
		}
		
		if(!p.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
			return false;
		}
		
		plugin.getCooldownManager().setCooldown(p, this, 0);
		return false;
	}
	
	private void executeDash(Player p) {
		Vector velocity = this.lastMove;
		if(velocity == null) {
			velocity = p.getLocation().getDirection().setY(0);
			velocity.multiply(0.5);
			velocity.normalize().setY(0.2);
		}
		
		p.setVelocity(velocity);
		p.addPotionEffect(this.haste);
	}
	
	private boolean teleport(Player p, Location loc) {
		if(p.getLocation().distance(loc) > this.range) {
			return false;
		}
		p.teleport(loc);
		p.addPotionEffect(this.haste);
		return true;
	}
}
