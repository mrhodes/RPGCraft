package net.tigerstudios.RPGCraft.skills;

import java.util.Random;

import net.tigerstudios.RPGCraft.RPG_Character;
import net.tigerstudios.RPGCraft.mgr_Player;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

// TODO: Add more informative information to the 
// error messages.  Particularly when it comes to not being ready for certain tools
// Make messages more "fuzzy" like, "Need a lot more practice with this tool", or "You close to mastering this, keep it up" 


public class FarmSystem implements Listener {
	private static Random randomizer = new Random();
		
	public static boolean harvest(Player p, Block bCrops, ItemStack iHoe)
	{	SpoutPlayer sp = SpoutManager.getPlayer(p);		
		RPG_Character rpgChar = mgr_Player.getCharacter(p);
		
		if(rpgChar == null)
		{	// Player cannot harvest without chosing a race first
			sp.sendNotification("Harvest", "Must choose Race first!", bCrops.getType());
			p.sendMessage("[§2RPG§f] You cannot farm until you have first choosen a race.");
			p.sendMessage("[§2RPG§f] Please type /rpg for more information.");
			return true;
		}
		
		float toolValue = 1;
		int lootDrop, lootMax = 4;				// Used for success vs fail ratio
		int maxUses = 0;
		int hoeType = iHoe.getTypeId();			// Get our values based on tool used
		if(hoeType == 290){ toolValue = 10; lootMax = 2; maxUses = 60;}		// Wooden Hoe
		if(hoeType == 291){ toolValue = 20; lootMax = 3; maxUses = 132;}	// Stone Hoe
		if(hoeType == 292){ toolValue = 30; lootMax = 4; maxUses = 251;}	// Iron Hoe
		if(hoeType == 293){ toolValue = 40; lootMax = 6; maxUses = 1562;}	// Diamond Hoe
		if(hoeType == 294){ toolValue = 50; lootMax = 8; maxUses = 33;}		// Gold Hoe
					
		
		// Make sure the player can use this tool
		if(((toolValue > 10) && !rpgChar.race.equalsIgnoreCase("halfling")) || 
				((toolValue > 30) && !rpgChar.race.equalsIgnoreCase("human")) )
		{	p.sendMessage("[§2RPG§f] You cannot use this hoe as a "+rpgChar.race);
			return true;
		}
		
		// Make sure the wheat is ready to farm...
		// if it is not ready the crops will be destroyed
		// and no drops / no skill ups will happen
		if((bCrops.getType() == Material.CROPS) && (bCrops.getData() < 7))
		{	sp.sendNotification("Harvest", "Crops not ready to harvest", bCrops.getType());
			p.sendMessage("[§2RPG§f] You can't get any wheat from this, it's not even ready yet.");
			return false;
		} // if(bCrops.getData() != 7)
		
		Block bFarmland = bCrops.getRelative(0, -1, 0);
		
		float farmlevel 	= rpgChar.farming;
		float farmMod 		= rpgChar.farmRaceMod;
				
		// Values used to calculate the skill increase when farming
		float harvestBonus = 1;	float lootBonus = 1;		
			
		harvestBonus = (farmlevel / toolValue) * farmMod;				
		if((farmlevel + 10) < toolValue )
		{	p.sendMessage("[§2RPG§f] You are not ready to use that tool. Try something more your level.");
			return true;
		}		
		
		Material matSeeds; Material matDrops;
		
		matSeeds = Material.SEEDS; matDrops = Material.WHEAT;
		if(bCrops.getType() == Material.MELON_BLOCK) { matSeeds = Material.MELON_SEEDS; matDrops = Material.MELON; }
		if(bCrops.getType() == Material.PUMPKIN) { matSeeds = Material.PUMPKIN_SEEDS; matDrops = Material.PUMPKIN; }
		if(bCrops.getType() == Material.SUGAR_CANE_BLOCK) {matDrops = Material.SUGAR_CANE; }
				
		// Harvest bonus is within the characters ability
		// Figure out how much this harvest will drop		
		if(bCrops.getType() == Material.PUMPKIN)
		{	// Just drop one pumpkin
			bCrops.getWorld().dropItem(bCrops.getLocation(), new ItemStack(matDrops, 1));
		}else if(bCrops.getType() == Material.SUGAR_CANE_BLOCK)
		{
			// Need to make sure player can harvest Reeds.
			// Needs at least level 5 Farmer.
			if(rpgChar.farming < 5)
			{	p.sendMessage("[§2RPG§f] You don't have enough farming skill to harvest Reeds.");
				return true;
			}
			
			if(bCrops.getRelative(BlockFace.UP).getType() == Material.SUGAR_CANE_BLOCK)
			{	p.sendMessage("[§2RPG§f] Break the top block first if you want to get anything from it.");
				return true;
			}
			// Only drop one sugar cane.... otherwise player can get too many
			lootBonus = 0;
			bCrops.getWorld().dropItem(bCrops.getLocation(), new ItemStack(matDrops, 1));
		}else
		{
			// TODO: Update this code to modify the drops based on level
			lootDrop = (int) ((randomizer.nextInt(lootMax + 1) * farmMod));	
			if(lootDrop > (lootMax * farmMod))
				lootDrop = (int)(lootMax * farmMod) ;
			lootBonus = (float)lootDrop / lootMax;			
		
			if(rpgChar.race.equalsIgnoreCase("halfling") && (bCrops.getType() != Material.SUGAR_CANE_BLOCK))
			{	// Chance to drop some seeds.
				// Seeds will be extra and not included in loot drops!
				int seeds = randomizer.nextInt(lootDrop + 1);
				for(int i = 0; i < seeds; i++)
					bCrops.getWorld().dropItem(bCrops.getLocation(), new ItemStack(matSeeds, 1));
			} // if(rpgChar.race.equalsIgnoreCase("halfling"))	
			
			int count = lootDrop;
			for(int j = 0; j < count; j++)
				bCrops.getWorld().dropItem(bCrops.getLocation(), new ItemStack(matDrops, 1));
		}
				
		//Skill increase will be based on some randomness, but also on how the player harvested.
		// lootBonus, 
		float skillIncrease = ((harvestBonus * 10) / farmlevel)  * (lootBonus * farmMod);
		if(bCrops.getType() == Material.SUGAR_CANE_BLOCK)
			skillIncrease = 0;
		
		// Now reset these crops
		// Wheat is easy to clean up
		if(bCrops.getType() == Material.CROPS)
		{	bCrops.setTypeId(0);
			bFarmland.setTypeId(3);
		}else
		{	bCrops.setTypeId(0);		
		}			
		iHoe.setDurability((short) (iHoe.getDurability() + 1));
		if(iHoe.getDurability() >= maxUses)
			p.setItemInHand(null);
		
		if(rpgChar.race.equalsIgnoreCase("halfling"))
		{
			float lvlExp = skillIncrease / 10;
			skillIncrease -= lvlExp;
			rpgChar.addExperience(lvlExp, sp);			
		}
		rpgChar.farmSkillBar += skillIncrease;
		
		// If player is a halfling add 10% of this skil
						
		if(rpgChar.farmSkillBar >= 75)
		{	// let's increase the skill.
			rpgChar.farming += 1;
			rpgChar.farmSkillBar = 0;
			
			sp.sendNotification("Farming "+rpgChar.farming, "Farming skill increased!", Material.DIAMOND_HOE);
			p.sendMessage("[§2RPG§f] Farming is now: "+rpgChar.farming);	
		}				
		return true;		
	} // public static void processHarvest(BlockBreakEvent event)
	
			
	public static void animalInteractions(Player p, Animals animal)
	{	
		// Find out if player is trying to breed animals.
		if(p.getItemInHand().getType() == Material.WHEAT)
		{	// Now need to make sure this player has the right farming level
			// to go ahead with this taming.
			RPG_Character rpgChar = mgr_Player.getCharacter(p);
			if(rpgChar == null || !rpgChar.race.equalsIgnoreCase("halfling"))
			{
				p.sendMessage("[§2RPG§f] Only Halflings have the skill to breed animals.");
				return;
			} // if(rpgChar == null || !rpgChar.race.equalsIgnoreCase("halfling"))
				
			
			if(!animal.canBreed())
			{	p.sendMessage("[§2RPG§f] This animal is not ready to breed.");
				if(!animal.isAdult())
					p.sendMessage("[§2RPG§f] You should wait until this little fella grows up a bit.");
				SpoutManager.getPlayer(p).sendNotification("Breeding", "Can't breed this animal", Material.WHEAT);
				return;					
			} // if(!creature.canBreed())
				
			int farmlevel = rpgChar.farming;
			// Now need to make sure the halfling is a good enough farmer for breeding 
			// animals.
			// Chickens, Pigs - level 5
			// Cows, and Sheep - level 10
			if(animal.getType().getName().equalsIgnoreCase("Cow"))
			{
				if(farmlevel >= 10)
				{	p.sendMessage("[§2RPG§f] This Cow is now ready to.... get busy");
					SpoutManager.getPlayer(p).sendNotification("Breeding", "Successful Cow breeding!", Material.RAW_BEEF);
					return;
				}else
				{	p.sendMessage("[§2RPG§f] This Cow is not ready to breed");
					p.sendMessage("[§2RPG§f] Perhaps you should train more in farming.");
					SpoutManager.getPlayer(p).sendNotification("Breeding", "Failed Cow breeding!", Material.RAW_BEEF);
					return;
				} // case "Cow":
			}					
			if(animal.getType().getName().equalsIgnoreCase("Chicken"))
			{
				if(farmlevel >= 5)
				{	p.sendMessage("[§2RPG§f] This Chicken is now ready to.... get busy");
					SpoutManager.getPlayer(p).sendNotification("Breeding", "Successful Chick breeding", Material.RAW_CHICKEN);
					return;
				}else
				{	p.sendMessage("[§2RPG§f] This Chicken is not ready to breed");
					p.sendMessage("[§2RPG§f] Perhaps you should train more in farming.");
					SpoutManager.getPlayer(p).sendNotification("Breeding", "Failed Chicken breeding!", Material.RAW_CHICKEN);
					return;
				} // case "Chicken":
			}	
			if(animal.getType().getName().equalsIgnoreCase("Pig"))
			{
				if(farmlevel >= 5)
				{	p.sendMessage("[§2RPG§f] This Pig is now ready to.... get busy");
					SpoutManager.getPlayer(p).sendNotification("Breeding", "Successful Pig breeding!", Material.PORK);
					return;
				}else
				{	p.sendMessage("[§2RPG§f] This Pig is not ready to breed");
					p.sendMessage("[§2RPG§f] Perhaps you should train more in farming.");
					SpoutManager.getPlayer(p).sendNotification("Breeding", "Failed Pig breeding!", Material.PORK);
					return;
				} // case "Pig":
			}					
			if(animal.getType().getName().equalsIgnoreCase("Sheep"))
			{
				if(farmlevel >= 10)
				{	p.sendMessage("[§2RPG§f] This Sheep is now ready to.... get busy");
					SpoutManager.getPlayer(p).sendNotification("Breeding", "Successful Sheep breeding!", Material.WOOL);
					return;
				}else
				{	p.sendMessage("[§2RPG§f] This Sheep is not ready to breed");
					p.sendMessage("[§2RPG§f] Perhaps you should train more in farming.");
					SpoutManager.getPlayer(p).sendNotification("Breeding", "Failed Sheep breeding!", Material.WOOL);
					return;
				} // case "Sheep":				
			} // switch(creature.getType().getName())
		} // if(p.getItemInHand().getType() == Material.WHEAT)	
	} // public void animalInteractions(PlayerInteractEntityEvent event)
		
	
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerShearEntity(PlayerShearEntityEvent event)
	{
		RPG_Character rpgChar = mgr_Player.getCharacter(event.getPlayer());
		if(rpgChar == null)
		{
			event.getPlayer().sendMessage("[§2RPG§f] You need to select a race before you can shear sheep.");
			event.getPlayer().sendMessage("[§2RPG§f] To select a race type §2/rpg reset§f.");
			event.setCancelled(true);
			return;
		} // if(rpgChar == null)
		
		if(!rpgChar.race.equalsIgnoreCase("halfling") && !rpgChar.race.equalsIgnoreCase("human"))
		{
			event.getPlayer().sendMessage("[§2RPG§f] Only Halflings and Humans can shear sheep.");
			event.setCancelled(true);
			return;
		}	
	} // public void onPlayerShearEntity(PlayerShearEntityEvent event)
	
	public FarmSystem()
	{
		randomizer.setSeed(System.nanoTime());		
	}
} // public class FarmSystem implements Listener
