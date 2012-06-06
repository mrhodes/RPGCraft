package net.tigerstudios.RPGCraft;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

public class RaceSystem {
	
	
	/*
	Steps:
		Get data directory for races
		for each file	
	*/
	
	public static Race loadRaceFile(String filename)
	{
		Race raceData = new Race();
		File file = new File(filename);
		YamlConfiguration dataFile = YamlConfiguration.loadConfiguration(file);
		
		raceData.Name = dataFile.getString("Name");
		raceData.Description = dataFile.getString("Description");		
		
		System.out.println("Race: "+ raceData.Name);
		return raceData;
	} // public Race loadRaceFile(String filename)
	
} // public class RaceSystem
