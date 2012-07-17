// This is the main Monster class.  Each monster that spawns will have 
// it's own set of properties and possibly behaviours.  This will be 
// controlled in this class as well as a new class that it not created
// yet (mgr_Monsters)

package net.tigerstudios.RPGCraft;

import org.bukkit.entity.Monster;


// mobs need to have certain properties

public interface RPG_Mob extends Monster {
	int level = 1;
	int speed = 1;
	
	
	
	
} // public class RPG_Monster
