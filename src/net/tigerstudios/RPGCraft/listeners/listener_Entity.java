package net.tigerstudios.RPGCraft.listeners;


import java.util.Random;

import net.tigerstudios.RPGCraft.RPGCraft;
import net.tigerstudios.RPGCraft.RPG_Character;
import net.tigerstudios.RPGCraft.mgr_Player;
import net.tigerstudios.RPGCraft.utils.CTextFileWriter;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Monster;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.player.SpoutPlayer;


// TODO: This code needs a complete re write!!!
// This code repeats itself for every mob type.


public class listener_Entity implements Listener{
	private Random rndSeed = null;
	private int rndNum = 0;
	private int rndNum2 = 0;
	private Monster monsterEnt = null;
	private Player mcPlayer = null;
	private SpoutPlayer sPlayer = null;
	private ItemStack iStack = null;	
	
	private static CTextFileWriter fLog = null;
	
	// ----------------------------------------------------
	// Entity Tame Event
	// 
	//	Event called when a player tried to tame a creature
	//	This is something that is Race specific. Elves can
	//  tame wolves, and humans can tame cats.
	/*@EventHandler	// ------------------------------------
	public void onEntityTame(EntityTameEvent event)
	{
		mcPlayer = Bukkit.getPlayer(event.getOwner().getName());
		if(mcPlayer != null)
		{
			if(event.getEntityType().equals(EntityType.WOLF))
			{
				RPG_Character rpgChar = mgr_Player.getCharacter(mcPlayer);
				if(rpgChar == null)
					return;
			
				if(!rpgChar.race.equalsIgnoreCase("elf"))
				{
					mcPlayer.sendMessage("[§2RPG§f] You're not able to tame wolves as a "+rpgChar.race+".");
					mcPlayer.sendMessage("[§2RPG§f] See an Elf if you'd like to have a pet wolf.");
					event.setCancelled(true);
					fLog.writeLine("TAME EVENT,"+mcPlayer.getName());
					return;				
				} // if(!rpgChar.race.equalsIgnoreCase("elf"))
			} // if(event.getEntityType().equals(EntityType.WOLF))
		
			if(event.getEntityType().equals(EntityType.OCELOT))
			{
			
			} // if(event.getEntityType().equals(EntityType.OCELOT))
		} // if(p != null)
	} // public void onEntityTame(final EntityTameEvent event)
	*/
		
