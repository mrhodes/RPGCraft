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
	private static Plugin rpgPlugin = null;
	private static Server rpgServer = null;
		
	
	public static RPG_Player getPlayer(String mcName)
	{
		// If there are no player loaded or the name provided is null, just return
		if(rpgPlayers.isEmpty() || mcName == null)
			return null;		
		
		return rpgPlayers.get(mcName.hashCode());				
	} // public static RPG_Player getPlayer(String name)
		
	
	public static boolean playerRegister(Player p)
	{
		RPG_Player player = null;
		
		// New player, create a RPG Character for this player.
		player = new RPG_Player(p.getName());
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
	/*
	public static List<RPG_Player> getPlayerArray()
	{
		;
	}*/	
			
	public static void SaveAllData()
	{
		Collection<RPG_Player> players = rpgPlayers.values();
		
		for(RPG_Player p: players)
			p.savePlayerData();		
		
		RPGCraft.log.info("[RPGCraft] ---> Successfully saved all player data.");
	} // public static void SaveAllData()
		
	
	public static void LoadAllData()
	{
		ResultSet rs = null;
		// Clear the list first. Then start fresh
		rpgPlayers.clear();
		
		// Load all record from the playerData Table
		rs = SQLiteManager.SQLQuery("select * from playerData;");
		if(rs != null)
		{
			try {
				while(rs.next())
				{	// Create a new RPG_Player object and fill it with the values that
					// were loaded.
					RPG_Player rpgPlayer = new RPG_Player(rs.getString("mcName"));
					
					rpgPlayer.mcName 				= rs.getString("mcName");
					rpgPlayer.rpgName 				= rs.getString("rpgName");
					rpgPlayer.Gold					= rs.getInt("gold");
					rpgPlayer.Silver				= rs.getInt("silver");
					rpgPlayer.Copper				= rs.getInt("copper");					
					
					// Check if this player is online, if so set the Player field.  Will return null
					// if player is not online.
					rpgPlayer.player = rpgServer.getPlayer(rpgPlayer.mcName);
					if(rpgPlayer.player != null)
						rpgPlayer.bIsOnline = true;
					else
						rpgPlayer.bIsOnline = false;
					
					// Get the group, prefix and suffix from the permissions file.
				//	rpgPlayer.serverGroup	= RPGCraft.Permissions.getGroup(rpgPlayer.mcName, null);
				//	rpgPlayer.displayPrefix	= RPGCraft.Permissions.getGroupPrefix(rpgPlayer.mcName, null);
				//	rpgPlayer.displaySuffix	= RPGCraft.Permissions.getGroupSuffix(rpgPlayer.mcName, null);
					
					rpgPlayer.displayName = rpgPlayer.displayPrefix+" "+rpgPlayer.rpgName+" "+rpgPlayer.displaySuffix; 
					
					rpgPlayers.put(rpgPlayer.mcName.hashCode(), rpgPlayer);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
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
		rpgPlugin = p;
		rpgServer = p.getServer();
	}


} // public class mgr_Player {
