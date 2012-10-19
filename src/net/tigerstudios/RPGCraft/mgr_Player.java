package net.tigerstudios.RPGCraft;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.tigerstudios.RPGCraft.CombatSystem.CombatSystem;
import net.tigerstudios.RPGCraft.utils.SQLManager;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class mgr_Player {
	private static Map<Integer, RPG_Player> rpgPlayers = new HashMap<Integer, RPG_Player>(); 
	private static Map<Integer, Player> mcPlayers = new HashMap<Integer, Player>();
		
	
	public static RPG_Player getPlayer(int nameHash)
	{	// If there are no player loaded or the name provided is null, just return
		if(rpgPlayers.isEmpty() || nameHash == 0)
			return null;
		
		return rpgPlayers.get(nameHash);				
	} // public static RPG_Player getPlayer(String name)
	
	
	public static Player getMCPlayer(int id)
	{		
		if(mcPlayers.size() == 0 || id == 0)
			return null;
		
		return mcPlayers.get(id);
	} // public static Player getMCPlayer(int id)
	
	
	public static RPG_Character getCharacter(Player player)
	{
		if(rpgPlayers.isEmpty() || (player == null))
			return null;
		
		if((rpgPlayers.get(player.getName().hashCode()) == null))
			return null;				
		else
			return rpgPlayers.get(player.getName().hashCode()).getCharacter();
	} // public static RPG_Character getCharacter(Player player)
		
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
		SQLManager.SQLUpdate(query);	
		
		return true;
	} // public boolean playerRegister(Player p, String name)
	public static boolean playerLogin(Player p)
	{
		int account_id = 0;		
		
		String query = "SELECT account_id from Accounts WHERE mc_Name = '"+p.getName()+"'";
		ResultSet rs = SQLManager.SQLQuery(query);
		
		// No reason this should fail
		try {
			if(rs.next())
			{	account_id = rs.getInt("account_id");
				rs.close();				
			} // if(rs.next())		
		} catch (SQLException e) { e.printStackTrace();	} 
			
		if(account_id == 0)
			return false;
		
		// Store Player object in Map for safe keeping, AccountID is the Key
		mcPlayers.put(account_id, p);
		rpgPlayers.put(p.getName().hashCode(), new RPG_Player(p.getName(), account_id));		
				
		if(getCharacter(p) == null)
		{
			p.sendMessage(RPGCraft.divider);
			p.sendMessage("Please select a race for yourself.");
			p.sendMessage("Type /§arpg list §fto see race choices.");
			p.sendMessage("and then type /§arpg choose §f<§drace§f> to choose the race.");
			p.sendMessage(RPGCraft.divider);
		}
		
		return true;
	} // public static boolean playerLogin(Player p)
	
	
	public static void playerLogout(Player p)
	{	if(p==null)	return;
	
		RPG_Player rpgPlayer = getPlayer(p.getName().hashCode());
		
		if(rpgPlayer != null)
		{	rpgPlayer.saveCharacterData();				
			rpgPlayer.setPlayer(null);
			
			// Remove this player from the collection of player data
			rpgPlayers.remove(p.getName().hashCode());
			mcPlayers.remove(rpgPlayer.getAccountID());
		} // if(rpgPlayer != null)
		return;
	} // public boolean playerLogout(Player p, String name)	
	
	public static void SaveAllData()
	{
		Collection<RPG_Player> players = rpgPlayers.values();
		
		for(RPG_Player p: players)
			p.saveCharacterData();		
		
		RPGCraft.log.info("[RPGCraft] ---> Successfully saved all player data.");
	} // public static void SaveAllData()
		
	
	public static void logoutAllPlayers()
	{
		Collection<RPG_Player> players = rpgPlayers.values();
		for(RPG_Player rpgPlayer: players)
			playerLogout(rpgPlayer.getPlayer());
			
	} // public static void logoutAllPlayers()
	
	public static void initialize(Plugin p)
	{		
		rpgPlayers.clear();
	} // public static void initialize(Plugin p)

} // public class mgr_Player {
