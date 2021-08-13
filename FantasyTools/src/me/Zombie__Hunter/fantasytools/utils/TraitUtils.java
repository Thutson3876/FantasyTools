package me.Zombie__Hunter.fantasytools.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class TraitUtils {

	public static boolean heal(LivingEntity e, double healAmt) {
		if(e == null) {
			return false;
		}
		
		double maxHealth = e.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		double currentHealth = e.getHealth();
		if(currentHealth == maxHealth) {
			return false;
		}
		double heal = currentHealth + healAmt;
		
		if(heal >= maxHealth) {
			e.setHealth(maxHealth);
		}
		else {
			e.setHealth(heal);
		}
		return true;
	}
	
	//Measures the distance between player and all nearby entities and selects the closest (ignoring y distance)
	public static Entity closestEntity(Player p, Entity[] nearbyEntities, double minHeight) {
		Entity closest = null;
		double distanceToPlayer = 1000; //Specifically the distance from the player without taking the y value into account (only differences between X and Z)
		
		
		for(Entity e : nearbyEntities) {
			if(e == null) {
				return null;
			}
			Location entityLoc = e.getLocation();
			Location playerLoc = p.getLocation();
			
			if(playerLoc.getY() - entityLoc.getY() > minHeight) {
				double xDiffSqrd = (playerLoc.getX() - entityLoc.getX()) * (playerLoc.getX() - entityLoc.getX());
				double zDiffSqrd = (playerLoc.getZ() - entityLoc.getZ()) * (playerLoc.getZ() - entityLoc.getZ());
				
				//distance formula
				if(distanceToPlayer > Math.sqrt(xDiffSqrd + zDiffSqrd)) {
					closest = e;
				}
			}
		}
		
		return closest;
	}
	
    public static LivingEntity getCursorEntity(Player player, double range){
        Block targetBlock = player.getTargetBlock(null, (int)Math.round(range));
        Location blockLoc = targetBlock.getLocation();
        double bx = blockLoc.getX();
        double by = blockLoc.getY();
        double bz = blockLoc.getZ();
        List<Entity> nearby = player.getNearbyEntities(range, range, range);
 
        for(Entity entity : nearby){
            if(entity instanceof LivingEntity){
                Location loc = entity.getLocation();
                double ex = loc.getX();
                double ey = loc.getY();
                double ez = loc.getZ();
 
                if((bx - 1.5 <= ex && ex <= bx + 2) && (bz - 1.5 <= ez && ez <= bz + 2) && (by - 1 <= ey && ey <= by + 2.5)){
                    return (LivingEntity) entity;
                }
            }
        }
 
        return null;
    }
	
	public static Entity getCursorEntity(Player p, double distance, double maxAngle) {
		List<Entity> enemies = getEntitiesInAngle(p, distance, maxAngle);
		double angle;
		Vector dirToDestination;
		Vector playerDirection;
		Entity e = null;
		double eAngle = maxAngle;
		boolean first = true;
		Entity entity;
		
		for(int i = 0; i < enemies.size(); i++) {
			entity = enemies.get(i);
			if(p.hasLineOfSight(entity)) {
				dirToDestination = enemies.get(i).getLocation().toVector().subtract(p.getEyeLocation().toVector());
				playerDirection = p.getEyeLocation().getDirection();
				angle = dirToDestination.angle(playerDirection);
				
				if(angle<maxAngle && angle>-maxAngle && eAngle<angle) {
					e = entity;
					eAngle = angle;
				}
				else if(first) {
					e = entity;
					eAngle = angle;
					first = false;
				}
			}
		}
		
		if(!first || (first && eAngle<maxAngle && eAngle>-maxAngle)) {
			return e;
		}
		else {
			return null;
		}
	}
	
	public static List<Material> getCropMaterials(){
		List<Material> materials = new LinkedList<>();
		materials.add(Material.POTATOES);
		materials.add(Material.CARROTS);
		materials.add(Material.WHEAT);
		materials.add(Material.BEETROOTS);
		materials.add(Material.COCOA_BEANS);
		materials.add(Material.MELON_STEM);
		materials.add(Material.ATTACHED_MELON_STEM);
		materials.add(Material.PUMPKIN_STEM);
		materials.add(Material.ATTACHED_PUMPKIN_STEM);
		return materials;
	}
	
	public static List<Location> generateRectangle(Location center, double xLength, double zLength){
		List<Location> locations = new LinkedList<>();
		double tempX = -(xLength / 2);
		double tempZ = -(zLength / 2);
		double y = center.getY();
		Location temp;
		
		for(int i = 0; i < xLength; i++) {
			tempX += i;
			for(int j = 0; j < zLength; j++) {
				tempZ += j;
				
				temp = new Location(center.getWorld(), center.getX() + tempX, y, center.getZ() + tempZ);
				locations.add(temp);
			}
		}
		
		return locations;
	}
	
	public static List<Location> generateCuboid(Location center, double xLength, double yLength, double zLength) {
		List<Location> locations = new LinkedList<>();
		
		double tempX = -(xLength / 2);
		double tempZ = -(zLength / 2);
		double tempY = -(yLength / 2);
		Location temp;
		
		for(int i = 0; i < xLength; i++) {
			tempX += i;
			for(int j = 0; j < zLength; j++) {
				tempZ += j;
				for(int l = 0; l < yLength; l++) {
					tempY += l;
					temp = new Location(center.getWorld(), center.getX() + tempX, center.getY() + tempY, center.getZ() + tempZ);
					//System.out.println("X: " + temp.getX() + " Y: " + temp.getY() + " Z: " + temp.getZ());
					locations.add(temp);
				}
			}
		}
		
		return locations;
	}
	
	public static Entity closestEntityFromList(List<Entity> list, Entity entity) {
		if(list == null) {
			return null;
		}
		if(list.isEmpty()) {
			return null;
		}
		
		Entity closest = null;
		double distance = 10000;
		double tempDistance = 1000;
		for(Entity e : list) {
			tempDistance = e.getLocation().distance(entity.getLocation());
			if(tempDistance < distance) {
				distance = tempDistance;
				closest = e;
			}
		}
		
		return closest;
	}
	
	public static List<Entity> getEntitiesInAngle(Player p, double maxAngle, double maxDistance){
		Vector dirToDestination;
		Vector playerDirection;
		double angle;
		List<Entity> enemies = p.getNearbyEntities(maxDistance, maxDistance, maxDistance);
		List<Entity> targets = new LinkedList<>();

		for(Entity e : enemies) {
			if(p.hasLineOfSight(e)) {
				dirToDestination = e.getLocation().toVector().subtract(p.getEyeLocation().toVector());
				playerDirection = p.getEyeLocation().getDirection();
				angle = dirToDestination.angle(playerDirection);
				
				if(angle<maxAngle && angle>-maxAngle) {
					targets.add(e);
				}
			}
		}
		
		return targets;
	}
	
	//Filters any non-living entities out of an array of entities
	public static Entity[] onlyLiving(Entity[] entities) {
		ArrayList<Entity> entitiesAsList = new ArrayList<>();
		for(Entity e : entities) {
			if(e instanceof LivingEntity) {
				entitiesAsList.add(e);
			}
		}
		
		return entitiesAsList.toArray(new Entity[1]);
	}
	
	public static void moveToward(Entity entity, Location to, double speed) {
        Location loc = entity.getLocation();
        double x = loc.getX() - to.getX();
        double y = loc.getY() - to.getY();
        double z = loc.getZ() - to.getZ();
        Vector velocity = new Vector(x, y, z).normalize().multiply(-speed);
        entity.setVelocity(velocity);   
    }
	
	public static void moveTowardPlusY(Entity entity, Location to, double speed, double bonusY) {
        Location loc = entity.getLocation();
        double x = loc.getX() - to.getX();
        double y = loc.getY() - to.getY() + bonusY;
        double z = loc.getZ() - to.getZ();
        Vector velocity = new Vector(x, y, z).normalize().multiply(-speed);
        entity.setVelocity(velocity);   
    }
	
	public static Block[] getBlocksAroundPlayer(Player p, int radius) {
		ArrayList<Block> blockList = new ArrayList<>();
		Location playerLoc = p.getLocation();
		Block temp = null;
		
		for(Location loc : Sphere.generateSphere(playerLoc, radius, false)) {
			temp = loc.getBlock();
			blockList.add(temp);
		}
		
		return blockList.toArray(new Block[1]);
	}
	
	public static double getHeightAboveGround(Entity e) {
		if(e.isOnGround()) {
			return 0;
		}
		
		Location eLoc = e.getLocation();
		Block currentBlock = eLoc.getBlock();
		Block nextBlock;
		
		for(int i = 0; i < 2048; i++) {
			nextBlock = currentBlock.getRelative(BlockFace.DOWN);
			if(!nextBlock.isPassable()) {
				return eLoc.distance(currentBlock.getLocation());
			}
			currentBlock = nextBlock;
		}
		
		return -999;
	}
	
	public static Block getBlockBelowEntity(Entity e) {
		Location eLoc = e.getLocation();
		Block currentBlock = eLoc.getBlock();
		Block nextBlock;
		
		for(int i = 0; i < 2048; i++) {
			nextBlock = currentBlock.getRelative(BlockFace.DOWN);
			if(!nextBlock.isPassable()) {
				return nextBlock;
			}
			currentBlock = nextBlock;
		}
		return null;
	}
}
