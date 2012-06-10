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
	private static Server rpgServer = null;		
	
	
	public static RPG_Player getPlayer(int nameHash)
	{	// If there are no player loaded or the name provided is null, just return
		if(rpgPlayers.isEmpty() || nameHash == 0)
			return null;		
		
		return rpgPlayers.get(nameHash);				
	} // public static RPG_Player getPlayer(String name)
	
	
	// ----------------------------------------------------
	// playerRegister()
	//
	// This method is called when a new player joins the
	// server.  This method does not create any new characters 
	public static boolean playerRegister(Player p)
	{
		String query = null;
		
		// Need to create an entry in the Accounts Table with this players
		// Name and Date joined.
		query = "INSERT INTO Accounts (mc_Name, joined) VALUES ('"+p.getName()+"', DATE());";
		SQLiteManager.SQLUpdate(query);	
		
		return true;
	} // public boolean playerRegister(Player p, String name)
	
	public static boolean playerLogin(Player p)
	{
		String query;
		ResultSet rs;
		int account_id = 0;
		RPG_Player rpgPlayer;
		
		query = "SELECT account_id from Accounts WHERE mc_Name = '"+p.getName()+"';";
		rs = SQLiteManager.SQLQuery(query);
		
		// No reason this should fail
		try {
			if(rs.next())
			{
				account_id = rs.getInt("account_id");
				rs.close();
				if(account_id == 0)
					return false;
			}else
				{ rs.close(); return false; }
			
		} catch (SQLException e) { e.printStackTrace();	} 
									
		// New player, create a RPG Character for this player.
		rpgPlayer = new RPG_Player(p.getName(), account_id);
		rpgPlayer.initialize(p.getName());
			
		rpgPlayer.loadCharacterData();
		rpgPlayer.getSpoutPlayer().setTitle(rpgPlayer.getCharacter().displaySuffix);
		rpgPlayer.bIsOnline = true;		
		// Add this player to the Player map
		rpgPlayers.put(p.getName().hashCode(), rpgPlayer);			
					
		return true;
	} // public static boolean playerLogin(Player p)
		
	public static void playerLogout(Player p)
	{	if(p==null)
			return;
		RPG_Player rpgPlayer = mgr_Player.getPlayer(p.getName().hashCode());
		if(rpgPlayer != null)
		{	if(rpgPlayer.bIsOnline)
			{	rpgPlayer.saveCharacterData();				
				rpgPlayer.setPlayer(null);
				rpgPlayer.bIsOnline = false;
			} // if(rpgPlayer.bIsOnline)
		} // if(rpgPlayer != null)
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
			p.saveCharacterData();		
		
		RPGCraft.log.info("[RPGCraft] ---> Successfully saved all player data.");
	} // public static void SaveAllData()
		
	
	public static void LoadAllData()
	{	ResultSet rs = null;
		// Clear the list first. Then start fresh
		rpgPlayers.clear();
		
		// Load all records from the Accounts Table
		rs = SQLiteManager.SQLQuery("select * from Accounts");
		
		try {
			while(rs.next())
			{	// Create a new RPG_Player object and fill it with the values that
				// were loaded.
				RPG_Player rpgPlayer = new RPG_Player(rs.getString("mc_Name"), rs.getInt("account_id"));
								
				// Check if this player is online, if so set the Player field.  Will return null
				// if player is not online.
				rpgPlayer.setPlayer(rpgServer.getPlayer(rpgPlayer.getMCName()));
				if(rpgPlayer.getPlayer() != null)
					rpgPlayer.bIsOnline = true;
				else
					rpgPlayer.bIsOnline = false;	
				
				// Load this players character.
				rpgPlayer.loadCharacterData();
				
				rpgPlayers.put(rpgPlayer.getMCName().hashCode(), rpgPlayer);
			} // while(rs.next())	
			rs.close();		
		} catch (SQLException e) { e.printStackTrace();	} 
				
		RPGCraft.log.info("[RPGCraft] ---> Successfully loaded all player data.");
	} // public static void LoadAllData()
	
	
	public static void logoutAllPlayers()
	{
		Collection<RPG_Player> players = rpgPlayers.values();
		for(RPG_Player rpgPlayer: players)
			playerLogout(rpgPlayer.getPlayer());
			
	} // public static void logoutAllPlayers()
	
	public static void initialize(Plugin p)
	{
		rpgServer = p.getServer();
		rpgPlayers.clear();
	} // public static void initialize(Plugin p)

} // public class mgr_Player {
