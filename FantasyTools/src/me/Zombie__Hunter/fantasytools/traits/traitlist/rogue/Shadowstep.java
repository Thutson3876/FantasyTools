package me.Zombie__Hunter.fantasytools.traits.traitlist.rogue;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;
import me.Zombie__Hunter.fantasytools.utils.TraitUtils;

public class Shadowstep extends AbstractBasicTrait {

	private double range = 6.0;
	
	public Shadowstep(AbstractClassTool tool) {
		super(tool);
		this.skillPointCost = 7;
		this.maximumLevel = 3;
		this.coolDowninTicks = 20 * 20;
	}

	@Override
	public boolean trigger(Event event) {
		if(!(event instanceof PlayerInteractEvent)) {
			return false;
		}
		PlayerInteractEvent e = (PlayerInteractEvent)event;
		Player p = e.getPlayer();
		
		if(!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return false;
		}
		
		if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
			return false;
		}
		if(this.isOnCooldown()) {
			return false;
		}
		LivingEntity target = TraitUtils.getCursorEntity(p, this.range);
		if(target == null) {
			return false;
		}
		
		omaewa(p, target);
		return true;
	}

	@Override
	public String getName() {
		return "Shadowstep";
	}

	@Override
	public String getDescription() {
		return "Step through mist and shadow to appear behind your target.";
	}

	@Override
	public String getActivation() {
		return "Right click while aiming at your target within range";
	}

	@Override
	public void levelModifier(int level) {
		this.range = 4.0 + (2.0 * level);
	}
	
	public void omaewa(Player p, LivingEntity target) {
		Location targetLoc = target.getLocation();
		Location behindTarget = targetLoc.add(targetLoc.getDirection().multiply(-0.8));
		
		p.teleport(behindTarget.add(new Location(target.getWorld(), 0, 0.1, 0)));
	}
}
