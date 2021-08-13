package me.Zombie__Hunter.fantasytools.traits.traitlist.fisherman;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class PufferfishTipped extends AbstractBasicTrait {

	private int duration = 15 * 20;
	private int amp = 0;
	
	public PufferfishTipped(AbstractClassTool tool) {
		super(tool);
		this.coolDowninTicks = 12 * 20;
		this.maximumLevel = 5;
		this.skillPointCost = 5;
	}

	@Override
	public boolean trigger(Event event) {
		if(!(event instanceof PlayerFishEvent)) {
			return false;
		}

		PlayerFishEvent e = (PlayerFishEvent)event;
		if(!(e.getCaught() instanceof LivingEntity)) {
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
		
		applyPotionEffects((LivingEntity)e.getCaught());
		return true;
	}

	@Override
	public String getName() {
		return "Pufferfish Tipped";
	}

	@Override
	public String getDescription() {
		return "Your experience around native sealife gives you a distinct advantage in battle allowing you to tip your hook with pufferfish poison.";
	}

	@Override
	public String getActivation() {
		return "Passive: on hooking living entity";
	}

	@Override
	public void levelModifier(int level) {
		this.duration = (14 + level) * 20;
		if(level >= 3) {
			this.amp = 1;
		}
		
	}
	
	private void applyPotionEffects(LivingEntity entity) {
		entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, this.duration, this.amp));
		entity.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, this.duration, this.amp));
		entity.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, this.duration, this.amp));
	}
}
