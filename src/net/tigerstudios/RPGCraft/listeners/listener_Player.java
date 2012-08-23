package net.tigerstudios.RPGCraft.listeners;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.tigerstudios.RPGCraft.RPGCraft;
import net.tigerstudios.RPGCraft.RPG_Character;
import net.tigerstudios.RPGCraft.RPG_Player;
import net.tigerstudios.RPGCraft.mgr_Player;
import net.tigerstudios.RPGCraft.CombatSystem.CombatSystem;
import net.tigerstudios.RPGCraft.skills.FarmSystem;
import net.tigerstudios.RPGCraft.utils.SQLManager;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class listener_Player implements Listener {		
			
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event)
	{
		CombatSystem.updateArmorStats((Player) event.getPlayer());
	} // public void onInventoryClose(InventoryCloseEvent event)
	
	@EventHandler
	public void onPlayerItemBreak(PlayerItemBreakEvent event)
	{
		CombatSystem.updateArmorStats(event.getPlayer());
	}// public void onPlayerItemBreak(PlayerItemBreakEvent event)
	
	
	@EventHandler
	public void onPlayerInteract(final PlayerInteractEvent event)
	{
		// I don't do anything if player has empty hand
		if(event.getItem() == null)
			return;
		
		ItemStack item = event.getItem();
				
		if(item.getDurability() > 1023)
		{	
			Player p = event.getPlayer();
			RPG_Character character = mgr_Player.getCharacter(p);
			
			if(character != null)
			{													
				short id = item.getDurability();
					
				RPG_Player rpgPlayer = mgr_Player.getPlayer(p.getName().hashCode());
				// Important, only do this if within the 10 second timer.
				if( (rpgPlayer.getTimer() + 10000) <= System.currentTimeMillis())
				{	// Reset Timer and then exit
					rpgPlayer.setTimer(0);
					return; 
				}				
				// Check for Copper, Silver, or Gold
				double total = 0;
				if(id == RPGCraft.cp)	total += item.getAmount();
				if(id == RPGCraft.sp)	total += item.getAmount() * 100;
				if(id == RPGCraft.gp)	total += item.getAmount() * 10000;
							
				p.setItemInHand(null);
				RPGCraft.econ.depositPlayer(p.getName(), total);
				double g = 0, s = 0, c;
				c = RPGCraft.econ.getBalance(p.getName());
				while(c >= 100){ s+=1; c-=100;}
				while(s >= 100){ g+=1; s-=100;}
				p.sendMessage("[§2RPG§f] Balance: §6"+g+" Gold§f, §7"+s+" Silver§f, §c"+c+" Copper§f.");	
			} // if(character != null)
		 }// if(item.getDurability() > 1023)
	} // public void onPlayerInteract(final PlayerInteractEvent event)
		
		
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
	{	if(event.getRightClicked() instanceof Animals)
		{
			FarmSystem.animalInteractions(event.getPlayer(), (Animals)event.getRightClicked());
			event.setCancelled(true);
			return;
		} // if(event.getRightClicked() instanceof Animals)
		
	} // public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
	
	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		Player p = event.getPlayer();
		ResultSet rs = null;
		
		// See if this player is in the database.
		String query = "SELECT * from Accounts WHERE mc_Name='"+p.getName()+"'";
		rs = SQLManager.SQLQuery(query);
		
		try {
			if(!rs.next())
			{	// New player.  Need to register this as a new player before logging in.
				mgr_Player.playerRegister(p);
				rs.close();
			}			
		} catch (SQLException e) { e.printStackTrace();	}
		
		mgr_Player.playerLogin(p);			
		return;		
	} // public void onPlayerJoin(final PlayerJoinEvent event)
	
	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent event)
	{
		Player p = event.getPlayer();
		mgr_Player.playerLogout(p);
		return;
	} // public void onPlayerQuit(PlayerEvent event)
	
	public listener_Player(){} // public listener_Player(Plugin p)
			
} // public class listener_Player implements Listener
