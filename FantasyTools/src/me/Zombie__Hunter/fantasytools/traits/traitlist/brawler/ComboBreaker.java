package me.Zombie__Hunter.fantasytools.traits.traitlist.brawler;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import me.Zombie__Hunter.fantasytools.FantasyTools;
import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class ComboBreaker extends AbstractBasicTrait {

	private int duration = 2 * 20;
	private boolean isOn = false;
	private int tickRate = 1;
	private Window window;
	private int taskID;
	Player p1 = null;
	Entity p2 = null;
	
	public ComboBreaker(AbstractClassTool tool) {
		super(tool);
		this.skillPointCost = 8;
		this.maximumLevel = 3;
		this.coolDowninTicks = 12 * 20;
		
		if(tool != null) {
			this.window = new Window(tool.getOwner(), this.duration, this);
		}
	}

	@Override
	public boolean trigger(Event event) {
		if(event instanceof EntityDamageEvent) {
			EntityDamageEvent e = (EntityDamageEvent)event;
			if(e.getCause().equals(DamageCause.FALL)) {
				if(e.getEntity().equals(this.p1)) {
					e.setDamage(0.0);
					this.p1 = null;
				}
				else if(e.getEntity().equals(this.p2)) {
					e.setDamage(0.0);
					this.p2 = null;
				}
			}
		}
		if(event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)event;
			if(!(e.getDamager() instanceof Player)) {
				return false;
			}
			
			Player p = (Player)e.getDamager();
			
			if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
				return false;
			}
			if(isOnCooldown()) {
				if(this.isOn) {
					Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {

						@Override
						public void run() {
							knockUp(p, e.getEntity());
						}
						
					}, 1L);
					
					reset(e);
				}
				return false;
			}
			else {
				if(!this.isOn) {
					toggleWindow();
				}
				Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {

					@Override
					public void run() {
						knockUp(p, e.getEntity());
					}
					
				}, 1L);
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "Combo Breaker";
	}

	@Override
	public String getDescription() {
		return "Your strikes knock up your opponent and yourself for the crowd to see.";
	}

	@Override
	public String getActivation() {
		return "Crouch and then hit an entity";
	}

	@Override
	public void levelModifier(int level) {
		duration = (2*20) + (10 * level);
	}
	
	private void knockUp(Player p, Entity e) {
		Vector knockBack = p.getEyeLocation().getDirection().normalize().multiply(0.2).add(new Vector(0, 0.7, 0));
		e.setVelocity(knockBack);
		p.setVelocity(knockBack);
		this.p1 = p;
		this.p2 = e;
	}
	
	public void toggleWindow() {
		if(!this.isOn) {
			this.window.resetCounter();
			this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(FantasyTools.getPlugin(), this.window, 0L, this.tickRate);
			this.isOn = true;
		}
		else {
			Bukkit.getScheduler().cancelTask(this.taskID);
			this.isOn = false;
		}
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
		
		this.window.resetCounter();
		return false;
	}
	
	protected void setOn(boolean on) {
		this.isOn = on;
	}
	
	protected int getTaskID() {
		return this.taskID;
	}
	
	private class Window implements Runnable {
		
		private BossBar bar = Bukkit.createBossBar("Combo Breaker", BarColor.PINK, BarStyle.SEGMENTED_20, new org.bukkit.boss.BarFlag[0]);
		private int duration;
		private int counter;
		private ComboBreaker trait;
		
		protected Window(Player player, int duration, ComboBreaker trait) {
			this.duration = duration;
			this.counter = duration;
			this.trait = trait;
			this.bar.setProgress(1.0);
			this.bar.setVisible(false);
			if(player != null) {
				this.bar.addPlayer(player);
			}
		}
		
		@Override
		public void run() {
			double value = (double)this.counter / (double)this.duration;
			if(value <= 0.0) {
				cancel();
				return;
			}
	        value = Math.min(Math.max(0.0D, value), 1.0D);
	        this.bar.setProgress(value);
	        if (!this.bar.isVisible()) {
	        	this.bar.setVisible(true); 
	        }
			this.counter--;
		}
		
		public void resetCounter() {
			this.counter = this.duration;
		}
		
		public void cancel() {
			this.bar.setVisible(false);
			this.bar.setProgress(1.0);
			Bukkit.getScheduler().cancelTask(this.trait.getTaskID());
			this.trait.setOn(false);
		}
	}
}
