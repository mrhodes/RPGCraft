package net.tigerstudios.RPGCraft;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.tigerstudios.RPGCraft.utils.SQLiteManager;

import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;


/* ----------------------------------------------------------------------------
 * RPG_Player
 * 
 * The RPG_Player class will be the main class for each in game player. 
 */
public class RPG_Player {	
	
	int AccountID = 0;			// unique account ID.  Value stored in Database
	boolean bIsOnline;			// Is this player currently online
	
	private String mcName = null;					// Players Minecraft Name
	private Player player = null;					// Minecraft Player class
	private SpoutPlayer SptPlayer = null;			// Spout Player object.	
	private RPG_Character rpgCharacter = null;		// This players rpg character
	
	
	// TODO: Add universal MT Timer/Callback system.  Pull timer out of Player class
	long lTimer = -1;		// Used for things that need a timer.  This
							// Value will be the end time of a time
							// length		
		
	public String getMCName() { return mcName; }		
	public void setMCName(String name) { mcName = name; }					
	
	public RPG_Player(String minecraftName, int id)
	{		
		mcName = minecraftName;
		AccountID = id;
	} // public void RPG_Player(Player p)
	
		
	public SpoutPlayer getSpoutPlayer() 
	{	if(SptPlayer == null)
			SptPlayer = SpoutManager.getPlayer(this.player);
		
		return SptPlayer; 
	} // public SpoutPlayer getSpoutPlayer() 
	
	public RPG_Character getCharacter() { return rpgCharacter; }
	public void setCharacter(RPG_Character character) { rpgCharacter = character; } 
	public Player getPlayer() { return player; }
	public void setPlayer(Player p) { player = p; if(p!=null) SptPlayer = SpoutManager.getPlayer(p); }
		
	
	// This method will be called when a new player registers to be a RPG character.
	public void initialize(String name)
	{	
		mcName = name;
		player = RPGCraft.getBukkitServer().getPlayer(name);
		SptPlayer = SpoutManager.getPlayer(player);
	} // public void initialize()
	
	public void saveCharacterData()
	{
		rpgCharacter.saveCharacter();		
	} // public void saveCharacterData()
	
	
	public void loadCharacterData()
	{
		String query = "SELECT * FROM Characters WHERE account_id = "+this.AccountID+";";
		ResultSet rs = SQLiteManager.SQLQuery(query);
		
		try {
			if(rs.next())
			{
				RPG_Character character = new RPG_Character();				
								
				character.AccountID		= rs.getInt("account_id");
				character.CharacterID	= rs.getInt("char_id");
				character.Name     		= rs.getString("name");
				character.displayPrefix	= rs.getString("namePrefix");
				character.displaySuffix = rs.getString("nameSuffix");
				character.race			= rs.getString("race");
				character.level			= rs.getInt("level");
				character.experience	= rs.getInt("experience");
				character.exp_to_level	= rs.getInt("exp_to_levelup");
				character.strength		= rs.getInt("strength");
				character.dexterity		= rs.getInt("dexterity");
				character.constitution	= rs.getInt("constitution");
				character.intelligence	= rs.getInt("intelligence");
				character.mining		= rs.getInt("mining");
				character.farming		= rs.getInt("farming");
				character.blacksmithing	= rs.getInt("blacksmithing");
				character.enchanting	= rs.getInt("enchanting");
				character.alchemy		= rs.getInt("alchemy");
				character.cooking		= rs.getInt("cooking");
				character.fishing		= rs.getInt("fishing");
				character.trading		= rs.getInt("trading");
				character.Copper		= rs.getInt("copper");				
			
				setCharacter(character);
				rs.close();
			} // if(rs.next())
		} catch (SQLException e) { e.printStackTrace(); }
		
	} // public void loadCharacterData()		
} // public class RPG_Player
