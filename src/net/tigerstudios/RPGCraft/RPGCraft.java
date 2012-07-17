// RPGCraft Plugin
// Author: Michael Rhodes
// 
// Permission Nodes:
//		rpgcraft.*
//		rpgcraft.users.*
//		rpgcraft.users.race
//		rpgcraft.users.coin
//		rpgcraft.rpg.*
//		rpgcraft.rpg.mods
//			/rpg <saveall | loadall>
//			/rpg resetname <player>
//		rpgcraft.rpg.admins
//		rpgcraft.money.*					Access to currency commands
//		rpgcraft.money.mods
//			/balance <all | playername>
//		rpgcraft.bank.*
//		rpgcraft.bank.banker

//

package net.tigerstudios.RPGCraft;

import java.io.File;
import java.sql.SQLException;
import java.util.logging.Logger;

//import net.milkbowl.vault.economy.Economy;
import net.tigerstudios.RPGCraft.utils.SQLiteManager;
import net.tigerstudios.RPGCraft.utils.SpoutFeatures;
import net.tigerstudios.RPGCraft.utils.custom.CCoin;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.material.CustomItem;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;


public class RPGCraft extends JavaPlugin{
	private static String name;
	private static String version;
	//public static Economy economy = null;
	public static Logger log = null;	
	public static String divider = "***************************************************************";
		
	private static Server mcServer;	
	
	// Listener Classes to handle the events
	private static listener_Player	playerListener = null;
	private static listener_Block blockListener = null;
	private static listener_Currency currencyListener = null;	
	private static listener_Entity entityListener = null;
	private static CombatSystem combatSystem = null;
	private static FarmSystem farmSystem = null;
	private static MiningSystem mineSystem = null;
	private static AppearanceListener appearanceListener = null;
	public static CustomItem copperCoin;
	public static CustomItem silverCoin;
	public static CustomItem goldCoin;
	public static int cp, sp, gp;
			
	public static String mainDirectory = "plugins" + File.separatorChar + "RPGCraft" + File.separatorChar;
	public static String logDirectory = mainDirectory + "logs" + File.separatorChar;
	public static PermissionManager pexMan = null;
	
	static FileConfiguration config = null;	

	@Override
	public void onDisable() {		
		// Shutdown all Manager classes
		try {
			mgr_Player.SaveAllData();
			mgr_Player.logoutAllPlayers();
			SQLiteManager.closeConnection("RPGCraft");
		} catch (SQLException e1) { e1.printStackTrace();}
		
		log.info(name + " " + version + " disabled.");
		log = null;
	} // public void onDisable()

	
	
