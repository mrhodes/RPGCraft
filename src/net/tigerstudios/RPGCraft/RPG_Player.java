package net.tigerstudios.RPGCraft;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
	// Map of this players rpg Characters.
	private Map<Integer, RPG_Character> rpgCharacters = new HashMap<Integer, RPG_Character>();
	
	String mcName;				// Players Minecraft Name
	int AccountID = 0;			// unique account ID.  Value stored in Database
	int CurrentCharID = 0;		// Currently loaded character
	int CharacterTotal;
	boolean bIsOnline;			// Is this player currently online
	Player player;				// Minecraft Player class
	SpoutPlayer SptPlayer;		// Spout Player object.	
    	
	// TODO: Add universal MT Timer/Callback system.  Pull timer out of Player class
	long lTimer = -1;		// Used for things that need a timer.  This
							// Value will be the end time of a time
							// length		
		
	public String getMCName() { return mcName; }		
	public void setMCName(String name) { mcName = name; }					
	public RPG_Player(String minecraftName, int id, int charTotal)
	{		
		mcName = minecraftName;
		AccountID = id;
		CurrentCharID = 0;
		CharacterTotal = charTotal;
	} // public void RPG_Player(Player p)	
	public Player GetPlayer() { return player; }
	public void setPlayer(Player p) { player = p; if(p!=null) SptPlayer = SpoutManager.getPlayer(p); }
	public SpoutPlayer GetSpoutPlayer() { return SptPlayer; }
	public RPG_Character getCharacter() { return rpgCharacters.get(CurrentCharID); } 
	
	// This method will be called when a new player registers to be a RPG character.
	public void initialize()
	{	
		player = RPGCraft.getBukkitServer().getPlayer(mcName);
		SptPlayer = SpoutManager.getPlayer(player);

	} // public void initialize()
	
	public void saveCharacterData()
	{
		Collection<RPG_Character> characters = rpgCharacters.values();
		for(RPG_Character c: characters)
			c.saveCharacter();	
		
	} // public void saveCharacterData()
	
	
	public void loadCharacterData()
	{
		String query = "SELECT * FROM Characters WHERE account_id = "+this.AccountID+";";
		ResultSet rs = SQLiteManager.SQLQuery(query);
		
		// Loop Through all the results and load each character.
		try {
			while(rs.next())
			{	RPG_Character character = new RPG_Character();				
				character.initialize();
				
				character.AccountID		= rs.getInt("account_id");
				character.Name     		= rs.getString("name");
				character.displayPrefix	= rs.getString("namePrefix");
				character.displaySuffix = rs.getString("nameSuffix");
				character.race			= rs.getInt("race");
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
				character.Copper		= rs.getInt("copper");
				character.skinURL		= rs.getString("skinURL");
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} // public void loadCharacterData()
		
}
