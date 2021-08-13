package me.Zombie__Hunter.fantasytools.traits.traitlist.misc;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

import me.Zombie__Hunter.fantasytools.FantasyTools;
import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;
//import me.Zombie__Hunter.fantasytools.utils.TaskCanceller;

public class ComboBreaker2 extends AbstractBasicTrait {

	private int duration = 5 * 20;
	private boolean isOn = false;
	private boolean isBarToggled = false;
	private int tickRate = 1;
	private Window window;
	private int taskID;
	//private TaskCanceller taskCanceller;
	
	public ComboBreaker2(AbstractClassTool tool) {
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
			
			toggleBar();
			return false;
		}
		else if(event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)event;
			if(!(e.getDamager() instanceof Player)) {
				return false;
			}
			
			Player p = (Player)e.getDamager();
			
			if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
				return false;
			}
			
			if(this.isBarToggled) {
				Bukkit.getScheduler().runTaskLater(plugin, new Runnable( ) {

					@Override
					public void run() {
						knockUp(p, e.getEntity());
					}
					
				}, 1L);
				
				toggleBar();
				toggleWindow();
				return true;
			}
			else {
				if(!this.isOn) {
					return false;
				}
				
				Bukkit.getScheduler().runTaskLater(plugin, new Runnable( ) {

					@Override
					public void run() {
						knockUp(p, e.getEntity());
					}
					
				}, 1L);
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
		duration = (5*20) + (20 * level);
	}
	
	private void knockUp(Player p, Entity e) {
		Vector knockBack = p.getEyeLocation().getDirection().normalize().multiply(0.2).add(new Vector(0, 0.8, 0));
		e.setVelocity(knockBack);
		p.setVelocity(knockBack);
	}
	
	private void toggleBar(){
		if(this.isBarToggled) {
			window.getBar().setVisible(false);
			this.isBarToggled = false;
			return;
		}
		window.getBar().setVisible(true);
		this.isBarToggled = true;
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
		private ComboBreaker2 trait;
		
		protected Window(Player player, int duration, ComboBreaker2 trait) {
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
		
		public BossBar getBar() {
			return this.bar;
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
