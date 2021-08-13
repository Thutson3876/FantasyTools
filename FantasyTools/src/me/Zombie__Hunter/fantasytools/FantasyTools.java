package me.Zombie__Hunter.fantasytools;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.classtools.ClassToolManager;
import me.Zombie__Hunter.fantasytools.commands.CommandManager;
import me.Zombie__Hunter.fantasytools.cooldowns.CooldownManager;
import me.Zombie__Hunter.fantasytools.listeners.registrylisteners.FantasyPlayerRegistryListener;
import me.Zombie__Hunter.fantasytools.listeners.traitlisteners.EntityListener;
import me.Zombie__Hunter.fantasytools.listeners.traitlisteners.PlayerListener;
//import me.Zombie__Hunter.fantasytools.particles.ParticleManager;
import me.Zombie__Hunter.fantasytools.playermanagement.PlayerManager;
import me.Zombie__Hunter.fantasytools.traits.Trait;

public class FantasyTools extends JavaPlugin {
		protected static FantasyTools plugin;
		
		private ClassToolManager toolManager;
		
		private CommandManager commandManager;
		
		private CooldownManager cooldownManager;
		
		private PlayerManager playerManager;
		
		//private ParticleManager particleManager;
		
		protected static final int MAX_TRAITS_PER_ITEM = 5;
		
		private boolean isCurrentEventCancelled = false;
		
		@Override
		public void onEnable() {
			plugin = this;
			toolManager = new ClassToolManager();
			commandManager = new CommandManager();
			cooldownManager = new CooldownManager();
			playerManager = new PlayerManager();
			//particleManager = new ParticleManager();
			registerListeners();
		}
		
		@Override
		public void onDisable() {
			cooldownManager.shutdown();
			toolManager.deInit();
			//particleManager.deInit();
		}
		
		private void registerListeners() {
			new PlayerListener();
			new EntityListener();
			new FantasyPlayerRegistryListener();
		}

		public static FantasyTools getPlugin() {
			return plugin;
		}

		public static int getMaxTraitsPerItem() {
			return MAX_TRAITS_PER_ITEM;
		}

		public void log(String string, Throwable exp) {
			System.out.println(string);
			exp.printStackTrace();
		}
		
		public void log(String string) {
			System.out.println(string);
		}
		
		public void registerEvents(Listener listener) {
			if (listener == null)
				return; 
			Bukkit.getPluginManager().registerEvents(listener, this);
		}
		
		public void triggerAllTraits(Event e) {
			for(AbstractClassTool tool : this.toolManager.getRegisteredTools()) {
				for(Trait trait : tool.getTraits()) {
					if(trait.getCurrentLevel() <= 0) {
						continue;
					}
					if(trait.trigger(e)) {
						trait.triggerCooldown();
					}
				}
			}
		}
		
		public void setCurrentEventCancelled(boolean cancel) {
			this.isCurrentEventCancelled = cancel;
		}
		
		public boolean getCurrentEventCancelled() {
			return this.isCurrentEventCancelled;
		}
		
		public Map<Trait, Integer> getAllCooldownsForPlayer(Player p){
			return this.cooldownManager.getAllCooldownsForPlayer(p);
		}

		public ClassToolManager getToolManager() {
			return toolManager;
		}

		public CommandManager getCommandManager() {
			return commandManager;
		}
		
		public CooldownManager getCooldownManager() {
			return cooldownManager;
		}

		public PlayerManager getPlayerManager() {
			return playerManager;
		}

		/*public ParticleManager getParticleManager() {
			return particleManager;
		}*/
		
}
