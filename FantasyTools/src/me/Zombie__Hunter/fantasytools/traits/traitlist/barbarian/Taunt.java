package me.Zombie__Hunter.fantasytools.traits.traitlist.barbarian;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class Taunt extends AbstractBasicTrait {
	
	private double range = 6.0;
	
	public Taunt(AbstractClassTool tool) {
		super(tool);
		this.skillPointCost = 3;
		this.maximumLevel = 5;
		this.coolDowninTicks = 10 * 20;
	}

	@Override
	public boolean trigger(Event event) {
		if(!(event instanceof PlayerInteractEvent)) {
			return false;
		}
		
		PlayerInteractEvent e = (PlayerInteractEvent) event;
		Player p = e.getPlayer();
		
		if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
			return false;
		}
		
		if(this.isOnCooldown()) {
			return false;
		}
		
		if(!e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			return false;
		}
		
		if(!p.getInventory().getItemInMainHand().equals(this.tool.getItemStack()) && !p.getInventory().getItemInOffHand().equals(this.tool.getItemStack())) {
			return false;
		}
		
		return aggroNearbyEntities(p);
	}

	@Override
	public String getName() {
		return "Taunt";
	}

	@Override
	public String getDescription() {
		return "Let out an intimidating shout that attracts the attention of all nearby mobs.";
	}
	
	@Override
	public String getActivation() {
		return "Right click the air.";
	}

	@Override
	public void levelModifier(int level) {
		this.range = 6.0 + (level * 2);
	}
	
	private boolean aggroNearbyEntities(Player p) {
		List<Entity> entities = p.getNearbyEntities(range, range, range);
		boolean hasActivated = false;
		
		if(entities == null) {
			return false;
		}
		if(entities.isEmpty()) {
			return false;
		}
		
		for(Entity e : entities) {
			if(!(e instanceof Mob)) {
				continue;
			}
			((Mob)e).setTarget(p);
			hasActivated = true;
		}
			
		return hasActivated;
	}
}
