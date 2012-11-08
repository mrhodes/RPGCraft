package net.tigerstudios.RPGCraft.skills;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.tigerstudios.RPGCraft.RPGCraft;
import net.tigerstudios.RPGCraft.RPG_Character;
import net.tigerstudios.RPGCraft.RaceSystem;
import net.tigerstudios.RPGCraft.mgr_Player;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

public class MiningSystem {
	private static Random randomizer = new Random();
	
	public static Map<Location,Integer> placedBlocks = new HashMap<Location,Integer>();
	
	public static boolean mine(Block bTarget, Player pMiner, ItemStack iPickaxe)
	{
		//SpoutPlayer sp = SpoutManager.getPlayer(pMiner);		
		RPG_Character rpgChar = mgr_Player.getCharacter(pMiner);
		boolean bNoExp = false;
				
		if(rpgChar == null)
		{	// Player cannot harvest without chosing a race first
			//sp.sendNotification("Mining", "Must choose a Race!", bTarget.getType());
			pMiner.sendMessage("[§2RPG§f] You cannot mine until you have first choosen a race.");
			pMiner.sendMessage("[§2RPG§f] Please type /rpg for more information.");
			return true;
		} // if(rpgChar == null)		
		
		int mineLevel = rpgChar.mining;
		float mineMod = RaceSystem.getModifier(rpgChar.race, "mine");
				
		float mineBonus, lootBonus;
		float toolMod = 0.0F;
		int toolValue = 0;
		int maxUses = 0;
		
		// Same as in farming... get the toolvalue
		// ToolValue is the level this tool can be used at
		Material matPick = iPickaxe.getType();
		if(matPick == Material.WOOD_PICKAXE) { toolValue = 1; toolMod = 0.75f; maxUses = 60;}
		if(matPick == Material.STONE_PICKAXE) {toolValue = 10; toolMod = 1.0f;maxUses = 132;}
		if(matPick == Material.IRON_PICKAXE) {toolValue = 20; toolMod = 1.2f;maxUses = 251;}
		if(matPick == Material.GOLD_PICKAXE) {toolValue = 50; toolMod = 2.0f;maxUses = 33;}
		if(matPick == Material.DIAMOND_PICKAXE) {toolValue = 35; toolMod = 1.5f;maxUses = 1562;}		
		
	// TODO: Make this check based on skill and not race directly
		// Make sure the player can use this tool
		if((toolValue > 1) && (!rpgChar.race.equalsIgnoreCase("dwarf") && !rpgChar.race.equalsIgnoreCase("human")))
		{	pMiner.sendMessage("[§2RPG§f] Only Dwarves and Humans can use tools better then wood.");
			return false;
		}
		
		// Prevent Humans from using tools above Iron
		if((toolValue > 20) && rpgChar.race.equalsIgnoreCase("human"))
		{
			pMiner.sendMessage("[§2RPG§f] Iron pickaxes are the highest level you can use as a Human.");
			return false;			
		}
			
		// Check that the player is using the right tool for his level.
		if(mineLevel < toolValue)
		{	pMiner.sendMessage("[§2RPG§f] You are not ready for this tool. \n[§2RPG§f] Try something more your level.");
			pMiner.sendMessage("You need to have at least "+toolValue+" skill in mining for");
			pMiner.sendMessage("a "+matPick.name());
			return false;
		} // if((mineLevel+10) < toolValue)		
		
		Material matBlock = bTarget.getType();
		Material matDrop = Material.COBBLESTONE;
		int blockValue = 1;
		int lootMax = 1;
		
		// blockValue is the level the block can be mined at.  Also will be used to calculate experience
		// gained
		switch(matBlock)
		{
		case STONE:
			matDrop = Material.COBBLESTONE;	blockValue = 1;	lootMax = 1;	break;			
		case COBBLESTONE:
			matDrop = Material.COBBLESTONE;	blockValue = 1;	lootMax = 1;	break;			
		
		case COAL_ORE:
			matDrop = Material.COAL;		blockValue = 5;	lootMax = 2;	break;
		case NETHERRACK:
			matDrop = Material.NETHERRACK;	blockValue = 5;	lootMax = 2;	break;
		
		case MOSSY_COBBLESTONE:
			matDrop = Material.MOSSY_COBBLESTONE;	blockValue = 10;lootMax = 2;	break;		
		
		case IRON_ORE:
			matDrop = Material.IRON_ORE;	blockValue = 15;lootMax = 2;	break;
		case NETHER_BRICK:
			matDrop = Material.NETHER_BRICK;blockValue = 15;lootMax = 3;	break;	
			
		case IRON_BLOCK:
			matDrop = Material.IRON_BLOCK;	blockValue = 20; lootMax = 1; bNoExp = false; break;
		case GOLD_ORE:
			matDrop = Material.GOLD_ORE;	blockValue = 20;lootMax = 3;	break;			
		case LAPIS_ORE:
			matDrop = Material.INK_SACK;	blockValue = 20;lootMax = 6;	break;	
			
		case GOLD_BLOCK:
			matDrop = Material.GOLD_BLOCK;	blockValue = 25;lootMax = 1; bNoExp = false; break;
		
		case REDSTONE_ORE:
			matDrop = Material.REDSTONE; blockValue = 25; lootMax = 8; break;
		case GLOWING_REDSTONE_ORE:
			matDrop = Material.REDSTONE; blockValue = 25; lootMax = 8; break;
		
		case EMERALD_ORE:
			matDrop = Material.EMERALD;	blockValue = 25; lootMax = 3; break;			
			
		case DIAMOND_ORE:
			matDrop = Material.DIAMOND;		blockValue = 30;lootMax = 3;	break;	
		
		case EMERALD_BLOCK:
			matDrop = Material.EMERALD_BLOCK;
			blockValue = 30;
			lootMax = 1;
			break;	
			
		case DIAMOND_BLOCK:
			matDrop = Material.DIAMOND_BLOCK; blockValue = 35; lootMax = 1; bNoExp = false; break;
		case OBSIDIAN:
			matDrop = Material.OBSIDIAN;	blockValue = 35;lootMax = 2;	break;
			
			default:
				blockValue = 0;							
		} // switch(matBlock)
		
		// Make sure player can mine this block
		if(mineLevel < blockValue)
		{	pMiner.sendMessage("[§2RPG§f] You are not able to mine "+bTarget.getType().name());
			pMiner.sendMessage("[§2RPG§f] at your level.  You need at least "+blockValue+" in mining.");
			return true;
		}// if(mineLevel < blockValue)
		
		if(mineLevel >= 50)
			bNoExp = false;
		
		// Figure out how much loot should drop
		int lootDrop = (int) (randomizer.nextInt(lootMax + 1));			
		lootBonus = (float)lootDrop / lootMax;
		
		// Prepare and drop loot
		ItemStack loot = new ItemStack(matDrop, 1);
		if(matDrop.equals(Material.INK_SACK))
			loot.setDurability((short) 4);
		
		Location loc = bTarget.getLocation();
		if(!placedBlocks.isEmpty())
		{	if(placedBlocks.containsKey(loc))
			{	// ok, this player has placed blocks.... check for duping
				// exploit
				bTarget.getWorld().dropItemNaturally(loc, loot);
				bNoExp = false;		// Set no exp flag
				lootBonus = 0;
				pMiner.sendMessage("[§2RPG§f] You can't gain experience with placed blocks.");
				placedBlocks.remove(loc);
			}// if(placedBlocks.containsKey(loc))
		} // if(!placedBlocks.isEmpty())		
		
		// Special Checks for enchanted Items
		float dropPercent = 1.0f;
		if(!iPickaxe.getEnchantments().isEmpty())
		{
			// Need to get a random number.
			int chance = randomizer.nextInt(10000 + 1);
			
			boolean bDmgTool = false;
			
			// Fortune Enchantment - Somewhat modified
			if(iPickaxe.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS))
			{	int encLevel = iPickaxe.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
				switch(encLevel)
				{
				case 1: dropPercent = 1.25f;
				case 2: dropPercent = 1.50f;
				case 3:	dropPercent = 2.0f;
				} // switch(encLevel)
			} // if(iPickaxe.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS))
			
			// Unbreaking
			if(iPickaxe.containsEnchantment(Enchantment.DURABILITY))
			{	int encLevel = iPickaxe.getEnchantmentLevel(Enchantment.DURABILITY);
				switch(encLevel)
				{ 
				case 1: if(chance <= 5000) bDmgTool = true; // 50 %
				case 2: if(chance <= 3300) bDmgTool = true;// 33 %
				case 3: if(chance <= 2500) bDmgTool = true;// 25%
				} // switch(encLevel)
			} // if(iPickaxe.containsEnchantment(Enchantment.DURABILITY))
			
			if(bDmgTool)
				iPickaxe.setDurability((short) (iPickaxe.getDurability() + 1));
		} // if(!iPickaxe.getEnchantments().isEmpty())
		else{
			iPickaxe.setDurability((short) (iPickaxe.getDurability() + 1));
		}
		
