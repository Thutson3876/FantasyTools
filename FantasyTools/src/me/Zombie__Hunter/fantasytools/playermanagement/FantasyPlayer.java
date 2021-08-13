package me.Zombie__Hunter.fantasytools.playermanagement;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;

public class FantasyPlayer {
	private Player player;
	private int skillPoints = 0;
	private Iterator<Advancement> advancements = Bukkit.getServer().advancementIterator();
	
	public FantasyPlayer(Player player) {
		this.player = player;
		this.skillPoints = this.determineSkillPointsFromAdvancements();
		
	}
	
	public FantasyPlayer(Player player, int skillPoints) {
		this.player = player;
		this.skillPoints = skillPoints;
	}
	
	private int determineSkillPointsFromAdvancements() {
		int points = 0;
		while(advancements.hasNext()) {
			Advancement adv = advancements.next();
			if(player.getAdvancementProgress(adv).isDone() && PlayerManager.checkAdvancementValidity(adv)) {
				//System.out.println(adv.getCriteria());
				points++;
			}
		}
		return points;
	}
	
	public void addSkillPoints(int amt) {
		this.skillPoints += amt;
	}

	public boolean spendSkillPoints(int amt) {
		int newAmt = this.skillPoints - amt;
		if(newAmt < 0) {
			return false;
		}
		
		this.skillPoints = newAmt;
		return true;
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getSkillPoints() {
		return skillPoints;
	}

	public void setSkillPoints(int skillPoints) {
		this.skillPoints = skillPoints;
	}
	
}
