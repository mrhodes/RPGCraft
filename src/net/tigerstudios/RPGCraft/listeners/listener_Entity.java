package net.tigerstudios.RPGCraft.listeners;


import net.tigerstudios.RPGCraft.RPG_Character;
import net.tigerstudios.RPGCraft.mgr_Entity;
import net.tigerstudios.RPGCraft.mgr_Player;

import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class listener_Entity implements Listener{	
		
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event)
	{	
		if(mgr_Entity.bControlAllSpawns)
		{ 	// Cancel all spawnning unless this plugin is the reason
			if(!event.getSpawnReason().equals(SpawnReason.CUSTOM))
			{	event.setCancelled(true); 
				return;
			}
		} // if(mgr_Entity.bControlAllSpawns)
		
	} // public void onCreatureSpawn(CreatureSpawnEvent event)
	
		
	@EventHandler
	public void onEntityDeath(final EntityDeathEvent event)
	{	
		if(event.getEntity().getKiller() instanceof Player)
		{			
			Player player = event.getEntity().getKiller();
			if(player == null)
				return;
			
			if(player.getGameMode() != GameMode.CREATIVE)
			{
				RPG_Character rpgChar = mgr_Player.getCharacter(player);				
				rpgChar.addExperience(event.getDroppedExp(), player);
			}
			mgr_Entity.removeEntity(event.getEntity().getEntityId());		
		} // if(event.getEntity() instanceof Monster)
		
		event.setDroppedExp(0);
	} // public void onEntityDeath(final EntityDeathEvent event)
		
	
	@EventHandler
	public void onEntityCombust(EntityCombustEvent event)
	{
		// Find out if this is a skeleton or zombie
		if( (event.getEntityType().equals(EntityType.ZOMBIE)) || event.getEntityType().equals(EntityType.SKELETON))
		{	event.setCancelled(true);	}		
	}
	
	
	public listener_Entity(){	} // public listener_Entity(Plugin p)	
} // public class listener_Entity implements Listener