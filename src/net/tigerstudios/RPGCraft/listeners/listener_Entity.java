package net.tigerstudios.RPGCraft.listeners;


import net.tigerstudios.RPGCraft.RPG_Character;
import net.tigerstudios.RPGCraft.mgr_Entity;
import net.tigerstudios.RPGCraft.mgr_Player;
import net.tigerstudios.RPGCraft.CombatSystem.CombatSystem;
import net.tigerstudios.RPGCraft.CombatSystem.RPG_Entity;

import org.bukkit.GameMode;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

public class listener_Entity implements Listener{	
	
	public listener_Entity(){	} // public listener_Entity(Plugin p)	
	
	
	
	
	@EventHandler    // Creature and Animal Spawning Control
	public void onCreatureSpawn(CreatureSpawnEvent event)
	{	
		// Check to make sure only this plugin is spawning any mobs
		if(mgr_Entity.bControlAllSpawns)
		{ 	// Cancel all spawnning unless this plugin is the reason
			if(!event.getSpawnReason().equals(SpawnReason.CUSTOM))
			{	event.setCancelled(true); 
				return;
			}
		} // if(mgr_Entity.bControlAllSpawns)
	} // public void onCreatureSpawn(CreatureSpawnEvent event)
	
	
		
	
	@EventHandler
	public void onEntityShootBow(EntityShootBowEvent event)
	{	// Find out if this is a player 
		
		if(mgr_Entity.bControlAllSpawns)
		{
			
		if(event.getEntity() instanceof Player)
		{	RPG_Character rpgChar = mgr_Player.getCharacter((Player) event.getEntity());
			if(rpgChar != null)
			{	if(rpgChar.race.equalsIgnoreCase("elf"))	// TODO: Remove Race specific reference
				{	event.getProjectile().setVelocity(event.getProjectile().getVelocity().multiply(1.5f));
					return;
				}
				
				if(!event.getBow().getEnchantments().isEmpty())
				{	
					rpgChar.sendMessage("[§2RPG§f] Only Elves are able to use enchanted bows.");
					event.setCancelled(true);
					return;
				}
			} // if(rpgChar != null)			
		} // if(event.getEntity() instanceof Player)
		
		}
	} // public void onEntityShootBow(EntityShootBowEvent event)	
	
	
	@EventHandler
	public void onEntityDamageByEntity(final EntityDamageByEntityEvent event)
	{
		// If a player is not involved, exit now
		if(!(event.getEntity() instanceof Player) && !(event.getDamager() instanceof Player))
			return;
		
		if(mgr_Entity.bControlAllSpawns){
		
		// Get the Attacker and Defender
		RPG_Entity attacker = null, defender = null;
		Entity eDmger = event.getDamager();
		Entity eDefender = event.getEntity();
		
		// Find out if the Attacker is a player, or Projectile
		if(eDmger instanceof Player)
		{
			((Player) eDmger).sendMessage("You are the attacker");
			attacker = mgr_Player.getCharacter((Player)eDmger);			
		}		
		
		if(eDefender instanceof Player)
		{
			((Player) eDefender).sendMessage("You are the defender");
			defender = mgr_Player.getCharacter((Player) eDefender);			
		}
		
		
/* TODO:
	Need to add checks here for Projectile Entities.
*/
		if(event.getDamager() instanceof Monster){attacker = mgr_Entity.getMonster(event.getDamager().getEntityId());}
		if(event.getDamager() instanceof Animals){attacker = mgr_Entity.getAnimal(event.getDamager().getEntityId());}
		if(event.getEntity() instanceof Player)	{defender = mgr_Player.getCharacter((Player)event.getEntity());}
		if(event.getEntity() instanceof Monster){defender = mgr_Entity.getMonster(event.getEntity().getEntityId());}
		if(event.getEntity() instanceof Animals){defender = mgr_Entity.getAnimal(event.getEntity().getEntityId());}
				
		event.setDamage(CombatSystem.calculateDamage(attacker, defender, 0));
		
		} // if(mgr_Entity.bControlAllSpawns){
		
	} // public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	
	
	
	@EventHandler
	public void onEntityDeath(final EntityDeathEvent event)
	{	
		if(event.getEntity().getKiller() instanceof Player)
		{	Player player = event.getEntity().getKiller();
			if(player == null)
			{	mgr_Entity.removeEntity(event.getEntity());	
				return;
			}			
			if(player.getGameMode() != GameMode.CREATIVE)
			{	RPG_Character rpgChar = mgr_Player.getCharacter(player);				
	// NPE here...
			rpgChar.addExperience(event.getDroppedExp(), player);				
			}				
		} // if(event.getEntity().getKiller() instanceof Player)	
		mgr_Entity.removeEntity(event.getEntity());	
		event.setDroppedExp(0);
		
	} // public void onEntityDeath(final EntityDeathEvent event)
} // public class listener_Entity implements Listener