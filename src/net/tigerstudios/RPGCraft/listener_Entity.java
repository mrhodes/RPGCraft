package net.tigerstudios.RPGCraft;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.inventory.SpoutItemStack;

public class listener_Entity implements Listener{
	private Random rndSeed = null;
	private int rndNum = 0;
	private int rndNum2 = 0;
	private Monster monsterEnt = null;
	private Animals animalEnt = null;
	private Player mcPlayer = null;
	private ItemStack iStack = null;
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(final EntityDeathEvent event)
	{
		FileConfiguration cfg = RPGCraft.config;
		int dropRate = 0;
		
		if(event.getEntity() instanceof Monster)
		{	if(rndSeed.nextInt(100) >= RPGCraft.config.getInt("DropRates.monsters"))
				return;
			
			
		
			monsterEnt = (Monster) event.getEntity();
			mcPlayer = monsterEnt.getKiller();
			if(mcPlayer == null)
				return;
			
			iStack = mcPlayer.getItemInHand();
			if(iStack.getType() == Material.FISHING_ROD)
			{	mcPlayer.sendMessage("You can not gain anything by killing with a fishing rod.");
				return;
			}
		   	

			rndNum = rndSeed.nextInt(100);
// --------------------------------------------------------------------------------------
// Basic Mobs
			// Drop Chance 30%
			if(monsterEnt instanceof Zombie && !(monsterEnt instanceof PigZombie))
			{	// Rare chance of Basic Mobs dropping 1 Silver
				dropRate = cfg.getInt("DropRates.Creature.Zombie");
				if(rndNum <= ( dropRate / 10))
				{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.silverCoin, 1));
					return;	}
				
				if(rndNum <= dropRate)
				{	rndNum2 = rndSeed.nextInt(1000);
					if(rndNum2 <= 150)
					{ 	mcPlayer.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 5));
						return;
					}
					if(rndNum2 <= 350)
					{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 3));
						return;
					}					
					monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 1));
				} // if(rndNum <= 300)
				return;
			} // if(monsterEnt instanceof Zombie)
			
			// Drop Chance 45%
			if(monsterEnt instanceof Skeleton)
			{	
				dropRate = cfg.getInt("DropRates.Creature.Skeleton");
				// Rare chance of Basic Mobs dropping 1 Silver
				if(rndNum <= (dropRate / 10))
				{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.silverCoin, 1));
					return;	}
				
				if(rndNum <= dropRate)
				{	rndNum2 = rndSeed.nextInt(1000);
					if(rndNum2 <= 150)
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
				{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.silverCoin, 2));
					return;	}
				if(rndNum <= dropRate)
				{	rndNum2 = rndSeed.nextInt(1000);
					if(rndNum2 <= 150)
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
				if(rndNum <= (dropRate / 10))
				{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.silverCoin, 1));
					monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 25));
					return;	
				}
				
				if(rndNum <= dropRate)
				{	rndNum2 = rndSeed.nextInt(1000);
					if(rndNum2 <= 150)
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
				{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.silverCoin, 2));
					monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 50));
					return;	}
							
				if(rndNum <= dropRate)
				{	rndNum2 = rndSeed.nextInt(1000);
					if(rndNum2 <= 150)
					{ 	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 15));
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
				{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.silverCoin, 1));
					monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 50));
				return;	}
				
				if(rndNum <= dropRate)
				{	rndNum2 = rndSeed.nextInt(1000);
					if(rndNum2 <= 150)
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
				{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.goldCoin, 1));
					monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 50));
				return;	}
				
				if(rndNum <= dropRate)
				{	rndNum2 = rndSeed.nextInt(1000);
					if(rndNum2 <= 250)
					{ 	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.silverCoin, 3));
						return;
					}
					if(rndNum2 <= 400)
					{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.silverCoin, 2));
						return;
					}										
					monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 50));
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
				{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.silverCoin, 1));
					monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 25));
					return;	}
							
				if(rndNum <= dropRate)
				{	rndNum2 = rndSeed.nextInt(1000);
					if(rndNum2 <= 150)
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
				if(rndNum <= (dropRate/10))
				{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.silverCoin, 1));
					return;	}
										
				if(rndNum <= dropRate)
				{	rndNum2 = rndSeed.nextInt(1000);
					if(rndNum2 <= 150)
					{ 	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 35));
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
				if(rndNum <= (dropRate / 10))
				{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.goldCoin, 1));
					monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.silverCoin, 10));
					return;	}
													
					if(rndNum <= dropRate)
					{	rndNum2 = rndSeed.nextInt(1000);
						if(rndNum2 <= 150)
						{ 	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.silverCoin, 3));
							return;
						}
						if(rndNum2 <= 350)
						{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.silverCoin, 1));
							return;
						}										
						monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 25));
					} // if(rndNum < 437)
					return;
				} // if(monsterEnt instanceof Ghast)
			
			// Drop Chance 40 %
			if(monsterEnt instanceof MagmaCube)
			{	
				dropRate = cfg.getInt("DropRates.Creature.MagmaCube");
				// Rare chance of Basic Mobs dropping 1 Silver
				if(rndNum <= (dropRate / 10))
				{	monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.silverCoin, 2));
					monsterEnt.getWorld().dropItem(monsterEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 25));
					return;	}
					
				// Chance to drop slime ball
				int slimeChance = rndSeed.nextInt(1000);
				if(slimeChance <= 250)
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
		
		if(event.getEntity() instanceof Animals)
		{	animalEnt = (Animals) event.getEntity();
			mcPlayer = animalEnt.getKiller();
			if(mcPlayer == null)
				return;		
			
			if(rndSeed.nextInt(100) >= cfg.getInt("DropRates.animals"))
				return;
			
			rndNum = rndSeed.nextInt(100);
		
			// Drop Chance 35 %
			if(animalEnt instanceof Wolf)
			{	dropRate = cfg.getInt("DropRates.Creature.Wolf");
				if(rndNum > dropRate)
					return;
				
				// Rare chance of Basic Mobs dropping 1 Silver
				if(rndNum <= (dropRate / 10))
				{	animalEnt.getWorld().dropItem(animalEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 15));
					return;	}
								
				if(rndSeed.nextInt(1000) <= 350)
					animalEnt.getWorld().dropItem(animalEnt.getLocation(), new ItemStack(Material.RAW_CHICKEN, 2));
							
				if(rndNum <= dropRate)
				{	rndNum2 = rndSeed.nextInt(1000);
					if(rndNum2 <= 150)
					{ 	animalEnt.getWorld().dropItem(animalEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 5));
						return;
					}
					if(rndNum2 <= 350)
					{	animalEnt.getWorld().dropItem(animalEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 3));
						return;
					}										
					animalEnt.getWorld().dropItem(animalEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 1));
				} // if(rndNum < 437)
				return;
			} // if(animalEnt instanceof Wolf)
			
			
			if(animalEnt instanceof Chicken)
			{	dropRate = cfg.getInt("DropRates.Creature.Chicken");
				if(rndNum > dropRate)
					return;
				
				if(rndNum <= (dropRate / 10)){
					animalEnt.getWorld().dropItem(animalEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 15));
					return;	}
				
				if(rndNum <= dropRate)
				{	rndNum2 = rndSeed.nextInt(1000);
				if(rndNum2 <= 150)
				{ 	animalEnt.getWorld().dropItem(animalEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 5));
					return;
				}
				if(rndNum2 <= 350)
				{	animalEnt.getWorld().dropItem(animalEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 3));
					return;
				}										
				animalEnt.getWorld().dropItem(animalEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 1));
				} // if(rndNum < 437)
				return;
			} // if(animalEnt instanceof Chicken)
			
			if(animalEnt instanceof Cow)
			{	dropRate = cfg.getInt("DropRates.Creature.Cow");
				if(rndNum > dropRate)
					return;
				
				if(rndNum <= (dropRate / 10)){
					animalEnt.getWorld().dropItem(animalEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 15));
					return;	}
				
				if(rndNum <= dropRate)
				{	if(rndNum2 <= 150)
					{ 	animalEnt.getWorld().dropItem(animalEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 5));
						return;
					}
					if(rndNum2 <= 350)
					{	animalEnt.getWorld().dropItem(animalEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 3));
						return;
					}										
					animalEnt.getWorld().dropItem(animalEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 1));
				} // if(rndNum < 437)
				return;
			} // if(animalEnt instanceof Cow)
			
			if(animalEnt instanceof Pig)
			{	dropRate = cfg.getInt("DropRates.Creature.Pig");
				if(rndNum > dropRate)
					return;
				
				if(rndNum <= (dropRate / 10)){
					animalEnt.getWorld().dropItem(animalEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 15));
					return;	}
				
				if(rndNum <= dropRate)
				{	if(rndNum2 <= 150)
					{ 	animalEnt.getWorld().dropItem(animalEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 5));
						return;
					}
					if(rndNum2 <= 350)
					{	animalEnt.getWorld().dropItem(animalEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 3));
						return;
					}										
					animalEnt.getWorld().dropItem(animalEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 1));} // if(rndNum < 437)
					return;
				} // if(animalEnt instanceof Pig)
			
			if(animalEnt instanceof Squid)
			{	dropRate = cfg.getInt("DropRates.Creature.Squid");
				if(rndNum > dropRate)
					return;
				
				if(rndNum <= (dropRate / 10)){
					animalEnt.getWorld().dropItem(animalEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 15));
					return;	}
				
				if(rndNum <= dropRate)
				{	if(rndNum2 <= 150)
					{ 	animalEnt.getWorld().dropItem(animalEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 5));
						return;
					}
					if(rndNum2 <= 350)
					{	animalEnt.getWorld().dropItem(animalEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 3));
						return;
					}										
					animalEnt.getWorld().dropItem(animalEnt.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, 1));
				} // if(rndNum < 437)
				return;
			} // if(animalEnt instanceof Squid)
			
			
			return;
		} // if(event.getEntity() instanceof Animals)			
	} // public void onEntityDeath(final EntityDeathEvent event)
	
	
	public listener_Entity(Plugin p)
	{
		Bukkit.getServer().getPluginManager().registerEvents(this, p);
		rndSeed = new Random(System.currentTimeMillis());	
	 } // public listener_Entity(Plugin p)	
} // public class listener_Entity implements Listener