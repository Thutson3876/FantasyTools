package me.Zombie__Hunter.fantasytools.cooldowns;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;

import me.Zombie__Hunter.fantasytools.traits.Trait;

public class CooldownList extends LinkedList<CooldownContainer> {
	private static final long serialVersionUID = -4831143921438385640L;

	public boolean contains(Player player, Trait trait) {
	    return (get(player, trait) != null);
	  }
	  
	  public CooldownContainer get(Player player, Trait trait) {
	    for (CooldownContainer container : this) {
	      if (container.getPlayer().equals(player) && 
	        container.getTrait().getName().equalsIgnoreCase(trait.getName()))
	    	  // ^could change back to comparing the traits themselves
	        return container; 
	    } 
	    return null;
	  }
	  
	  public CooldownContainer remove(Player player, Trait trait) {
	    CooldownContainer container = get(player, trait);
	    if (container != null)
	      remove(container); 
	    return container;
	  }
	  
	  public void add(Player player, Trait trait, int cooldownTime) {
	    remove(player, trait);
	    add(new CooldownContainer(player, trait, cooldownTime));
	  }
	  
	  public void tickAll() {
	    List<CooldownContainer> remove = new LinkedList<>();
	    for (CooldownContainer container : this) {
	      container.tick();
	      if (container.getCooldownTime() <= 0)
	        remove.add(container); 
	    }
	    for (CooldownContainer removed : remove)
	      remove(removed); 
	  }
}
