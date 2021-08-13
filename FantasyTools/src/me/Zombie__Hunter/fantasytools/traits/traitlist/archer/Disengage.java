package me.Zombie__Hunter.fantasytools.traits.traitlist.archer;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class Disengage extends AbstractBasicTrait {

	private double yBoost = 0.5;
	private double velocityAmp = 2.0;
	
	public Disengage(AbstractClassTool tool) {
		super(tool);
		this.skillPointCost = 5;
		this.maximumLevel = 5;
		this.coolDowninTicks = 7 * 20;
	}

	@Override
	public boolean trigger(Event event) {
		if(!(event instanceof PlayerInteractEvent)) {
			return false;
		}
		PlayerInteractEvent e = (PlayerInteractEvent) event;
		Player p = e.getPlayer();
		if(!e.getAction().equals(Action.LEFT_CLICK_AIR) && !e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			return false;
		}
		
		if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
			return false;
		}
		if(this.isOnCooldown()) {
			return false;
		}
		if(!this.tool.getItemStack().equals(e.getItem())) {
			return false;
		}
		
		p.setVelocity(p.getEyeLocation().getDirection().multiply(-this.velocityAmp).add(new Vector(0, this.yBoost, 0)));
		return true;
	}

	@Override
	public String getName() {
		return "Disengage";
	}

	@Override
	public String getDescription() {
		return "Leap backwards away from all danger.";
	}
	
	@Override
	public String getActivation() {
		return "Left click with your class tool in hand.";
	}

	@Override
	public void levelModifier(int level) {
		this.yBoost = 0.3 + (0.025 * level);
		this.velocityAmp = 0.8 + (0.05 * level);
	}
}