	@Override
	public void onEnable() {		
		name = this.getDescription().getName();
		version = this.getDescription().getVersion();
							
		if(initializeRPGCraft() == false)
		{ 	// An error occurred while initiailizing the plugin.
			log.info(name + " " + version + " did not initialize.");
			log.info(name + " is not loaded.");
			log = null;
		}
		return;			
	} // public void onEnable()
	
	
	private boolean initializeRPGCraft()
	{
		log = Logger.getLogger("Minecraft.RPGCraft");
		mcServer = getBukkitServer();
		
		// Load the config file before anything else.  This way settings for other systems
		// can be added to the config
		loadConfig();	
		
		// Log Directory not needed right now (June 13 2012)
		//new File(logDirectory).mkdirs();		// Make the log Directory
			
		setupPermissions();		
		if(!setupDatabase())
			return false;
		
	/*	if(setupEconomy())
			log.info("§f[§2RPGCraft§f] Found Vault and will use it.");
		*/
		// Load the Race data files
		RaceSystem.loadRaceFile("halfling.yml");	RaceSystem.loadRaceFile("human.yml");
		RaceSystem.loadRaceFile("elf.yml");			RaceSystem.loadRaceFile("dwarf.yml");
		
		copperCoin = new CCoin(this, "Copper Coin", config.getString("URL Images."+ "copperIcon"));
		silverCoin = new CCoin(this, "Silver Coin", config.getString("URL Images."+ "silverIcon"));
		goldCoin = new CCoin(this, "Gold Coin", config.getString("URL Images."+ "goldIcon"));
		cp = copperCoin.getCustomId(); sp = silverCoin.getCustomId(); gp = goldCoin.getCustomId();
						
		SpoutFeatures.setup(this);	
		mgr_Player.initialize(this);
				
		playerListener = new listener_Player(this);
		blockListener = new listener_Block(this);
		currencyListener = new listener_Currency(this);
		entityListener = new listener_Entity(this);
		combatSystem = new CombatSystem(this);
		farmSystem = new FarmSystem(this);
		mineSystem = new MiningSystem(this);
		appearanceListener = new AppearanceListener(this);
		
		FarmSystem.setup();
		getServer().getPluginManager().registerEvents(this.appearanceListener, this);
		//bankListener = new listener_Bank(this);
						
		return true;
	} // private boolean initializeRPGCraft()	
	
	
	private boolean setupDatabase()
	{
		if(SQLiteManager.initialize("RPGCraft")==false)
		{	log.info("[RPGCraft]   Error when loading the SQLite library. RPGCraft cannot");
			log.info("[RPGCraft]   run without this.  Please make sure you have sqlite-jdbc-3.7.2.jar");
			log.info("[RPGCraft]   in the root of the server directory.");
			return false;
		} // if(SQLiteManager.initialize()==false)
		
		
		SQLiteManager.newConnection(mainDirectory+"RPGCraftDB.db", "RPGCraft");
		
		// Setup the account Table...
		// When a new user joins the server this table will be updated with
		// that new users info.  This is not character related.
		if(SQLiteManager.TableExists("Accounts", "RPGCraft") == false)
		{	log.info("[RPGCraft] --->   Creating Accounts table.");
			SQLiteManager.SQLUpdate("create table Accounts ("+
					"account_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
					"mc_Name VARCHAR(24),"+
					"joined DATE"+
					");");
		} // if(SQLiteManager.TableExists("accounts", "RPGCraft") == false)
		
		// Setup the playerData Table..
		if(SQLiteManager.TableExists("Characters", "RPGCraft") == false)
		{	log.info("[RPGCraft] --->   Creating Characters table.");
			SQLiteManager.SQLUpdate("create table Characters ("+
					"char_id INTEGER PRIMARY KEY AUTOINCREMENT," + 	// Primary key
					"account_id SMALLINT UNSIGNED NOT NULL," +		// Foreign key
					"name varchar(16) NOT NULL," +
					"namePrefix varchar(32), 	nameSuffix varchar(64), "+
					"race varchar(10), level tinyint, experience int, exp_to_levelup int, " +
					"strength int, dexterity int, constitution int, intelligence int, " +
					"statPointsUsed int, statPointsTotal int, " +
					"attack int, defense int, parry int," +
					"mine int, mineSkillBar float, mineRaceMod float, " +
					"farm int, farmSkillBar float, farmRaceMod float, " +
					"blacksmith int, blacksmithSkillBar float, blacksmithRaceMod float, "+
					"enchant int, enchantSkillBar float, enchantRaceMod float, " +
					"alchemy int, alchemySkillBar float, alchemyRaceMod float, " +
					"cook int, cookSkillBar float, cookRaceMod float, " +
					"fish int, fishSkillBar float, fishRaceMod float, " +
					"trade int, tradeSkillBar float, tradeRaceMod float, "+
					"alcoholTolerance int, thrist int, "+
					"copper int"+					
					");");
		} // if(SQLiteManager.TableExists("characters", "RPGCraft") == false)
		
		// TODO: Make a table to Party groups.		
		return true;
	} // private void setupDatabase()	
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,	String label, String[] args)
	{		
		if(sender instanceof Player)
		{
			if(command.getName().equalsIgnoreCase("rpg"))
				if(CommandProcessor.rpgCommands((Player)sender, args))
					return true;			
		} // if(sender instanceof Player)
		
		if(currencyListener.currencyProcessor(sender, command, label, args))
			return true;
		
		return false;
	} // public boolean onCommand(CommandSender sender, Command command,	String label, String[] args)			
	
	public void setupPermissions() {
		Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("PermissionsEx");

		if (RPGCraft.pexMan == null) {
			if (permissionsPlugin != null) {
				RPGCraft.pexMan = PermissionsEx.getPermissionManager();
				log.info("[RPGCraft] ---> Permissions plugin detected.");
			} else {
				log.info("[RPGCraft] ---> Permissions plugin not detected, defaulting to Bukkit's built-in system.");
			}
		}
	} // public void setupPermissions()	
	
	
	public void makeWhiteList()
	{
		File f = new File("whitelist.txt");
		String query = "select mc_Name from Accounts";
		
		
		
	}
	
	
	public void loadConfig()
	{	config = this.getConfig();
		String path = "DropRates.Creature";
		
		config.createSection("URL Images");
		config.addDefault("URL Images."+ "copperIcon", "http://tigerstudios.net/minecraft/textures/copper.png");
		config.addDefault("URL Images." + "silverIcon", "http://tigerstudios.net/minecraft/textures/silver.png");
		config.addDefault("URL Images." +"goldIcon", "http://tigerstudios.net/minecraft/textures/gold.png");
				
		config.addDefault("DropRates.monsters", 80);
		
		// Set Defaults rates for Monsters
		config.addDefault(path + ".Blaze", 75);
		config.addDefault(path + ".CaveSpider", 65);
		config.addDefault(path + ".Creeper", 70);
		config.addDefault(path + ".EnderDragon", 100);
		config.addDefault(path + ".EnderMan", 8);
		config.addDefault(path + ".Ghast", 75);
		config.addDefault(path + ".Giant", 80);
		config.addDefault(path + ".MagmaCube", 60);
		config.addDefault(path + ".PigZombie", 55);
		config.addDefault(path + ".SilverFish", 50);
		config.addDefault(path + ".Skeleton", 70);
		config.addDefault(path + ".Slime", 60);
		config.addDefault(path + ".Zombie", 70);
		config.addDefault(path + ".Spider", 75);		
				
		config.options().copyDefaults(true);
		saveConfig();
	} // public void loadConfig()
	
	
	/* private boolean setupEconomy()
	 {
		 RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
	        if (economyProvider != null) {
	            economy = economyProvider.getProvider();
	        }

	        return (economy != null);
	    }
	
	*/
	public static Server getBukkitServer() {
        return mcServer;
    }		
} // public class RPGCraft extends JavaPlugin{
