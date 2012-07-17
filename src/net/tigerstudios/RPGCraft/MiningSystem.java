package net.tigerstudios.RPGCraft;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

public class MiningSystem {
	private static Random randomizer = new Random();
	
	// TODO: Save this value to the database when server shuts down
	// ad load from database when server starts
	public static Map<Location,Integer> placedBlocks = new HashMap<Location,Integer>();
	
	public static boolean mine(Block bTarget, Player pMiner, ItemStack iPickaxe)
	{
		SpoutPlayer sp = SpoutManager.getPlayer(pMiner);		
		RPG_Character rpgChar = mgr_Player.getCharacter(pMiner);
		boolean bNoExp = false;
		boolean bNoDrop = false;
		
		if(rpgChar == null)
		{	// Player cannot harvest without chosing a race first
			sp.sendNotification("Mining", "Must choose a Race!", bTarget.getType());
			pMiner.sendMessage("[§2RPG§f] You cannot mine until you have first choosen a race.");
			pMiner.sendMessage("[§2RPG§f] Please type /rpg for more information.");
			return true;
		} // if(rpgChar == null)		
				
		int mineLevel = rpgChar.mining;
		float mineMod = rpgChar.mineRaceMod;	
		
		float mineBonus, lootBonus;
		int toolValue = 0;
		int maxUses = 0;
		
		// Same as in farming... get the toolvalue
		// ToolValue is the level this tool can be used at
		Material matPick = iPickaxe.getType();
		if(matPick == Material.WOOD_PICKAXE) { toolValue = 1; maxUses = 60;}
		if(matPick == Material.STONE_PICKAXE) {toolValue = 5; maxUses = 132;}
		if(matPick == Material.IRON_PICKAXE) {toolValue = 15; maxUses = 251;}
		if(matPick == Material.GOLD_PICKAXE) {toolValue = 25; maxUses = 33;}
		if(matPick == Material.DIAMOND_PICKAXE) {toolValue = 25; maxUses = 1562;}
		
		// Make sure the player can use this tool
		if((toolValue > 5) && (!rpgChar.race.equalsIgnoreCase("dwarf") && !rpgChar.race.equalsIgnoreCase("human")))
		{	pMiner.sendMessage("[§2RPG§f] You will not gain any more skill from this pickaxe.");
			bNoExp = true;
		}
		if((toolValue > 15) && rpgChar.race.equalsIgnoreCase("human"))
		{	pMiner.sendMessage("[§2RPG§f] You cannot use this pickaxe as a "+rpgChar.race);
			bNoExp = true;
			return false;
		}
		
		// Check that the player is using the right tool for his level.
		if(mineLevel < toolValue)
		{	pMiner.sendMessage("[§2RPG§f] You are not ready for this tool.  Try something more your level.");
			pMiner.sendMessage("You need to have at least "+toolValue+" skill in mining for");
			pMiner.sendMessage("a "+matPick.name());
			return true;
		} // if((mineLevel+10) < toolValue)
		
		if((mineLevel > (toolValue + 10)))
		{	pMiner.sendMessage("[§2RPG§f] You have out grown the use of this tool.");
			pMiner.sendMessage("You can still use it but you will not receive exp.");
			bNoExp = true;
		}
				
		Material matBlock = bTarget.getType();
		Material matDrop = Material.COBBLESTONE;
		int blockValue = 1;
		int lootMax = 1;
		
		//pMiner.sendMessage(mineBonus);
		// blockValue is the level the block can be mined at
		switch(matBlock)
		{
		case STONE:
			matDrop = Material.COBBLESTONE;	blockValue = 1;	lootMax = 3;	break;			
		case COBBLESTONE:
			matDrop = Material.COBBLESTONE;	blockValue = 1;	lootMax = 1;	break;			
		case COAL_ORE:
			matDrop = Material.COAL;		blockValue = 1;	lootMax = 2;	break;
	
		case IRON_ORE:
			matDrop = Material.IRON_ORE;	blockValue = 10;lootMax = 3;	break;
		case NETHERRACK:
			matDrop = Material.NETHERRACK;	blockValue = 5;	lootMax = 3;	break;
			
		case GOLD_ORE:
			matDrop = Material.GOLD_ORE;	blockValue = 20;lootMax = 3;	break;			
		case MOSSY_COBBLESTONE:
			matDrop = Material.MOSSY_COBBLESTONE;	blockValue = 10;lootMax = 4;	break;		
			
		case REDSTONE_ORE:
			matDrop = Material.REDSTONE;	blockValue = 20;lootMax = 8;	break;
		case LAPIS_ORE:
			matDrop = Material.INK_SACK;	blockValue = 25;lootMax = 8;	break;		
		case NETHER_BRICK:
			matDrop = Material.NETHER_BRICK;blockValue = 25;lootMax = 4;	break;
			
		case DIAMOND_ORE:
			matDrop = Material.DIAMOND;		blockValue = 25;lootMax = 4;	break;	
		case OBSIDIAN:
			matDrop = Material.OBSIDIAN;	blockValue = 35;lootMax = 3;	break;
			
			default:
				blockValue = 0;							
		} // switch(matBlock)
		
		// Make sure player can mine this block
		if(mineLevel < blockValue)
		{
			pMiner.sendMessage("[§2RPG§f] You are not able to mine "+bTarget.getType().name());
			pMiner.sendMessage("[§2RPG§f] at your level.  You need at least "+blockValue+" in mining.");
			return true;
		}// if(mineLevel < blockValue)
		
		// Figure out how much loot should drop
		int lootDrop = (int) (randomizer.nextInt(lootMax + 1));	
		lootBonus = (float)lootDrop / lootMax;		
		mineBonus = ((float)toolValue / (float)mineLevel) * mineMod;
		
		// Prepare and drop loot
		ItemStack loot = new ItemStack(matDrop, 1);
		if(matDrop.equals(Material.INK_SACK))
		{
			loot.setDurability((short) 4);
		}
		
		Location loc = bTarget.getLocation();
		if(!placedBlocks.isEmpty())
		{
			if(placedBlocks.containsKey(loc))
			{	// ok, this player has placed blocks.... check for duping
				// exploit
				bTarget.getWorld().dropItemNaturally(loc, loot);
				bNoExp = false;		// Set no exp flag
				lootBonus = 0;
				pMiner.sendMessage("[§2RPG§f] You can't gain experience with placed blocks.");
				placedBlocks.remove(loc);
			}// if(placedBlocks.containsKey(loc))
		}
		
		if(lootBonus > 0)
		{
			for(int i = 0; i < lootDrop; i++)
				bTarget.getWorld().dropItemNaturally(loc, loot);
		}
		// Take care of experience
		float skillIncrease = 0;
		if(!bNoExp)
		{	skillIncrease = ((mineBonus * 10) / mineLevel)  * lootBonus;
			if(bTarget.getType().equals(Material.NETHERRACK))
				skillIncrease = skillIncrease / 2;
		}else
			skillIncrease = 0;
				
		
		// Clean up
		bTarget.setTypeId(0);
		iPickaxe.setDurability((short) (iPickaxe.getDurability() + 1));
		if(iPickaxe.getDurability() >= maxUses)
			pMiner.setItemInHand(null);
		
		rpgChar.mineSkillBar += skillIncrease;
		
		pMiner.setExp(rpgChar.mineSkillBar / 75);
		pMiner.setLevel(mineLevel);
				
		if(rpgChar.mineSkillBar >= 75)
		{	// let's increase the skill.
			rpgChar.mining += 1;
			rpgChar.mineSkillBar = 0;
			
			sp.sendNotification("Mining "+rpgChar.mining, "Mining skill increased!", Material.DIAMOND_PICKAXE);
			pMiner.sendMessage("[§2RPG§f] Mining is now: "+rpgChar.mining);	
		} // if(rpgChar.mineSkillBar >= 75)	
		
		return true; 
	} // public static boolean mine(Block bTarget, Player pMiner, ItemStack iPickaxe)
	
	
	public MiningSystem(Plugin p)
	{
		randomizer.setSeed(System.nanoTime());
		placedBlocks.clear();	
		//Bukkit.getServer().getPluginManager().registerEvents(this, p);
	} // public MiningSystem(Plugin p)
	
} // public class MiningSystem
