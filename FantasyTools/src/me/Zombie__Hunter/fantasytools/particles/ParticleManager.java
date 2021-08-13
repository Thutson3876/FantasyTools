package me.Zombie__Hunter.fantasytools.particles;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;

import me.Zombie__Hunter.fantasytools.FantasyTools;
import me.Zombie__Hunter.fantasytools.traits.Trait;

public class ParticleManager implements Runnable {

	private int taskID = 0;
	private List<Trait> traits = new LinkedList<>();
	
	public ParticleManager() {
		init();
	}
	
	private void init() {
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(FantasyTools.getPlugin(), this, 1L, 1L);
	}
	
	public void deInit() {
		Bukkit.getScheduler().cancelTask(this.taskID);
	}
	
	public void update() {
		deInit();
		init();
	}
	
	public void add(Trait trait) {
		this.traits.add(trait);
	}
	
	public void add(List<Trait> traits) {
		this.traits.addAll(traits);
	}
	
	public void remove(Trait trait) {
		this.traits.remove(trait);
	}
	public void remove(List<Trait> traits) {
		this.traits.removeAll(traits);
	}

	@Override
	public void run() {
		for(Trait trait : this.traits) {
			trait.particles();
		}
	}
}
