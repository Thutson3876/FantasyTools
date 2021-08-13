package me.Zombie__Hunter.fantasytools.playermanagement;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import me.Zombie__Hunter.fantasytools.FantasyTools;
import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;

public class PlayerManager {
	private Set<FantasyPlayer> players = new HashSet<>();
	
	public PlayerManager() {
		init();
	}
	
	public Set<FantasyPlayer> getFantasyPlayers() {
		return players;
	}
	
	public FantasyPlayer getPlayer(Player player) {
		for(FantasyPlayer p : players) {
			if(p.getPlayer().equals(player)) {
				return p;
			}
		}
		
		return null;
	}
	
	private void init(){
		for(Player p : Bukkit.getOnlinePlayers()) {
			players.add(new FantasyPlayer(p));
		}
	}
	
	public boolean addPlayer(Player player) {
		if(player == null || contains(player)) {
			return false;
		}
		
		players.add(new FantasyPlayer(player));
		return true;
	}
	
	public void removePlayer(Player player) {
		FantasyPlayer fPlayer = getPlayer(player);
		if(fPlayer == null) {
			return;
		}
		
		players.remove(fPlayer);
	}
	
	public boolean contains(Player player) {
		for(FantasyPlayer p : players) {
			if(p.getPlayer().equals(player)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean checkAdvancementValidity(Advancement adv) {
		Collection<String> criteriaList = adv.getCriteria();
		for(String criteria : criteriaList) {
			if(criteria.equalsIgnoreCase("has_the_recipe")) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean hasNonToolUnbreakableItems(Player p) {
		AbstractClassTool tool = FantasyTools.getPlugin().getToolManager().getTool(p);
		boolean hasTool = true;
		if(tool == null) {
			hasTool = false;
		}
		
		for(ItemStack item : p.getInventory().getStorageContents()) {
			if(item == null) {
				continue;
			}
			if(item.getItemMeta().isUnbreakable()) {
				if(hasTool) {
					if(tool.getItemStack().equals(item)) {
						continue;
					}
					else {
						return true;
					}
				}
				else {
					return true;
				}
			}
		}
		return false;
	}
	
	public static List<ItemStack> getUnbreakableItems(Player p){
		List<ItemStack> unbreakableItems = new LinkedList<>();
		for(ItemStack item : p.getInventory().getStorageContents()) {
			if(item == null) {
				continue;
			}
			if(item.getItemMeta().isUnbreakable()) {
				unbreakableItems.add(item);
			}
		}
		
		return unbreakableItems;
	}
	
	public static boolean removeAllUnbreakablesButTool(Player p) {
		AbstractClassTool tool = FantasyTools.getPlugin().getToolManager().getTool(p);
		boolean hasTool = true;
		boolean hasNonTool = false;
		if(tool == null) {
			hasTool = false;
		}
		
		List<ItemStack> unbreakables = getUnbreakableItems(p);
		if(unbreakables.isEmpty()) {
			return false;
		}
		PlayerInventory inventory = p.getInventory();
		
		for(ItemStack item : unbreakables) {
			if(hasTool) {
				if(tool.getItemStack().equals(item)) {
					continue;
				}
			}
			inventory.remove(item);
			ItemMeta meta = item.getItemMeta();
			meta.setUnbreakable(false);
			item.setItemMeta(meta);
			inventory.addItem(item);
			hasNonTool = true;
		}
		
		return hasNonTool;
	}
	
	public static void makeAllBreakable(Player p) {
		List<ItemStack> unbreakables = getUnbreakableItems(p);
		
		for(ItemStack item : unbreakables) {
			ItemMeta meta = item.getItemMeta();
			meta.setUnbreakable(false);
			item.setItemMeta(meta);
		}
	}
}
