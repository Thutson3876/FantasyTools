package me.Zombie__Hunter.fantasytools.traits.traitlist.archer;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class Ninjitsu extends AbstractBasicTrait {

	private int duration = 3 * 20;
	
	public Ninjitsu(AbstractClassTool tool) {
		super(tool);
		this.skillPointCost = 6;
		this.maximumLevel = 3;
		this.coolDowninTicks = 16 * 20;
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
		
		addPotionEffects(p);
		return true;
	}

	@Override
	public String getName() {
		return "Ninjitsu";
	}

	@Override
	public String getDescription() {
		return "Become invisible and move faster temporarily.";
	}
	
	@Override
	public String getActivation() {
		return "Swap your classtool to your offhand (or vice versa)";
	}

	@Override
	public void levelModifier(int level) {
		this.duration = (3 + (1*level)) * 20;
	}
	
	private void addPotionEffects(Player p) {
		p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, this.duration, 0));
		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, this.duration, 0));
	}
}
