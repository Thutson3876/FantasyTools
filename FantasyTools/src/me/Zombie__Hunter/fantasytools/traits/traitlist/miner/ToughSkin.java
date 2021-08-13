package me.Zombie__Hunter.fantasytools.traits.traitlist.miner;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class ToughSkin extends AbstractBasicTrait {

	private int tickDuration = 8 * 20;
	private int amp = 1;
	
	public ToughSkin(AbstractClassTool tool) {
		super(tool);
		this.skillPointCost = 7;
		this.maximumLevel = 3;
		this.coolDowninTicks = 16 * 20;
	}

	@Override
	public boolean trigger(Event event) {
		if(!(event instanceof PlayerInteractEvent)) {
			return false;
		}
		PlayerInteractEvent e = (PlayerInteractEvent) event;
		if(!(e.getAction().equals(Action.RIGHT_CLICK_AIR)) && !(e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
			return false;
		}
		Player p = e.getPlayer();
		
		if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
			return false;
		}
		if(!p.getInventory().getItemInMainHand().equals(this.tool.getItemStack()) && !p.getInventory().getItemInOffHand().equals(this.tool.getItemStack())) {
			return false;
		}
		
		if(this.isOnCooldown()) {
			return false;
		}
		
		applyPotionEffects(p);
		return true;
	}

	@Override
	public String getName() {
		return "Tough Skin";
	}

	@Override
	public String getDescription() {
		return "Your time in the mines has toughened your skin to the point where you can resist pain and flames for a while before their effects get to you.";
	}

	@Override
	public String getActivation() {
		return "Right click while holding your class tool";
	}

	@Override
	public void levelModifier(int level) {
		this.tickDuration = (7 + level) * 20;
	}
	
	private void applyPotionEffects(Player p) {
		p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, this.tickDuration + (this.currentLevel * 20), this.amp));
		p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, this.tickDuration, this.amp));
		p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, this.tickDuration, this.amp));
	}
}
