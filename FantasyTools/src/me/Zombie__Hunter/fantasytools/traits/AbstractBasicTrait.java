package me.Zombie__Hunter.fantasytools.traits;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.Listener;

import me.Zombie__Hunter.fantasytools.FantasyTools;
import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;

public abstract class AbstractBasicTrait implements Trait, Listener {
	
	protected final AbstractClassTool tool;
	
	public AbstractBasicTrait(AbstractClassTool tool) {
		this.tool = tool;
	}
	
	public void init() {}
	
	public void deInit() {}

	public String getCommandName() {
		return this.getName().replaceAll(" ", "_");
	}
	
	public AbstractClassTool getTool() {
		return tool;
	}

	public int getCurrentLevel() {
		return this.currentLevel;
	}
	
	public void setCurrentLevel(int newLevel) {
		this.currentLevel = newLevel;
		this.levelModifier(newLevel);
	}
	
	public int getMaxLevel() {
		return this.maximumLevel;
	}
	
	public int getSkillPointCost() {
		return this.skillPointCost;
	}
	
	public int getCooldownInTicks() {
		return this.coolDowninTicks;
	}
	
	public double getCooldownInSeconds() {
		return this.coolDowninTicks / 20;
	}
	
	public void triggerCooldown() {
		plugin.getCooldownManager().setCooldown(tool.getOwner(), this, this.coolDowninTicks);
	}
	
	public int getMaxCooldown() {
		return this.coolDowninTicks;
	}
	
	public boolean isOnCooldown() {
		return (-1 != plugin.getCooldownManager().stillHasCooldown(tool.getOwner(), this));
	}
	
	public void particles() {};
	
	protected static final FantasyTools plugin = FantasyTools.getPlugin();
	
	  protected int coolDowninTicks = 20;
	  
	  protected int currentLevel = 0;
	  
	  protected int skillPointCost = 3;
	  
	  protected int maximumLevel = 90000000;
	  
	  //protected final Set<Biome> biomes = new HashSet<>();
	  
	  //protected final Set<Material> wearing = new HashSet<>();
	  
	  protected boolean onlyInWater = false;
	  
	  protected boolean onlyOnLand = false;
	  
	  protected boolean onlyInLava = false;
	  
	  protected boolean onlyOnSnow = false;
	  
	  protected boolean onlyInNight = false;
	  
	  protected boolean onlyOnDay = false;
	  
	  protected String displayName;
	  
	  protected int aboveElevation = Integer.MIN_VALUE;
	  
	  protected int belowElevation = Integer.MAX_VALUE;
	  
	  protected boolean onlyInRain = false;
	  
	  protected int onlyAfterDamaged = -1;
	  
	  protected int onlyAfterNotDamaged = -1;
	  
	  //protected long minUplinkShowTime = 3L;
	  
	  protected boolean disableCooldownNotice = false;
	  
	  protected final List<Material> onlyOnBlocks = new LinkedList<>();
	  
	  protected final List<Material> notOnBlocks = new LinkedList<>();
	  
	  protected boolean onlyWhileSneaking = false;
	  
	  protected boolean onlyWhileNotSneaking = false;
	  
	  protected String neededPermission = "";
	  
	  protected String traitDiscription = "";
	  
}
