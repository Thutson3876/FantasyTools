package me.Zombie__Hunter.fantasytools.particles;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;

public class ParticleContainer implements Runnable {

	List<Particle> particles = new LinkedList<>();
	List<Location> locations = new LinkedList<>();
	int count = 1;
	int tickRate = 1;
	long duration = 20;
	
	
	
	public ParticleContainer(Particle particle, int count, Location location, int tickRate, long duration) {
		this.particles.add(particle);
		this.locations.add(location);
		this.count = count;
		this.tickRate = tickRate;
		this.duration = duration;
		
	}
	public ParticleContainer(Particle particle, int count, List<Location> locations, int tickRate, long duration) {
		this.particles.add(particle);
		this.locations = locations;
		this.count = count;
		this.tickRate = tickRate;
		this.duration = duration;
	}
	public ParticleContainer(List<Particle> particles, int count, List<Location> locations, List<Double> xOffsets, List<Double> yOffsets, List<Double> zOffsets, int tickRate, long duration) {
		
	}
	
	@Override
	public void run() {
		for(Location loc : this.locations) {
			for(Particle particle : this.particles) {
				loc.getWorld().spawnParticle(particle, loc, this.count);
			}
		}
	}
	
	public List<Particle> getParticles() {
		return particles;
	}
	public List<Location> getLocations() {
		return locations;
	}
	public int getCount() {
		return count;
	}
	public int getTickRate() {
		return tickRate;
	}
	public long getDuration() {
		return duration;
	}
	
	

}