		// Check if Pickaxe is broken yet
		if(iPickaxe.getDurability() >= maxUses)
			pMiner.setItemInHand(null);	
		
		bTarget.setTypeId(0);
		
		if(lootBonus > 0)
			for(int i = 0; i < (lootDrop * dropPercent); i++)
				bTarget.getWorld().dropItemNaturally(loc, loot);
				
		// Take care of experience
		if(!bNoExp)
		{	float skillIncrease = 0;
			mineBonus = (toolMod + (blockValue / 2) / mineLevel) * mineMod;
			skillIncrease = ((mineBonus * 10) / mineLevel)  * lootBonus;
			if(bTarget.getType().equals(Material.NETHERRACK))
				skillIncrease = skillIncrease / 2;
		
			if(rpgChar.race.equalsIgnoreCase("dwarf"))
			{
				float lvlExp = skillIncrease / 10;
				skillIncrease -= lvlExp;
				rpgChar.addExperience(lvlExp, pMiner);			
			}
			rpgChar.mineSkillBar += skillIncrease;
						
			if(rpgChar.mineSkillBar >= 75)
			{	// let's increase the skill.
				rpgChar.mining += 1;
				rpgChar.mineSkillBar = 0;
			
				//sp.sendNotification("Mining "+rpgChar.mining, "Mining skill increased!", Material.DIAMOND_PICKAXE);
				pMiner.sendMessage("[§2RPG§f] Mining is now: "+rpgChar.mining);	
			} // if(rpgChar.mineSkillBar >= 75)	
		} // if(!bNoExp)		
		
