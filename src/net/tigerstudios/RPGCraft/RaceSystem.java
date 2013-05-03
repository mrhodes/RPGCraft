package net.tigerstudios.RPGCraft;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class RaceSystem {
	private static Plugin plugin = null;
	public static Map<String, Race> races = new HashMap<String, Race>();
	
	// Get the Races modifier for a givin skill
	// This will need to be optimized later to not use
	// strings.
	public static float getModifier(String race, String mod)
	{	if(mod.equalsIgnoreCase("mine"))
			return races.get(race.toLowerCase()).mining;
		if(mod.equalsIgnoreCase("farm"))
			return races.get(race.toLowerCase()).farming;
		if(mod.equalsIgnoreCase("enchant"))
			return races.get(race.toLowerCase()).enchanting;
		if(mod.equalsIgnoreCase("alchemy"))
			return races.get(race.toLowerCase()).alchemy;
		
		return 1.0f;
	} // public static float getModifier(String race, String mod)
	
	public static Race getRace(String name)
	{
		if(races.isEmpty() ||(name == null))
			return null;
		
		return races.get(name.toLowerCase());
	} // public static Race getRace(String name)
	
	public static void listRaces(Player p)
	{
		for(Race race: races.values())
			p.sendMessage("[§2RPG§f] "+race.Name +": "+race.Description);
		
		return;
	} // public static void listRaces(Player p)
	
	// The following method will load the data from a Race configuration file.
	// TODO: Add error detection code here.
	public static void loadRaces()
	{
		// Load all race files.
		File dir = new File(RPGCraft.mainDirectory, "Races");

		// First we simply register all materials.
		for (File file : dir.listFiles()) 
		{
			if (file.getName().endsWith(".race")){
				try {
					YamlConfiguration dataFile = YamlConfiguration.loadConfiguration(file);
					Race raceData = new Race();
					
					raceData.Name = dataFile.getString("Name");
					raceData.Description = dataFile.getString("Description");		
					raceData.dex_mod = dataFile.getInt("Modifiers.Dexterity");
					raceData.str_mod = dataFile.getInt("Modifiers.Strength");
					raceData.int_mod = dataFile.getInt("Modifiers.Intelligence");
					raceData.con_mod = dataFile.getInt("Modifiers.Constitution");
					raceData.speed =  (float)dataFile.getDouble("Modifiers.RunSpeed");
					raceData.maxArmour = dataFile.getString("MaxArmor");
					raceData.maxWeapon = dataFile.getString("MaxWeapon");
					raceData.maxTool = dataFile.getString("MaxTool");
					raceData.specialTool = dataFile.getString("SpecialItem");
					raceData.farming =  (float) dataFile.getDouble("SkillRateBonus.Farming");
					raceData.cooking = (float) dataFile.getDouble("SkillRateBonus.Cooking");
					raceData.mining = (float) dataFile.getDouble("SkillRateBonus.Mining");
					raceData.blacksmithing = (float) dataFile.getDouble("SkillRateBonus.Blacksmithin");
					raceData.alchemy = (float) dataFile.getDouble("SkillRateBonus.Alchemy");
					raceData.enchanting = (float) dataFile.getDouble("SkillRateBonus.Enchanting");
				
					// Add to the Races collection
					races.put(raceData.Name.toLowerCase(), raceData);
					RPGCraft.log.info(" --> Loaded "+raceData.Name+" race.");
					
				} catch (Exception exception) {	exception.printStackTrace(); }
			}// if (file.getName().endsWith(".race"))
		}// for (File file : dir.listFiles()) 
	} // public Race loadRaceFile(String filename)
	
} // public class RaceSystem