package net.tigerstudios.RPGCraft;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.tigerstudios.RPGCraft.utils.SQLiteManager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;


public class listener_Player implements Listener {
	private Plugin rpgPlugin = null;
	
	/*@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerItemHeld(PlayerItemHeldEvent event)
	{
		player = event.getPlayer();
		ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
		
		if(newItem.getType() == Material.DIAMOND_AXE)
			player.sendMessage("Switched to Diamond Axe");
		
	} // public void onPlayerItemHeld(PlayerItemHeldEvent event)
	*/
		
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(final PlayerInteractEvent event)
	{
		// I don't do anything if player has empty hand
		if(event.getItem() == null)
			return;
		
		ItemStack item = event.getItem();
				
		// Water the crops only if the targetd block is farmland
		if(item.getType() == Material.WATER_BUCKET)
		{
			Block bTarget = event.getClickedBlock();
			if(bTarget == null)
				return;			
			
			if((bTarget.getType() == Material.CROPS) || (bTarget.getType() == Material.SOIL))
			{
				Player p = event.getPlayer();
				p.sendMessage("Dumped water on crops or soil.");
				if(bTarget.getType() == Material.CROPS)
					bTarget = bTarget.getRelative(0, -1, 0);
				
				p.sendMessage("Soil Dryness was: "+bTarget.getData());
				if(bTarget.getData() < 0x7)
					bTarget.setData((byte) 7);						
				p.sendMessage("Soil dryness is now: "+bTarget.getData());
				p.setItemInHand(new ItemStack(Material.BUCKET));
				event.setCancelled(true);
				return;				
			} // if((bTarget.getType() == Material.CROPS) || (bTarget.getType() == Material.SOIL))
			return;
		} // if(item.getType() == Material.WATER_BUCKET)
			
		
		if(item.getDurability() > 1023)
		{	
			Player p = event.getPlayer();
			RPG_Character character = mgr_Player.getCharacter(p);
			
			if(character != null)
			{	RPG_Player rpgPlayer = mgr_Player.getPlayer(p.getName().hashCode());
				// Important, only do this if within the 10 second timer.
				if( (rpgPlayer.lTimer + 10000) <= System.currentTimeMillis())
				{	// Reset Timer and then exit
					rpgPlayer.lTimer = 0;
					return; 
				}
				
				short id = item.getDurability();
				// Check for Copper, Silver, or Gold
				if(id == RPGCraft.cp)
					character.addCopper(item.getAmount());
				
				if(id == RPGCraft.sp)
					character.addSilver(item.getAmount());
	
				if(id == RPGCraft.gp)
					character.addGold(item.getAmount());
							
				p.setItemInHand(null);
				p.sendMessage("[�2RPG�f] Balance: �6"+rpgPlayer.getCharacter().getGold()+" Gold�f, �7"+rpgPlayer.getCharacter().getSilver()+" Silver�f, �c"+rpgPlayer.getCharacter().getCopper()+" Copper�f.");
			} // if(character != null)
		 }// if(item.getDurability() > 1023)
	} // public void onPlayerInteract(final PlayerInteractEvent event)
		
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		Player p = event.getPlayer();
		ResultSet rs = null;
		
		// See if this player is in the database.
		String query = "SELECT * from Accounts WHERE mc_Name='"+p.getName()+"'";
		rs = SQLiteManager.SQLQuery(query);
		
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
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(final PlayerQuitEvent event)
	{
		Player p = event.getPlayer();
		mgr_Player.playerLogout(p);
		return;
	} // public void onPlayerQuit(PlayerEvent event)
	
	public listener_Player(Plugin p)
	{
		Bukkit.getServer().getPluginManager().registerEvents(this, p);
		rpgPlugin = p;
	 } // public listener_Player(Plugin p)
			
} // public class listener_Player implements Listener
