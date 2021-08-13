package me.Zombie__Hunter.fantasytools.traits.traitlist.archer;

import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityShootBowEvent;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class PiercingArrows extends AbstractBasicTrait {

	private int pierceLevel = 1;
	private double speedMult = 1.1;
	
	public PiercingArrows(AbstractClassTool tool) {
		super(tool);
		this.skillPointCost = 6;
		this.maximumLevel = 3;
		this.coolDowninTicks = 3 * 20;
		
	}

	@Override
	public boolean trigger(Event event) {
		if(!(event instanceof EntityShootBowEvent)) {
			return false;
		}
		
		EntityShootBowEvent e = (EntityShootBowEvent) event;
		
		if(!(e.getEntity() instanceof Player)) {
			return false;
		}
		Player p = (Player) e.getEntity();
		
		if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
			return false;
		}
		if(this.isOnCooldown()) {
			return false;
		}
		
		if(!(e.getProjectile() instanceof AbstractArrow)) {
			return false;
		}
		
		e.setConsumeItem(false);
		AbstractArrow arrow = (AbstractArrow) e.getProjectile();
		arrow.setPierceLevel(arrow.getPierceLevel() + this.pierceLevel);
		arrow.setVelocity(arrow.getVelocity().multiply(this.speedMult));
		return true;
	}

	@Override
	public String getName() {
		return "Piercing Arrows";
	}

	@Override
	public String getDescription() {
		return "Your arrows are so precise that they can pierce through multiple foes.";
	}
	
	@Override
	public String getActivation() {
		return "Passive: on bow shot.";
	}

	@Override
	public void levelModifier(int level) {
		this.pierceLevel = 1 * level;
		this.coolDowninTicks = (4 - level) * 20;
		this.speedMult = 1 + (0.1 * level);
	}
	
	
}
