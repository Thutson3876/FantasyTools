package me.Zombie__Hunter.fantasytools.traits.traitlist.sindorei;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;
import me.Zombie__Hunter.fantasytools.utils.TraitUtils;

public class BloodBolt extends AbstractBasicTrait {

	private double velocityMod = 3.75;
	private double healthCost = 4.0;
	private double damage = 9.0;
	private Arrow arrow = null;
	private EntityDamageEvent event;
	
	public BloodBolt(AbstractClassTool tool) {
		super(tool);
		this.coolDowninTicks = 8 * 20;
		this.maximumLevel = 3;
		this.skillPointCost = 5;
	}
	
	@Override
	public boolean trigger(Event event) {
		if(event instanceof PlayerInteractEvent) {
			PlayerInteractEvent e = (PlayerInteractEvent) event;
			if(!(e.getAction().equals(Action.RIGHT_CLICK_AIR))) {
				return false;
			}
			
			Player p = e.getPlayer();
			
			if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
				return false;
			}
			if(!p.getInventory().getItemInMainHand().equals(this.tool.getItemStack()) && !p.getInventory().getItemInOffHand().equals(this.tool.getItemStack())) {
				return false;
			}
			
			if(this.isOnCooldown()) {
				return false;
			}
			
			fireBloodBolt(p);
			return true;
		}
		else if(event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
			if(!(e.getDamager() instanceof Arrow)) {
				return false;
			}
			Arrow projectile = (Arrow) e.getDamager();
			Player p = this.tool.getOwner();
			
			if(!p.equals(projectile.getShooter())) {
				return false;
			}
			if(!projectile.equals(this.arrow)) {
				return false;
			}
			//Location ownerLoc = this.tool.getOwner().getLocation();
			Location targetLoc = e.getEntity().getLocation();
			double distance = this.tool.getOwner().getLocation().distance(e.getEntity().getLocation());
			//double bonusY = targetLoc.getY() - ownerLoc.getY();
			
			TraitUtils.moveToward(this.tool.getOwner(), targetLoc, distance);
			TraitUtils.heal(p, this.healthCost + 2.0);
			
			return false;
		}
		
		return false;
	}
	@Override
	public String getName() {
		return "Blood Bolt";
	}
	@Override
	public String getDescription() {
		return "Fire a bolt of coagulated blood at your target, piercing them and pulling you towards them if it hits.";
	}
	
	@Override
	public String getActivation() {
		return "Right click while holding your class tool.";
	}
	
	@Override
	public void levelModifier(int level) {
		this.damage = 6.0 + (3.0 * level);
		this.velocityMod = 3.5 + (0.25 * level);
		event = new EntityDamageEvent(this.tool.getOwner(), DamageCause.CUSTOM, this.healthCost);
	}
	
	private void fireBloodBolt(Player p) {
		Vector velocity = p.getEyeLocation().getDirection().multiply(this.velocityMod);
		Location spawnAt = p.getEyeLocation().toVector().add(p.getEyeLocation().getDirection()).toLocation(p.getWorld());
		this.arrow = p.getWorld().spawnArrow(spawnAt, velocity, 1F, 1F);
		this.arrow.setShooter(p);
		this.arrow.setDamage(this.damage);
		this.arrow.setPickupStatus(PickupStatus.DISALLOWED);
		
		double newHealth = p.getHealth() - this.healthCost;
		if(newHealth <= 0) {
			newHealth = 0;
		}
		
		p.setHealth(newHealth);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}
	
	public void particles() {
		if(this.arrow == null) {
			return;
		}
		if(this.arrow.isInBlock()) {
			return;
		}
		if(this.arrow.isDead()) {
			return;
		}
		
		particleTrail(this.arrow);
	}
	
	private void particleTrail(Arrow arrow) {
		arrow.getWorld().spawnParticle(Particle.DRIP_LAVA, arrow.getLocation(), 1);
	}
}
