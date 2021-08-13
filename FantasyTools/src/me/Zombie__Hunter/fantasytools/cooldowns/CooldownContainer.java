package me.Zombie__Hunter.fantasytools.cooldowns;

import org.bukkit.entity.Player;

import me.Zombie__Hunter.fantasytools.traits.Trait;

public class CooldownContainer {
	  private final Player player;
	  
	  private final Trait trait;
	  
	  private int cooldownTime;
	  
	  public CooldownContainer(Player player, Trait trait, int cooldownTime) {
	    this.player = player;
	    this.trait = trait;
	    this.cooldownTime = cooldownTime;
	  }
	  
	  public void tick() {
	    this.cooldownTime--;
	  }
	  
	  public Player getPlayer() {
	    return this.player;
	  }
	  
	  public Trait getTrait() {
	    return this.trait;
	  }
	  
	  public int getCooldownTime() {
	    return this.cooldownTime;
	  }
	  
	  public boolean equals(Object obj) {
	    if (this == obj)
	      return true; 
	    if (obj == null)
	      return false; 
	    if (getClass() != obj.getClass())
	      return false; 
	    CooldownContainer other = (CooldownContainer)obj;
	    if (this.trait.getName() == null) {
	      if (other.trait.getName() != null)
	        return false; 
	    } else if (!this.trait.equals(other.trait)) {
	      return false;
	    } 
	    if (this.cooldownTime != other.cooldownTime)
	      return false; 
	    if (this.player == null) {
	      if (other.player != null)
	        return false; 
	    } else if (!this.player.equals(other.player)) {
	      return false;
	    } 
	    return true;
	  }
	  
	  public String toString() {
	    return this.player.getDisplayName() + " has: " + this.cooldownTime + " on: " + this.trait.getName();
	  }
	}

