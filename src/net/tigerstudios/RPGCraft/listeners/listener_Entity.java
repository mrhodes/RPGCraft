package net.tigerstudios.RPGCraft.listeners;

import net.tigerstudios.RPGCraft.RPG_Character;
import net.tigerstudios.RPGCraft.mgr_Player;
import net.tigerstudios.RPGCraft.CombatSystem.mgr_Mob;

import org.bukkit.GameMode;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

public class listener_Entity implements Listener{
		
	// ----------------------------------------------------
	// Entity Tame Event
	// 
	//	Event called when a player tried to tame a creature
	//	This is something that is Race specific. Elves can
	//  tame wolves, and humans can tame cats.
	/*@EventHandler	// ------------------------------------
	public void onEntityTame(EntityTameEvent event)
	{
		mcPlayer = Bukkit.getPlayer(event.getOwner().getName());
		if(mcPlayer != null)
		{
			if(event.getEntityType().equals(EntityType.WOLF))
			{
				RPG_Character rpgChar = mgr_Player.getCharacter(mcPlayer);
				if(rpgChar == null)
					return;
			
				if(!rpgChar.race.equalsIgnoreCase("elf"))
				{
					mcPlayer.sendMessage("[§2RPG§f] You're not able to tame wolves as a "+rpgChar.race+".");
					mcPlayer.sendMessage("[§2RPG§f] See an Elf if you'd like to have a pet wolf.");
					event.setCancelled(true);
					fLog.writeLine("TAME EVENT,"+mcPlayer.getName());
					return;				
				} // if(!rpgChar.race.equalsIgnoreCase("elf"))
			} // if(event.getEntityType().equals(EntityType.WOLF))
		
			if(event.getEntityType().equals(EntityType.OCELOT))
			{
			
			} // if(event.getEntityType().equals(EntityType.OCELOT))
		} // if(p != null)
	} // public void onEntityTame(final EntityTameEvent event)
	*/
		
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event)
	{
		// Get the type of creature, if it is a Mob create a new
		// Mob entry for it.
		if(event.getEntity() instanceof Monster)
		{	mgr_Mob.createMob(event.getEntity());
		} // if(event.getEntity() instanceof Monster)
				
	} // public void onCreatureSpawn(CreatureSpawnEvent event)
		
		
	@EventHandler
	public void onEntityDeath(final EntityDeathEvent event)
	{	
		if((event.getEntity() instanceof Monster) && (event.getEntity().getKiller() instanceof Player))
		{			
			SpoutPlayer sPlayer = SpoutManager.getPlayer(event.getEntity().getKiller());
			if(sPlayer == null)
				return;
			
			if(sPlayer.getGameMode() != GameMode.CREATIVE)
			{
				RPG_Character rpgChar = mgr_Player.getCharacter(sPlayer);				
				rpgChar.addExperience(event.getDroppedExp(), sPlayer);
			}
			mgr_Mob.removeMob(event.getEntity().getEntityId());		
		} // if(event.getEntity() instanceof Monster)
		
		event.setDroppedExp(0);
	} // public void onEntityDeath(final EntityDeathEvent event)
		
	
	public listener_Entity(Plugin p)
	{		
	} // public listener_Entity(Plugin p)	
} // public class listener_Entity implements Listener