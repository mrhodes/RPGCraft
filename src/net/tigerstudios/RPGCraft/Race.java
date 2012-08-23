package net.tigerstudios.RPGCraft;


public class Race {
	public String Name;
	public String Description;
	private String LongDesc;	// Detailed description of this race
	private int RaceID; 		// Will be used to identify this race in the database
	
	// Stat Modifiers
	public int dex_mod, str_mod, con_mod, int_mod;
	public float speed;
		
	// Skill rate bonuses
	public float farming, cooking, mining, blacksmithing, alchemy, enchanting;
	
	public String maxWeapon, maxTool, maxArmour, specialTool;
} // public class Race
