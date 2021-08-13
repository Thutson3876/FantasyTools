package me.Zombie__Hunter.fantasytools.traits.traitlist.rogue;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class Poison extends AbstractBasicTrait {

	private int amp = 0;
	private int duration = 10 * 20;
	
	public Poison(AbstractClassTool tool) {
		super(tool);
		this.coolDowninTicks = 25 * 20;
		this.maximumLevel = 5;
		this.skillPointCost = 4;
	}

	@Override
	public boolean trigger(Event event) {
		if(!(event instanceof EntityDamageByEntityEvent)) {
			return false;
		}
		EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)event;
		if(!e.getDamager().equals(this.tool.getOwner())) {
			return false;
		}
		Player p = (Player)e.getDamager();
		
		if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
			return false;
		}
		
		if(this.isOnCooldown()) {
			return false;
		}
		
		if(!p.getInventory().getItemInMainHand().equals(this.tool.getItemStack()) && !p.getInventory().getItemInOffHand().equals(this.tool.getItemStack())) {
			return false;
		}
		
		if(!(e.getEntity() instanceof LivingEntity)) {
			return false;
		}
		LivingEntity livingEntity = (LivingEntity)e.getEntity();
		if(livingEntity.getHealth() <= e.getFinalDamage()) {
			return false;
		}
		
		applyPotionEffects(livingEntity);
		return true;
	}

	@Override
	public String getName() {
		return "Poison";
	}

	@Override
	public String getDescription() {
		return "Tip your weapon in a deadly poison that inflicts weakness as well.";
	}

	@Override
	public String getActivation() {
		return "Passive: on hit with your class tool";
	}

	@Override
	public void levelModifier(int level) {
		this.duration = (8 + (2*level)) * 20;
		if(level >= 3) {
			this.amp = 1;
		}
	}
	
	private void applyPotionEffects(LivingEntity entity) {
		entity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, this.duration, this.amp));
		entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, this.duration, this.amp));
	}
}