		return true; 
	} // public static boolean mine(Block bTarget, Player pMiner, ItemStack iPickaxe)
	
	
	public MiningSystem(Plugin p)
	{	randomizer.setSeed(System.nanoTime());
		placedBlocks = loadPlacedBlocks();
	} // public MiningSystem(Plugin p)
	
	
	public static void shutDown()
	{
		if(!placedBlocks.isEmpty())
			save((HashMap<Location, Integer>) placedBlocks);		
	} // public static void shutDown()
	// --------------------------------------------------------------
	// Save and load the Map of player placed blocks.  This is to 
	// prevent players from cheating by placing blocks and rebreaking
	// them to level up fast and dupe blocks from multiple drops 
	public static void save(HashMap<Location,Integer> map)
	{	try
		{	ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RPGCraft.mainDirectory+"placedBlocks.bin"));
			oos.writeObject(map);
			oos.flush();
			oos.close();
			//Handle I/O exceptions
		}
		catch(Exception e)
		{	e.printStackTrace();	}
	} // public void save(HashMap<Location,Integer> map, String path)
	
	@SuppressWarnings(value = {"resource", "unchecked" })
	public HashMap<Location,Integer> loadPlacedBlocks()
	{	placedBlocks.clear();
		
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new FileInputStream(RPGCraft.mainDirectory+"placedBlocks.bin"));
			Object result = ois.readObject();
			RPGCraft.log.info("[RPGCraft-INFO] - Placed Blocks file loaded successfully.");
			return (HashMap<Location,Integer>)result;	
		} catch (Exception e)
		{
			RPGCraft.log.info("[RPGCraft-INFO] - Placed Blocks file does not exist.");
		}
		return (HashMap<Location, Integer>) placedBlocks;
	} // public HashMap<Location,Integer> loadPlacedBlocks()
	
} // public class MiningSystem
