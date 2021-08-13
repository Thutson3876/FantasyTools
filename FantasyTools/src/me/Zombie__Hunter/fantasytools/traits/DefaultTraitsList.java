package me.Zombie__Hunter.fantasytools.traits;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.traitlist.archer.Disengage;
import me.Zombie__Hunter.fantasytools.traits.traitlist.archer.ExplosiveArrow;
import me.Zombie__Hunter.fantasytools.traits.traitlist.archer.Ninjitsu;
import me.Zombie__Hunter.fantasytools.traits.traitlist.archer.PiercingArrows;
import me.Zombie__Hunter.fantasytools.traits.traitlist.barbarian.DeepDive;
import me.Zombie__Hunter.fantasytools.traits.traitlist.barbarian.Enrage;
import me.Zombie__Hunter.fantasytools.traits.traitlist.barbarian.Hamstring;
import me.Zombie__Hunter.fantasytools.traits.traitlist.barbarian.Taunt;
import me.Zombie__Hunter.fantasytools.traits.traitlist.brawler.ComboBreaker;
import me.Zombie__Hunter.fantasytools.traits.traitlist.brawler.Dash;
import me.Zombie__Hunter.fantasytools.traits.traitlist.brawler.DownToEarth;
import me.Zombie__Hunter.fantasytools.traits.traitlist.fisherman.AcquiredTaste;
import me.Zombie__Hunter.fantasytools.traits.traitlist.fisherman.FishyBusiness;
import me.Zombie__Hunter.fantasytools.traits.traitlist.fisherman.PufferfishTipped;
import me.Zombie__Hunter.fantasytools.traits.traitlist.fisherman.RainDancer;
import me.Zombie__Hunter.fantasytools.traits.traitlist.forager.HuntingTrap;
import me.Zombie__Hunter.fantasytools.traits.traitlist.forager.Nimble;
import me.Zombie__Hunter.fantasytools.traits.traitlist.forager.Resourceful;
import me.Zombie__Hunter.fantasytools.traits.traitlist.forager.TreeFeller;
import me.Zombie__Hunter.fantasytools.traits.traitlist.miner.DungeonDelver;
import me.Zombie__Hunter.fantasytools.traits.traitlist.miner.Excavation;
import me.Zombie__Hunter.fantasytools.traits.traitlist.miner.HotHands;
import me.Zombie__Hunter.fantasytools.traits.traitlist.miner.ToughSkin;
import me.Zombie__Hunter.fantasytools.traits.traitlist.misc.ComboBreaker2;
import me.Zombie__Hunter.fantasytools.traits.traitlist.misc.Finisher;
import me.Zombie__Hunter.fantasytools.traits.traitlist.misc.Wings;
import me.Zombie__Hunter.fantasytools.traits.traitlist.oceancrawler.Cleanse;
import me.Zombie__Hunter.fantasytools.traits.traitlist.oceancrawler.PolearmMastery;
import me.Zombie__Hunter.fantasytools.traits.traitlist.oceancrawler.SerenityNow;
import me.Zombie__Hunter.fantasytools.traits.traitlist.oceancrawler.UnderTheSea;
import me.Zombie__Hunter.fantasytools.traits.traitlist.priest.AngelicLeap;
import me.Zombie__Hunter.fantasytools.traits.traitlist.priest.BlessLand;
import me.Zombie__Hunter.fantasytools.traits.traitlist.priest.Grace;
import me.Zombie__Hunter.fantasytools.traits.traitlist.priest.Prayer;
import me.Zombie__Hunter.fantasytools.traits.traitlist.rashid.Easifa;
import me.Zombie__Hunter.fantasytools.traits.traitlist.rashid.NewHeights;
import me.Zombie__Hunter.fantasytools.traits.traitlist.rashid.Tornado;
import me.Zombie__Hunter.fantasytools.traits.traitlist.rashid.WingStroke;
import me.Zombie__Hunter.fantasytools.traits.traitlist.rogue.Backstab;
import me.Zombie__Hunter.fantasytools.traits.traitlist.rogue.Poison;
import me.Zombie__Hunter.fantasytools.traits.traitlist.rogue.Shadowstep;
import me.Zombie__Hunter.fantasytools.traits.traitlist.rogue.Stealth;
import me.Zombie__Hunter.fantasytools.traits.traitlist.sindorei.BloodBolt;
import me.Zombie__Hunter.fantasytools.traits.traitlist.sindorei.BloodBond;
import me.Zombie__Hunter.fantasytools.traits.traitlist.sindorei.DrainingAura;
import me.Zombie__Hunter.fantasytools.traits.traitlist.sindorei.Leech;

