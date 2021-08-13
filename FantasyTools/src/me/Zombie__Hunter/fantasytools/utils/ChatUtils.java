package me.Zombie__Hunter.fantasytools.utils;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

import me.Zombie__Hunter.fantasytools.traits.Trait;

public class ChatUtils {
	
	public static String chat(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	public static String traitListToString(List<Trait> list) {
		String traitNames = "";
		for(Trait t : list) {
			traitNames += "&6" + t.getCommandName() + "&3, ";
		}
		StringUtils.chop(traitNames);
		StringUtils.chop(traitNames);
		
		return chat("&3Traits: " + traitNames);
	}
}

