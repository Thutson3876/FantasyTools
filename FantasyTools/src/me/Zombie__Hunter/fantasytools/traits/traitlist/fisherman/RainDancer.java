package me.Zombie__Hunter.fantasytools.traits.traitlist.fisherman;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;
import me.Zombie__Hunter.fantasytools.utils.ChatUtils;

public class RainDancer extends AbstractBasicTrait {
	
	private int taskID = 0;
	private BukkitRunnable task;
	private final PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 40, 0);
	private final PotionEffect strength = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40, 0);
	private final PotionEffect regen = new PotionEffect(PotionEffectType.REGENERATION, 40, 0);
	
	public RainDancer(AbstractClassTool tool) {
		super(tool);
		this.coolDowninTicks = 1 * 20;
		this.maximumLevel = 1;
		this.skillPointCost = 10;
	}

	@Override
	public void init() {
		if(this.tool == null || this.tool.getOwner() == null) {
			task = null;
			return;
		}
		
		
		task = new BukkitRunnable() {

			@Override
			public void run() {
				Player p = tool.getOwner();
				if(p == null) {
					deInit();
					return;
				}
				
				if(p.isDead()) {
					return;
				}
				if(p.getLocation().getY() < p.getWorld().getHighestBlockYAt(p.getLocation())) {
					return;
				}
				if(p.getWorld().isClearWeather()) {
					return;
				}
				applyPotionEffects(p);
			}
			
		};
	}
	
	@Override
	public void deInit() {
		if(this.task == null || this.taskID == 0) {
			return;
		}
		Bukkit.getScheduler().cancelTask(this.taskID);
		this.taskID = 0;
	}
	
	@Override
	public boolean trigger(Event event) {
		if(event instanceof WeatherChangeEvent) {
			if(this.tool.getOwner() != null) {
				WeatherChangeEvent e = (WeatherChangeEvent)event;
				if(e.toWeatherState()) {
					this.tool.getOwner().sendMessage(ChatUtils.chat("&6You begin to feel empowered..."));
				}
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "Rain Dancer";
	}

	@Override
	public String getDescription() {
		return "While in the rain you gain speed, strength, and regeneration.";
	}

	@Override
	public String getActivation() {
		return "Passive: while in the rain";
	}

	@Override
	public void levelModifier(int level) {
		if(level > 0 && this.taskID == 0 && this.task != null) {
			this.taskID = task.runTaskTimer(plugin, 20L, 20L).getTaskId();
		}
	}

	private void applyPotionEffects(Player p) {
		p.addPotionEffect(speed);
		p.addPotionEffect(strength);
		p.addPotionEffect(regen);
	}
}
