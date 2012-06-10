package net.tigerstudios.RPGCraft;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.tigerstudios.RPGCraft.utils.SQLiteManager;

import org.bukkit.entity.Player;

// The RPG Character class represents an ingame character and what events
// happen to the character.
public class RPG_Character {
	int CharacterID = 0;		// ID for this particular rpg character
	int AccountID = 0;			// Account of player that owns this character
	
	String Name;				// Players Role Play name
	String displayPrefix;	
	String displaySuffix;
	
	// Race Stats
	String race;		int level;
	int experience;		int exp_to_level;
		
	// Character Stats
	int dexterity; 	int constitution;
	int strength;	int intelligence;
	int attack, defense, parry;
		
	// Character Ablities
	int mining, farming, blacksmithing;
	int enchanting, alchemy, cooking;
	int fishing, trading;
	
	// Currency Methods
	int Copper;	
		
	public RPG_Character(Race r, int acc_id)
	{
		// First initialize character to default stats
		initialize(acc_id);		
		// Now adjust the stats for the given race.
		displaySuffix = "the "+r.Name;		
		dexterity 		+= r.dex_mod;
		constitution 	+= r.con_mod;
		strength		+= r.str_mod;
		intelligence	+= r.int_mod;
		
		saveCharacter();
	} // public RPG_Character(Race r)
	
	public RPG_Character(){	}
	
	public int getGold() { return Copper + (Copper * 100) * ( Copper * 10000); }
	public int getSilver() { return Copper + (Copper * 100); }
	public int getCopper() { return Copper; }
	public void setGold(int gp) { Copper = gp * 10000; }
	public void setSilver(int sp) { Copper = sp * 100; }
	public void setCopper(int cp) { Copper = cp; }	
	public int getTotalCopper(){ return Copper;}
	public void removeCopper(int cp, Player p)
	{	if(Copper > cp)
		{	p.sendMessage("[§2RPG§f] Error trying to remove " + cp + "from you.");
			p.sendMessage("[§2RPG§f] You only have "+ Copper);
			return;
		} // if(Copper > cp)
	} // public void removeCopper(int cp)
	
	public String getName() { return Name; }
	public void setName(String name) { Name = name; }
	
	
	public void initialize(int acc_id)
	{	
		Name = "unnamed";
		AccountID = acc_id;
		displayPrefix = "";	
		displaySuffix = "";	
				
		race = "default"; level = 1; experience = 0; exp_to_level = 0; 
		dexterity = 5; constitution = 5; strength = 5; intelligence = 5;
		attack = 1; defense = 1; parry = 1;
		
		// Character Ablities
		mining = 0; farming = 0; blacksmithing = 0;
		enchanting = 0; alchemy = 0; cooking = 0;
		fishing = 0; trading = 0;
		
		Copper = 0;
		
		// Save this new character to the db to get a CharacterID value
		saveCharacter();

	} // public void initialize()
	
	public int saveCharacter()
	{ 		
		String query = null;
				
		// Create the query to save the players data
		if(CharacterID != 0)
		{	// Already assigned an id, which means this is in the database already
			query = "update characters set " +
					"namePrefix 			= '"+displayPrefix+"'," +
					"nameSuffix				= '"+displaySuffix+"'," +
					"level					= "+level+","+
					"experience				= "+experience+","+
					"exp_to_levelup			= "+exp_to_level+","+
					"strength				= "+strength+","+
					"dexterity				= "+dexterity+","+
					"constitution			= "+constitution+","+
					"intelligence			= "+intelligence+","+
					"attack					= "+attack+","+
					"defense				= "+defense+","+
					"parry					= "+parry+"," +
					"mining					= "+mining+","+
					"farming				= "+farming+","+
					"blacksmithing			= "+blacksmithing+","+
					"enchanting				= "+enchanting+","+
					"alchemy				= "+alchemy+","+
					"cooking				= "+cooking+","+
					"fishing				= "+fishing+","+
					"trading                = "+trading+","+
					"copper					= "+Copper+
					" where char_id="+CharacterID+";";
			SQLiteManager.SQLUpdate(query);
			return CharacterID;
		} // if(CharacterID != 0)	
		
		query = "insert into characters (account_id, name, namePrefix, nameSuffix, race, level,"+
				"experience, exp_to_levelup, strength, dexterity, constitution, intelligence, mining," +
				"farming, blacksmithing, enchanting, alchemy, cooking, fishing, trading, copper) VALUES (" +
				AccountID+", '"+Name+"', '"+displayPrefix+"', '"+displaySuffix+"', '"+race+"', "+level+","+
				experience+","+exp_to_level+","+strength+","+dexterity+","+constitution+","+intelligence+","+
				mining+","+farming+","+blacksmithing+","+enchanting+","+alchemy+","+cooking+","+fishing+","+
				trading+","+Copper+")"; 
		
		SQLiteManager.SQLUpdate(query);
		
		query = "SELECT char_id FROM Characters WHERE account_id = "+this.AccountID;
		ResultSet rs = SQLiteManager.SQLQuery(query);
		
		try { rs.next();
			  CharacterID = rs.getInt("char_id");
			  rs.close();
		} catch (SQLException e) { e.printStackTrace();	}
		
		return CharacterID;
	} // public boolean savePlayerData()
	
} // public class RPG_Character