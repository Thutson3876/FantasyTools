package me.Zombie__Hunter.fantasytools.traits.traitlist.rashid;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import me.Zombie__Hunter.fantasytools.FantasyTools;
import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;
import me.Zombie__Hunter.fantasytools.utils.ChatUtils;

public class Easifa extends AbstractBasicTrait {

	private int duration = 2 * 20;
	private boolean isOn = false;
	private int tickRate = 1;
	private Window window;
	private int taskID;
	
	private double heightBonus = 1.1;
	private double comboBonus = 0.0;
	private double maxBonus = 16.0;
	
	public Easifa(AbstractClassTool tool) {
		super(tool);
		this.skillPointCost = 8;
		this.maximumLevel = 3;
		this.coolDowninTicks = 0 * 20;
		
		if(tool != null) {
			this.window = new Window(tool.getOwner(), this.duration, this);
		}
	}
	
	@Override
	public boolean trigger(Event event) {
		if(event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)event;
			if(!(e.getDamager() instanceof Player)) {
				return false;
			}
			
			Player p = (Player)e.getDamager();
			
			if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
				return false;
			}
			
			if(this.isOn) {
				e.setDamage(e.getDamage() + this.comboBonus);
				if(this.comboBonus > this.maxBonus) {
					this.comboBonus = this.maxBonus;
				}
				this.comboBonus += 2.0;
				reset(e);
			}
			else {
				this.comboBonus = 0.0;
				toggleWindow();
			}
			
			if(heightBonus(e)) {
				p.spawnParticle(Particle.BUBBLE_POP, e.getEntity().getLocation().add(0, e.getEntity().getHeight() + 0.1, 0), 1);
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "Easifa";
	}

	@Override
	public String getDescription() {
		return "Your strikes deal more damage when dealt within a set timeframe, and even more if you are above your target.";
	}

	@Override
	public String getActivation() {
		return "Passive: on consistent hits";
	}

	@Override
	public void levelModifier(int level) {
		this.duration = (3*10) + (5 * level);
		this.heightBonus = 1.0 + (0.1 * level);
	}
	
	private boolean heightBonus(EntityDamageByEntityEvent e) {
		if((e.getDamager().getLocation().getY() - e.getEntity().getLocation().getY()) > 0.5) {
			e.setDamage(e.getDamage() * this.heightBonus);
			return true;
		}
		return false;
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
	
	protected int getComboBonus() {
		return (int) Math.round(this.comboBonus / 2.0);
	}
	
	protected void setOn(boolean on) {
		this.isOn = on;
	}
	
	protected int getTaskID() {
		return this.taskID;
	}
	
	private class Window implements Runnable {
		
		private BossBar bar = Bukkit.createBossBar("Easifa: 0", BarColor.PINK, BarStyle.SEGMENTED_20, new org.bukkit.boss.BarFlag[0]);
		private int duration;
		private int counter;
		private Easifa trait;
		
		protected Window(Player player, int duration, Easifa trait) {
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
			this.bar.setTitle("Easifa: " + ChatUtils.chat("&6" + trait.getComboBonus()));
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
