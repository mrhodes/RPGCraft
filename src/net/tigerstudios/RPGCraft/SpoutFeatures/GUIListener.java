package net.tigerstudios.RPGCraft.SpoutFeatures;

import net.tigerstudios.RPGCraft.mgr_Player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericGradient;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.Screen;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

public class GUIListener implements Listener {
	private static Plugin rpgPlugin = null;			
	
	public static void fullTwoWay(Player player)
	{
		SpoutFeatures.updateTitleShortly(player, null);
		SpoutFeatures.updateTitleShortly(null, player);
	}
	
	public static void possiblyUpdateHealthBar(Entity entity)
	{
		if ( ! (entity instanceof Player)) return;
		Player player = (Player)entity;
		SpoutFeatures.updateTitle(player, null);			
	}	
	
	public GUIListener(Plugin p)
	{	rpgPlugin = p;			
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
	
	
	// -------------------------------------------- //
	// HEALTH BAR
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void monitorPlayerRespawnEvent(PlayerRespawnEvent event)
	{
		possiblyUpdateHealthBar(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		fullTwoWay(event.getPlayer());
		mgr_Player.getCharacter(event.getPlayer()).setSpeed(0);		
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerTeleport(PlayerTeleportEvent event)
	{
		if (event.getFrom().getWorld().equals(event.getTo().getWorld())) return;
		fullTwoWay(event.getPlayer());
		mgr_Player.getCharacter(event.getPlayer()).setSpeed(0);
	}

	@EventHandler
	public void onSpoutCraftEnable(final SpoutCraftEnableEvent event)
	{
		if(event.getPlayer().isSpoutCraftEnabled())
		{
			// Delay this to completely make sure the player is in game
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(rpgPlugin, new Runnable()
			{
				@Override
				public void run()
				{	fullTwoWay(event.getPlayer());
					mgr_Player.getCharacter(event.getPlayer());
					//SpoutFeatures.setWalkingSpeed(event.getPlayer(), (rpg != null) ? rpg.fWalkSpeed : 1.0f);
				}
			}, 10);			
			onSpoutCraftPlayer(event.getPlayer());
		} // if(event.getPlayer().isSpoutCraftEnabled())
	} // public void onSpoutCraftEnable(final SpoutCraftEnableEvent event)	
		
	
	public void onSpoutCraftPlayer(SpoutPlayer player)
	{
		Color back = new Color(0.0F, 0.0F, 0.0F, 0.75F);
		Color bottom = new Color(1.0F, 1.0F, 1.0F, 0.75F);		
		
		Screen screen = player.getMainScreen();
		//screen.removeWidgets(rpgPlugin);
		
		// Set the black title bar on screen
		screen.attachWidget(rpgPlugin, new GenericGradient()
		.setBottomColor(back).setTopColor(back).setMaxWidth(2048)
		.setX(0).setY(0).setWidth(2048).setHeight(11)
		.setAnchor(WidgetAnchor.TOP_LEFT).setPriority(RenderPriority.Highest));
		
		// Draw a separator line under it.
		screen.attachWidget(rpgPlugin, new GenericGradient()
		.setBottomColor(bottom).setTopColor(bottom).setX(0).setY(11)
		.setMaxWidth(2048).setWidth(2048).setHeight(1).setAnchor(WidgetAnchor.TOP_LEFT)
		.setPriority(RenderPriority.Highest));
		
		// This is the Top left menu...  Will be players name.
		GenericLabel nameLabel = new GenericLabel(player.getName());
		nameLabel.setMargin(0, 3).setFixed(true);
		nameLabel.setX(1).setY(1).setWidth(20).setHeight(9).setAnchor(WidgetAnchor.TOP_LEFT);
		screen.attachWidget(rpgPlugin, nameLabel);
		
		/*player.getMainScreen().getHealthBar()
		.setX(20).setY(1).setWidth(20).setHeight(9)
		.setPriority(RenderPriority.Highest).setFixed(true).setMargin(0, 3).setAnchor(WidgetAnchor.TOP_LEFT)
		.setDirty(true);*/
	}	
}
