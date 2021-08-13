package me.Zombie__Hunter.fantasytools.classtools;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;

import me.Zombie__Hunter.fantasytools.FantasyTools;
import me.Zombie__Hunter.fantasytools.classtools.classtoolslist.ArcherTool;
import me.Zombie__Hunter.fantasytools.classtools.classtoolslist.BarbarianTool;
import me.Zombie__Hunter.fantasytools.classtools.classtoolslist.BlankTool;
import me.Zombie__Hunter.fantasytools.classtools.classtoolslist.ForagerTool;
import me.Zombie__Hunter.fantasytools.classtools.classtoolslist.SindoreiTool;
import me.Zombie__Hunter.fantasytools.classtools.classtoolslist.MinerTool;
import me.Zombie__Hunter.fantasytools.classtools.classtoolslist.PriestTool;
import me.Zombie__Hunter.fantasytools.classtools.classtoolslist.RogueTool;
import me.Zombie__Hunter.fantasytools.classtools.classtoolslist.FishermanTool;
import me.Zombie__Hunter.fantasytools.classtools.classtoolslist.OceanCrawlerTool;
import me.Zombie__Hunter.fantasytools.classtools.classtoolslist.RashidTool;
import me.Zombie__Hunter.fantasytools.classtools.classtoolslist.BrawlerTool;
import me.Zombie__Hunter.fantasytools.traits.DefaultTraitsList;
import me.Zombie__Hunter.fantasytools.traits.Trait;
import me.Zombie__Hunter.fantasytools.utils.Tools;

public enum ClassTools {
	BARBARIAN_TOOL(Tools.AXE, DefaultTraitsList.getDefaultAxeTraits(), BarbarianTool.class),
	PRIEST_TOOL(Tools.SHOVEL, DefaultTraitsList.getDefaultShovelTraits(), PriestTool.class),
	FORAGER_TOOL(Tools.HOE, DefaultTraitsList.getDefaultHoeTraits(), ForagerTool.class),
	SINDOREI_TOOL(Tools.SWORD, DefaultTraitsList.getDefaultSwordTraits(), SindoreiTool.class),
	MINER_TOOL(Tools.PICKAXE, DefaultTraitsList.getDefaultPickaxeTraits(), MinerTool.class),
	ARCHER_TOOL(Tools.BOW, DefaultTraitsList.getDefaultBowTraits(), ArcherTool.class),
	ROGUE_TOOL(Tools.SWORD, DefaultTraitsList.getDefaultRogueTraits(), RogueTool.class),
	FISHERMAN_TOOL(Tools.FISHING_ROD, DefaultTraitsList.getDefaultFishermanTraits(), FishermanTool.class),
	OCEANCRAWLER_TOOL(Tools.TRIDENT, DefaultTraitsList.getDefaultOceanCrawlerTraits(), OceanCrawlerTool.class),
	RASHID_TOOL(Tools.SWORD, DefaultTraitsList.getDefaultRashidTraits(), RashidTool.class),
	BRAWLER_TOOL(Tools.PICKAXE, DefaultTraitsList.getDefaultBrawlerTraits(), BrawlerTool.class),
	
	BLANK_TOOL(Tools.STICK, new LinkedList<Class<? extends Trait>>(), BlankTool.class);
	
	private final Tools toolType;
	
	private final List<Class<? extends Trait>> defaultTraits;
	
	private final Class<? extends AbstractClassTool> clazz;
	
	private ClassTools(Tools toolType, List<Class<? extends Trait>> traits, Class<? extends AbstractClassTool> clazz) {
		this.toolType = toolType;
		this.defaultTraits = traits;
		this.clazz = clazz;
	}

	public Tools getToolType() {
		return toolType;
	}

	public List<Class<? extends Trait>> getDefaultTraits() {
		return defaultTraits;
	}

	public Class<? extends AbstractClassTool> getClazz() {
		return clazz;
	}
	
	
	public static List<AbstractClassTool> getDefaultClassTools(){
		List<AbstractClassTool> tools = new LinkedList<>();
		for(ClassTools t : values()) {
			if(t.getClazz() == null) {
				continue;
			}
			
			AbstractClassTool tool = generateToolFromClass(t.getClazz());
			if(tool == null) {
				FantasyTools.getPlugin().log("Unable to create default classtool using " + t.getClazz().getCanonicalName());
				continue;
			}
			
			tools.add(tool);
		}
		
		return tools;
	}
	
	private static AbstractClassTool generateToolFromClass(Class<? extends AbstractClassTool> clazz) {
		AbstractClassTool tool = null;
		if(clazz == null) {
			return null;
		}
		if(clazz.equals(BlankTool.class)) {
			return null;
		}
		
		try {
			Constructor<? extends AbstractClassTool> ctor = clazz.getConstructor();
			tool = ctor.newInstance();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return tool;
	}
	
	public static AbstractClassTool generateToolFromClassAndPlayer(Class<? extends AbstractClassTool> clazz, Player owner) {
		AbstractClassTool tool = null;
		try {
			Constructor<? extends AbstractClassTool> ctor = clazz.getConstructor(Player.class);
			tool = ctor.newInstance(owner);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return tool;
	}
}
