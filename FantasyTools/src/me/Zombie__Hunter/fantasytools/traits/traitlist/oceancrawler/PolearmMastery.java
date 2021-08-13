package me.Zombie__Hunter.fantasytools.traits.traitlist.oceancrawler;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerRiptideEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class PolearmMastery extends AbstractBasicTrait {

	private int taskCounter = 0;
	private int ticksPerTick = 8;
	private int taskDuration = 4;
	private double lightningRange = 3.0;
	
	private double velocityMod = 1.1;
	
	public PolearmMastery(AbstractClassTool tool) {
		super(tool);
		this.coolDowninTicks = 5 * 20;
		this.maximumLevel = 3;
		this.skillPointCost = 8;
	}

	@Override
	public boolean trigger(Event event) {
		if(event instanceof PlayerRiptideEvent) {
			PlayerRiptideEvent e = (PlayerRiptideEvent)event;
			Player p = e.getPlayer();
			
			if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
				return false;
			}
			if(this.isOnCooldown()) {
				return false;
			}
			
			lightningAura(p);
			return true;
		}
		else if(event instanceof ProjectileLaunchEvent) {
			ProjectileLaunchEvent e = (ProjectileLaunchEvent)event;
			if(!e.getEntityType().equals(EntityType.TRIDENT)) {
				return false;
			}
			if(!e.getEntity().getShooter().equals(this.tool.getOwner())) {
				return false;
			}
			Projectile proj = e.getEntity();
			Player p = (Player)e.getEntity().getShooter();
			
			proj.setVelocity(proj.getVelocity().multiply(this.velocityMod));
			
			if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
				return false;
			}
			if(this.isOnCooldown()) {
				return false;
			}
			
			rideTrident(p, proj);
			return true;
		}
		else if(event instanceof EntityDamageEvent) {
			EntityDamageEvent e = (EntityDamageEvent)event;
			if(!e.getCause().equals(DamageCause.LIGHTNING)) {
				return false;
			}
			if(!e.getEntity().equals(this.tool.getOwner())) {
				return false;
			}
			Player p = (Player)e.getEntity();
			if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
				return false;
			}
			
			plugin.setCurrentEventCancelled(true);
			return false;
		}
		return false;
	}

	@Override
	public String getName() {
		return "Polearm Mastery";
	}

	@Override
	public String getDescription() {
		return "Your experience with your weapon makes even your most unique abilities seem even more mystical to the untrained eye.";
	}

	@Override
	public String getActivation() {
		return "Passive: on riptide or trident throw";
	}

	@Override
	public void levelModifier(int level) {
		this.lightningRange = 2.0 + level;
		this.velocityMod = 1.0 + (0.1 * level);
	}
	
	private void rideTrident(Player p, Projectile trident) {
		trident.addPassenger(p);
	}
	
	private void lightningAura(Player p) {
		this.taskCounter = 0;
		BukkitRunnable task = new BukkitRunnable() {

			@Override
			public void run() {
				if(taskCounter > taskDuration) {
					this.cancel();
					return;
				}
				
				for(Entity e : p.getNearbyEntities(lightningRange, lightningRange, lightningRange)) {
					if(p.hasLineOfSight(e)) {
						e.getWorld().strikeLightning(e.getLocation());
					}
				}
				taskCounter++;
			}
			
		};
		
		task.runTaskTimer(plugin, this.ticksPerTick, this.ticksPerTick);
	}
}
