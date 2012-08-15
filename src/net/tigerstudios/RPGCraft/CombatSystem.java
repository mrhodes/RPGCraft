package net.tigerstudios.RPGCraft;


import net.tigerstudios.RPGCraft.utils.MathMethods;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

// 
public class CombatSystem implements Listener{
	private Player mcPlayer = null;
	private SpoutPlayer sPlayer = null;
	
	@EventHandler
	public void onEntityDamage(final EntityDamageEvent event)
	{
		if(event.getCause() == DamageCause.ENTITY_ATTACK)
		{	// See if a player is involved.
			if(event.getEntityType() == EntityType.PLAYER)
			{	// Player has been damaged.  Need to get the Character and
				// process damage
				mcPlayer = (Player)event.getEntity();
							
				sPlayer = SpoutManager.getPlayer(mcPlayer);
				sPlayer.sendNotification("Damage", "You've been hit for "+event.getDamage(), Material.DIAMOND_SWORD);
			} // if(event.getEntityType() == EntityType.PLAYER)		
		} // if(event.getCause() == DamageCause.ENTITY_ATTACK)		
	} // public void onEntityDamage(EntityDamageEvent event)
	
	
	@EventHandler
	public void onEntityDamageByEntity(final EntityDamageByEntityEvent event)
	{
		// If a player is not involved, exit now
		if(!(event.getEntity() instanceof Player) && !(event.getDamager() instanceof Player))
			return;
		
		if(event.getDamager() instanceof Projectile)
		{	if(event.getEntity() instanceof Player)
			{
				mcPlayer = (Player) event.getEntity();
				RPG_Player rpgP = mgr_Player.getPlayer(mcPlayer.getName().hashCode());
				
				if(rpgP != null)
					rpgP.getSpoutPlayer().sendNotification("Damage", "You've been hit for "+event.getDamage(), Material.APPLE);
				
			} // if(event.getEntity() instanceof Player)			
		} // if(event.getDamager() instanceof Projectile)	
		
		if(event.getDamager() instanceof Player)
		{	mcPlayer = (Player) event.getDamager();
			//p.sendMessage("You hit someone for "+event.getDamage());
		} // if(event.getDamager() instanceof Player)
		
		if(event.getDamager() instanceof Zombie)
		{
			// Play random sound effect...
			if(MathMethods.rnd.nextInt(1000 + 1) <= 100)
				SpoutManager.getSoundManager().playCustomSoundEffect(RPGCraft.getPlugin(),
					SpoutManager.getPlayer((Player)event.getEntity()), 
					RPGCraft.webBase+"sounds/ZombieComeHere.ogg", false);			
		} // if(event.getDamager() instanceof Monster)
		
		mcPlayer = null;
		sPlayer = null;		
	} // public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	
	
	@EventHandler
	public void onEntityShootBow(EntityShootBowEvent event)
	{
		// Find out if this is a player 
		if(event.getEntity() instanceof Player)
		{
			mcPlayer = (Player) event.getEntity();
			RPG_Character rpgChar = mgr_Player.getCharacter(mcPlayer);
			if(rpgChar != null)
			{
				if(rpgChar.race.equalsIgnoreCase("elf"))
				{
					event.getProjectile().setVelocity(event.getProjectile().getVelocity().multiply(1.5f));
					return;
				}
				
				if(!event.getBow().getEnchantments().isEmpty())
				{
					mcPlayer.sendMessage("[§2RPG§f] Only Elves are able to use enchanted bows.");
					event.setCancelled(true);
					return;
				}
			}			
		} // if(event.getEntity() instanceof Player)
			
	} // public void onEntityShootBow(EntityShootBowEvent event)
	
	
	public CombatSystem()
	{
		
	} // public CombatSystem(Plugin p)	
	
} // public class CombatSystem implements Listener
