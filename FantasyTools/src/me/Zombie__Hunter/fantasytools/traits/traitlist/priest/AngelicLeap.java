package me.Zombie__Hunter.fantasytools.traits.traitlist.priest;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.Zombie__Hunter.fantasytools.FantasyTools;
import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;
import me.Zombie__Hunter.fantasytools.utils.AbstractActivationWindow;

public class AngelicLeap extends AbstractBasicTrait {

	private int durationInTicks = 4 * 20;
	private double velocityMod = 1.3;
	
	private LeapTimer timer;
	private int timerID = 3;
	
	public AngelicLeap(AbstractClassTool tool) {
		super(tool);
		this.skillPointCost = 3;
		this.maximumLevel = 5;
		this.coolDowninTicks = 16 * 20;
		
		if(this.tool != null) {
			if(this.tool.getOwner() != null) {
				this.timer = new LeapTimer(this.tool.getOwner(), this.getName(), this.durationInTicks, this);
			}
		}
	}

	@Override
	public boolean trigger(Event event) {
		if(!(event instanceof PlayerToggleSneakEvent)) {
			return false;
		}
		PlayerToggleSneakEvent e = (PlayerToggleSneakEvent)event;
		Player p = e.getPlayer();
		if(((Entity)p).isOnGround()) {
			return false;
		}
		
		if(!p.isSneaking()) {
			return false;
		}
		
		if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
			return false;
		}
		
		if(this.isOnCooldown()) {
			if(timer.isOn()) {
				Block below = p.getLocation().getBlock().getRelative(BlockFace.DOWN);
				List<Block> blocks = generateCloud(below);

				if(blocks == null || blocks.isEmpty()) {
					return false;
				}
			
				for(Block block : blocks) {
					block.setType(Material.WHITE_WOOL);
				}
				
				BukkitRunnable task = new BukkitRunnable() {
					
					@Override
					public void run() {
						for(Block block : blocks) {
							if(block.getType().equals(Material.WHITE_WOOL)) {
								block.setType(Material.AIR);
							}
						}
					}
				};
				task.runTaskLater(FantasyTools.getPlugin(), 80);
				stopTimer();
			}
			return false;
		}
		
		if(!this.tool.isHoldingClassTool(p)) {
			return false;
		}
		
		if(!timer.isOn()) {
			p.setVelocity(p.getVelocity().add((new Vector(0, 1.0, 0)).multiply(this.velocityMod)));
			startTimer();
			return true;
		}
		return false;
	}
	
	private List<Block> generateCloud(Block below) {
		List<Block> blocks = new LinkedList<>();
		Block belowBelow = below.getRelative(BlockFace.DOWN);
		blocks.add(below);
		blocks.add(belowBelow);
		blocks.add(belowBelow.getRelative(BlockFace.EAST));
		blocks.add(belowBelow.getRelative(BlockFace.EAST).getRelative(BlockFace.SOUTH));
		blocks.add(belowBelow.getRelative(BlockFace.EAST, 2));
		blocks.add(belowBelow.getRelative(BlockFace.WEST));
		blocks.add(belowBelow.getRelative(BlockFace.SOUTH));
		
		for(Block block : blocks) {
			if(!block.isPassable()) {
				blocks.remove(block);
			}
		}
		
		return blocks;
	}

	@Override
	public String getName() {
		return "Angelic Leap";
	}

	@Override
	public String getDescription() {
		return "Leap high into the air and create a temporary cloud beneath yourself.";
	}

	@Override
	public String getActivation() {
		return "Press and let go of crouch while in the air.";
	}

	@Override
	public void levelModifier(int level) {
		this.velocityMod = 1.2 + (0.1 * level);
	}
	
	private void startTimer() {
		this.timer.init();
		this.timerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(FantasyTools.getPlugin(), this.timer, 0, 1);
	}
	
	public void stopTimer() {
		Bukkit.getScheduler().cancelTask(this.timerID);
		this.timer.deInit();
	}
	
	private class LeapTimer extends AbstractActivationWindow {

		private AngelicLeap trait;
		
		public LeapTimer(Player player, String title, int maxTickDuration, AngelicLeap trait) {
			super(player, title, maxTickDuration);
			this.trait = trait;
		}

		@Override
		public void run() {
			this.update();
		}
		
		public void update() {
			if ((this.tickCounter -= 1) <= 1) {
				this.tickCounter = this.maxTickDuration;
				this.trait.stopTimer();
            } 
			else {
				//System.out.println("Tickcounter: " + this.tickCounter + "/" + this.maxTickDuration);
				this.updateBar();
             }
		}
		
	}
}
