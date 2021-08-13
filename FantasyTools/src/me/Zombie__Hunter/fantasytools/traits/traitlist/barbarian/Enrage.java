package me.Zombie__Hunter.fantasytools.traits.traitlist.barbarian;

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class Enrage extends AbstractBasicTrait {

	private int durationInTicks = 10 * 20;
	
	public Enrage(AbstractClassTool tool) {
		super(tool);
		this.maximumLevel = 3;
		this.skillPointCost = 6;
		this.coolDowninTicks = 20 * 20;
	}
	
	@Override
	public String getName() {
		return "Enrage";
	}

	@Override
	public String getDescription() {
		return "Enter a state of unbridled fury granting you increased speed, haste, resistance, and strength.";
	}
	
	@Override
	public String getActivation() {
		return "Passive: when you are damaged.";
	}

	@Override
	public void levelModifier(int level) {
		this.durationInTicks = (10 + (level * 2)) * 20;
	}

	@Override
	public boolean trigger(Event event) {
		if(!(event instanceof EntityDamageByEntityEvent)) {
			return false;
		}
		
		EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
		if(!(e.getEntity() instanceof Player)) {
			return false;
		}
		Player p = (Player)e.getEntity();
		
		if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
			return false;
		}
		
		if(this.isOnCooldown()) {
			return false;
		}
		
		applyPotionEffects(p);
		return true;
	}
	
	private void applyPotionEffects(Player p) {
		Collection<PotionEffect> potionEffects = new HashSet<>();
		potionEffects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, durationInTicks, 0));
		potionEffects.add(new PotionEffect(PotionEffectType.ABSORPTION, durationInTicks, 1));
		
		p.addPotionEffects(potionEffects);
	}

}
