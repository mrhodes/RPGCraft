package net.tigerstudios.RPGCraft.CombatSystem;

import java.util.HashMap;
import java.util.Map;

import net.tigerstudios.RPGCraft.RPGCraft;

import org.bukkit.entity.LivingEntity;

public class mgr_Mob {
	private static Map<Integer,RPG_Mob> mobList = new HashMap<Integer,RPG_Mob>();
	
	// A list of places that can spawn mobs will be held.  These are locations marked
	// with an itemframe on an emeraldore with a particular object in it. 
	
	// Wood Sword - 45  deg	= 1 - 2 Mob
	//              135 deg = 2 - 4 Mob
	// 				205 deg = 4 - 8 Mob
	//				295 deg = 8 - 11 Mob
	
	public static void createMob(LivingEntity ent)
	{		
		RPG_Mob mob = new RPG_Mob(ent, 1);
		mobList.put(ent.getEntityId(), mob);
	} // public static void createMob(LivingEntity ent)
	
	public static RPG_Mob getMob(int id)
	{	return mobList.get(id);
	} // public static RPG_Mob getMob(int id)
	
	public static void removeMob(int id)
	{		
		mobList.remove(id);		
	} // public static void removeMob(int id)
	
	// Empty Constructor
	public mgr_Mob(){}
	
	public static void shutdown()
	{
		RPGCraft.log.info("[RPGCraft] Cleared "+mobList.size()+" Mobs from Mob Manager.");
		mobList.clear();
	}
	
} // public class mgr_Mob
