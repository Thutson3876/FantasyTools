package me.Zombie__Hunter.fantasytools.traits.traitlist.fisherman;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class AcquiredTaste extends AbstractBasicTrait {

	private static final List<Material> FISHIES = fishies();
	private int satDuration = 20 * 20;
	private int conduitDuration = 20 * 20;
	
	public AcquiredTaste(AbstractClassTool tool) {
		super(tool);
		this.coolDowninTicks = 25 * 20;
		this.maximumLevel = 5;
		this.skillPointCost = 5;
	}

	@Override
	public boolean trigger(Event event) {
		if(!(event instanceof PlayerItemConsumeEvent)) {
			return false;
		}
		PlayerItemConsumeEvent e = (PlayerItemConsumeEvent)event;
		Player p = e.getPlayer();
		
		if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
			return false;
		}
		
		if(this.isOnCooldown()) {
			return false;
		}
		
		if(!FISHIES.contains(e.getItem().getType())) {
			return false;
		}
		
		applyPotionEffects(p);
		return true;
	}

	@Override
	public String getName() {
		return "Acquired Taste";
	}

	@Override
	public String getDescription() {
		return "Your pallet has grown similar to that of a cat's. Eating fish grants you the powers of a conduit and saturation.";
	}

	@Override
	public String getActivation() {
		return "Passive: on eating fish";
	}

	@Override
	public void levelModifier(int level) {
		this.satDuration = (20 + level*2) * 20;
		this.conduitDuration = (20 + level*2) * 20;
	}
	
	private void applyPotionEffects(Player p) {
		p.addPotionEffect(new PotionEffect(PotionEffectType.CONDUIT_POWER, this.conduitDuration, 0));
		p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, this.satDuration, 0));
	}
	
	private static List<Material> fishies() {
		List<Material> fishies = new LinkedList<>();
		fishies.add(Material.COOKED_COD);
		fishies.add(Material.COOKED_SALMON);
		fishies.add(Material.TROPICAL_FISH);
		fishies.add(Material.PUFFERFISH);
		fishies.add(Material.COD);
		fishies.add(Material.SALMON);
		
		return fishies;
	}
}