public class DefaultTraitsList {
	
	public static List<Trait> getTraits() {
		List<Trait> traits = new LinkedList<>();
		
		//Barbarian Traits
		traits.add(generateTraitFromClass(DeepDive.class));
		traits.add(generateTraitFromClass(Enrage.class));
		traits.add(generateTraitFromClass(Hamstring.class));
		traits.add(generateTraitFromClass(Taunt.class));
		//Sindorei Traits
		traits.add(generateTraitFromClass(Leech.class));
		traits.add(generateTraitFromClass(DrainingAura.class));
		traits.add(generateTraitFromClass(BloodBond.class));
		traits.add(generateTraitFromClass(BloodBolt.class));
		//Archer Traits
		traits.add(generateTraitFromClass(PiercingArrows.class));
		traits.add(generateTraitFromClass(Disengage.class));
		traits.add(generateTraitFromClass(Ninjitsu.class));
		traits.add(generateTraitFromClass(ExplosiveArrow.class));
		//Priest Traits
		traits.add(generateTraitFromClass(AngelicLeap.class));
		traits.add(generateTraitFromClass(BlessLand.class));
		traits.add(generateTraitFromClass(Prayer.class));
		traits.add(generateTraitFromClass(Grace.class));
		//Miner Traits
		traits.add(generateTraitFromClass(Excavation.class));
		traits.add(generateTraitFromClass(HotHands.class));
		traits.add(generateTraitFromClass(ToughSkin.class));
		traits.add(generateTraitFromClass(DungeonDelver.class));
		//Forager Traits
		traits.add(generateTraitFromClass(TreeFeller.class));
		traits.add(generateTraitFromClass(Resourceful.class));
		traits.add(generateTraitFromClass(Nimble.class));
		traits.add(generateTraitFromClass(HuntingTrap.class));
		//Rogue Traits
		traits.add(generateTraitFromClass(Backstab.class));
		traits.add(generateTraitFromClass(Poison.class));
		traits.add(generateTraitFromClass(Shadowstep.class));
		traits.add(generateTraitFromClass(Stealth.class));
		//Fisherman Traits
		traits.add(generateTraitFromClass(AcquiredTaste.class));
		traits.add(generateTraitFromClass(FishyBusiness.class));
		traits.add(generateTraitFromClass(PufferfishTipped.class));
		traits.add(generateTraitFromClass(RainDancer.class));
		//Ocean Crawler Traits
		traits.add(generateTraitFromClass(Cleanse.class));
		traits.add(generateTraitFromClass(PolearmMastery.class));
		traits.add(generateTraitFromClass(SerenityNow.class));
		traits.add(generateTraitFromClass(UnderTheSea.class));
		//Rashid Traits
		traits.add(generateTraitFromClass(Easifa.class));
		traits.add(generateTraitFromClass(NewHeights.class));
		traits.add(generateTraitFromClass(Tornado.class));
		traits.add(generateTraitFromClass(WingStroke.class));
		//Brawler Traits
		traits.add(generateTraitFromClass(ComboBreaker.class));
		traits.add(generateTraitFromClass(Dash.class));
		traits.add(generateTraitFromClass(DownToEarth.class));
		
		//Miscellanious
		traits.add(generateTraitFromClass(Wings.class));
		traits.add(generateTraitFromClass(ComboBreaker2.class));
		traits.add(generateTraitFromClass(Finisher.class));
		
		return traits;
	}
	
