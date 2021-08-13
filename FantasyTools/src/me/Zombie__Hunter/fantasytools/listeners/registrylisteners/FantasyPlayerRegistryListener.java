package me.Zombie__Hunter.fantasytools.listeners.registrylisteners;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import me.Zombie__Hunter.fantasytools.FantasyTools;
import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.playermanagement.FantasyPlayer;
import me.Zombie__Hunter.fantasytools.playermanagement.PlayerManager;

public class FantasyPlayerRegistryListener implements Listener {
	private static final FantasyTools plugin = FantasyTools.getPlugin();
	
	public FantasyPlayerRegistryListener() {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		plugin.getPlayerManager().addPlayer(e.getPlayer());
		e.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(e.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
	}
	
	@EventHandler
	public void onPlayerLeaveEvent(PlayerQuitEvent e) {
		plugin.getPlayerManager().removePlayer(e.getPlayer());
		
		PlayerManager.makeAllBreakable(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayerKickEvent(PlayerKickEvent e) {
		plugin.getPlayerManager().removePlayer(e.getPlayer());
		
		PlayerManager.makeAllBreakable(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayerAdvancement(PlayerAdvancementDoneEvent e) {
		FantasyPlayer player = plugin.getPlayerManager().getPlayer(e.getPlayer());
		if(PlayerManager.checkAdvancementValidity(e.getAdvancement())) {
			player.addSkillPoints(1);
		}
	}
	
	@EventHandler
	public void onPlayerEnchantEvent(EnchantItemEvent e) {
		AbstractClassTool tool = plugin.getToolManager().getTool(e.getEnchanter());
		if(tool == null) {
			return;
		}
		
		if(e.getItem().equals(tool.getItemStack())) {
			System.out.println(tool.getOwner().getDisplayName() + " added enchants to their tool.");
			ItemStack temp = tool.getItemStack().clone();
			temp.addEnchantments(e.getEnchantsToAdd());
			tool.setEnchantments(temp.getEnchantments());
		}
	}
}
