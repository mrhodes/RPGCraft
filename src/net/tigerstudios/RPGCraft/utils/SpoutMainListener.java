package net.tigerstudios.RPGCraft.utils;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;

public class SpoutMainListener implements Listener
{
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onSpoutCraftEnable(SpoutCraftEnableEvent event)
	{
		Player p = event.getPlayer().getPlayer();
		
		SpoutFeatures.updateTitle(p, null);
		SpoutFeatures.updateTitle(null, p);
	} // public void onSpoutCraftEnable(SpoutCraftEnableEvent event)
} // public class SpoutMainListener implements Listener
