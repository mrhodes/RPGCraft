package net.tigerstudios.RPGCraft.skills;

import org.bukkit.block.Block;

import net.tigerstudios.RPGCraft.RPGCraft;
import net.tigerstudios.RPGCraft.RPG_Character;


public class EnchantingSystem{
			
	public EnchantingSystem(){	} // public EnchantingSystem()
	
	
	/* --------------------------------------------------------------
	 * activateTable()
	 */
	public static void activateTable(RPG_Character rpgChar, Block bTable)
	{
		// For now tell the player that Enchanting Tables are disabled.
		rpgChar.sendMessage(RPGCraft.divider);
		rpgChar.sendMessage(" Sorry, but at this time Enchanting is disabled.");
		rpgChar.sendMessage(RPGCraft.divider);			
		
	} // public void activateTable(RPG_Character rpgChar, Block bTable)
	
} // public class EnchantingSystem implements Listener
