package me.Zombie__Hunter.fantasytools.cooldowns;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import me.Zombie__Hunter.fantasytools.FantasyTools;
import me.Zombie__Hunter.fantasytools.traits.Trait;

public class PlayerCooldownDragonBar {
		  private final Player player;
		  
		  private final Map<Trait, BossBar> map = new HashMap<>();
		  
		  public PlayerCooldownDragonBar(Player player) {
		    this.player = player;
		  }
		  
		  public void tick() {
		    Player player = this.player;
		    if (player == null || !player.isOnline()) {
		      if (!this.map.isEmpty()) {
		        for (BossBar bar : this.map.values()) {
		          bar.removeAll();
		          bar.setVisible(false);
		        } 
		        this.map.clear();
		      } 
		      return;
		    } 
		    
		    Map<Trait, Integer> cooldowns = FantasyTools.getPlugin().getAllCooldownsForPlayer(player);
		    for (Map.Entry<Trait, Integer> entry : cooldowns.entrySet()) {
		      Trait trait = entry.getKey();
		      double remaining = entry.getValue();
		      double max = trait.getMaxCooldown();
		      BossBar bar = this.map.get(trait);
		      if (bar == null && remaining <= 1) {
		    	  continue; 
		      }
		    	  
		      if (bar == null && remaining > 1) {
		        bar = Bukkit.createBossBar(trait.getName(), BarColor.RED, BarStyle.SEGMENTED_20, new org.bukkit.boss.BarFlag[0]);
		        bar.addPlayer(player);
		        this.map.put(trait, bar);
		        continue;
		      } 
		      if (bar != null && remaining <= 1) {
		        bar.setVisible(false);
		        continue;
		      } 
		      if (bar != null && remaining > 1) {
		        double value = remaining / max;
		        value = Math.min(Math.max(0.0D, value), 1.0D);
		        bar.setProgress(value);
		        if (!bar.isVisible()) {
		        	bar.setVisible(true); 
		        }
		          
		      } 
		    } 
		  }
}
