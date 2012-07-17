package net.tigerstudios.RPGCraft;

import net.tigerstudios.RPGCraft.utils.SpoutFeatures;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

public class AppearanceListener implements Listener{

	Plugin rpgPlugin;
	
	public AppearanceListener(Plugin p)
	{
		this.rpgPlugin = p;
	}
	
	public void fullTwoWay(Player player)
	{
		SpoutFeatures.updateTitleShortly(player, null);
		SpoutFeatures.updateTitleShortly(null, player);
	}	
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event)
	{		
		fullTwoWay(event.getPlayer());
		RPG_Character rpgChar = mgr_Player.getCharacter(event.getPlayer());
		/*if(rpgChar != null)
			SpoutFeatures.setSpeed(event.getPlayer(), mgr_Player.getCharacter(event.getPlayer()).fWalkSpeed);*/
		
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerTeleport(PlayerTeleportEvent event)
	{
		if (event.getFrom().getWorld().equals(event.getTo().getWorld())) return;
		fullTwoWay(event.getPlayer());
		RPG_Character rpgChar = mgr_Player.getCharacter(event.getPlayer());
		/*if(rpgChar != null)
			SpoutFeatures.setSpeed(event.getPlayer(), mgr_Player.getCharacter(event.getPlayer()).fWalkSpeed);*/
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		fullTwoWay(event.getPlayer());
		RPG_Character rpgChar = mgr_Player.getCharacter(event.getPlayer());
		/*if(rpgChar != null)
			SpoutFeatures.setSpeed(event.getPlayer(), mgr_Player.getCharacter(event.getPlayer()).fWalkSpeed);*/
	}	
	
	// -------------------------------------------- //
	// HEALTH BAR
	// -------------------------------------------- //

	public static void possiblyUpdateHealthBar(Entity entity)
	{
		if ( ! (entity instanceof Player)) return;
		Player player = (Player)entity;
		SpoutFeatures.updateTitle(player, null);
		/*if(mgr_Player.getCharacter(player) != null)
			SpoutFeatures.setSpeed(player, mgr_Player.getCharacter(player).fWalkSpeed);	*/	
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void monitorEntityDamageEvent(EntityDamageEvent event)
	{
			possiblyUpdateHealthBar(event.getEntity());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void monitorEntityRegainHealthEvent(EntityRegainHealthEvent event)
	{
		possiblyUpdateHealthBar(event.getEntity());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void monitorPlayerRespawnEvent(PlayerRespawnEvent event)
	{
		possiblyUpdateHealthBar(event.getPlayer());
	}	
}


