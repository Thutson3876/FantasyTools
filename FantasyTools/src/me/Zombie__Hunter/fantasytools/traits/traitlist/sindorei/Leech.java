package me.Zombie__Hunter.fantasytools.traits.traitlist.sindorei;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;
import me.Zombie__Hunter.fantasytools.utils.TraitUtils;

public class Leech extends AbstractBasicTrait {

	//private double originalMaxHealth = 20.0;
	private double leechAmt = 3.0; 
	//private double maxHealthModifier = 1.0;
	
	//private int tickCounter = 0;
	
	public Leech(AbstractClassTool tool) {
		super(tool);
		this.skillPointCost = 7;
		this.maximumLevel = 5;
		this.coolDowninTicks = 6 * 20;
	}
	
	@Override
	public void init() {
		if(this.tool.getOwner() == null) {
			return;
		}
	}
	
	@Override
	public void deInit() {
		if(this.tool.getOwner() == null) {
			return;
		}
	}

	@Override
	public boolean trigger(Event event) {
		if(!(event instanceof EntityDamageByEntityEvent)) {
			return false;
		}
		EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)event;
		if(!(e.getDamager() instanceof Player)) {
			return false;
		}
		Player p = (Player)e.getDamager();
		
		if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
			return false;
		}
		if(!p.getInventory().getItemInMainHand().equals(this.tool.getItemStack()) && !p.getInventory().getItemInOffHand().equals(this.tool.getItemStack())) {
			return false;
		}
		if(this.isOnCooldown()) {
			return false;
		}
		if(!e.getCause().equals(DamageCause.ENTITY_ATTACK) && !e.getCause().equals(DamageCause.PROJECTILE)) {
			return false;
		}
		
		return TraitUtils.heal(p, this.leechAmt);
	}

	@Override
	public String getName() {
		return "Leech";
	}

	@Override
	public String getDescription() {
		return "Siphon health from your foe with your strike.";
	}
	
	@Override
	public String getActivation() {
		return "Passive: when you hit something with your class tool";
	}

	@Override
	public void levelModifier(int level) {
		this.coolDowninTicks = (7 - level) * 20; 
		//this.maxHealthModifier = (2.0 * level);
		
		//Player p = this.getTool().getOwner();
		//p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue() + maxHealthModifier);
	}

}
