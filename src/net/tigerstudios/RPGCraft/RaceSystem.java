package net.tigerstudios.RPGCraft;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class RaceSystem {
	private static Map<String, Race> races = new HashMap<String, Race>();
	
	/*
	Steps:
		Get data directory for races
		for each file	
	*/
	
	
	public static void listRaces(Player p)
	{
		for(Race race: races.values())
		{
			p.sendMessage(""+race.Name +": "+race.Description);
		}		
		return;
	}
	
	public static Race getRace(String name)
	{
		return races.get(name);
	} // public static Race getRace(String name)
	
	// The following method will load the data from a Race configuration file.
	// TODO: Add error detection code here.
	public static Race loadRaceFile(String filename)
	{
		Race raceData = new Race();
		File file = new File(RPGCraft.mainDirectory+File.separatorChar+"Races"+File.separatorChar, filename);
		YamlConfiguration dataFile = YamlConfiguration.loadConfiguration(file);
		try { dataFile.load(file); } catch (Exception e) {	e.printStackTrace(); }
		
		raceData.Name = dataFile.getString("Name");
		raceData.Description = dataFile.getString("Description");		
		raceData.dex_mod = dataFile.getInt("Modifiers.Dexterity");
		raceData.str_mod = dataFile.getInt("Modifiers.Strength");
		raceData.int_mod = dataFile.getInt("Modifiers.Intelligence");
		raceData.con_mod = dataFile.getInt("Modifiers.Constitution");
		raceData.speed = ( dataFile.getInt("Modifiers.RunSpeed") / 100 );
		raceData.maxArmour = dataFile.getString("MaxArmor");
		raceData.maxWeapon = dataFile.getString("MaxWeapon");
		raceData.maxTool = dataFile.getString("MaxTool");
		raceData.specialTool = dataFile.getString("SpecialItem");
		raceData.farming = ( dataFile.getInt("SkillRateBonus.Farming") / 100 );
		raceData.cooking = ( dataFile.getInt("SkillRateBonus.Cooking") / 100 );
		raceData.mining = ( dataFile.getInt("SkillRateBonus.Mining") / 100 );
		raceData.blacksmithing = ( dataFile.getInt("SkillRateBonus.Blacksmithin") / 100 );
		raceData.alchemy = ( dataFile.getInt("SkillRateBonus.Alchemy") / 100 );
		raceData.enchanting = ( dataFile.getInt("SkillRateBonus.Enchanting") / 100 );
		
		// Add to the Races collection
		races.put(raceData.Name, raceData);
		
		return raceData;
	} // public Race loadRaceFile(String filename)
	
} // public class RaceSystem