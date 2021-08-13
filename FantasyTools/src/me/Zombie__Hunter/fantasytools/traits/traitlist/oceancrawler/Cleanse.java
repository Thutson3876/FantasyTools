package me.Zombie__Hunter.fantasytools.traits.traitlist.oceancrawler;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffectType;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class Cleanse extends AbstractBasicTrait {

	private static final List<PotionEffectType> BADEFFECTS = generateEffectsList();
	
	public Cleanse(AbstractClassTool tool) {
		super(tool);
		this.coolDowninTicks = 12 * 20;
		this.maximumLevel = 5;
		this.skillPointCost = 5;
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
		
		return removePotionEffects(p);
	}

	@Override
	public String getName() {
		return "Cleanse";
	}

	@Override
	public String getDescription() {
		return "You've grown accustomed to dealing with the various creatures of the depths and can ward off their curses. And those of others...";
	}

	@Override
	public String getActivation() {
		return "Swap your classtool to your offhand (or vice versa)";
	}

	@Override
	public void levelModifier(int level) {
		this.coolDowninTicks = (13 - level) * 20;
	}
	
	private boolean removePotionEffects(Player p) {
		boolean hasTriggered = false;
		for(PotionEffectType effect : BADEFFECTS) {
			if(p.hasPotionEffect(effect)) {
				p.removePotionEffect(effect);
				hasTriggered = true;
			}
		}
		
		return hasTriggered;
	}
	
	private static List<PotionEffectType> generateEffectsList(){
		List<PotionEffectType> effects = new LinkedList<>();
		effects.add(PotionEffectType.BAD_OMEN);
		effects.add(PotionEffectType.UNLUCK);
		effects.add(PotionEffectType.BLINDNESS);
		effects.add(PotionEffectType.CONFUSION);
		effects.add(PotionEffectType.HUNGER);
		effects.add(PotionEffectType.WITHER);
		effects.add(PotionEffectType.WEAKNESS);
		effects.add(PotionEffectType.LEVITATION);
		effects.add(PotionEffectType.SLOW);
		effects.add(PotionEffectType.SLOW_DIGGING);
		effects.add(PotionEffectType.POISON);
		
		return effects;
	}
}
