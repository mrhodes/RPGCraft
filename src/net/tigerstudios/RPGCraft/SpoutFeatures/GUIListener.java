package net.tigerstudios.RPGCraft.SpoutFeatures;


import net.tigerstudios.RPGCraft.RPG_Character;
import net.tigerstudios.RPGCraft.mgr_Player;

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
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.ContainerType;
import org.getspout.spoutapi.gui.GenericContainer;
import org.getspout.spoutapi.gui.GenericGradient;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.Screen;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

public class GUIListener implements Listener {
	private static Plugin rpgPlugin = null;			
	
	@EventHandler
	public void onSpoutCraftEnable(SpoutCraftEnableEvent event)
	{
		if(event.getPlayer().isSpoutCraftEnabled())
		{
			fullTwoWay(event.getPlayer());
			RPG_Character rpg = mgr_Player.getCharacter(event.getPlayer());
			SpoutFeatures.setWalkingSpeed(event.getPlayer(), (rpg != null) ? rpg.fWalkSpeed : 1.0f);
			onSpoutCraftPlayer(event.getPlayer());
		}
	}	
	
	public void onSpoutCraftPlayer(SpoutPlayer player)
	{
		Color back = new Color(0.0F, 0.0F, 0.0F, 0.75F);
		Color bottom = new Color(1.0F, 1.0F, 1.0F, 0.75F);		
		
		Screen screen = player.getMainScreen();
		screen.removeWidgets(rpgPlugin);
		
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
		GenericContainer header = new GenericContainer();
		header.setLayout(ContainerType.HORIZONTAL)
		.setAlign(WidgetAnchor.TOP_LEFT).setAnchor(WidgetAnchor.TOP_LEFT)
		.setWidth(427).setHeight(10).setX(0).setY(1);
				
		GenericLabel nameLabel = new GenericLabel(player.getName());
		nameLabel.setResize(true).setMargin(0, 3).setFixed(true);
			
		header.addChildren(nameLabel);	
		screen.attachWidgets(rpgPlugin, header.getChildren());
		//screen.attachWidget(rpgPlugin, hb);
		
		/*Container center;
		screen.attachWidget(rpgPlugin, 
				center = (Container)new GenericContainer()
		.setLayout(ContainerType.HORIZONTAL)
		.setAlign(WidgetAnchor.TOP_CENTER)
		.setAnchor(WidgetAnchor.TOP_CENTER)
		.setWidth(427).setHeight(11).setX(-213)
		.setY(1));		
		
		Container right;
		screen.attachWidget(rpgPlugin, 
				right = (Container)new GenericContainer()
		.setLayout(ContainerType.HORIZONTAL)
		.setAlign(WidgetAnchor.TOP_RIGHT)
		.setAnchor(WidgetAnchor.TOP_RIGHT)
		.setWidth(427)
		.setHeight(11)
		.setX(-427)
		.setY(1));		*/		
	}
	
	
	public static void fullTwoWay(Player player)
	{
		SpoutFeatures.updateTitleShortly(player, null);
		SpoutFeatures.updateTitleShortly(null, player);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerTeleport(PlayerTeleportEvent event)
	{
		if (event.getFrom().getWorld().equals(event.getTo().getWorld())) return;
		fullTwoWay(event.getPlayer());
		RPG_Character rpgChar = mgr_Player.getCharacter(event.getPlayer());
		if(rpgChar != null)
			SpoutFeatures.setWalkingSpeed(SpoutManager.getPlayer(event.getPlayer()), rpgChar.fWalkSpeed);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		fullTwoWay(event.getPlayer());
		RPG_Character rpgChar = mgr_Player.getCharacter(event.getPlayer());
		if(rpgChar != null)
			SpoutFeatures.setWalkingSpeed(SpoutManager.getPlayer(event.getPlayer()), rpgChar.fWalkSpeed);
	}	
	
	
	// -------------------------------------------- //
	// HEALTH BAR
	// -------------------------------------------- //

	public static void possiblyUpdateHealthBar(Entity entity)
	{
		if ( ! (entity instanceof Player)) return;
		Player player = (Player)entity;
		SpoutFeatures.updateTitle(player, null);			
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
		
	
	public GUIListener(Plugin p)
	{	rpgPlugin = p;			
	}	
}
