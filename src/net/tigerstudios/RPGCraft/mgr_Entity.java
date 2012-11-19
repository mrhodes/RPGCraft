package net.tigerstudios.RPGCraft;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

import net.tigerstudios.RPGCraft.CombatSystem.RPG_Animal;
import net.tigerstudios.RPGCraft.CombatSystem.RPG_Entity;
import net.tigerstudios.RPGCraft.CombatSystem.RPG_Mob;


public class mgr_Entity {
	public static boolean bControlAllSpawns = false;
	private static Map<Integer, RPG_Mob> mobList = new HashMap<Integer, RPG_Mob>();
	private static Map<Integer, RPG_Animal> animalList = new HashMap<Integer, RPG_Animal>();
	
	private static int maxAnimals, maxMonsters;
	
	public static RPG_Mob getMonster(int entID)
	{	if(mobList.containsKey(entID))
			return mobList.get(entID);
		return null;
	} // public static RPG_Mob getMonster(int entID)
	
	public static RPG_Animal getAnimal(int entID)
	{	if(animalList.containsKey(entID))
			return animalList.get(entID);
		return null;
	} // public static RPG_Animal getAnimal(int entID)
	
	private static boolean addEntity(RPG_Entity ent, int type)
	{
		if(ent == null)
			return false;
		
		switch(type)
		{
		case 1:		// Animal
			if(animalList.size() >= maxAnimals)
				return false;
			
			animalList.put(ent.getEntityID(), (RPG_Animal)ent);
			break;
			
		case 2:		// Monster
			if(mobList.size() >= maxMonsters)
				return false;
			
			mobList.put(ent.getEntityID(), (RPG_Mob) ent);
			break;
			
		default:
			return false;
		} // switch(type)
		
		return true;
	} // public boolean addEntity(RPG_Entity ent, int type)
	public static void removeEntity(int entID)
	{		
		
	} // public void removeEntity(int entID)
	
		
	public static void initialize()
	{	// Get the options about 
		if(RPGCraft.config.getBoolean("SpawnControl.All"))
			bControlAllSpawns = true;
		
		maxAnimals = Bukkit.getServer().getAnimalSpawnLimit();
		maxMonsters = Bukkit.getServer().getMonsterSpawnLimit();
		
		mobList.clear();
		animalList.clear();		
	} // public void initialize() 
	
	
	/* ------------------------------------------------------------
	 * public static void update()
	 * 
	 * 
	 * --------------------------------------------------------- */
	public static void update()
	{
		/* Things to do here...
		 * 
		 * Remove unneeded entities
		 * 	- Check players online
		 * 	- remove mobs and animals that are far away
		 * 		- spare any farm animals and player pets
		 * Spawn new entities
		 * 	- Only around online players
		 * 	- If no players online then despawn everything
		 */
		
	} // public static void update()
	
	
	
	public mgr_Entity()
	{
		initialize();		
	}
	
} // public class mgr_Entity
