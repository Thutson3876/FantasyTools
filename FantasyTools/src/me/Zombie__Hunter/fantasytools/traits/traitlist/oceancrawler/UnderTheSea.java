package me.Zombie__Hunter.fantasytools.traits.traitlist.oceancrawler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class UnderTheSea extends AbstractBasicTrait {

	private int taskID = 0;
	private BukkitRunnable task;
	private final PotionEffect conduit = new PotionEffect(PotionEffectType.CONDUIT_POWER, 40, 0);
	
	public UnderTheSea(AbstractClassTool tool) {
		super(tool);
		this.coolDowninTicks = 1 * 20;
		this.maximumLevel = 1;
		this.skillPointCost = 12;
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
				if(!p.isInWater()) {
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
		return false;
	}

	@Override
	public String getName() {
		return "Under the Sea";
	}

	@Override
	public String getDescription() {
		return "You gain the powers of a conduit while in water.";
	}

	@Override
	public String getActivation() {
		return "Passive: when in water";
	}

	@Override
	public void levelModifier(int level) {
		if(level > 0 && this.taskID == 0 && this.task != null) {
			this.taskID = task.runTaskTimer(plugin, 20L, 20L).getTaskId();
		}
	}
	
	private void applyPotionEffects(Player p) {
		p.addPotionEffect(conduit);
	}
}
