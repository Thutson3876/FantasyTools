package me.Zombie__Hunter.fantasytools.traits.traitlist.rogue;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class Stealth extends AbstractBasicTrait {

	private PotionEffect invis;
	private PotionEffect speed;
	private PotionEffect jump;
	private boolean isOn = false;
	private BukkitRunnable task;
	private Player owner;
	
	private int duration = 11 * 20;
	
	public Stealth(AbstractClassTool tool) {
		super(tool);
		this.skillPointCost = 8;
		this.maximumLevel = 2;
		this.coolDowninTicks = 30 * 20;
		if(this.tool != null) {
			if(this.tool.getOwner() != null) {
				this.owner = this.tool.getOwner();
				this.updatePotionEffects();
			}
		}
	}

	@Override
	public boolean trigger(Event event) {
		if(event instanceof PlayerSwapHandItemsEvent) {
			PlayerSwapHandItemsEvent e = (PlayerSwapHandItemsEvent) event;
			Player p = e.getPlayer();
			
			if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
				return false;
			}
			if(this.isOnCooldown()) {
				return false;
			}
			
			if(!this.getTool().getItemStack().equals(e.getMainHandItem()) && !this.getTool().getItemStack().equals(e.getOffHandItem())) {
				return false;
			}
			
			resetTask();
			this.isOn = true;
			this.task.runTaskLater(plugin, this.duration);
			addPotionEffects(p);
			return true;
		}
		else if(event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)event;
			if(e.getEntity().equals(this.owner) || e.getDamager().equals(this.owner)) {
				if(isOn) {
					this.task.cancel();
					this.task.run();
				}
			}
			
			return false;
		}
		
		return false;
	}

	@Override
	public String getName() {
		return "Stealth";
	}

	@Override
	public String getDescription() {
		return "Hide from the eyes of onlookers to do your dirty deeds in the dark, becoming completely invisible unless you attack or are attacked.";
	}

	@Override
	public String getActivation() {
		return "Swap your classtool to your offhand (or vice versa)";
	}

	@Override
	public void levelModifier(int level) {
		this.duration = (6 + (level * 5)) * 20;
		this.updatePotionEffects();
	}
	
	private void updatePotionEffects() {
		this.invis = new PotionEffect(PotionEffectType.INVISIBILITY, this.duration, 0);
		this.speed = new PotionEffect(PotionEffectType.SPEED, this.duration, 0);
		this.jump = new PotionEffect(PotionEffectType.JUMP, this.duration, 2);
		
		resetTask();
	}
	
	private void addPotionEffects(Player p) {
		for(Player player : Bukkit.getOnlinePlayers()) {
			player.hidePlayer(plugin, p);
		}
		System.out.println(owner.getDisplayName() + " is invisible");
		p.addPotionEffect(this.invis);
		p.addPotionEffect(this.speed);
		p.addPotionEffect(this.jump);
	}
	
	private void resetTask() {
		this.task = new BukkitRunnable() {

			@Override
			public void run() {
				for(Player player : Bukkit.getOnlinePlayers()) {
					player.showPlayer(plugin, owner);
				}
				System.out.println(owner.getDisplayName() + " is no longer invisible");
				owner.removePotionEffect(invis.getType());
				owner.removePotionEffect(speed.getType());
				owner.removePotionEffect(jump.getType());
				isOn = false;
			}
			
		};
	}
}
