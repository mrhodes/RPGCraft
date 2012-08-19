package net.tigerstudios.RPGCraft.CombatSystem;

import java.util.HashMap;
import java.util.Map;

import net.tigerstudios.RPGCraft.RPGCraft;

import org.bukkit.entity.LivingEntity;

public class mgr_Mob {
	private static Map<Integer,RPG_Mob> mobList = new HashMap<Integer,RPG_Mob>();
		
	
	public static void createMob(LivingEntity ent)
	{
		RPG_Mob mob = new RPG_Mob(ent);		
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
