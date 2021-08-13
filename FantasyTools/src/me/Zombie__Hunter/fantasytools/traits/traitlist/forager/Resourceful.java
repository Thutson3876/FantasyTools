package me.Zombie__Hunter.fantasytools.traits.traitlist.forager;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class Resourceful extends AbstractBasicTrait {

	private double chanceOnBreak = 0.1;
	private double goldenChance = 0.001;
	private static final List<Material> foods = generateFoods();
	private static final List<Material> leaves = generateLeaves();
	
	public Resourceful(AbstractClassTool tool) {
		super(tool);
		this.skillPointCost = 4;
		this.maximumLevel = 4;
		this.coolDowninTicks = 1 * 20;
	}

	@Override
	public boolean trigger(Event event) {
		if(!(event instanceof BlockBreakEvent)) {
			return false;
		}
		BlockBreakEvent e = (BlockBreakEvent)event;
		Player p = e.getPlayer();
		
		if(!leaves.contains(e.getBlock().getType())) {
			return false;
		}
		
		if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
			return false;
		}
		
		if(!p.getInventory().getItemInMainHand().equals(this.tool.getItemStack()) && !p.getInventory().getItemInOffHand().equals(this.tool.getItemStack())) {
			return false;
		}
		
		Random rng = new Random();
		double rolled = rng.nextDouble();
		if(rolled < this.chanceOnBreak) {
			if(rolled < this.goldenChance) {
				spawnGolden(e.getBlock().getLocation());
			}
			else {
				spawnFood(e.getBlock().getLocation());
			}
			return true;
		}
		
		return false;
	}

	@Override
	public String getName() {
		return "Resourceful";
	}

	@Override
	public String getDescription() {
		return "You are able to collect large amounts of fruits and vegetables with ease.";
	}

	@Override
	public String getActivation() {
		return "Passive: chance when you break leaves with your class tool";
	}

	@Override
	public void levelModifier(int level) {
		this.chanceOnBreak = 0.09 + (0.01 * level);
		this.goldenChance = 0.001 + (0.001 * level);
	}
	
	private void spawnFood(Location loc) {
		Random rng = new Random();
		Material type = foods.get(rng.nextInt(foods.size()));
		
		ItemStack food = new ItemStack(type);
		loc.getWorld().dropItemNaturally(loc, food);
	}
	
	private void spawnGolden(Location loc) {
		loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.GOLDEN_CARROT));
	}
	
	private static List<Material> generateFoods() {
		List<Material> foods = new LinkedList<>();
		foods.add(Material.APPLE);
		foods.add(Material.CARROT);
		foods.add(Material.POTATO);
		foods.add(Material.BEETROOT);
		foods.add(Material.SWEET_BERRIES);
		foods.add(Material.GLOW_BERRIES);
		
		return foods;
	}
	
	private static List<Material> generateLeaves() {
		List<Material> leaves = new LinkedList<>();
		leaves.add(Material.ACACIA_LEAVES);
		leaves.add(Material.AZALEA_LEAVES);
		leaves.add(Material.BIRCH_LEAVES);
		leaves.add(Material.DARK_OAK_LEAVES);
		leaves.add(Material.FLOWERING_AZALEA_LEAVES);
		leaves.add(Material.JUNGLE_LEAVES);
		leaves.add(Material.OAK_LEAVES);
		leaves.add(Material.SPRUCE_LEAVES);
		
		return leaves;
	}
}
