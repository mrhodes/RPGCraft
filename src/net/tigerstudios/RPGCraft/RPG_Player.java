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
	
	int PlayerID = 0;			// unique player ID.  Value stored in Database
	boolean bIsOnline;			// Is this player currently online
	Player player;				// Minecraft Player class
	String mcName;				// Players Minecraft Name
	String rpgName;				// Players Role Play name
	String serverGroup;				// Players group on the server
	String displayPrefix;	
	String displaySuffix;
	
    // Race Stats
	int race;
	int profession;
	int level;
	int experience;
	int agility;
	int stamina;
	int strength;
	int intelligence;
	
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
	{
			
	}
	
	public RPG_Player(String minecraftName)
	{
		this.player = null;		
		mcName = minecraftName;
		this.rpgName = mcName;
		
		PlayerID = mcName.hashCode();		
	} // public void RPG_Player(Player p)
	
	
	public Player GetPlayer() { return player; }
	
	// This method will be called when a new player registers to be a RPG character.
	public void initialize()
	{	// Give initial amount of coin
		setGold(RPGCraft.config.getInt("default_gold"));
		setSilver(RPGCraft.config.getInt("default_silver"));
		setCopper(RPGCraft.config.getInt("default_copper"));

	} // public void initialize()
	
	public boolean savePlayerData()
	{ 
		String query = null;
		ResultSet rs = null;
		boolean bInDB = false;
		// Create the query to save the players data
				
		rs = SQLiteManager.SQLQuery("select * from playerData where mcName ='"+mcName+"';");
	
		try {
			bInDB = rs.next();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(bInDB)
		{	query = "update playerData set " +
				"	mcName 			= '"+mcName+"'," +
				"   rpgName			= '"+rpgName+"'," +
				"   copper = "+Copper +
				" where mcName='"+mcName+"';";
		}else {
			query = "insert into playerData values (" +
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
		query = "select * from playerData where mcName='"+mcName+"'";
		
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
