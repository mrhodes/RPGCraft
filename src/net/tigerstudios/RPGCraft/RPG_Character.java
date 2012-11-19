package net.tigerstudios.RPGCraft;

import java.sql.ResultSet;
import java.sql.SQLException;


import net.tigerstudios.RPGCraft.CombatSystem.RPG_Entity;
import net.tigerstudios.RPGCraft.SpoutFeatures.SpoutFeatures;
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
	String displayPrefix;		// Choosen Title
	String displaySuffix;		// Suffix
	
	// Race Stats
	public String race;			// Race of this character
	public float experience;		// Players total experience
		
	//public ItemStack itemInHand;
		
	// Character Stats
	int statPtsUsed, statPtsTotal;	
	
	int alcoholTolerance;	int drunkenLevel;
			
	// Character Ablities
	public int mining;
	public int farming;
	int blacksmithing;
	int enchanting, alchemy, cooking;
	int fishing, trading;
	
	public float mineSkillBar;
	public float farmSkillBar;
	float blacksmithSkillBar;
	float enchantSkillBar, alchemySkillBar, cookSkillBar;
	float fishSkillBar, tradeSkillBar;
	
	// Modifier values
	public float fSwimSpeed;
	public float fJumpMult, fGravityMult;
	
	// debuging info.
	float totalLootBonus, totalHarvestBonus, totalSkillIncrease;
	int loot, harvest, skill, count;
	float avgLootBonus, avgHarvestBonus, avgSkillIncrease;
	int totalSeeds, totalWheat;
	
	
	// This Method only gets called when a New character is created.
	// Otherwise, if the character already exists then the empty constructor is called
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
		level				= 1;
		setEntityID(mgr_Player.getMCPlayer(acc_id).getEntityId());
		setSpeed(r.speed);		
				
		saveCharacter();
	} // public RPG_Character(Race r)
	
	public RPG_Character() {}

	@Override 
	public void setSpeed(float spd)
	{
		if(SpoutFeatures.isEnabled() != true) return;
		
		if(spd > 0)
			speed = spd;
		
		SpoutManager.getPlayer(mgr_Player.getMCPlayer(AccountID)).setWalkingMultiplier(speed);
	}
	public int getAccountID() { return AccountID; }
	public String getName() { return Name; }
	public void setName(String name) { Name = name;  }
	public void addExperience(float exp, Player p)
	{				
		experience += exp;
		
		if(experience >= expTable[level])
		{	// Leveled up!
			level += 1;
			p.setLevel(level);		
			
			if(SpoutFeatures.isEnabled())
			{
				SpoutManager.getSoundManager().playCustomSoundEffect(RPGCraft.getPlugin(), (SpoutPlayer)p, "http://tigerstudios.net/minecraft/sounds/levelup.ogg", false);
				((SpoutPlayer) p).sendNotification("Level Up!", "You are now level "+level, Material.DIAMOND);
			}
			Bukkit.broadcastMessage("    [§2RPG§f] "+p.getName()+" just leveled up! Now "+p.getName()+" is a level "
					+level+" "+race+".");
			p.sendMessage(" You now have an extra point to increase one of your primary stats!");
			p.sendMessage(" See your stats page for more info.");
			this.statPtsTotal++;
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
		dexterity = 3; constitution = 3; strength = 3; intelligence = 3;
		attack = 1; defense = 1; parry = 1;
		
		// Character Ablities
		mining = 1; farming = 1; blacksmithing = 1;
		enchanting = 1; alchemy = 1; cooking = 1;
		fishing = 1; trading = 1;
		
		// Set to a default speed.  This will be updated when a race is loaded
		speed = 1.0f;
		
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
					"farm					= "+farming+","+
					"farmSkillBar			= "+farmSkillBar+","+
					"blacksmith				= "+blacksmithing+","+
					"blacksmithSkillBar		= "+blacksmithSkillBar+","+
					"enchant				= "+enchanting+","+					
					"enchantSkillBar		= "+enchantSkillBar+","+
					"alchemy				= "+alchemy+","+
					"alchemySkillBar		= "+alchemySkillBar+","+
					"cook					= "+cooking+","+
					"cookSkillBar			= "+cookSkillBar+","+
					"fish					= "+fishing+","+
					"fishSkillBar			= "+fishSkillBar+","+
					"trade                	= "+trading+","+
					"tradeSkillBar			= "+tradeSkillBar+","+
					"alcoholTolerance       = "+alcoholTolerance+
					" where char_id="+CharacterID+";";
			SQLManager.SQLUpdate(query);
			return CharacterID;
		} // if(CharacterID != 0)	
		
		query = "insert into characters (account_id, name, namePrefix, nameSuffix, race, level,"+
				"experience, statPointsUsed, statPointsTotal, strength, dexterity, " +
				"constitution, intelligence, attack, defense, parry, mine, mineSkillBar, " +
				"farm, farmSkillBar, blacksmith, blacksmithSkillBar, enchant, enchantSkillBar, alchemy, "+
				"alchemySkillBar, cook, cookSkillBar, fish, fishSkillBar, trade, tradeSkillBar, alcoholTolerance) "+
				"VALUES ("+AccountID+", '"+Name+"', '"+displayPrefix+"', '"+getDisplaySuffix()+"', '"+race+"', "+level+","+
				experience+","+statPtsUsed+","+statPtsTotal+","+strength+","+dexterity+","+constitution+","+intelligence+","+
				attack+","+defense+","+parry+","+mining+","+mineSkillBar+","+farming+","+farmSkillBar+","+blacksmithing+","+
				blacksmithSkillBar+","+enchanting+","+enchantSkillBar+","+alchemy+","+alchemySkillBar+","+cooking+","+
				cookSkillBar+","+fishing+","+fishSkillBar+","+trading+","+tradeSkillBar+","+alcoholTolerance+")"; 
		
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