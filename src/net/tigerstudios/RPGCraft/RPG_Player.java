package net.tigerstudios.RPGCraft;

import java.util.HashMap;
import java.util.Map;

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
		player = RPGCraft.getBukkitServer().getPlayer(minecraftName);
		SptPlayer = SpoutManager.getPlayer(player);
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
		

	} // public void initialize()
	
	public void saveCharacterData()
	{
		
	}
	
	public void loadCharacterData()
	{
		
	}
		
}
