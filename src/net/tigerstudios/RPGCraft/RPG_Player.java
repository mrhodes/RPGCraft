package net.tigerstudios.RPGCraft;

import java.sql.ResultSet;
import java.sql.SQLException;


import net.tigerstudios.RPGCraft.utils.SQLiteManager;

import org.bukkit.entity.Player;


/* ----------------------------------------------------------------------------
 * RPG_Player
 * 
 * The RPG_Player class will be the main class for each in game player. 
 */
public class RPG_Player{
	int AccountID = 0;			// unique account ID.  Value stored in Database
	int CharacterID = 0;		// ID for this particular rpg character
	boolean bIsOnline;			// Is this player currently online
	Player player;				// Minecraft Player class
	String mcName;				// Players Minecraft Name
	String rpgName;				// Players Role Play name
	String displayPrefix;	
	String displaySuffix;
	String skinURL;
	
    // Race Stats
	int race;
	int level;
	int experience;
	
	// Character Stats
	int agility; 	int stamina;
	int strength;	int intelligence;
	int attack, defense, parry;
	
	// Character Ablities
	int mining, farming, blacksmithing;
	int enchanting, alchemist;	
	
	// TODO: Add universal MT Timer/Callback system.  Pull timer out of Player class
	long lTimer = -1;		// Used for things that need a timer.  This
							// Value will be the end time of a time
							// length
	
	// Currency Methods
	int Copper;	
		
	public int getGold() { return Copper + (Copper * 100) * ( Copper * 10000); }
	public int getSilver() { return Copper + (Copper * 100); }
	public int getCopper() { return Copper; }
	public int getTotalCopper(){ return Copper;}
	public String getMCName() { return mcName; }
	public String getRpgName() { return rpgName; }
	
	public void setGold(int gp) { Copper = gp * 10000; }
	public void setSilver(int sp) { Copper = sp * 100; }
	public void setCopper(int cp) { Copper = cp; }	
	public void setMCName(String name) { mcName = name; }
	public void setRpgName(String name) { rpgName = name; }
	
	public void setPlayer(Player p) { player = p; }
				
	public void removeCopper(int cp)
	{	if(Copper > cp)
		{	player.sendMessage("[RPG] Error trying to remove " + cp + "from you.");
			player.sendMessage("[RPG] You only have "+ Copper);
			return;
		} // if(Copper > cp)
	} // public void removeCopper(int cp)
	
	
	public RPG_Player(String minecraftName, int id)
	{
		player = null;		
		mcName = minecraftName;
		rpgName = mcName;
		AccountID = id;
		CharacterID = 0;
	} // public void RPG_Player(Player p)
	
	
	public Player GetPlayer() { return player; }
	
	// This method will be called when a new player registers to be a RPG character.
	public void initialize()
	{	// Give initial amount of coin
		setGold(RPGCraft.config.getInt("default_gold"));
		setSilver(RPGCraft.config.getInt("default_silver"));
		setCopper(RPGCraft.config.getInt("default_copper"));
		
		displayPrefix = "";	
		displaySuffix = "";	
		skinURL = "";
		
		race = 0; level = 1; experience = 0; 
		agility = 10; stamina = 10; strength = 10;	intelligence = 10;
		attack = 1; defense = 1; parry = 1;
		
		// Character Ablities
		mining = 0; farming = 0; blacksmithing = 0;
		enchanting = 0; alchemist = 0;

	} // public void initialize()
	
	public boolean savePlayerData()
	{ 		
		// First check if the player even has a loaded 
		// character to save.
		if(CharacterID == 0)
			return false;
		
		String query = null;
		ResultSet rs = null;
		boolean bInDB = false;
		
		// Create the query to save the players data
		// Get all Character data for this player (This can return multiple records)
		rs = SQLiteManager.SQLQuery("select * from Characters where char_id = "+CharacterID+";");
	
		try { bInDB = rs.next();} 
		catch (SQLException e1) { e1.printStackTrace();	}
		
		if(bInDB)
		{	query = "update Characters set " +
				"	mcName 			= '"+mcName+"'," +
				"   rpgName			= '"+rpgName+"'," +
				"   copper = "+Copper +
				" where mcName='"+mcName+"';";
		}else {
			query = "insert into Characters values (" +
				"	'"+mcName+"'," +
				"   '"+rpgName+"'," +
				"    "+Copper+");";   
		}
		SQLiteManager.SQLUpdate(query);
				
		return true;
	} // public boolean savePlayerData()
	
	public boolean loadPlayerData()
	{
		String query = null;
		ResultSet rs = null;
		
		query = "select * from Characters where account_id = "+this.AccountID+";";
		rs = SQLiteManager.SQLQuery(query);
		
		if(rs != null)
		{	try 
			{	if(rs.getFetchSize() != 0)
				{										
					rpgName = rs.getString("rpgName");
					Copper = rs.getInt("copper");
				} // if(rs.getFetchSize() != 0)
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} // if(rs != null)
		
		return true;
	} // public boolean loadPlayerData()	
}
