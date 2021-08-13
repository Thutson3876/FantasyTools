package me.Zombie__Hunter.fantasytools.listeners.traitlisteners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRiptideEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import me.Zombie__Hunter.fantasytools.FantasyTools;

public class PlayerListener implements Listener {

	private static final FantasyTools plugin = FantasyTools.getPlugin();
	
	public PlayerListener() {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent e) {
		plugin.triggerAllTraits(e);
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		plugin.triggerAllTraits(e);
	}
	
	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
		plugin.triggerAllTraits(e);
		
		if(plugin.getCurrentEventCancelled()) {
			//plugin.log("drop event cancelled");
			e.setCancelled(true);
			plugin.setCurrentEventCancelled(false);
		}
	}
	
	@EventHandler
	public void onPlayerSwapHandsEvent(PlayerSwapHandItemsEvent e) {
		plugin.triggerAllTraits(e);
	}
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent e) {
		plugin.triggerAllTraits(e);
		
		if(plugin.getCurrentEventCancelled()) {
			e.setCancelled(true);
			plugin.setCurrentEventCancelled(false);
		}
	}
	
	@EventHandler
	public void onPlayerToggleFlightEvent(PlayerToggleFlightEvent e) {
		plugin.triggerAllTraits(e);
		
		if(plugin.getCurrentEventCancelled()) {
			e.setCancelled(true);
			plugin.setCurrentEventCancelled(false);
		}
	}
	
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e) {
		plugin.triggerAllTraits(e);
	}
	
	@EventHandler
	public void onPlayerFishEvent(PlayerFishEvent e) {
		plugin.triggerAllTraits(e);
	}
	
	@EventHandler
	public void onPlayerEatEvent(PlayerItemConsumeEvent e) {
		plugin.triggerAllTraits(e);
	}
	
	@EventHandler
	public void onPlayerRiptideEvent(PlayerRiptideEvent e) {
		plugin.triggerAllTraits(e);
	}
}
