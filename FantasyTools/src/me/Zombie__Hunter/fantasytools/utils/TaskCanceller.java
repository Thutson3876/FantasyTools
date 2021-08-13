package me.Zombie__Hunter.fantasytools.utils;

import org.bukkit.Bukkit;

public class TaskCanceller implements Runnable {

	private int taskID;
	
	public TaskCanceller(int taskID) {
		this.taskID = taskID;
	}
	
	@Override
	public void run() {
		if(Bukkit.getScheduler().isCurrentlyRunning(this.taskID)) {
			Bukkit.getScheduler().cancelTask(this.taskID);
		}
	}
}