	public static List<Class<? extends Trait>> getDefaultAxeTraits(){
		List<Class<? extends Trait>> traits = new LinkedList<>();
		traits.add(DeepDive.class);
		traits.add(Enrage.class);
		traits.add(Hamstring.class);
		traits.add(Taunt.class);
		
		return traits;
	}
	
	public static List<Class<? extends Trait>> getDefaultPickaxeTraits(){
		List<Class<? extends Trait>> traits = new LinkedList<>();
		traits.add(Excavation.class);
		traits.add(HotHands.class);
		traits.add(ToughSkin.class);
		traits.add(DungeonDelver.class);
		
		return traits;
	}
	
	public static List<Class<? extends Trait>> getDefaultShovelTraits() {
		List<Class<? extends Trait>> traits = new LinkedList<>();
		traits.add(AngelicLeap.class);
		traits.add(BlessLand.class);
		traits.add(Prayer.class);
		traits.add(Grace.class);
		
		return traits;
	}
	
	public static List<Class<? extends Trait>> getDefaultSwordTraits() {
		List<Class<? extends Trait>> traits = new LinkedList<>();
		traits.add(Leech.class);
		traits.add(DrainingAura.class);
		traits.add(BloodBond.class);
		traits.add(BloodBolt.class);
		
		return traits;
	}
	
	public static List<Class<? extends Trait>> getDefaultHoeTraits() {
		List<Class<? extends Trait>> traits = new LinkedList<>();
		traits.add(TreeFeller.class);
		traits.add(Resourceful.class);
		traits.add(Nimble.class);
		traits.add(HuntingTrap.class);
		
		return traits;
	}
	
	public static List<Class<? extends Trait>> getDefaultBowTraits() {
		List<Class<? extends Trait>> traits = new LinkedList<>();
		traits.add(PiercingArrows.class);
		traits.add(Disengage.class);
		traits.add(Ninjitsu.class);
		traits.add(ExplosiveArrow.class);
		
		return traits;
	}
	
	public static List<Class<? extends Trait>> getDefaultRogueTraits() {
		List<Class<? extends Trait>> traits = new LinkedList<>();
		traits.add(Backstab.class);
		traits.add(Poison.class);
		traits.add(Shadowstep.class);
		traits.add(Stealth.class);
		
		return traits;
	}
	
	public static List<Class<? extends Trait>> getDefaultFishermanTraits() {
		List<Class<? extends Trait>> traits = new LinkedList<>();
		traits.add(AcquiredTaste.class);
		traits.add(FishyBusiness.class);
		traits.add(PufferfishTipped.class);
		traits.add(RainDancer.class);
		
		return traits;
	}
	
	public static List<Class<? extends Trait>> getDefaultOceanCrawlerTraits() {
		List<Class<? extends Trait>> traits = new LinkedList<>();
		traits.add(Cleanse.class);
		traits.add(PolearmMastery.class);
		traits.add(SerenityNow.class);
		traits.add(UnderTheSea.class);
		
		return traits;
	}
	
	public static List<Class<? extends Trait>> getDefaultRashidTraits() {
		List<Class<? extends Trait>> traits = new LinkedList<>();
		traits.add(Easifa.class);
		traits.add(NewHeights.class);
		traits.add(Tornado.class);
		traits.add(WingStroke.class);
		
		return traits;
	}
	
	public static List<Class<? extends Trait>> getDefaultBrawlerTraits() {
		List<Class<? extends Trait>> traits = new LinkedList<>();
		traits.add(ComboBreaker.class);
		traits.add(Dash.class);
		traits.add(DownToEarth.class);
		
		return traits;
	}
	
	private static Trait generateTraitFromClass(Class<? extends AbstractBasicTrait> clazz) {
		Trait trait = null;
		try {
			Constructor<? extends Trait> ctor = clazz.getConstructor(AbstractClassTool.class);
			trait = ctor.newInstance((AbstractClassTool)null);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return trait;
	}
}
