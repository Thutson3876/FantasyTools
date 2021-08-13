package me.Zombie__Hunter.fantasytools.listeners.traitlisteners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import me.Zombie__Hunter.fantasytools.FantasyTools;

public class EntityListener implements Listener {

		private static final FantasyTools plugin = FantasyTools.getPlugin();
		
		public EntityListener() {
			Bukkit.getPluginManager().registerEvents(this, plugin);
		}
		
		@EventHandler
		public void onItemDespawnEvent(ItemDespawnEvent e) {
			plugin.triggerAllTraits(e);
		}
		
		@EventHandler
		public void onEntityDamagedByEntityEvent(EntityDamageByEntityEvent e) {
			plugin.triggerAllTraits(e);
		}
		
		@EventHandler
		public void onEntityDamageEvent(EntityDamageEvent e) {
			plugin.triggerAllTraits(e);
		}
		
		@EventHandler
		public void onEntityShootBowEvent(EntityShootBowEvent e) {
			plugin.triggerAllTraits(e);
		}
		
		@EventHandler
		public void onProjectileHitEvent(ProjectileHitEvent e) {
			plugin.triggerAllTraits(e);
		}
		
		@EventHandler
		public void onProjectileLaunchEvent(ProjectileLaunchEvent e) {
			plugin.triggerAllTraits(e);
		}
		
		@EventHandler
		public void onCreatureSpawnEvent(CreatureSpawnEvent e) {
			plugin.triggerAllTraits(e);
			
			if(plugin.getCurrentEventCancelled()) {
				e.setCancelled(true);
				plugin.setCurrentEventCancelled(false);
			}
		}
		
		@EventHandler
		public void onEntityToggleGlideEvent(EntityToggleGlideEvent e) {
			plugin.triggerAllTraits(e);
			
			if(plugin.getCurrentEventCancelled()) {
				e.setCancelled(true);
				plugin.setCurrentEventCancelled(false);
			}
		}
		
		@EventHandler
		public void onEntityDeathEvent(EntityDeathEvent e) {
			plugin.triggerAllTraits(e);
		}
		
		@EventHandler
		public void onEntityInteractEvent(EntityInteractEvent e) {
			plugin.triggerAllTraits(e);
		}
		
		@EventHandler
		public void onWeatherChangeEvent(WeatherChangeEvent e) {
			plugin.triggerAllTraits(e);
		}
}
