package me.Zombie__Hunter.fantasytools.traits.traitlist.miner;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class DungeonDelver extends AbstractBasicTrait {

	private int taskID = 0;
	private BukkitRunnable task;
	private int maxLightLevel = 8;
	private final PotionEffect darkVision = new PotionEffect(PotionEffectType.NIGHT_VISION, 240, 0);
	private final PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 40, 0);
	//private final PotionEffect haste = new PotionEffect(PotionEffectType.FAST_DIGGING, 40, 0);
	private final PotionEffect strength = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40, 0);
	
	public DungeonDelver(AbstractClassTool tool) {
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
				Location loc = p.getLocation();
				if(loc.getBlock().getLightLevel() > maxLightLevel || loc.getWorld().getHighestBlockAt(loc).getY() < loc.getY()) {
					p.removePotionEffect(PotionEffectType.NIGHT_VISION);
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName() {
		return "Dungeon Delver";
	}

	@Override
	public String getDescription() {
		return "While in darkness underground, you gain night vision, speed, and strength.";
	}

	@Override
	public String getActivation() {
		return "Passive: when in a light level less than 8";
	}

	@Override
	public void levelModifier(int level) {
		if(level > 0 && this.taskID == 0 && this.task != null) {
			this.taskID = task.runTaskTimer(plugin, 20L, 20L).getTaskId();
		}
	}
	
	private void applyPotionEffects(Player p) {
		p.addPotionEffect(darkVision);
		p.addPotionEffect(speed);
		p.addPotionEffect(strength);
	}
}
