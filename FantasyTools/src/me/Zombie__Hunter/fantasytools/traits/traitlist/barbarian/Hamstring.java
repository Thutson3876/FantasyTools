package me.Zombie__Hunter.fantasytools.traits.traitlist.barbarian;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class Hamstring extends AbstractBasicTrait {

	private int durationInTicks = 2 * 20;
	private int potionLevel = 1;
	
	public Hamstring(AbstractClassTool tool) {
		super(tool);
		this.skillPointCost = 7;
		this.maximumLevel = 3;
		this.coolDowninTicks = 10 * 20;
	}

	@Override
	public boolean trigger(Event event) {
		if(!(event instanceof EntityDamageByEntityEvent)) {
			return false;
		}
		
		EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)event;
		if(!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof LivingEntity)) {
			return false;
		}
		
		Player p = (Player)e.getDamager();
		
		if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
			return false;
		}
		
		if(!p.getInventory().getItemInMainHand().equals(this.tool.getItemStack()) && !p.getInventory().getItemInOffHand().equals(this.tool.getItemStack())) {
			return false;
		}
		
		if(this.isOnCooldown()) {
			return false;
		}
		
		applyEffects((LivingEntity)e.getEntity());
		return true;
	}

	@Override
	public String getName() {
		return "Hamstring";
	}

	@Override
	public String getDescription() {
		return "Slice at your opponent's hamstrings, slowing them down and nauseating them.";
	}
	
	@Override
	public String getActivation() {
		return "Passive: when you injure something.";
	}

	@Override
	public void levelModifier(int level) {
		this.durationInTicks = (2 + level) * 20;
	}
	
	private void applyEffects(LivingEntity e) {
		e.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, durationInTicks, potionLevel));
		e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, durationInTicks, potionLevel));
	}
}
