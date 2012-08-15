package net.tigerstudios.RPGCraft;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.tigerstudios.RPGCraft.utils.SQLManager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

// The RPG Character class represents an ingame character and what events
// happen to the character.
public class RPG_Character 
{
	int CharacterID = 0;		// ID for this particular rpg character
	int AccountID = 0;			// Account of player that owns this character
	
	String Name;				// Players Role Play name
	String mcName;
	String displayPrefix;	
	private String displaySuffix;
	
	// Race Stats
	public String race;		int level;
	float experience;		float exp_to_level;
	float levelExpTotal;
		
	// Character Stats
	int statPtsUsed, statPtsTotal;
	int dexterity; 	int constitution;
	int strength;	int intelligence;
	int attack, defense, parry;
	int alcoholTolerance;
	
	public int drunkenLevel;
			
	// Character Ablities
	public int mining;
	public int farming;
	int blacksmithing;
	int enchanting, alchemy, cooking;
	int fishing, trading;
	
	public float mineSkillBar;
	public float mineRaceMod;
	public float farmSkillBar;
	public float farmRaceMod;
	float blacksmithSkillBar;
	float blacksmithRaceMod;
	float enchantSkillBar, enchantRaceMod, alchemySkillBar, alchemyRaceMod, cookSkillBar, cookRaceMod;
	float fishSkillBar, fishRaceMod, tradeSkillBar, tradeRaceMod;
	
	// Modifier values
	public float fWalkSpeed;
	public float fSwimSpeed;
	public float fJumpMult, fGravityMult;
	
	// debuging info.
	float totalLootBonus, totalHarvestBonus, totalSkillIncrease;
	int loot, harvest, skill, count;
	float avgLootBonus, avgHarvestBonus, avgSkillIncrease;
	int totalSeeds, totalWheat;
	
	// Currency Methods
	int Copper, Silver, Gold;
		
	public RPG_Character(Race r, int acc_id)
	{
		if(r == null)
			return;
		
		// First initialize character to default stats
		initialize(acc_id);		
		// Now adjust the stats for the given race.
		
		race 				= r.Name;
		setDisplaySuffix("the "+r.Name);		
		dexterity 			+= r.dex_mod;
		constitution 		+= r.con_mod;
		strength			+= r.str_mod;
		intelligence		+= r.int_mod;
		fWalkSpeed			= r.speed;		
		
		farmRaceMod			= r.farming;
		mineRaceMod			= r.mining;
		blacksmithRaceMod 	= r.blacksmithing;
		enchantRaceMod		= r.enchanting;
		
		saveCharacter();
	} // public RPG_Character(Race r)
	
	
	public RPG_Character(){	}
	
	public int getGold() { return Gold; }
	public int getSilver() { return Silver; }
	public int getCopper() { return Copper; }
	public void addGold(int gp)	{	Gold += gp; }
	public void addSilver(int sp) {	 Silver += sp; optimizeCoin(); }
	public void addCopper(int cp) { Copper += cp; optimizeCoin(); }	
	public int getAccountID() { return AccountID; }
	public int getTotalCopper()
	{	return Copper + (Silver * 100) + (Gold * 10000); }
	
	public void optimizeCoin()
	{	if(Copper > 0)
		{	while(Copper > 100) { Copper -= 100; Silver += 1; }
			while(Silver > 100)	{ Silver -= 100; Gold += 1; }
			return;
		}		
		if(Copper < 0)
		{	while(Copper < 0) { Silver -= 1; Copper += 100;}
			while(Silver < 0) { Gold -= 1; Silver += 100; }
			return;
		}
		
	} // public void optimizeCoin()
	
	public void removeCopper(int cp, Player p)
	{
		if(getTotalCopper() < cp)
		{	
			p.sendMessage("[§2RPG§f] Error trying to remove " + cp + " from you.");
			p.sendMessage("[§2RPG§f] You only have "+ Copper);
			return;
		} // if(Copper > cp)
		
		Copper -= cp;
		optimizeCoin();
				
	} // public void removeCopper(int cp)
	
	public String getName() { return Name; }
	public void setName(String name) { Name = name;  }
	public void setMCName(String n) { mcName = n; }
	public String getMCName() { return mcName; }
	
	public void addExperience(float exp, SpoutPlayer p)
	{
		if(level == 50)
			return;
		exp_to_level -= exp;
		experience += exp;
		
		if(exp_to_level <= 0)
		{
			// Leveled up!
			// Play Sound, increase level, and recalculate next exp_to_level value
			level += 1;		
			exp_to_level = (float)(level * 100) * 1.25f;
			levelExpTotal = exp_to_level;		
			
			SpoutManager.getSoundManager().playCustomSoundEffect(RPGCraft.getPlugin(), p, 
					"http://tigerstudios.net/minecraft/sounds/levelup.ogg", false);
			p.sendNotification("Level Up!", "You are now level "+level, Material.DIAMOND);
			Bukkit.broadcastMessage("   [§2RPG§f] "+p.getName()+" just leveled up! Now "+p.getName()+" is a level "
					+level+" "+race+".");
		} // if(exp_to_level <= 0)
		
		p.setLevel(level);
		p.setExp( (levelExpTotal - exp_to_level) / levelExpTotal );				
	} // public void addExperience(float exp, SpoutPlayer p)
	
	public String getDisplaySuffix() {
		return displaySuffix;
	}

	public void setDisplaySuffix(String displaySuffix) {
		this.displaySuffix = displaySuffix;
	}

