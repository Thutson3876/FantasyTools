package me.Zombie__Hunter.fantasytools.traits.traitlist.misc;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class Wings extends AbstractBasicTrait {
	
	public Wings(AbstractClassTool tool) {
		super(tool);
		this.skillPointCost = 15;
		this.maximumLevel = 1;
		this.coolDowninTicks = 1 * 20;
	}

	@Override
	public boolean trigger(Event event) {
		if(event instanceof PlayerToggleFlightEvent) {
			PlayerToggleFlightEvent e = (PlayerToggleFlightEvent)event;
			plugin.setCurrentEventCancelled(true);
			Player player = e.getPlayer();
			
			if(!this.tool.checkOwnership(player) || !AbstractClassTool.hasOwnedAndRegisteredTool(player)) {
				return false;
			}
			
			if(player.isFlying()) {
				return false;
			}
			if(player.isGliding()) {
				player.setGliding(false);
				return true;
			}
			player.setGliding(true);
			return true;
		}
		else if(event instanceof EntityToggleGlideEvent) {
			EntityToggleGlideEvent e = (EntityToggleGlideEvent)event;
			if(e.getEntity().equals(this.tool.getOwner())) {
				plugin.setCurrentEventCancelled(true);
				return false;
			}
		}
		else if(event instanceof PlayerMoveEvent) {
			PlayerMoveEvent e = (PlayerMoveEvent)event;
			Player p = e.getPlayer();
			
			if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
				return false;
			}
			
			Location loc = e.getTo();
			if(loc.getY() < e.getFrom().getY()) {
				if(!loc.getBlock().getRelative(BlockFace.DOWN).isPassable()) {
					if(p.isGliding()) {
						p.setGliding(false);
					}
					p.setFlying(false);
					p.setAllowFlight(false);
					
					BukkitRunnable task = new BukkitRunnable() {
	
						@Override
						public void run() {
							p.setAllowFlight(true);
						}
					};
					task.runTaskLaterAsynchronously(plugin, 10);
				}
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "Wings";
	}

	@Override
	public String getDescription() {
		return "Sprout angelic wings and gain the ability to safely glide through the sky.";
	}

	@Override
	public String getActivation() {
		return "Jump while midair in order to start gliding. Jump while gliding to toggle it off.";
	}

	@Override
	public void levelModifier(int level) {
		if(this.tool != null) {
			if(this.tool.getOwner() != null) {
				this.tool.getOwner().setAllowFlight(true);
			}
		}
	}
}
