package me.Zombie__Hunter.fantasytools.traits.traitlist.oceancrawler;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class SerenityNow extends AbstractBasicTrait {

	private int duration = 4 * 20;
	private int amp = 1;
	
	public SerenityNow(AbstractClassTool tool) {
		super(tool);
		this.coolDowninTicks = 15 * 20;
		this.maximumLevel = 2;
		this.skillPointCost = 8;
	}

	@Override
	public boolean trigger(Event event) {
		if(!(event instanceof PlayerDropItemEvent)) {
			return false;
		}
		
		PlayerDropItemEvent e = (PlayerDropItemEvent)event;
		Player p = e.getPlayer();
		if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
			return false;
		}
		if(this.isOnCooldown()) {
			return false;
		}
		if(!e.getItemDrop().getItemStack().equals(this.getTool().getItemStack())) {
			return false;
		}
		plugin.setCurrentEventCancelled(true);

		applyPotionEffects(p);
		return true;
	}

	@Override
	public String getName() {
		return "Serenity Now";
	}

	@Override
	public String getDescription() {
		return "Grant yourself a brief window of respite, gaining regeneration and dolphin's grace for a short duration.";
	}

	@Override
	public String getActivation() {
		return "Press the drop key while holding your class tool";
	}

	@Override
	public void levelModifier(int level) {
		this.amp = level;
	}
	
	private void applyPotionEffects(Player p) {
		p.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, this.duration, this.amp));
		p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, this.duration, this.amp));
	}
}
