package net.tigerstudios.RPGCraft;

import net.tigerstudios.RPGCraft.utils.SQLiteManager;
import org.bukkit.entity.Player;
import java.sql.ResultSet;
import java.sql.SQLException;

// The RPG Character class represents an ingame character and what events
// happen to the character.
public class RPG_Character {
	int CharacterID = 0;		// ID for this particular rpg character
	int AccountID = 0;			// Account of player that owns this character
	String Name;				// Players Role Play name
	String displayPrefix;	
	String displaySuffix;
	String skinURL;
		
	// Race Stats
	int race;			int level;
	int experience;		int exp_to_level;
		
	// Character Stats
	int dexterity; 	int constitution;
	int strength;	int intelligence;
	int attack, defense, parry;
		
	// Character Ablities
	int mining, farming, blacksmithing;
	int enchanting, alchemy, cooking;
	
	// Currency Methods
	int Copper;	
	public int getGold() { return Copper + (Copper * 100) * ( Copper * 10000); }
	public int getSilver() { return Copper + (Copper * 100); }
	public int getCopper() { return Copper; }
	public void setGold(int gp) { Copper = gp * 10000; }
	public void setSilver(int sp) { Copper = sp * 100; }
	public void setCopper(int cp) { Copper = cp; }	
	public int getTotalCopper(){ return Copper;}
	public void removeCopper(int cp, Player p)
	{	if(Copper > cp)
		{	p.sendMessage("[RPG] Error trying to remove " + cp + "from you.");
			p.sendMessage("[RPG] You only have "+ Copper);
			return;
		} // if(Copper > cp)
	} // public void removeCopper(int cp)
	
	public String getName() { return Name; }
	public void setName(String name) { Name = name; }
	public void initialize()
	{	
		displayPrefix = "";	
		displaySuffix = "";	
		skinURL = "";
		
		race = 0; level = 1; experience = 0; exp_to_level = 0; 
		dexterity = 5; constitution = 5; strength = 5; intelligence = 5;
		attack = 1; defense = 1; parry = 1;
		
		// Character Ablities
		mining = 0; farming = 0; blacksmithing = 0;
		enchanting = 0; alchemy = 0; cooking = 0;
		
		Copper = 0;

	} // public void initialize()
	public boolean saveCharacter()
	{ 		
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
				"namePrefix 			= '"+displayPrefix+"'," +
				"nameSuffix				= '"+displaySuffix+"'," +
				"level					= "+level+","+
				"experience				= "+experience+","+
				"exp_to_levelup			= "+exp_to_level+","+
				"strength				= "+strength+","+
				"dexterity				= "+dexterity+","+
				"constitution			= "+constitution+","+
				"intelligence			= "+intelligence+","+
				"mining					= "+mining+","+
				"farming				= "+farming+","+
				"blacksmithing			= "+blacksmithing+","+
				"enchanting				= "+enchanting+","+
				"alchemy				= "+alchemy+","+
				"cooking				= "+cooking+","+
				"skinURL				= '"+skinURL+"',"+
				"copper					= "+Copper+
				"where char_id="+CharacterID+";";
						
		}else {
			query = "insert into Characters values (" +
					"account_id				= "+AccountID+","+
					"name					= "+Name+","+
					"namePrefix 			= '"+displayPrefix+"'," +
					"nameSuffix				= '"+displaySuffix+"'," +
					"race					= "+race+","+
					"level					= "+level+","+
					"experience				= "+experience+","+
					"exp_to_levelup			= "+exp_to_level+","+
					"strength				= "+strength+","+
					"dexterity				= "+dexterity+","+
					"constitution			= "+constitution+","+
					"intelligence			= "+intelligence+","+
					"mining					= "+mining+","+
					"farming				= "+farming+","+
					"blacksmithing			= "+blacksmithing+","+
					"enchanting				= "+enchanting+","+
					"alchemy				= "+alchemy+","+
					"cooking				= "+cooking+","+
					"copper					= "+Copper+","+
					"skinURL				= '"+skinURL+"' "+
					");";   
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
					Name = rs.getString("rpgName");
					Copper = rs.getInt("copper");
				} // if(rs.getFetchSize() != 0)
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} // if(rs != null)
		
		return true;
	} // public boolean loadPlayerData()
} // public class RPG_Character
