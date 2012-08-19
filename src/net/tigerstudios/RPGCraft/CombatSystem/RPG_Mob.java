// This is the main Monster class.  Each monster that spawns will have 
// it's own set of properties and possibly behaviours.  This will be 
// controlled in this class as well as a new class that it not created
// yet (mgr_Monsters)

package net.tigerstudios.RPGCraft.CombatSystem;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;


// mobs need to have certain properties
public class RPG_Mob extends RPG_Entity {
	boolean bBossMob = false;
	boolean bCanMove = true;
	
	EntityType mobType = null;	
		
	// ----------------------------------------------------
	// Constructor
	// Set all the nessicary values for this Monster type
	public RPG_Mob(LivingEntity ent)
	{
		EntID = ent.getEntityId();
		mobType = ent.getType();
	} // public Mob(LivingEntity ent)
	
} // public class RPG_Monster
