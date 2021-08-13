package me.Zombie__Hunter.fantasytools.traits.traitlist.priest;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class Grace extends AbstractBasicTrait {

	private int tickDuration = 8 * 20;
	
	public Grace(AbstractClassTool tool) {
		super(tool);
		this.skillPointCost = 4;
		this.maximumLevel = 3;
		this.coolDowninTicks = 18 * 20;
	}

	@Override
	public boolean trigger(Event event) {
		if(!(event instanceof PlayerSwapHandItemsEvent)) {
			return false;
		}
		
		PlayerSwapHandItemsEvent e = (PlayerSwapHandItemsEvent) event;
		Player p = e.getPlayer();
		
		if(((Entity)p).isOnGround()) {
			return false;
		}
		
		if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
			return false;
		}
		
		if(this.isOnCooldown()) {
			return false;
		}
		
		if(!this.getTool().getItemStack().equals(e.getMainHandItem()) && !this.getTool().getItemStack().equals(e.getOffHandItem())) {
			return false;
		}
		
		applyPotionEffect(p);
		
		return true;
	}

	@Override
	public String getName() {
		return "Grace";
	}

	@Override
	public String getDescription() {
		return "Drift down from the sky with the grace of a god... and slowfall.";
	}

	@Override
	public String getActivation() {
		return "Swap your classtool to your offhand (or vice versa) while in air";
	}

	@Override
	public void levelModifier(int level) {
		this.coolDowninTicks = (19 - (1 * level)) * 20;
	}
	
	private void applyPotionEffect(Player p) {
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, this.tickDuration, 0));
	}
}
