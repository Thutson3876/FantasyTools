package me.Zombie__Hunter.fantasytools.traits.traitlist.sindorei;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import me.Zombie__Hunter.fantasytools.FantasyTools;
import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;
import me.Zombie__Hunter.fantasytools.utils.TraitUtils;

public class DrainingAura extends AbstractBasicTrait {
	
	private double damagePerAuraTick = 1.0;
	private double healAmt = 0.5;
	private double range = 3.0;
	private double aoeDamage = 5.0;
	private int tickRate = 20;
	private boolean isOn = false;
	private int taskID;
	
	private Aura aura;
	
	public DrainingAura(AbstractClassTool tool) {
		super(tool);
		this.skillPointCost = 5;
		this.maximumLevel = 3;
		this.coolDowninTicks = 0;
		
		if(tool != null) {
			this.aura = new Aura(tool.getOwner(), this.damagePerAuraTick, this.aoeDamage, this.range);
		}
	}

	@Override
	public boolean trigger(Event event) {
		if(!(event instanceof PlayerToggleSneakEvent)) {
			return false;
		}
		
		PlayerToggleSneakEvent e = (PlayerToggleSneakEvent) event;
		Player p = e.getPlayer();
		
		if(((Entity)p).isOnGround() || !p.isSneaking()) {
			return false;
		}
		
		if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
			return false;
		}
		
		toggleAura();
		
		return true;
	}

	@Override
	public String getName() {
		return "Draining Aura";
	}

	@Override
	public String getDescription() {
		return "Grants you a damaging and slowing aura at the cost of your own health.";
	}
	
	@Override
	public String getActivation() {
		return "Press and let go of crouch while in air";
	}

	@Override
	public void levelModifier(int level) {
		this.aoeDamage = 3.0 + (level * 2.0);
		this.range = 3.0 + (1 * level);
	}
	
	public static List<LivingEntity> getNearbyLivingEntities(Player p, double range){
		List<Entity> entities = p.getNearbyEntities(range, range, range);
		List<LivingEntity> livingEntities = new LinkedList<>();		
		if(entities == null) {
			return null;
		}
		if(entities.isEmpty()) {
			return null;
		}
		
		for(Entity e : entities) {
			if(e instanceof LivingEntity) {
				livingEntities.add((LivingEntity) e);
			}
		}
		
		if(livingEntities.isEmpty()) {
			return null;
		}
					
		return livingEntities;
	}
	
	private void toggleAura() {
		if(!this.isOn) {
			taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(FantasyTools.getPlugin(), aura, 0L, this.tickRate);
			this.isOn = true;
		}
		else {
			Bukkit.getScheduler().cancelTask(this.taskID);
			this.aura.getBar().setVisible(false);
			this.isOn = false;
		}
	}
	
	public void particles() {
		if(!isOn) {
			return;
		}
		
		List<Location> particleSpawns = TraitUtils.generateRectangle(this.tool.getOwner().getLocation(), range, range);
		for(Location loc : particleSpawns) {
			this.tool.getOwner().getWorld().spawnParticle(Particle.HEART, loc, 1);
		}
	}
	
	private class Aura implements Runnable {
		
		private final Player player;
		private final double damagePerAuraTick;
		private final double range;
		private final double aoeDamage;
		private BossBar bar = Bukkit.createBossBar("Draining Aura", BarColor.RED, BarStyle.SEGMENTED_20, new org.bukkit.boss.BarFlag[0]);
		private EntityDamageEvent event;
		
		protected Aura(Player player, double damagePerAuraTick, double aoeDamage, double range) {
			this.player = player;
			this.damagePerAuraTick = damagePerAuraTick;
			this.aoeDamage = aoeDamage;
			this.range = range;
			this.bar.setProgress(1.0);
			this.bar.setVisible(false);
			if(player != null) {
				this.bar.addPlayer(player);
			}
			event = new EntityDamageEvent(player, DamageCause.CUSTOM, damagePerAuraTick);
		}
		
		@Override
		public void run() {
			if(this.player.isDead()) {
				return;
			}
			
			List<LivingEntity> entities = DrainingAura.getNearbyLivingEntities(this.player, this.range);
			
			double newHealth = this.player.getHealth() - this.damagePerAuraTick;
			if(newHealth <= 0) {
				newHealth = 0;
			}
			this.player.setHealth(newHealth);
			Bukkit.getServer().getPluginManager().callEvent(event);
			this.bar.setVisible(true);
			
			if(entities == null) {
				return;
			}
			
			PotionEffect pot = new PotionEffect(PotionEffectType.SLOW, 30, 0);
			for(LivingEntity e : entities) {
				e.addPotionEffect(pot);
				e.damage(this.aoeDamage, this.player);
				TraitUtils.heal(this.player, healAmt);
			}
		}
		
		public BossBar getBar() {
			return this.bar;
		}
	}
	
}


