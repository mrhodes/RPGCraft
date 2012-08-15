package net.tigerstudios.RPGCraft;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.tigerstudios.RPGCraft.SpoutFeatures.SpoutFeatures;
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
public class RPG_Player {	
	private int AccountID = 0;			// unique account ID.  Value stored in Database
		
	private String mcName = null;					// Players Minecraft Name
	private Player player = null;					// Minecraft Player class
	private SpoutPlayer SptPlayer = null;			// Spout Player object.	
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
		SptPlayer = SpoutManager.getPlayer(player);
		if(loadCharacterData())
		{
			SptPlayer.setWalkingMultiplier(rpgCharacter.fWalkSpeed);
			bCharLoaded = true;
		}
	} // public void RPG_Player(Player p)
	
		
	public SpoutPlayer getSpoutPlayer() 
	{	if(SptPlayer == null)
			SptPlayer = SpoutManager.getPlayer(this.player);
		
		return SptPlayer; 
	} // public SpoutPlayer getSpoutPlayer() 
	
	public RPG_Character getCharacter() { if(bCharLoaded) return rpgCharacter; return null;}
	public void setCharacter(RPG_Character character) { rpgCharacter = character; bCharLoaded = true; } 
	public Player getPlayer() { return player; }
	public void setPlayer(Player p) { player = p; if(p!=null) SptPlayer = SpoutManager.getPlayer(p); }
			
	public int getAccountID() { return AccountID; }
	public long getTimer() { return lTimer; }
	public void setTimer(long tMs) { lTimer = tMs; }
	
	public void resetCharacter()
	{		
		rpgCharacter = null;
		bCharLoaded = false;
		SpoutFeatures.setWalkingSpeed(SptPlayer, 1.0f);
		SpoutFeatures.updateTitle(player, null);
		return;
	}
	
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
									
				character.AccountID		= rs.getInt("account_id");
				character.CharacterID	= rs.getInt("char_id");
				character.Name     		= rs.getString("name");
				character.displayPrefix	= rs.getString("namePrefix");
				character.setDisplaySuffix(rs.getString("nameSuffix"));
				character.race			= rs.getString("race");
				character.level			= rs.getInt("level");
				character.experience	= rs.getInt("experience");
				character.exp_to_level	= rs.getInt("exp_to_levelup");
				character.strength		= rs.getInt("strength");
				character.dexterity		= rs.getInt("dexterity");
				character.constitution	= rs.getInt("constitution");
				character.intelligence	= rs.getInt("intelligence");
				character.statPtsUsed	= rs.getInt("statPointsUsed");
				character.statPtsTotal	= rs.getInt("statPointsTotal");
				character.mining		= rs.getInt("mine");
				character.mineSkillBar	= rs.getFloat("mineSkillBar");
				character.mineRaceMod	= rs.getFloat("mineRaceMod");
				character.farming		= rs.getInt("farm");
				character.farmSkillBar	= rs.getFloat("farmSkillBar");
				character.farmRaceMod	= rs.getFloat("farmRaceMod");
				character.blacksmithing	= rs.getInt("blacksmith");
				character.enchanting	= rs.getInt("enchant");
				character.alchemy		= rs.getInt("alchemy");
				character.cooking		= rs.getInt("cook");
				character.fishing		= rs.getInt("fish");
				character.trading		= rs.getInt("trade");
				character.alcoholTolerance = rs.getInt("alcoholTolerance");
				character.Copper		= rs.getInt("copper");				
				rs.close();
				
				character.fWalkSpeed = RaceSystem.getRace(character.race).speed;
				character.levelExpTotal = (float)(character.level * 100) * 1.25f;
				
				if(player != null)
				{
					player.setLevel(character.level);
					player.setExp( (character.levelExpTotal - character.exp_to_level) / character.levelExpTotal ); 
				}
				character.optimizeCoin();
				setCharacter(character);				
				
				return true;
			} // if(rs.next())
		} catch (SQLException e) { e.printStackTrace(); }		
		
		return false;
	} // public void loadCharacterData()	
	
} // public class RPG_Player
