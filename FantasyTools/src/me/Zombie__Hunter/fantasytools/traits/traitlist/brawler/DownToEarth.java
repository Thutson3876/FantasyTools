package me.Zombie__Hunter.fantasytools.traits.traitlist.brawler;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;
import me.Zombie__Hunter.fantasytools.utils.TraitUtils;

public class DownToEarth extends AbstractBasicTrait {

	public DownToEarth(AbstractClassTool tool) {
		super(tool);
		this.skillPointCost = 8;
		this.maximumLevel = 3;
		this.coolDowninTicks = 12 * 20;
	}

	@Override
	public boolean trigger(Event event) {
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
		
		return teleportDown(p);
	}

	@Override
	public String getName() {
		return "Down To Earth";
	}

	@Override
	public String getDescription() {
		return "Teleport downwards.";
	}

	@Override
	public String getActivation() {
		return "Swap your classtool to your offhand (or vice versa)";
	}

	@Override
	public void levelModifier(int level) {
		this.coolDowninTicks = (13 - level) * 20;
	}
	
	private boolean teleportDown(Player p) {
		double height = TraitUtils.getHeightAboveGround(p);
		if(height < 1.3) {
			return false;
		}
		Block below = TraitUtils.getBlockBelowEntity(p);
		if(below == null) {
			return false;
		}
		Location loc = below.getLocation().add(0, 1.0, 0);
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);
		p.setFallDistance(0.0F);
		p.teleport(loc, TeleportCause.PLUGIN);
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);
		return true;
		
	}
}