	/*@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event)
	{
		World wrld = event.getLocation().getWorld();
		if(wrld.getMonsterSpawnLimit() <= wrld.getEntities().size())
		{	event.setCancelled(true);
			return; 
		}			
			
		// Make Blazes spawn randomly in the Nether
		if(wrld.getEnvironment().equals(Environment.NETHER))
		{	if(this.rndSeed.nextInt(100 + 1) <= 10)
			{
				wrld.spawnEntity(event.getLocation(), EntityType.BLAZE);
				event.setCancelled(true);
				return;
			}
			
		} // if(event.getLocation().getWorld().getEnvironment().equals(Environment.NETHER))	
		
		wrld.spawnEntity(event.getLocation(), event.getEntityType());
		event.setCancelled(true);	
	} // public void onCreatureSpawn(CreatureSpawnEvent event)
	*/
	
		
	@EventHandler
	public void onEntityDeath(final EntityDeathEvent event)
	{				
		FileConfiguration cfg = RPGCraft.getRPGConfig();
		int dropRate = 0;
		
		if(event.getEntity() instanceof Monster)
		{	if(rndSeed.nextInt(100) >= cfg.getInt("DropRates.monsters"))
				return;
					
			monsterEnt = (Monster) event.getEntity();
			mcPlayer = monsterEnt.getKiller();
			if(mcPlayer == null)
				return;
			
			iStack = mcPlayer.getItemInHand();
			if(iStack.getType() == Material.FISHING_ROD)
			{	mcPlayer.sendMessage("[§2RPG§f] You can not gain anything by killing with a fishing rod.");
				return;
			}

			rndNum = rndSeed.nextInt(1000);
// --------------------------------------------------------------------------------------
// Basic Mobs
			// Drop Chance 30%
			if(monsterEnt instanceof Zombie && !(monsterEnt instanceof PigZombie))
			{	// Rare chance of Basic Mobs dropping 1 Silver
				dropRate = cfg.getInt("DropRates.Creature.Zombie");
				
				ItemStack ccoin = new SpoutItemStack(RPGCraft.copperCoin, 1);
				
				
				if(rndNum <= ( dropRate / 10))
				{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), ccoin);
					fLog.writeLine("ENTITY DEATH,"+mcPlayer.getName()+","+monsterEnt.getType().getName()+",1");
					return;	}
				
				if(rndNum <= dropRate)
				{	rndNum2 = rndSeed.nextInt(1000);
					if(rndNum2 <= 200)
					{ 	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), ccoin);
						fLog.writeLine("ENTITY DEATH,"+mcPlayer.getName()+","+monsterEnt.getType().getName()+",1");
						return;
					}
					if(rndNum2 <= 350)
					{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), ccoin);
						fLog.writeLine("ENTITY DEATH,"+mcPlayer.getName()+","+monsterEnt.getType().getName()+",1");
						return;
					}					
					monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), ccoin);
					fLog.writeLine("ENTITY DEATH,"+mcPlayer.getName()+","+monsterEnt.getType().getName()+",1");
					return;
				} // if(rndNum <= 300)
				fLog.writeLine("ENTITY DEATH,"+mcPlayer.getName()+","+monsterEnt.getType().getName()+",0");
				return;
			} // if(monsterEnt instanceof Zombie)
			
			// Drop Chance 45%
			if(monsterEnt instanceof Skeleton)
			{	
				dropRate = cfg.getInt("DropRates.Creature.Skeleton");
				// Rare chance of Basic Mobs dropping 1 Silver
				if(rndNum <= (dropRate / 10))
				{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 10));
					return;	}
				
				if(rndNum <= dropRate)
				{	rndNum2 = rndSeed.nextInt(1000);
					if(rndNum2 <= 200)
					{ 	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 5));
						return;
					}
					if(rndNum2 <= 350)
					{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 3));
						return;
					}										
					monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 1));
				} // if(rndNum < 437)
				return;
			} // if(monsterEnt instanceof Skeleton)
			
			// Drop Chance = 65 %
			if(monsterEnt instanceof Creeper)
			{	
				dropRate = cfg.getInt("DropRates.Creature.Creeper");
				// Rare chance of Basic Mobs dropping 1 Silver
				if(rndNum <= ( dropRate / 10) )
				{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 20));
					return;	}
				if(rndNum <= dropRate)
				{	rndNum2 = rndSeed.nextInt(1000);
					if(rndNum2 <= 200)
					{ 	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 5));
						return;
					}
					if(rndNum2 <= 350)
					{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 3));
						return;
					}					
					monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 1));
				} // if(rndNum < 437)
				return;
			} // if(monsterEnt instanceof Creeper)
			
			// Drop Chance 40 %
			if(monsterEnt instanceof Spider)
			{	
				dropRate = cfg.getInt("DropRates.Creature.Spider");
				// Rare chance of Basic Mobs dropping 1 Silver
				if(rndNum <= (dropRate / 5))
				{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 20));
					return;	
				}
				
				if(rndNum <= dropRate)
				{	rndNum2 = rndSeed.nextInt(1000);
					if(rndNum2 <= 200)
					{ 	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 5));
						return;
					}
					if(rndNum2 <= 350)
					{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 3));
						return;
					}					
					monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 1));
				} // if(rndNum < 437)
				return;
			} // if(monsterEnt instanceof Spider)
				
			// Drop Chance 40 %
			if(monsterEnt instanceof Slime && !(monsterEnt instanceof MagmaCube))
			{	
				dropRate = cfg.getInt("DropRates.Creature.Slime");
				// Rare chance of Basic Mobs dropping 1 Silver
				if(rndNum <= (dropRate / 10))
				{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 20));
					return;	}
							
				if(rndNum <= dropRate)
				{	rndNum2 = rndSeed.nextInt(1000);
					if(rndNum2 <= 200)
					{ 	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 10));
						return;
					}
					if(rndNum2 <= 350)
					{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 5));
						return;
					}					
					monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 3));
				} // if(rndNum < 437)
				return;
			} // if(monsterEnt instanceof Spider)
			
			
			// Drop Chance 60 %
			if(monsterEnt instanceof Enderman)
			{	// Rare chance of Basic Mobs dropping 1 Silver
				dropRate = cfg.getInt("DropRates.Creature.Enderman");
				if(rndNum <= (dropRate / 10))
				{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 15));
				return;	}
				
				if(rndNum <= dropRate)
				{	rndNum2 = rndSeed.nextInt(1000);
					if(rndNum2 <= 200)
					{ 	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 5));
						return;
					}
					if(rndNum2 <= 350)
					{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 3));
						return;
					}										
					monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 1));
				} // if(rndNum < 437)
				return;
			} // if(monsterEnt instanceof Enderman)	
			
			if(monsterEnt instanceof Silverfish)
			{	// Rare chance of Basic Mobs dropping 1 Silver
				dropRate = cfg.getInt("DropRates.Creature.Silverfish");
				if(rndNum <= (dropRate / 10))
				{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 30));
				return;	}
				
				if(rndNum <= dropRate)
				{	rndNum2 = rndSeed.nextInt(1000);
					if(rndNum2 <= 250)
					{ 	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 10));
						return;
					}
					if(rndNum2 <= 400)
					{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 5));
						return;
					}										
					monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 1));
				} // if(rndNum < 437)
				return;
			} // if(monsterEnt instanceof Silverfish)	
