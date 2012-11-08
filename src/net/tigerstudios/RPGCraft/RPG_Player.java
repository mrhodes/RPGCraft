package net.tigerstudios.RPGCraft;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.tigerstudios.RPGCraft.utils.SQLManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;


/* ----------------------------------------------------------------------------
 * RPG_Player
 * 
 * The RPG_Player class will be the main class for each in game player. 
 */
public class RPG_Player{	
	private int AccountID = 0;			// unique account ID.  Value stored in Database
		
	private String mcName = null;					// Players Minecraft Name
	private Player player = null;					// Minecraft Player class
	private RPG_Character rpgCharacter = null;		// This players rpg character	
	private boolean bCharLoaded = false;
	
	// TODO: Add universal MT Timer/Callback system.  Pull timer out of Player class
	long lTimer = -1;		// Used for things that need a timer.  This
							// qValue will be the end time of a time
							// length		
		
	public String getMCName() { return mcName; }		
	public void setMCName(String name) { mcName = name; }					
	
	public RPG_Player(String minecraftName, int id)
	{		
		mcName = minecraftName;
		AccountID = id;
		player = Bukkit.getPlayer(minecraftName);
		if(loadCharacterData())
			bCharLoaded = true;		
	} // public void RPG_Player(Player p)
	
		
	public SpoutPlayer getSpoutPlayer() { return SpoutManager.getPlayer(this.player);} // public SpoutPlayer getSpoutPlayer() 
	
	public RPG_Character getCharacter() { if(bCharLoaded) return rpgCharacter; return null;}
	public void setCharacter(RPG_Character character) { rpgCharacter = character; bCharLoaded = true; } 
	public Player getPlayer() { return player; }
	public void setPlayer(Player p) { player = p; }
			
	public int getAccountID() { return AccountID; }
	public long getTimer() { return lTimer; }
	public void setTimer(long tMs) { lTimer = tMs; }
		
	public void saveCharacterData()
	{
		// Make sure play has a character to save
		if(rpgCharacter != null)
			rpgCharacter.saveCharacter();		
	} // public void saveCharacterData()
	public boolean loadCharacterData()
	{
		String query = "SELECT * FROM Characters WHERE account_id = "+this.AccountID+";";
		ResultSet rs = SQLManager.SQLQuery(query);
			
		try {
			if(rs.next())
			{
				RPG_Character character = new RPG_Character();				
				
				character.EntID			= player.getEntityId();
				character.AccountID 	= AccountID;
				character.CharacterID	= rs.getInt("char_id");
				character.setName(rs.getString("name"));
				character.race			= rs.getString("race");
				character.displayPrefix	= rs.getString("namePrefix");
				character.setDisplaySuffix(rs.getString("nameSuffix"));
				character.setLevel(rs.getInt("level"));
				character.experience	= rs.getInt("experience");
				character.setStrength(rs.getInt("strength"));
				character.setDexterity(rs.getInt("dexterity"));
				character.setConstitution(rs.getInt("constitution"));
				character.setIntelligence(rs.getInt("intelligence"));
				character.statPtsUsed	= rs.getInt("statPointsUsed");
				character.statPtsTotal	= rs.getInt("statPointsTotal");
				character.mining		= rs.getInt("mine");
				character.mineSkillBar	= rs.getFloat("mineSkillBar");
				character.farming		= rs.getInt("farm");
				character.farmSkillBar	= rs.getFloat("farmSkillBar");
				character.blacksmithing	= rs.getInt("blacksmith");
				character.enchanting	= rs.getInt("enchant");
				character.alchemy		= rs.getInt("alchemy");
				character.cooking		= rs.getInt("cook");
				character.fishing		= rs.getInt("fish");
				character.trading		= rs.getInt("trade");
				character.alcoholTolerance = rs.getInt("alcoholTolerance");
				character.setSpeed(RaceSystem.getRace(character.race).speed);
				rs.close();
									
				character.updateExpBar();				
				setCharacter(character);				
				
				return true;
			} // if(rs.next())
		} catch (SQLException e) { e.printStackTrace(); }		
		
		return false;
	} // public void loadCharacterData()	
	
} // public class RPG_Player