	public void initialize(int acc_id)
	{	
		Name = "unnamed";
		mcName = null;
		AccountID = acc_id;
		displayPrefix = "";	
		setDisplaySuffix("");	
				
		race = "default"; level = 1; experience = 0; exp_to_level = 100; 
		dexterity = 5; constitution = 5; strength = 5; intelligence = 5;
		attack = 1; defense = 1; parry = 1;
		
		// Character Ablities
		mining = 1; farming = 1; blacksmithing = 1;
		enchanting = 1; alchemy = 1; cooking = 1;
		fishing = 1; trading = 1;
		
		Copper = 0;
	} // public void initialize()
	
	public int saveCharacter()
	{ 		
		String query = null;
				
		// Create the query to save the players data
		if(CharacterID != 0)
		{	// Already assigned an id, which means this is in the database already
			query = "update characters set " +
					"namePrefix 			= '"+displayPrefix+"'," +
					"nameSuffix				= '"+getDisplaySuffix()+"'," +
					"name					= '"+Name+"',"+
					"race					= '"+race+"',"+
					"level					= "+level+","+
					"experience				= "+experience+","+
					"exp_to_levelup			= "+exp_to_level+","+
					"statPointsUsed			= "+statPtsUsed+","+
					"statPointsTotal        = "+statPtsTotal+","+
					"strength				= "+strength+","+
					"dexterity				= "+dexterity+","+
					"constitution			= "+constitution+","+
					"intelligence			= "+intelligence+","+
					"attack					= "+attack+","+
					"defense				= "+defense+","+
					"parry					= "+parry+"," +
					"mine					= "+mining+","+
					"mineSkillBar			= "+mineSkillBar+","+
					"mineRaceMod			= "+mineRaceMod+","+
					"farm					= "+farming+","+
					"farmSkillBar			= "+farmSkillBar+","+
					"farmRaceMod			= "+farmRaceMod+","+
					"blacksmith				= "+blacksmithing+","+
					"blacksmithSkillBar		= "+blacksmithSkillBar+","+
					"blacksmithRaceMod		= "+blacksmithRaceMod+","+
					"enchant				= "+enchanting+","+					
					"enchantSkillBar		= "+enchantSkillBar+","+
					"enchantRaceMod			= "+enchantRaceMod+","+
					"alchemy				= "+alchemy+","+
					"alchemySkillBar		= "+alchemySkillBar+","+
					"alchemyRaceMod			= "+alchemyRaceMod+","+
					"cook					= "+cooking+","+
					"cookSkillBar			= "+cookSkillBar+","+
					"cookRaceMod			= "+cookRaceMod+","+
					"fish					= "+fishing+","+
					"fishSkillBar			= "+fishSkillBar+","+
					"fishRaceMod			= "+fishRaceMod+","+
					"trade                	= "+trading+","+
					"tradeSkillBar			= "+tradeSkillBar+","+
					"tradeRaceMod			= "+tradeRaceMod+","+
					"alcoholTolerance       = "+alcoholTolerance+","+
					"copper					= "+getTotalCopper()+					
					" where char_id="+CharacterID+";";
			SQLManager.SQLUpdate(query);
			return CharacterID;
		} // if(CharacterID != 0)	
		
		query = "insert into characters (account_id, name, namePrefix, nameSuffix, race, level,"+
				"experience, exp_to_levelup, statPointsUsed, statPointsTotal, strength, dexterity, " +
				"constitution, intelligence, attack, defense, parry, mine, mineSkillBar, mineRaceMod, " +
				"farm, farmSkillBar, farmRaceMod, blacksmith, blacksmithSkillBar, blacksmithRaceMod, " +
				"enchant, enchantSkillBar, enchantRaceMod, alchemy, alchemySkillBar, alchemyRaceMod, " +
				"cook, cookSkillBar, cookRaceMod, fish, fishSkillBar, fishRaceMod, trade, tradeSkillBar, tradeRaceMod, " +
				"alcoholTolerance, copper) " +
				"VALUES ("+AccountID+", '"+Name+"', '"+displayPrefix+"', '"+getDisplaySuffix()+"', '"+race+"', "+level+","+
				experience+","+exp_to_level+","+statPtsUsed+","+statPtsTotal+","+strength+","+dexterity+","+constitution+","+intelligence+","+
				attack+","+defense+","+parry+","+mining+","+mineSkillBar+","+mineRaceMod+","+farming+","+farmSkillBar+","+farmRaceMod+"," +
				blacksmithing+","+blacksmithSkillBar+","+blacksmithRaceMod+","+enchanting+","+enchantSkillBar+"," +
				enchantRaceMod+","+alchemy+","+alchemySkillBar+","+alchemyRaceMod+","+cooking+","+cookSkillBar+","+cookRaceMod+"," +
				fishing+","+fishSkillBar+","+fishRaceMod+","+trading+","+tradeSkillBar+","+tradeRaceMod+","+alcoholTolerance+","+Copper+")"; 
		
		SQLManager.SQLUpdate(query);
		
		query = "SELECT char_id FROM Characters WHERE account_id = "+this.AccountID;
		ResultSet rs = SQLManager.SQLQuery(query);
		
		try { rs.next();
			  CharacterID = rs.getInt("char_id");
			  rs.close();
		} catch (SQLException e) { e.printStackTrace();	}
		
		return CharacterID;
	} // public boolean savePlayerData()
	
} // public class RPG_Character