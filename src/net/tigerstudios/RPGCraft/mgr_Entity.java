package net.tigerstudios.RPGCraft;


public class mgr_Entity {
	public static boolean bControlAllSpawns = false;
	
	
	
		
	public static void initialize()
	{
		// Get the options about 
		if(RPGCraft.config.getBoolean("SpawnControl.All"))
			bControlAllSpawns = true;		
	}
	
	
	/* ------------------------------------------------------------
	 * public static void update()
	 * 
	 * 
	 * --------------------------------------------------------- */
	public static void update()
	{
		
		
	}
	
	
	public mgr_Entity()	{	}
}
