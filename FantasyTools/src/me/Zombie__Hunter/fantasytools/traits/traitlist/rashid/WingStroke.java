package me.Zombie__Hunter.fantasytools.traits.traitlist.rashid;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class WingStroke extends AbstractBasicTrait {

	private int hitWindow = 20;
	private PotionEffect haste;
	
	public WingStroke(AbstractClassTool tool) {
		super(tool);
		this.skillPointCost = 5;
		this.maximumLevel = 3;
		this.coolDowninTicks = 5 * 20;
	}
	
	@Override
	public boolean trigger(Event event) {
		if(event instanceof PlayerInteractEvent) {
			PlayerInteractEvent e = (PlayerInteractEvent)event;
			Player p = e.getPlayer();
			if(!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				return false;
			}
			
			if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
				return false;
			}
			if(!p.getInventory().getItemInMainHand().equals(this.tool.getItemStack()) && !p.getInventory().getItemInOffHand().equals(this.tool.getItemStack())) {
				return false;
			}
			
			if(this.isOnCooldown()) {
				return false;
			}
			
			executeDash(p);
			return true;
		}
		else if(event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)event;
			reset(e);
		}
		return false;
	}

	@Override
	public String getName() {
		return "Wing Stroke";
	}

	@Override
	public String getDescription() {
		return "You fly forward and gain a short haste buff. If you hit an enemy while it is active, this ability's cooldown is reset.";
	}

	@Override
	public String getActivation() {
		return "Right click with your class tool in hand";
	}

	@Override
	public void levelModifier(int level) {
		this.coolDowninTicks = (6 - level) * 20;
		this.hitWindow = 20 + (3 * level);
		this.haste = new PotionEffect(PotionEffectType.FAST_DIGGING, this.hitWindow, 1);
	}
	
	private boolean reset(EntityDamageByEntityEvent e) {
		if(!(e.getDamager() instanceof Player)) {
			return false;
		}
		if(!(e.getEntity() instanceof LivingEntity)) {
			return false;
		}
		Player p = (Player)e.getDamager();
		
		if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
			return false;
		}
		
		if(!p.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
			return false;
		}
		
		plugin.getCooldownManager().setCooldown(p, this, 0);
		return false;
	}
	
	private void executeDash(Player p) {
		Vector velocity = p.getLocation().getDirection();
		velocity.multiply(0.6);
		velocity.normalize().add(new Vector(0, 0.2, 0));
		
		p.setVelocity(velocity);
		p.addPotionEffect(this.haste);
	}
}
