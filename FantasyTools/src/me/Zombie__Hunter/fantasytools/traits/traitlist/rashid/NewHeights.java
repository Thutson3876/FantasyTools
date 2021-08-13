package me.Zombie__Hunter.fantasytools.traits.traitlist.rashid;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.util.Vector;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class NewHeights extends AbstractBasicTrait {

	private double fallDamageReduction = 3.0;
	
	public NewHeights(AbstractClassTool tool) {
		super(tool);
		this.skillPointCost = 5;
		this.maximumLevel = 3;
		this.coolDowninTicks = 8 * 20;
	}

	@Override
	public boolean trigger(Event event) {
		if(event instanceof EntityDamageEvent) {
			EntityDamageEvent e = (EntityDamageEvent)event;
			if(e.getCause().equals(DamageCause.FALL)) {
				double newDamage = e.getDamage() - this.fallDamageReduction;
				if(newDamage < 0.0) {
					newDamage = 0.0;
				}
				e.setDamage(newDamage);
			}
		}
		if(!(event instanceof PlayerSwapHandItemsEvent)) {
			return false;
		}
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
		
		leap(p);
		return true;
	}

	@Override
	public String getName() {
		return "New Heights";
	}

	@Override
	public String getDescription() {
		return "Leap forward and high into the air. You take less damage from falling.";
	}

	@Override
	public String getActivation() {
		return "Swap your classtool to your offhand (or vice versa)";
	}

	@Override
	public void levelModifier(int level) {
		this.coolDowninTicks = (9 - level) * 20;
		this.fallDamageReduction = 2.0 + level;
	}
	
	private void leap(Player p) {
		Vector velocity = p.getLocation().getDirection().setY(0);
		velocity.multiply(0.2);
		velocity.normalize().setY(1.2);
		p.setVelocity(velocity);
	}
}
