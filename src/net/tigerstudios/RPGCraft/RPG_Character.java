package net.tigerstudios.RPGCraft;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.tigerstudios.RPGCraft.CombatSystem.RPG_Entity;
import net.tigerstudios.RPGCraft.utils.SQLManager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

// The RPG Character class represents an ingame character and what events
// happen to the character.
public class RPG_Character extends RPG_Entity
{
	private static int[] expTable;	
	
	int CharacterID = 0;		// ID for this particular rpg character
	int AccountID = 0;			// Account of player that owns this character
	
	String Name;				// Players Role Play name
	String displayPrefix;	
	String displaySuffix;
	
	// Race Stats
	public String race;		
	public float experience;		// Players total experience
		
	//public ItemStack itemInHand;
		
	// Character Stats
	int statPtsUsed, statPtsTotal;	
	
	int alcoholTolerance;	
	int drunkenLevel;
			
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
	public float fSwimSpeed;
	public float fJumpMult, fGravityMult;
	
	// debuging info.
	float totalLootBonus, totalHarvestBonus, totalSkillIncrease;
	int loot, harvest, skill, count;
	float avgLootBonus, avgHarvestBonus, avgSkillIncrease;
	int totalSeeds, totalWheat;
	
	public RPG_Character(Race r, int acc_id)
	{
		if(r == null || acc_id == 0)
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
		EntID = mgr_Player.getMCPlayer(acc_id).getEntityId();
		setSpeed(r.speed);		
		
		farmRaceMod			= r.farming;
		mineRaceMod			= r.mining;
		blacksmithRaceMod 	= r.blacksmithing;
		enchantRaceMod		= r.enchanting;
		
		saveCharacter();
	} // public RPG_Character(Race r)
	
	@Override
	public void setSpeed(float spd)
	{
		if(spd != 0.0)
			this.speed = spd;
		SpoutManager.getPlayerFromId(EntID).setWalkingMultiplier(this.speed);
	}
	public int getAccountID() { return AccountID; }
	public String getName() { return Name; }
	public void setName(String name) { Name = name;  }
	public void addExperience(float exp, SpoutPlayer p)
	{				
		experience += exp;
		
		if(experience >= expTable[level])
		{	// Leveled up!
			level += 1;
			p.setLevel(level);		
			
			SpoutManager.getSoundManager().playCustomSoundEffect(RPGCraft.getPlugin(), p, "http://tigerstudios.net/minecraft/sounds/levelup.ogg", false);
			p.sendNotification("Level Up!", "You are now level "+level, Material.DIAMOND);
			
			Bukkit.broadcastMessage("    [§2RPG§f] "+p.getName()+" just leveled up! Now "+p.getName()+" is a level "
					+level+" "+race+".");
		} // if(exp_to_level <= 0)		
		updateExpBar();			
	} // public void addExperience(float exp, SpoutPlayer p)
	public void updateExpBar()
	{
		Player p = mgr_Player.getMCPlayer(AccountID);
		if(p == null)
			return;
		p.setLevel(level);
		p.setTotalExperience((int)experience);	
		p.setExp((experience - expTable[level-1]) / (expTable[level] - expTable[level-1]));				
	} // public void updateExpBar()
	public String getDisplaySuffix() {return displaySuffix;}
	public void setDisplaySuffix(String displaySuffix) { this.displaySuffix = displaySuffix; }
	public void initialize(int acc_id)
	{	
		Name = "Commoner";
		AccountID = acc_id;
		displayPrefix = "";	
		setDisplaySuffix("");	
				
		race = "unknown"; level = 1; experience = 0; 
		dexterity = 5; constitution = 5; strength = 5; intelligence = 5;
		attack = 1; defense = 1; parry = 1;
		
		// Character Ablities
		mining = 1; farming = 1; blacksmithing = 1;
		enchanting = 1; alchemy = 1; cooking = 1;
		fishing = 1; trading = 1;
		
		updateExpBar();		
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
					"alcoholTolerance       = "+alcoholTolerance+
					" where char_id="+CharacterID+";";
			SQLManager.SQLUpdate(query);
			return CharacterID;
		} // if(CharacterID != 0)	
		
		query = "insert into characters (account_id, name, namePrefix, nameSuffix, race, level,"+
				"experience, statPointsUsed, statPointsTotal, strength, dexterity, " +
				"constitution, intelligence, attack, defense, parry, mine, mineSkillBar, mineRaceMod, " +
				"farm, farmSkillBar, farmRaceMod, blacksmith, blacksmithSkillBar, blacksmithRaceMod, " +
				"enchant, enchantSkillBar, enchantRaceMod, alchemy, alchemySkillBar, alchemyRaceMod, " +
				"cook, cookSkillBar, cookRaceMod, fish, fishSkillBar, fishRaceMod, trade, tradeSkillBar, tradeRaceMod, " +
				"alcoholTolerance) " +
				"VALUES ("+AccountID+", '"+Name+"', '"+displayPrefix+"', '"+getDisplaySuffix()+"', '"+race+"', "+level+","+
				experience+","+statPtsUsed+","+statPtsTotal+","+strength+","+dexterity+","+constitution+","+intelligence+","+
				attack+","+defense+","+parry+","+mining+","+mineSkillBar+","+mineRaceMod+","+farming+","+farmSkillBar+","+farmRaceMod+"," +
				blacksmithing+","+blacksmithSkillBar+","+blacksmithRaceMod+","+enchanting+","+enchantSkillBar+"," +
				enchantRaceMod+","+alchemy+","+alchemySkillBar+","+alchemyRaceMod+","+cooking+","+cookSkillBar+","+cookRaceMod+"," +
				fishing+","+fishSkillBar+","+fishRaceMod+","+trading+","+tradeSkillBar+","+tradeRaceMod+","+alcoholTolerance+")"; 
		
		SQLManager.SQLUpdate(query);
		
		query = "SELECT char_id FROM Characters WHERE account_id = "+this.AccountID;
		ResultSet rs = SQLManager.SQLQuery(query);
		
		try { rs.next();
			  CharacterID = rs.getInt("char_id");
			  rs.close();
		} catch (SQLException e) { e.printStackTrace();	}
		
		return CharacterID;
	} // public boolean savePlayerData()
	public static void initialize()
	{
		expTable = new int[51];
		expTable[0]=0;
		for(int i=1; i <= 50; i++)
			expTable[i] = (int)Math.pow((2 * i), 2) * 50;		
	}
	
} // public class RPG_Character