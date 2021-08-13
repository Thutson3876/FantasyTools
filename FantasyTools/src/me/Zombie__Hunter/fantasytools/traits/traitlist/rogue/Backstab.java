package me.Zombie__Hunter.fantasytools.traits.traitlist.rogue;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class Backstab extends AbstractBasicTrait {

	private double dmgMult = 1.4;
	
	public Backstab(AbstractClassTool tool) {
		super(tool);
		this.skillPointCost = 7;
		this.maximumLevel = 3;
		this.coolDowninTicks = 3 * 20;
	}

	@Override
	public boolean trigger(Event event) {
		if(!(event instanceof EntityDamageByEntityEvent)) {
			return false;
		}
		EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)event;
		if(!e.getDamager().equals(this.tool.getOwner())) {
			return false;
		}
		Player p = (Player)e.getDamager();
		
		if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
			return false;
		}
		
		if(this.isOnCooldown()) {
			return false;
		}
		
		Entity damaged = e.getEntity();
		Entity damager = e.getDamager();
		
		float damagedYaw = (damaged.getLocation().getYaw() >= 0 ? damaged.getLocation().getYaw() : 180+-damaged.getLocation().getYaw());
		float damagerYaw = (damager.getLocation().getYaw() >= 0 ? damager.getLocation().getYaw() : 180+-damager.getLocation().getYaw());
		float angle = (damagedYaw - damagerYaw >= 0 ? damagedYaw - damagerYaw : damagerYaw - damagedYaw);
				 
		if (angle <= 50) {
			e.setDamage(e.getDamage() * this.dmgMult);
			if(e.getFinalDamage() >= ((Damageable)e.getEntity()).getHealth()) {
				dropPlayerHead(e.getEntity());
			}
			return true;
		}
		
		return false;
	}

	@Override
	public String getName() {
		return "Backstab";
	}

	@Override
	public String getDescription() {
		return "You're able to exploit the vulnerabilities of your opponents to deal more damage to them from behind.";
	}

	@Override
	public String getActivation() {
		return "Passive: on hit while your target is facing away from you";
	}

	@Override
	public void levelModifier(int level) {
		this.dmgMult = 1.3 + (0.1 * level);
	}
	
	private void dropPlayerHead(Entity victim) {
		if(!(victim instanceof Player)) {
			return;
		}
		Player p = (Player)victim;
		ItemStack head = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
		skullMeta.setOwningPlayer(p);
		head.setItemMeta(skullMeta);
		
		p.getWorld().dropItemNaturally(p.getLocation(), head);
	}
}