// --------------------------------------------------------------------------------------
			
// --------------------------------------------------------------------------------------
// Nether Mobs
			// Drop Chance 30 %
			if(monsterEnt instanceof PigZombie)
			{	
				dropRate = cfg.getInt("DropRates.Creature.PigZombie");
				// Rare chance of Basic Mobs dropping 1 Silver
				if(rndNum <= (dropRate / 10) )
				{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 15));
					return;	}
							
				if(rndNum <= dropRate)
				{	rndNum2 = rndSeed.nextInt(1000);
					if(rndNum2 <= 200)
					{ 	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 5));
						return;
					}
					if(rndNum2 <= 350)
					{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 3));
						return;
					}										
					monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 1));
				} // if(rndNum < 437)
				return;
			} // if(monsterEnt instanceof PigZombie)	
			
			// Drop Chance 50 %
			if(monsterEnt instanceof Blaze)
			{	
				dropRate = cfg.getInt("DropRates.Creature.Blaze");
				// Rare chance of Basic Mobs dropping 1 Silver
				if(rndNum <= (dropRate/5))
				{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.silverCoin, 1));
					return;	}
										
				if(rndNum <= dropRate)
				{	rndNum2 = rndSeed.nextInt(1000);
					if(rndNum2 <= 150)
					{ 	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 25));
						return;
					}
					if(rndNum2 <= 350)
					{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 15));
						return;
					}										
					monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 5));
				} // if(rndNum < 437)
				return;
			} // if(monsterEnt instanceof Blaze)
			
			// Drop Chance 50 %
			if(monsterEnt instanceof Ghast)
			{	
				dropRate = cfg.getInt("DropRates.Creature.Ghast");
				// Rare chance of Basic Mobs dropping 1 Silver
				if(rndNum <= (dropRate / 3))
				{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.silverCoin, 5));
					return;	}
													
					if(rndNum <= dropRate)
					{	rndNum2 = rndSeed.nextInt(1000);
						if(rndNum2 <= 150)
						{ 	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 20));
							return;
						}
						if(rndNum2 <= 350)
						{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 10));
							return;
						}										
						monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 3));
					} // if(rndNum < 437)
					return;
				} // if(monsterEnt instanceof Ghast)
			
			// Drop Chance 40 %
			if(monsterEnt instanceof MagmaCube)
			{	
				dropRate = cfg.getInt("DropRates.Creature.MagmaCube");
				// Rare chance of Basic Mobs dropping 1 Silver
				if(rndNum <= (dropRate / 10))
				{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 25));
					return;	}
					
				// Chance to drop slime ball
				int slimeChance = rndSeed.nextInt(1000);
				if(slimeChance <= 400)
					monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new ItemStack(Material.SLIME_BALL));
				
				if(rndNum <= dropRate)
				{	rndNum2 = rndSeed.nextInt(1000);
					if(rndNum2 <= 150)
					{ 	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 15));
						return;
					}
					if(rndNum2 <= 350)
					{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 7));
						return;
					}										
					monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 3));
				} // if(rndNum < 437)
				return;
			} // if(monsterEnt instanceof MagmaCube)
// --------------------------------------------------------------------------------------
			
			return;
		} // if(event.getEntity() instanceof Monster)	
		
	} // public void onEntityDeath(final EntityDeathEvent event)
		
	
	public listener_Entity(Plugin p)
	{
		fLog = new CTextFileWriter(RPGCraft.logDirectory + "entityListener.log");
		rndSeed = new Random(System.currentTimeMillis());	
	 } // public listener_Entity(Plugin p)	
} // public class listener_Entity implements Listener