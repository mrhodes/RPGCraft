package net.tigerstudios.RPGCraft;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.tigerstudios.RPGCraft.utils.SQLiteManager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.player.SpoutPlayer;


public class listener_Player implements Listener {
	private Plugin rpgPlugin = null;
	private Server rpgServer = null;
	private static Player player = null;
	private static SpoutPlayer sPlayer = null;
		
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerItemHeld(PlayerItemHeldEvent event)
	{
		player = event.getPlayer();
		ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
		
		if(newItem.getType() == Material.DIAMOND_AXE)
			player.sendMessage("Switched to Diamond Axe");
		
	} // public void onPlayerItemHeld(PlayerItemHeldEvent event)
	
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(final PlayerInteractEvent event)
	{
		if(event.getItem() == null)
			return;
		
		ItemStack item = event.getItem();
				
		if(item.getDurability() > 1023)
		{	Player p = event.getPlayer();
			RPG_Player rpgPlayer = mgr_Player.getPlayer(p.getName().hashCode());
					
			// Important, only do this if within the 10 second timer.
			if( (rpgPlayer.lTimer + 10000) <= System.currentTimeMillis())
			{	// Reset Timer and then exit
				rpgPlayer.lTimer = 0;
				return; 
			}
			
			short id = item.getDurability();
			// Check for Copper, Silver, or Gold
			if(id == RPGCraft.cp)
				rpgPlayer.getCharacter().setCopper(rpgPlayer.getCharacter().getCopper() + item.getAmount());
				
			if(id == RPGCraft.sp)
				rpgPlayer.getCharacter().setSilver(rpgPlayer.getCharacter().getSilver() + item.getAmount());
	
			if(id == RPGCraft.gp)
				rpgPlayer.getCharacter().setGold(rpgPlayer.getCharacter().getGold() + item.getAmount());
			
			p.setItemInHand(null);
			p.sendMessage("[§2RPG§f] Balance: §6"+rpgPlayer.getCharacter().getGold()+" Gold§f, §7"+rpgPlayer.getCharacter().getSilver()+" Silver§f, §c"+rpgPlayer.getCharacter().getCopper()+" Copper§f.");
						
		} // if(item.getDurability() > 1023)
	} // public void onPlayerInteract(final PlayerInteractEvent event)
		
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		Player p = event.getPlayer();
		ResultSet rs = null;
		
		// See if this player is in the database.
		String query = "SELECT * from Accounts WHERE mc_Name='"+p.getName()+"';";
		rs = SQLiteManager.SQLQuery(query);
		if(rs != null)
		{
			try {
				if(!rs.next())
				{	// New player.  Need to register this as a new player before logging in.
					mgr_Player.playerRegister(p);	
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} // if(rs != null)		
		
		RPG_Player rpgPlayer = mgr_Player.getPlayer(p.getName().hashCode());
		if(rpgPlayer!=null)
		{
			mgr_Player.playerLogin(p);
			p.sendMessage("Welcome back to the server "+p.getName()+".");
			return;
		}
		// If player has not been added to the RPG list then register and login the 
		// player now
		
		mgr_Player.playerLogin(p);			
		return;		
	} // public void onPlayerJoin(final PlayerJoinEvent event)	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(final PlayerQuitEvent event)
	{
		Player p = event.getPlayer();
		RPG_Player rpgPlayer = mgr_Player.getPlayer(p.getName().hashCode());
		if(rpgPlayer!=null)
		{
			if(rpgPlayer.bIsOnline)
				mgr_Player.playerLogout(p);
			
		} // if(rpgPlayer!=null)
		return;
	} // public void onPlayerQuit(PlayerEvent event)
	public listener_Player(Plugin p)
	{
		Bukkit.getServer().getPluginManager().registerEvents(this, p);
		rpgPlugin = p;
		rpgServer = rpgPlugin.getServer();
	 } // public listener_Player(Plugin p)
			
} // public class listener_Player implements Listener
