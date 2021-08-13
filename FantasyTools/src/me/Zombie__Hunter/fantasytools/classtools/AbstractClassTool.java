package me.Zombie__Hunter.fantasytools.classtools;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import me.Zombie__Hunter.fantasytools.FantasyTools;
import me.Zombie__Hunter.fantasytools.playermanagement.FantasyPlayer;
import me.Zombie__Hunter.fantasytools.traits.Trait;
import me.Zombie__Hunter.fantasytools.utils.ChatUtils;
import me.Zombie__Hunter.fantasytools.utils.Tools;

public abstract class AbstractClassTool {
	private Tools toolType;
	
	private List<Class <? extends Trait>> defaultTraits;
	
	private Map<Trait, Integer> skillMap = new HashMap<>();
	
	private List<Trait> traits = new LinkedList<>();
	
	private List<String> lore = new LinkedList<>();
	
	private Map<Enchantment, Integer> enchantments = new HashMap<>();

	private Player owner;
	
	private String name;
	
	private final String commandName;
	
	private Material mat;
	
	private ItemStack itemStack;

	public AbstractClassTool(String name, Player owner, Tools toolType, ItemStack item, List<Class <? extends Trait>> defaultTraits) {
		commandName = name.toLowerCase().subSequence(0, name.indexOf(' ')).toString();
		this.owner = owner;
		this.toolType = toolType;
		this.mat = item.getType();
		this.name = name;
		this.defaultTraits = defaultTraits;
		if(this.defaultTraits == null) {
			this.defaultTraits = new LinkedList<>();
		}
		
		generateDefaultTraits(defaultTraits);
		init();
		this.itemStack = item;
		this.setEnchantments(item.getEnchantments());
	}
	
	public AbstractClassTool(ClassTools classTool) {
		this.name = classTool.name();
		commandName = classTool.name().toLowerCase();
		Tools toolType = classTool.getToolType();
		this.owner = null;
		this.toolType = toolType;
		this.mat = toolType.getMats()[toolType.getMats().length - 1];
		
		generateDefaultTraits(classTool.getDefaultTraits());
	}
	
	public AbstractClassTool(Player owner, ClassTools classTool) {
		this.name = classTool.name();
		commandName = classTool.name().toLowerCase();
		Tools toolType = classTool.getToolType();
		this.owner = owner;
		this.toolType = toolType;
		this.mat = toolType.getMats()[toolType.getMats().length - 1];
		
		generateDefaultTraits(classTool.getDefaultTraits());
		init();
		this.itemStack = this.generateItem();
	}
	
