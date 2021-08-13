package me.Zombie__Hunter.fantasytools.traits.traitlist.barbarian;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.Zombie__Hunter.fantasytools.utils.TraitUtils;
import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class DeepDive extends AbstractBasicTrait {
	
	private float power = 2.0F;
	private double dropSpeed = 2.0;
	private double projectedEntitiesSpeed = 0.9;
	private double minDropHeight = 3;
	
	public DeepDive(AbstractClassTool tool) {
		super(tool);
		this.skillPointCost = 8;
		this.maximumLevel = 3;
		this.coolDowninTicks = 16 * 20;
	}

	@Override
	public String getName() {
		return "Deep Dive";
	}

	@Override
	public String getDescription() {
		return "Dive down from a great height to demolish your foes and the ground below.";
	}
	
	@Override
	public String getActivation() {
		return "Crouch while at least 3 blocks above any ground.";
	}

	@Override
	public void levelModifier(int level) {
		this.power = 1.5F + (level * 0.5F);
		this.projectedEntitiesSpeed = 0.9 + (0.15 * level);
	}

	@Override
	public int getCurrentLevel() {
		return this.currentLevel;
	}
	
	
	@Override
	public boolean trigger(Event event) {
		if(!(event instanceof PlayerToggleSneakEvent)) {
			return false;
		}
		
		PlayerToggleSneakEvent e = (PlayerToggleSneakEvent) event;
		Player p = e.getPlayer();
		
		if(TraitUtils.getHeightAboveGround(p) < this.minDropHeight) {
			return false;
		}
		
		if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
			return false;
		}
		
		if(this.isOnCooldown()) {
			return false;
		}
		
		return tempGroundPound(p, projectedEntitiesSpeed);
	}
	
	private boolean tempGroundPound(Player p, double speed) {
		Location pLoc = p.getLocation();
		
		double height = TraitUtils.getHeightAboveGround(p);
		
		p.setFallDistance(-(float)height);
		TraitUtils.moveToward(p, p.getLocation().subtract(0, height, 0), dropSpeed);
		
		World world = p.getWorld();
		Block[] blocksUnderPlayer = TraitUtils.getBlocksAroundPlayer(p, 5);
		
		if(blocksUnderPlayer[0] == null) {
			return false;
		}
		Random rng = new Random();
		
		Vector yAxis = new Vector(0, 1, 0);
		
		BukkitRunnable task = new BukkitRunnable() {

			@Override
			public void run() {
				Location bLoc = null;
				
				for(Block b : blocksUnderPlayer) {
					if(b != null) {
						bLoc = b.getLocation();
						FallingBlock temp = world.spawnFallingBlock(bLoc, b.getBlockData());
						
						double x = bLoc.getX() - pLoc.getX();
				        double y = bLoc.getY() - pLoc.getY();
				        double z = bLoc.getZ() - pLoc.getZ();
				        double angle = (Math.PI / 2) + 0.4*rng.nextDouble() - 0.2;
				        double yBoost = 1.0;
				        
				        Vector velocity = new Vector(x, y + yBoost, z).normalize().rotateAroundAxis(yAxis, angle).multiply(-speed);
				        
				        b.setType(Material.AIR);
						temp.setVelocity(velocity);
					}
				}
				p.setInvulnerable(true);
				//System.out.println("Invulnerable!");
				world.createExplosion(p.getLocation(), power, false, true, p);
				p.setInvulnerable(false);
				//System.out.println("No Longer Invulnerable!");
			}
		};
		
		task.runTaskLater(plugin, Math.round(height - (dropSpeed*1.2)));
		
		return true;
	}

	@SuppressWarnings("unused")
	private boolean tempAssassin(Player p) {
		double damage = 6.0;
		double dropSpeed = 2.0;
		
		double x = 1.7;
		double y = 20.0;
		double z = 1.7;

		Entity[] nearbyEntities = p.getNearbyEntities(x, y, z).toArray(new Entity[1]);
		Entity closestEntity = null;
		
		nearbyEntities = TraitUtils.onlyLiving(nearbyEntities);
		closestEntity = TraitUtils.closestEntity(p, nearbyEntities, 2.0);
		int height = 0;
		
		
		if(closestEntity != null) {
			height = (int)Math.round(p.getLocation().getY() - closestEntity.getLocation().getY());
			TraitUtils.moveToward(p, closestEntity.getLocation(), dropSpeed);
			p.setFallDistance(-height);
			((LivingEntity)closestEntity).damage(damage, p);
			//EntityDamageByEntityEvent damageEvent = new EntityDamageByEntityEvent(p, closestEntity, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage);
			//plugin.getServer().getPluginManager().callEvent(damageEvent);
			
			return true;
		}
		
		return false;
	}
}
