package net.tigerstudios.RPGCraft;

import org.bukkit.Material;


public class Race {
	String Name;
	String Description;
	
	// Stat Modifiers
	float dex_mod, str_mod, con_mod, int_mod;
	float speed;
	
	// Skill rate bonuses
	float farming, cooking, mining, blacksmithing, alchemy, enchanting;
	
	Material maxWeapon, maxTool, maxArmour, specialTool;
} // public class Race
