package me.Zombie__Hunter.fantasytools.traits.traitlist.archer;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class ExplosiveArrow extends AbstractBasicTrait {

	private float power = 1.0F;
	private boolean isOn = false;
	private BossBar bar;
	private Projectile projectile = null;
	
	public ExplosiveArrow(AbstractClassTool tool) {
		super(tool);
		this.skillPointCost = 6;
		this.maximumLevel = 3;
		this.coolDowninTicks = 8 * 20;
		
		if(this.tool != null) {
			if(tool.getOwner() != null) {
				bar = Bukkit.createBossBar(this.getName(), BarColor.RED, BarStyle.SEGMENTED_20, new org.bukkit.boss.BarFlag[0]);
				bar.addPlayer(this.tool.getOwner());
				bar.setVisible(false);
			}
		}
	}

	@Override
	public boolean trigger(Event event) {
		if(event instanceof PlayerToggleSneakEvent) {
			PlayerToggleSneakEvent e = (PlayerToggleSneakEvent)event;
			Player p = e.getPlayer();
			
			if(p.isSneaking()) {
				return false;
			}
			
			if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
				return false;
			}
			
			if(this.isOnCooldown()) {
				return false;
			}
			
			if(this.isOn) {
				this.isOn = false;
				bar.setVisible(false);
			}
			else {
				this.isOn = true;
				bar.setVisible(true);
			}
			return false;
		}
		else if(event instanceof EntityShootBowEvent) {
			EntityShootBowEvent e = (EntityShootBowEvent)event;
			if(!(e.getEntity() instanceof Player)) {
				return false;
			}
			Player p = (Player) e.getEntity();
			
			if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
				return false;
			}
			
			if(this.isOnCooldown()) {
				return false;
			}
			
			if(!this.isOn) {
				return false;
			}
			
			this.isOn = false;
			this.bar.setVisible(false);
			e.setConsumeItem(false);
			this.projectile = (Projectile) e.getProjectile();
			return true;
		}
		else if(event instanceof ProjectileHitEvent) {
			if(this.projectile == null) {
				return false;
			}
			ProjectileHitEvent e = (ProjectileHitEvent) event;
			Projectile eventProjectile = e.getEntity();
			if(!this.projectile.equals(eventProjectile)) {
				return false;
			}
			
			eventProjectile.getWorld().createExplosion(eventProjectile.getLocation(), this.power);
			//this.projectile = null;
			return false;
		}
		
		return false;
	}

	@Override
	public String getName() {
		return "Explosive Arrow";
	}

	@Override
	public String getDescription() {
		return "You release an arrow that destroys anything it impacts.";
	}

	@Override
	public String getActivation() {
		return "Crouch and fire before the activation timer runs out.";
	}
	
	@Override
	public void levelModifier(int level) {
		this.power = 0.75F + (0.25F * level);
	}
	
	public boolean isOn() {
		return isOn;
	}

	public void setOn(boolean isOn) {
		this.isOn = isOn;
	}
}
