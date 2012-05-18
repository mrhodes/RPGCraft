package net.tigerstudios.RPGCraft;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.tigerstudios.RPGCraft.utils.SQLiteManager;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class mgr_Player {
	private static Map<Integer, RPG_Player> rpgPlayers = new HashMap<Integer, RPG_Player>(); 
	
	// private static List<RPG_Player> rpgPlayers = new ArrayList<RPG_Player>();
	//private static Plugin rpgPlugin = null;
	private static Server rpgServer = null;		
	
	public static RPG_Player getPlayer(String mcName)
	{
		// If there are no player loaded or the name provided is null, just return
		if(rpgPlayers.isEmpty() || mcName == null)
			return null;		
		
		return rpgPlayers.get(mcName.hashCode());				
	} // public static RPG_Player getPlayer(String name)
		
	
	// ----------------------------------------------------
	// playerRegister()
	//
	// This method is called when a new player joins the
	// server.  The 
	public static boolean playerRegister(Player p)
	{
		RPG_Player player = null;
		ResultSet rs = null;
		int account_id = 0;
		
		// Need to create an entry in the Accounts Table with this players
		// Name and Date joined.
		String query = "INSERT INTO Accounts (mc_Name, joined) VALUES ('"+
		 p.getName()+"', DATE());";
		SQLiteManager.SQLUpdate(query);
		
		// Need to get the ID of this player before we continue.  
		query = "SELECT account_id from Accounts WHERE mc_Name = '"+p.getName()+"';";
		rs = SQLiteManager.SQLQuery(query);
		if(rs != null)
		{	try { while(rs.next()) { account_id = rs.getInt("account_id"); }}
			catch (SQLException e) { e.printStackTrace(); }
		} // if(rs != null)
		
		// TODO: Remove this line.  Only for testing purposes
		p.sendMessage("Account ID = " + account_id);
		
		// New player, create a RPG Character for this player.
		player = new RPG_Player(p.getName(), account_id);
		player.initialize();
				
		// Save this player in the database, and then add to the player list
		player.savePlayerData();		
		rpgPlayers.put(p.getName().hashCode(), player);		
		
		return true;
	} // public boolean playerRegister(Player p, String name)
	
	public static boolean playerLogin(Player p)
	{
		RPG_Player rpgPlayer = mgr_Player.getPlayer(p.getName());
		if(rpgPlayer != null)
		{
			rpgPlayer.setPlayer(p);
			rpgPlayer.loadPlayerData();
			rpgPlayer.bIsOnline = true;		
			return true;
		} // if(rpgPlayer != null)
					
		return false;
	} // public static boolean playerLogin(Player p)
		
	public static void playerLogout(Player p)
	{
		if(p==null)
			return;
		RPG_Player rpgPlayer = mgr_Player.getPlayer(p.getName());
		if(rpgPlayer != null)
		{
			if(rpgPlayer.bIsOnline)
			{
				rpgPlayer.savePlayerData();				
				rpgPlayer.setPlayer(null);
				rpgPlayer.bIsOnline = false;
			} // if(rpgPlayer.bIsOnline)
		}
		return;
	} // public boolean playerLogout(Player p, String name)	

	
	public static int getPlayerCount()
	{
		return rpgPlayers.size();
	} // public static int getPlayerCount()
	
			
	public static void SaveAllData()
	{
		Collection<RPG_Player> players = rpgPlayers.values();
		
		for(RPG_Player p: players)
			p.savePlayerData();		
		
		RPGCraft.log.info("[RPGCraft] ---> Successfully saved all player data.");
	} // public static void SaveAllData()
		
	
	public static void LoadAllData()
	{	ResultSet rs = null;
		// Clear the list first. Then start fresh
		rpgPlayers.clear();
		
		// Load all record from the playerData Table
		rs = SQLiteManager.SQLQuery("select * from playerData;");
		if(rs != null)
		{	try {
				while(rs.next())
				{	// Create a new RPG_Player object and fill it with the values that
					// were loaded.
					RPG_Player rpgPlayer = null;//new RPG_Player(rs.getString("mcName"));
					
					rpgPlayer.mcName 				= rs.getString("mcName");
					rpgPlayer.rpgName 				= rs.getString("rpgName");
					rpgPlayer.Copper				= rs.getInt("copper");					
					
					// Check if this player is online, if so set the Player field.  Will return null
					// if player is not online.
					rpgPlayer.player = rpgServer.getPlayer(rpgPlayer.mcName);
					if(rpgPlayer.player != null)
						rpgPlayer.bIsOnline = true;
					else
						rpgPlayer.bIsOnline = false;	
						
					rpgPlayers.put(rpgPlayer.mcName.hashCode(), rpgPlayer);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		RPGCraft.log.info("[RPGCraft] ---> Successfully loaded all player data.");
		
	} // public static void LoadAllData()
	
	
	public static void logoutAllPlayers()
	{
		Collection<RPG_Player> players = rpgPlayers.values();
		for(RPG_Player rpgPlayer: players)
			playerLogout(rpgPlayer.GetPlayer());
			
	} // public static void logoutAllPlayers()

	
	public static void initialize(Plugin p)
	{
		//rpgPlugin = p;
		rpgServer = p.getServer();
	}


} // public class mgr_Player {
