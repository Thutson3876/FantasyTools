package me.Zombie__Hunter.fantasytools.traits.traitlist.forager;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class Nimble extends AbstractBasicTrait {

	private int tickDuration = 10 * 20;
	
	public Nimble(AbstractClassTool tool) {
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
		PlayerInteractEvent e = (PlayerInteractEvent) event;
		if(!(e.getAction().equals(Action.RIGHT_CLICK_AIR) && !(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)))) {
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
		return "Nimble";
	}

	@Override
	public String getDescription() {
		return "Grant yourself speed and jump boost.";
	}

	@Override
	public String getActivation() {
		return "Right click while holding your class tool.";
	}

	@Override
	public void levelModifier(int level) {
		this.coolDowninTicks = (21 - level) * 20;
	}
	
	private void applyPotionEffects(Player p) {
		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, this.tickDuration, 0));
		p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, this.tickDuration, 2));
	}
}
