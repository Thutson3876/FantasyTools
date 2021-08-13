package me.Zombie__Hunter.fantasytools.traits.traitlist.priest;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;
import me.Zombie__Hunter.fantasytools.utils.TraitUtils;

public class Prayer extends AbstractBasicTrait {

	private double range = 10.0;
	private int absorptionAmp = 2;
	private int absorptionDuration = 8 * 20;
	private double healAmt = 4.0;
	
	public Prayer(AbstractClassTool tool) {
		super(tool);
		this.coolDowninTicks = 12 * 20;
		this.maximumLevel = 3;
		this.skillPointCost = 5;
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
		LivingEntity target = TraitUtils.getCursorEntity(p, this.range);
		if(target == null) {
			return restore(p);
		}
		return restore(target);
	}

	@Override
	public String getName() {
		return "Prayer";
	}

	@Override
	public String getDescription() {
		return "Restore health to your ally or yourself and grant a small shield as well.";
	}

	@Override
	public String getActivation() {
		return "Press the drop key while holding your class tool";
	}

	@Override
	public void levelModifier(int level) {
		this.healAmt = 3.0 + (1.0 * level);
	}
	
	private boolean restore(Entity e) {
		if(!(e instanceof LivingEntity)) {
			return false;
		}
		
		LivingEntity entity = (LivingEntity)e;
		entity.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, this.absorptionDuration, this.absorptionAmp));
		double maxHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		double heal = entity.getHealth() + this.healAmt;
		if(heal >= maxHealth) {
			entity.setHealth(maxHealth);
		}
		else {
			entity.setHealth(heal);
		}
		return true;
	}
}
