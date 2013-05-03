package net.tigerstudios.RPGCraft.utils;

import net.tigerstudios.RPGCraft.mgr_Entity;
import net.tigerstudios.RPGCraft.mgr_Player;


public class RPGCraftTimer implements Runnable {
		
	public RPGCraftTimer()	{}
	
	@Override
	public void run()
	{		
		// Monster Spawning Control
		// Player Management (ie. Hunger, Thirst, etc)
		// Crop growing, other Tradeskill updating
		mgr_Entity.update();
		mgr_Player.update();
	}
}
