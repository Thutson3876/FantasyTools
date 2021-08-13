package me.Zombie__Hunter.fantasytools.traits.traitlist.fisherman;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootContext.Builder;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class FishyBusiness extends AbstractBasicTrait {
	
	public FishyBusiness(AbstractClassTool tool) {
		super(tool);
		this.coolDowninTicks = 30 * 20;
		this.maximumLevel = 1;
		this.skillPointCost = 15;
	}

	@Override
	public boolean trigger(Event event) {
		if(!(event instanceof PlayerFishEvent)) {
			return false;
		}
		
		PlayerFishEvent e = (PlayerFishEvent)event;
		if(!e.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY)) {
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
		
		Entity entity = e.getCaught();
		if(!(entity instanceof LivingEntity)) {
			return false;
		}
		
		Random rng = new Random();
		Location loc = p.getLocation();
		if(entity instanceof Player) {
			Player target = (Player) entity;
			PlayerInventory inv = target.getInventory();
			ItemStack[] contents = inv.getContents();
			ItemStack stolenItem = contents[rng.nextInt(contents.length)];
			inv.remove(stolenItem);

			p.getWorld().dropItemNaturally(loc, stolenItem);
			return true;
		}
		else if(entity instanceof Mob) {
			Mob target = (Mob) entity;
			LootContext.Builder builder = new Builder(loc);
			
			Builder b = builder;
            b = b.killer(p);
            b = b.lootedEntity(entity);
            LootContext lc = b.build();
			
			for(ItemStack item : target.getLootTable().populateLoot(rng, lc)) {
				p.getWorld().dropItemNaturally(loc, item);
			}
			
			return true;
		}
		
		return false;
	}

	@Override
	public String getName() {
		return "Fishy Business";
	}

	@Override
	public String getDescription() {
		return "Fishing a player will pull something from their inventory. Fishing a mob will pull bonus drops from them.";
	}

	@Override
	public String getActivation() {
		return "Passive: on hook of living entity";
	}

	@Override
	public void levelModifier(int level) {}
	
	
}
