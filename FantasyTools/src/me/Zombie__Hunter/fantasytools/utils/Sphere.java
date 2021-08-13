package me.Zombie__Hunter.fantasytools.utils;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

public class Sphere {

    /**
     *
     * @param centerBlock Define the center of the sphere
     * @param radius Radius of your sphere
     * @param hollow If your sphere should be hollow (you only get the blocks outside) just put in "true" here
     * @return Returns the locations of the blocks in the sphere
     *
     */
    public static List<Location> generateSphere(Location centerBlock, int radius, boolean hollow) {
     
        List<Location> circleBlocks = new ArrayList<Location>();

        int bx = centerBlock.getBlockX();
        int by = centerBlock.getBlockY();
        int bz = centerBlock.getBlockZ();
     
        for(int x = bx - radius; x <= bx + radius; x++) {
            for(int y = by - radius; y <= by + radius; y++) {
                for(int z = bz - radius; z <= bz + radius; z++) {
                 
                    double distance = ((bx-x) * (bx-x) + ((bz-z) * (bz-z)) + ((by-y) * (by-y)));
                 
                    if(distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {
                     
                        Location l = new Location(centerBlock.getWorld(), x, y, z);
                        
                        //checks if air
                        if(l.getBlock().isPassable()) {
                        	circleBlocks.add(l);
                        }

                    }
                }
            }
        }
     
        return circleBlocks;
    }
 
}