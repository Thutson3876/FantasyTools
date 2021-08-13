package me.Zombie__Hunter.fantasytools.traits.traitlist.sindorei;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;
import me.Zombie__Hunter.fantasytools.utils.ChatUtils;
import me.Zombie__Hunter.fantasytools.utils.TraitUtils;

public class BloodBond extends AbstractBasicTrait {

	private double maxRange = 70.0;
	private double range = 5.0;
	private LivingEntity target;
	
	public BloodBond(AbstractClassTool tool) {
		super(tool);
		this.coolDowninTicks = 2 * 20;
		this.maximumLevel = 1;
		this.skillPointCost = 10;
	}

	@Override
	public boolean trigger(Event event) {
		if(event instanceof PlayerDropItemEvent) {
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
				return false;
			}
			
			return registerNewTarget(target);
		}
		else if(event instanceof EntityDamageEvent) {
			EntityDamageEvent e = (EntityDamageEvent)event;
			if(!(e.getEntity() instanceof Player)) {
				return false;
			}
			if(e.getEntity().equals(this.getTool().getOwner())) {
				playerTookDamage(e);
				return false;
			}
		}
		else if(event instanceof EntityDeathEvent) {
			EntityDeathEvent e = (EntityDeathEvent)event;
			if(e.getEntity().equals(this.target)) {
				this.target = null;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "Blood Bond";
	}

	@Override
	public String getDescription() {
		return "Create a bond between yourself and your chosen foe. Whenever you take damage, that damage is also applied to your bonded target.";
	}
	
	@Override
	public String getActivation() {
		return "Press the drop key while holding your class tool.";
	}

	@Override
	public void levelModifier(int level) {
		
	}
	
	private boolean registerNewTarget(Entity e) {
		if(!(e instanceof LivingEntity)) {
			return false;
		}
		unTarget();
		
		this.target = (LivingEntity) e;
		if(this.target instanceof Player) {
			((Player)this.target).sendMessage(ChatUtils.chat("&6You are the victim of a blood bond."));
		}
		
		return true;
	}
	
	private void playerTookDamage(EntityDamageEvent e) {
		if(this.target == null) {
			return;
		}
		if(this.target.isDead()) {
			unTarget();
			return;
		}
		if(this.target.getLocation().distance(this.getTool().getOwner().getLocation()) > maxRange) {
			unTarget();
			return;
		}
		
		this.target.damage(e.getFinalDamage(), this.getTool().getOwner());
		
		if(this.target.getHealth() <= 0) {
			unTarget();
		}
	}
	
	private void unTarget() {
		if(this.target == null) {
			return;
		}
		
		if(this.target instanceof Player) {
			((Player)this.target).sendMessage(ChatUtils.chat("&6You are no longer the victim of a blood bond."));
		}
		this.target = null;
	}
}
