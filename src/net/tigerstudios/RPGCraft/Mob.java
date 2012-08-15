// This is the main Monster class.  Each monster that spawns will have 
// it's own set of properties and possibly behaviours.  This will be 
// controlled in this class as well as a new class that it not created
// yet (mgr_Monsters)

package net.tigerstudios.RPGCraft;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;


// mobs need to have certain properties
public abstract class Mob{
	int level = 1;
	int speed = 1;
	int health, maxHealth;
	int EntID = 0;
	boolean bBossMob = false;
	
	EntityType mobType = null;
	LivingEntity livEntity = null;
	
	
	public int getHealth() { return health; }
	public void setHealth(int newHealth)
	{ 	health = (newHealth < 0) ? (health = 0) : newHealth; 
		health = (newHealth > maxHealth) ? (health = maxHealth) : newHealth;
	} // public void setHealth(int newHealth)
	
	
	// ----------------------------------------------------
	// Constructor
	// Set all the nessicary values for this Monster type
	public Mob(LivingEntity ent)
	{
		livEntity = ent;
		EntID = livEntity.getEntityId();
		mobType = ent.getType();
	} // public Mob(LivingEntity ent)
	
} // public class RPG_Monster
