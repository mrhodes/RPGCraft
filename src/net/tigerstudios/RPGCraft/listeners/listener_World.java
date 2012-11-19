package net.tigerstudios.RPGCraft.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class listener_World implements Listener {
	
	// Need to monitor Chunk loading and unloading
	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event)
	{					
		
	}
	
	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent event)
	{
		Bukkit.broadcastMessage("Entities Left in chunk: "+event.getChunk().getEntities().length);		
	}	
	public listener_World() { }
} // public class listener_World implements Listener
