package net.tigerstudios.RPGCraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.util.Vector;

import net.tigerstudios.RPGCraft.CombatSystem.RPG_Animal;
import net.tigerstudios.RPGCraft.CombatSystem.RPG_Entity;
import net.tigerstudios.RPGCraft.CombatSystem.RPG_Mob;


public class mgr_Entity {
	public static boolean bControlAllSpawns = false;
	public static boolean bControlAnimalSpawns = false;
	public static boolean bControlMonsterSpawns = false;
	
	private static Map<Integer, RPG_Mob> mobList = new HashMap<Integer, RPG_Mob>();
	private static Map<Integer, RPG_Animal> animalList = new HashMap<Integer, RPG_Animal>();
	
	// These are the max number allowed per chunk
	private static int maxAnimals, maxMonsters;
	
	private static Server server = null;
	
	public static boolean addEntity(RPG_Entity ent, int type)
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
	
	public static RPG_Animal getAnimal(int entID)
	{	if(animalList.containsKey(entID))
			return animalList.get(entID);
		return null;
	} // public static RPG_Animal getAnimal(int entID)
	public static RPG_Mob getMonster(int entID)
	{	if(mobList.containsKey(entID))
			return mobList.get(entID);
		return null;
	} // public static RPG_Mob getMonster(int entID)
	
	
	public static void initialize()
	{	// Get the options about 
		if(RPGCraft.config.getBoolean("SpawnControl.All"))
			bControlAllSpawns = true;
		
		server = Bukkit.getServer();
		
		maxAnimals = server.getAnimalSpawnLimit();
		maxMonsters = server.getMonsterSpawnLimit();
		
		mobList.clear();
		animalList.clear();
		
		List <World> worlds = server.getWorlds();
		for(World w: worlds)
		{
			List<LivingEntity> entList = w.getLivingEntities();
			RPGCraft.log.info("Removing "+ entList.size()+" entities");
			for(LivingEntity e: entList)
			{ 				
				EntityDeathEvent event = new EntityDeathEvent(e, null);
				Bukkit.getServer().getPluginManager().callEvent(event);	
				e.remove();
			}	
		}		
	} // public void initialize() 
	
		
	// Remove a creature from existance on the server.
	// This method will call the Death event and remove the creature from
	// the mob or animal list.
	public static void removeEntity(Entity ent)
	{	
		// Make sure the entityID is valid
		if(ent == null)
			return;
		
		int entID = ent.getEntityId();
		
		if(mobList.containsKey(entID)) { mobList.remove(entID);	return; }
		if(animalList.containsKey(entID)) { animalList.remove(entID); return; }
		
	} // public void removeEntity(int entID)
	
	
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
		
		// Get number of players online
		/*if(server.getOnlinePlayers().length == 0)
		{
			RPGCraft.log.info("No one online");
			
			// Since no one is on we should remove all monsters...
			List <World> wrlds = server.getWorlds();
						
		}
		*/	
		
		// Spawn Creatures..
		// For testing spawn this 10 blocks away from 'mrhodes'
		if(server.getPlayer("mrhodes") != null)
		{
			Location ploc = server.getPlayer("mrhodes").getLocation();
			Location mloc = ploc.add(new Vector(10.0, 0, 10.0));				
			
			if(!mloc.getBlock().isEmpty())
			{	// Block is not empty....  find next highest available block.
				mloc.setY(ploc.getWorld().getHighestBlockYAt(mloc));				
			}
			
			//Entity ent = mloc.getWorld().spawnEntity(mloc, EntityType.CHICKEN);
			
			
			
			//if(ent == null) { RPGCraft.log.info(" --> Error creating a chicken... Now what can we do ?"); return;	}			
		}
				
	} // public static void update()
	
		
	public mgr_Entity()
	{
		initialize();		
	}
	
} // public class mgr_Entity
