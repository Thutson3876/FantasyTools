package me.Zombie__Hunter.fantasytools.cooldowns;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import me.Zombie__Hunter.fantasytools.FantasyTools;
import me.Zombie__Hunter.fantasytools.cooldowns.PlayerCooldownDragonBar;
import me.Zombie__Hunter.fantasytools.traits.Trait;

public class CooldownManager {
	  protected CooldownList cooldownList;
	  
	  private int taskId;
	  
	  private final Map<Player, PlayerCooldownDragonBar> bars = new HashMap<>();
	  
	  public CooldownManager() {
	    this.cooldownList = new CooldownList();
	    
	    init();
	  }
	  
	  public void init() {
	    FantasyTools plugin = FantasyTools.getPlugin();
	    this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new UplinkReducingTask(), 1L, 1L);
	  }
	  
	  public void shutdown() {
	    Bukkit.getScheduler().cancelTask(this.taskId);
	  }
	  
	  public int stillHasCooldown(Player player, Trait trait) {
	    synchronized (this.cooldownList) {
	      if (!this.cooldownList.contains(player, trait))
	        return -1; 
	      return this.cooldownList.get(player, trait).getCooldownTime();
	    } 
	  }
	  
	  public void setCooldown(Player player, Trait trait, int time) {
	    synchronized (this.cooldownList) {
	      if (time <= 0) {
	        this.cooldownList.remove(player, trait);
	      } else {
	        this.cooldownList.add(player, trait, time);
	      } 
	    } 
	  }
	  
	  public List<Trait> getAllCooldownsOfPlayer(Player p) {
	    List<Trait> playerCooldownList = new LinkedList<>();
	    synchronized (this.cooldownList) {
	      for (CooldownContainer container : this.cooldownList) {
	        if (container.getPlayer().equals(p))
	          playerCooldownList.add(container.getTrait()); 
	      } 
	    } 
	    return playerCooldownList;
	  }
	  
	  public Map<Trait, Integer> getAllCooldownsForPlayer(Player p) {
	    Map<Trait, Integer> playerCooldownMap = new HashMap<>();
	    synchronized (this.cooldownList) {
	      for (CooldownContainer container : this.cooldownList) {
	        if (container.getPlayer().equals(p))
	          playerCooldownMap.put(container.getTrait(), Integer.valueOf(container.getCooldownTime())); 
	      } 
	    } 
	    return playerCooldownMap;
	  }
	  
	  protected void tick() {
	    if (this.cooldownList.isEmpty())
	      return; 
	    synchronized (this.cooldownList) {
	      this.cooldownList.tickAll();
	    } 
	      for (Player player : Bukkit.getOnlinePlayers()) {
	        PlayerCooldownDragonBar bar = this.bars.get(player);
	        if (bar == null) {
	        	this.bars.put(player, bar = new PlayerCooldownDragonBar(player)); 
	        }
	        
	        bar.tick();
	      }  
	  }
	  
	  protected class UplinkReducingTask implements Runnable {
	    public void run() {
	    	CooldownManager.this.tick();
	    }
	  }
	  
	  public void clearAllCooldowns() {
	    this.cooldownList.clear();
	  }
}
