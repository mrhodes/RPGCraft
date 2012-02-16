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
	
	int PlayerID = 0;		// Hash of mine craft name
	public boolean bIsOnline;		// Is this player currently online
	Player player;			// Minecraft Player class
	String mcName;
	String rpgName;
	String displayName;
	String serverGroup;		// Players group on the server
	String displayPrefix;	
	String displaySuffix;
	int Gold;
	int Silver;
	int Copper;
	
	// To be implemented at a later date
	int KillsPerMinute = 0;
	long lLastKillTime = 0;	// Used to prevent Farming Coin
	
	
	long lTimer = -1;		// Used for things that need a timer.  This
							// Value will be the end time of a time
							// length
		
	// Data gathered from the Permissions plugin
	String group = null;
	String prefix = null;
	String suffix = null;	
	
	public int getGold() { return Gold; }
	public int getSilver() { return Silver; }
	public int getCopper() { return Copper; }
	public int getTotalCopper(){ return Copper + (100 * Silver) + (10000 * Gold); }
	public String getMCName() { return mcName; }
	public String getRpgName() { return rpgName; }
	
	public void setGold(int gp) { Gold = gp; }
	public void setSilver(int sp) { Silver = sp; }
	public void setCopper(int cp) { Copper = cp; }	
	public void setMCName(String name) { mcName = name; }
	public void setRpgName(String name) { rpgName = name; }
	
	public void setPlayer(Player p) { player = p; }
	// methods used for
	
	public void optimizeCoin()
	{
		while(Copper > 99)
		{
			Silver = Silver + 1;
			Copper = Copper - 100;
		}
		while(Silver > 99)
		{
			Gold = Gold + 1;
			Silver = Silver - 100;
		}
	} // public void optimizeCoin()
	public void removeCopper(int cp)
	{
		int newGold = getGold();
		int newSilver = getSilver();
		int newCopper = getCopper();
		
		// remove the copper and then fix the numbers so there are no negatives
		newCopper = newCopper - cp;
		while(newCopper < 0)
		{
			newSilver = newSilver - 1;
			newCopper = newCopper + 100;
		}
		while(newSilver < 0)
		{
			newGold = newGold - 1;
			newSilver = newSilver + 100;
		}
		
		setGold(newGold);
		setSilver(newSilver);
		setCopper(newCopper);		
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
	{	// Set Stats
		int g = 0; int s = 0; int c = 0;
		// Give initial amount of coin
		g = RPGCraft.config.getInt("default_gold");
		s = RPGCraft.config.getInt("default_silver");
		c = RPGCraft.config.getInt("default_copper");
			
		setGold(g);
		setSilver(s);
		setCopper(c);
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
				"   gold = "+Gold+", silver = "+Silver+", copper = "+Copper +
				" where mcName='"+mcName+"';";
		}else {
			query = "insert into playerData values (" +
				"	'"+mcName+"'," +
				"   '"+rpgName+"'," +
				"   "+Gold+", "+Silver+", "+Copper+");";   
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
					Gold = rs.getInt("gold");
					Silver = rs.getInt("silver");
					Copper = rs.getInt("copper");
				} // if(rs.getFetchSize() != 0)
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} // if(rs != null)	
		
			
		// Set the players display name with prefix and suffix if applicable
		if(rpgName.equals("null"))
			displayName = mcName;
		else
			displayName = rpgName;
			
		this.player.setDisplayName(displayName);
		
		return true;
	} // public boolean loadPlayerData()	
}
