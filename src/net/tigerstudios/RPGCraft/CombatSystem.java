package net.tigerstudios.RPGCraft;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

// 
public class CombatSystem implements Listener{
	private Player mcPlayer = null;
	private SpoutPlayer sPlayer = null;
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent event)
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
	
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	{
		if(event.getDamager() instanceof Projectile)
		{	if(event.getEntity() instanceof Player)
			{
				Player p = (Player) event.getEntity();
				RPG_Player rpgP = mgr_Player.getPlayer(p.getName().hashCode());
				
				if(rpgP != null)
				{
					rpgP.getSpoutPlayer().sendNotification("Damage", "You've been hit for "+event.getDamage(), Material.APPLE);
				}
			} // if(event.getEntity() instanceof Player)			
		} // if(event.getDamager() instanceof Projectile)	
		
		if(event.getDamager() instanceof Player)
		{
			Player p = (Player) event.getDamager();
			//p.sendMessage("You hit someone for "+event.getDamage());
		} // if(event.getDamager() instanceof Player)
		
		if(event.getDamager() instanceof Monster)
		{
			
		} // if(event.getDamager() instanceof Monster)
		
	} // public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	
	/*
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event)
	{
		
	} // public void onEntityDeath(EntityDeathEvent event)
	
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onProjectileLaunch(ProjectileLaunchEvent event)
	{
		
	} // public void onProjectileLaunch(ProjectileLaunchEvent event)
	
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onProjectileHit(ProjectileHitEvent event)
	{
		
		
	} // public void onProjectileHit(ProjectileHitEvent event)
	*/
	
	public CombatSystem(Plugin p)
	{
		Bukkit.getServer().getPluginManager().registerEvents(this, p);
	} // public CombatSystem(Plugin p)	
	
} // public class CombatSystem implements Listener
