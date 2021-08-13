package me.Zombie__Hunter.fantasytools.traits.traitlist.misc;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.util.Vector;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;
import me.Zombie__Hunter.fantasytools.utils.TraitUtils;

public class Finisher extends AbstractBasicTrait {
	
	private int duration = 4 * 20;
	private double minHeight = 4.0;
	private boolean isBarToggled = false;
	private BossBar bar;
	
	public Finisher(AbstractClassTool tool) {
		super(tool);
		this.skillPointCost = 8;
		this.maximumLevel = 1;
		this.coolDowninTicks = 16 * 20;
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
			
			toggleBar();
			return false;
		}
		else if(event instanceof EntityDamageByEntityEvent) {
			if(!this.isBarToggled) {
				return false;
			}
			
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)event;
			if(!(e.getDamager() instanceof Player)) {
				return false;
			}
			Player p = (Player)e.getDamager();
			if(isOnCooldown()) {
				return false;
			}
			
			if(finish(p, e.getEntity())) {
				toggleBar();
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "Finisher";
	}

	@Override
	public String getDescription() {
		return "Toggle your next strike to launch a floating foe down into a pit of spikes.";
	}

	@Override
	public String getActivation() {
		return "Press your drop key while holding your class tool and then hit an entity thats in the air";
	}

	@Override
	public void levelModifier(int level) {
		if(this.tool != null) {
			if(this.tool.getOwner() != null) {
				this.bar = Bukkit.createBossBar(this.getName(), BarColor.PINK, BarStyle.SEGMENTED_20, new org.bukkit.boss.BarFlag[0]);
				this.bar.setProgress(1.0);
				this.bar.addPlayer(this.tool.getOwner());
				this.bar.setVisible(false);
			}
		}
	}
	
	private void toggleBar(){
		if(this.isBarToggled) {
			bar.setVisible(false);
			this.isBarToggled = false;
			return;
		}
		bar.setVisible(true);
		this.isBarToggled = true;
	}
	
	private boolean finish(Player p, Entity e) {
		double entityHeight = TraitUtils.getHeightAboveGround(e);
		if(entityHeight < this.minHeight) {
			return false;
		}
		
		List<Block> blocks = new LinkedList<>();
		Block below = TraitUtils.getBlockBelowEntity(e).getRelative(BlockFace.UP);
		blocks.add(below);
		for(Location loc : TraitUtils.generateRectangle(below.getLocation(), 1, 1)) {
			Block b = loc.getBlock();
			if(!blocks.contains(b) && b.isPassable()) {
				blocks.add(b);
			}
		}
		
		for(Block b : blocks) {
			b.setType(Material.POINTED_DRIPSTONE);
		}
		
		e.setVelocity(new Vector(0, -0.05, 0));
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {

			@Override
			public void run() {
				for(Block b : blocks) {
					if(b.getType().equals(Material.POINTED_DRIPSTONE)) {
						b.setType(Material.AIR);
					}
				}
			}
			
		}, this.duration);
		return true;
	}
}