	public static AbstractClassTool generateFromMat(ItemStack item, Player owner) {
		Tools type = null;
		for(Tools t : Tools.values()) {
			boolean matFound = false;
			for(Material m : t.getMats()) {
				if(m.equals(item.getType())) {
					matFound = true;
					break;
				}
			}
			if(matFound) {
				type = t;
				break;
			}
		}
		
		Class<? extends AbstractClassTool> clazz = null;
		for(ClassTools classTool : ClassTools.values()) {
			if(classTool.getToolType().equals(type)) {
				clazz = classTool.getClazz();
				break;
			}
		}
		
		if(clazz == null) {
			return null;
		}
		
		try {
			Constructor<? extends AbstractClassTool> ctor = clazz.getConstructor(Player.class, ItemStack.class);
			AbstractClassTool tool = ctor.newInstance(owner, item);
			
			return tool;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static AbstractClassTool generateFromClassToolAndMat(Player owner, ClassTools classTool, ItemStack item) {
		try {
			Constructor<? extends AbstractClassTool> ctor = classTool.getClazz().getConstructor(Player.class, ItemStack.class);
			AbstractClassTool tool = ctor.newInstance(owner, item);
			
			tool.generateDefaultTraits(classTool.getDefaultTraits());
			
			return tool;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static AbstractClassTool generateFromClassToolAndItem(Player owner, ClassTools classTool, ItemStack item) {
		try {
			Constructor<? extends AbstractClassTool> ctor = classTool.getClazz().getConstructor(Player.class, ItemStack.class);
			AbstractClassTool tool = ctor.newInstance(owner, item);
			
			tool.generateDefaultTraits(classTool.getDefaultTraits());
			
			ItemMeta meta = item.getItemMeta();
			if(!tool.getLore().get(0).contains(owner.getDisplayName())) {
				return null;
			}
			tool.init();
			tool.setLore(meta.getLore());
			tool.updateSkillMapFromLore(tool.lore);
			tool.update();
			
			if(tool.traits.size() != tool.getDefaultTraits().size()) {
				return null;
			}
			
			boolean cont = false;
			for(int i = 0; i < tool.traits.size(); i++) {
				if(!tool.getDefaultTraits().get(i).getClass().equals(tool.traits.get(i).getClass())){
					cont = true;
				}
			}
			if(cont) {
				return null;
			}
			
			return tool;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void init() {
		generateBasicMap();
		updateLore();
		for(Trait trait : this.traits) {
			trait.init();
		}
	}
	
	public void deInit() {
		for(Trait trait : this.traits) {
			trait.deInit();
		}
	}
	
	private void update() {
		updateSkillMap();
		updateLore();
		updateTraits();
		this.itemStack = this.generateItem();
	}
	
	public boolean isSimilar(AbstractClassTool other) {
		if(this.equals(other)) {
			return true;
		}
		
		return (this.owner.equals(other.owner) && this.mat.equals(other.mat) && this.lore.equals(other.lore));
	}
	
	public static boolean hasOwnedAndRegisteredTool(Player p) {
		for(AbstractClassTool tool : FantasyTools.getPlugin().getToolManager().getRegisteredTools()) {
			if(tool.checkOwnership(p)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean levelUpTrait(Trait trait, int amt) {
		FantasyTools plugin = FantasyTools.getPlugin();
		FantasyPlayer player = plugin.getPlayerManager().getPlayer(this.owner);
		if(player == null) {
			plugin.log(this.owner.getDisplayName() + " owns a tool but isn't registered as a fantasyplayer.");
			return false;
		}
		if(trait.getMaxLevel() < trait.getCurrentLevel() + amt) {
			plugin.log(this.owner.getDisplayName() + " tried leveling a trait above max.");
			this.owner.sendMessage(ChatUtils.chat("&4You can't level that trait above level &6" + trait.getMaxLevel()));
			return false;
		}
		
		if(player.spendSkillPoints(amt * trait.getSkillPointCost())) {
			int diff = trait.getCurrentLevel() + amt;
			if(diff < 0) {
				amt -= diff;
			}
			
			ItemStack item = this.getItemStack();
			trait.setCurrentLevel(trait.getCurrentLevel() + amt);
			this.skillMap.replace(trait, trait.getCurrentLevel());
			this.update();
			this.replaceCurrentItem(item);
			return true;
		}
		
		plugin.log(this.owner.getDisplayName() + " doesn't have enough skillpoints.");
		this.owner.sendMessage(ChatUtils.chat("&4You don't have enough skillpoints."));
		this.owner.sendMessage(ChatUtils.chat("&6" + trait.getName() + " &3costs &6" + trait.getSkillPointCost() + " &3skillpoints to level up."));
		return false;
	}

	public boolean addTrait(Class<? extends Trait> clazz) {
		if(this.traits.size() >= FantasyTools.getMaxTraitsPerItem()) {
			return false;
		}
		for(Trait trait : traits) {
			if(trait.getClass().equals(clazz)) {
				return false;
			}
		}
		
		try {
			Constructor<? extends Trait> ctor = clazz.getConstructor(AbstractClassTool.class);
			Trait trait = ctor.newInstance(this);
			this.traits.add(trait);
			
			ItemStack item = this.getItemStack();
			this.update();
			this.replaceCurrentItem(item);
			}
			catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		
		return true;
	}
	
	public boolean removeTrait(Class<? extends Trait> clazz) {
		if(this.traits.size() >= FantasyTools.getMaxTraitsPerItem()) {
			return false;
		}
		Trait temp = null;
		for(Trait trait : traits) {
			if(trait.getClass().equals(clazz)) {
				temp = trait;
				break;
			}
		}
		
		if(temp == null) {
			return false;
		}
		ItemStack item = this.getItemStack();
		FantasyTools.getPlugin().getPlayerManager().getPlayer(this.owner).addSkillPoints(temp.getCurrentLevel() * temp.getSkillPointCost());
		
		this.traits.remove(temp);
		
		this.update();
		this.replaceCurrentItem(item);
		
		return true;
	}
	
	public int totalSkillpointCost() {
		int cost = 0;
		for(Map.Entry<Trait, Integer> entry : this.skillMap.entrySet()) {
			cost += entry.getKey().getSkillPointCost() * entry.getValue();
		}
		return cost;
	}
	
	public boolean replaceCurrentItem(ItemStack item) {
		int invSlot = this.findInInventory(this.owner, item);
		this.itemStack = this.generateItem();
		if(-1 < invSlot) {
			this.owner.getInventory().setItem(invSlot, this.itemStack);
			return true;
		}
		return false;
	}
	
	public ItemStack disable() {
		ItemStack item = this.generateItem();
		ItemMeta meta = item.getItemMeta();
		
		meta.setUnbreakable(false);
		meta.setDisplayName(ChatUtils.chat("&4DISABLED"));
		meta.setLore(null);
		
		item.setItemMeta(meta);
		this.itemStack = item;
		return item;
	}
	
	public int findInInventory(Player p, ItemStack item) {
		return p.getInventory().first(item);
	}
	
	public void replaceCurrentWithDisabled(ItemStack item) {
		int invSlot = findInInventory(this.owner, item);
		if(invSlot < 0) {
			this.disable();
			return;
		}
		this.owner.getInventory().setItem(invSlot, this.disable());
	}
	
	private ItemStack generateItem() {
		ItemStack item = new ItemStack(this.mat);
		item.addEnchantments(this.enchantments);
		ItemMeta meta = item.getItemMeta();
		if(meta instanceof Damageable && this.itemStack != null) {
			((Damageable)meta).setDamage(((Damageable)this.itemStack.getItemMeta()).getDamage());
		}
		meta.setLore(this.lore);
		meta.setDisplayName(this.name);
		meta.setUnbreakable(true);
		item.setItemMeta(meta);
		return item;
	}
	
	public static AbstractClassTool generateFromItem(ItemStack item, Player owner) {
		Material material = item.getType();
		for(ClassTools ct : ClassTools.values()) {
			if(ct.getClazz() == null || ct.equals(ClassTools.BLANK_TOOL)) {
				continue;
			}
			boolean matchingType = false;
			Tools t = ct.getToolType();
			for(Material mat : t.getMats()) {
				if(material.equals(mat)) {
					matchingType = true;
					break;
				}
			}
			if(matchingType) {
				try {
					Constructor<? extends AbstractClassTool> ctor = ct.getClazz().getConstructor(Player.class, ItemStack.class);
					AbstractClassTool tool = ctor.newInstance(owner, item);
					ItemMeta meta = item.getItemMeta();
					if(!tool.getLore().get(0).contains(owner.getDisplayName())) {
						continue;
					}
					tool.init();
					tool.setLore(meta.getLore());
					
					if(!tool.updateSkillMapFromLore(tool.lore)) {
						System.out.println("Traits in skillmap didnt match for " + owner.getName());
						continue;
					}
					tool.update();
					
					if(tool.traits.size() != tool.getDefaultTraits().size()) {
						tool.deInit();
						continue;
					}
					
					boolean cont = false;
					for(int i = 0; i < tool.traits.size(); i++) {
						Class<? extends Trait> c1 = tool.getDefaultTraits().get(i);
						Class<? extends Trait> c2 = tool.traits.get(i).getClass();
						
						if(!c1.equals(c2)){
							System.out.println(ct.name() + ": wasn't instanciated cuz traits mismatch.");
							cont = true;
							break;
						}
					}
					if(cont) {
						tool.deInit();
						continue;
					}
					
					return tool;
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
		
		return null;
	}
	
	public boolean checkOwnership(Player holder) {
		if(holder != null && holder.equals(this.owner)) {
			return true;
		}
		return false;
	}
	
	public boolean isHoldingClassTool(Player p) {
		ItemStack mainItem = p.getInventory().getItemInMainHand();
		ItemStack offItem = p.getInventory().getItemInOffHand();
		
		if(this.itemStack.equals(mainItem)) {
			return true;
		}
		else if(this.itemStack.equals(offItem)) {
			return true;
		}
		
		return false;
	}
	
	
	private void updateLore() {
		List<String> lore = new LinkedList<>();
		lore.add("Owner: " + this.owner.getDisplayName());
		for(Trait t : getTraits()) {
			lore.add(" " + t.getName() + ": " + this.skillMap.get(t));
		}
		
		setLore(lore);
	}
	
	private boolean updateSkillMapFromLore(List<String> lore) {
		generateBasicMap();
		if(lore == null) {
			FantasyTools.getPlugin().log("Item lore is null");
			return false;
		}
		if(lore.isEmpty()) {
			FantasyTools.getPlugin().log("Item lore is empty");
			return false;
		}
		int validTraitCounter = 0;
		for(int i = 1; i < lore.size(); i++) {
			String s = lore.get(i);
			boolean hasTrait = false;
			for(Trait temp : this.skillMap.keySet()) {
				if(s.contains(temp.getName())) {
					int j = s.lastIndexOf(' ');
					this.skillMap.replace(temp, Integer.parseInt(s.substring(j + 1)));
					hasTrait = true;
				}
			}
			if(hasTrait) {
				validTraitCounter++;
			}
		}
		return (validTraitCounter == this.skillMap.keySet().size());
	}
	
	public void updateTraits() {
		for(Trait trait : this.traits) {
			trait.setCurrentLevel(this.skillMap.get(trait));
		}
	}
	
	private void updateSkillMap() {
		for(Trait trait : this.traits) {
			if(!this.skillMap.containsKey(trait)) {
				this.skillMap.put(trait, trait.getCurrentLevel());
			}
		}
		
		List<Trait> entriesToRemove = new LinkedList<>();
		for(Map.Entry<Trait, Integer> entry : this.skillMap.entrySet()) {
			if(!this.traits.contains(entry.getKey())) {
				entriesToRemove.add(entry.getKey());
			}
		}
		for(Trait trait : entriesToRemove) {
			this.skillMap.remove(trait);
		}
	}
	
	private void generateDefaultTraits(List<Class <? extends Trait>> defaultTraits) {
		if(defaultTraits == null) {
			this.traits = new LinkedList<>();
			return;
		}
		if(defaultTraits.isEmpty()) {
			this.traits = new LinkedList<>();
			return;
		}
		
		List<Trait> traits = new LinkedList<>();
		for(Class<? extends Trait> clazz : defaultTraits) {
			try {
			Constructor<? extends Trait> ctor = clazz.getConstructor(AbstractClassTool.class);
			Trait trait = ctor.newInstance(this);
			traits.add(trait);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		setTraits(traits);
	}
	
	private void generateBasicMap() {
		Map<Trait, Integer> skills = new HashMap<>();
		for(Trait t : getTraits()) {
			skills.put(t, 0);
		}
		setSkillMap(skills);
	}

	public Tools getToolType() {
		return toolType;
	}

	public void setToolType(Tools toolType) {
		this.toolType = toolType;
	}

	public List<Trait> getTraits() {
		return traits;
	}

	public void setTraits(List<Trait> traits) {
		this.traits = traits;
	}

	public List<String> getLore() {
		return lore;
	}

	public void setLore(List<String> lore) {
		this.lore = lore;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public Map<Trait, Integer> getSkillMap() {
		return skillMap;
	}

	public void setSkillMap(Map<Trait, Integer> skillMap) {
		this.skillMap = skillMap;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Material getMat() {
		return mat;
	}

	public void setMat(Material mat) {
		this.mat = mat;
	}

	public String getCommandName() {
		return commandName;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public abstract String getDescription();

	public List<Class <? extends Trait>> getDefaultTraits() {
		return defaultTraits;
	}
	
	public Map<Enchantment, Integer> getEnchantments() {
		return enchantments;
	}
	
	public void setEnchantments(Map<Enchantment, Integer> enchantments) {
		this.enchantments = enchantments;
		this.itemStack = this.generateItem();
	}

}
