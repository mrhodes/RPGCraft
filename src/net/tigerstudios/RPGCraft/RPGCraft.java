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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.tigerstudios.RPGCraft.CombatSystem.CombatSystem;
import net.tigerstudios.RPGCraft.CombatSystem.mgr_Mob;
import net.tigerstudios.RPGCraft.SpoutFeatures.GUIListener;
import net.tigerstudios.RPGCraft.SpoutFeatures.SpoutFeatures;
import net.tigerstudios.RPGCraft.gui.RPGMainWindow;
import net.tigerstudios.RPGCraft.listeners.listener_Block;
import net.tigerstudios.RPGCraft.listeners.listener_Entity;
import net.tigerstudios.RPGCraft.listeners.listener_Player;
import net.tigerstudios.RPGCraft.skills.FarmSystem;
import net.tigerstudios.RPGCraft.skills.MiningSystem;
import net.tigerstudios.RPGCraft.utils.MathMethods;
import net.tigerstudios.RPGCraft.utils.SQLManager;
import net.tigerstudios.RPGCraft.utils.custom.CCoin;
import net.tigerstudios.RPGCraft.utils.custom.CFood;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.keyboard.KeyBindingManager;
import org.getspout.spoutapi.keyboard.Keyboard;
import org.getspout.spoutapi.material.CustomItem;
import org.getspout.spoutapi.material.item.GenericCustomFood;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;


public class RPGCraft extends JavaPlugin{
	private static String name;
	private static String version;
	private static Plugin rpgPlugin;
	public static Logger log = null;	
	private static Server mcServer;	
	public static Economy econ = null;

		
	// Listener Classes to handle the events
	private static listener_Currency currencyListener = null;	
	public static CustomItem copperCoin, silverCoin, goldCoin;
	public static GenericCustomFood aleMug;
	public static int cp, sp, gp, aleID;
			
	public static String mainDirectory = "plugins" + File.separatorChar + "RPGCraft" + File.separatorChar;
	public static String logDirectory = mainDirectory + "logs" + File.separatorChar;
	public static String divider = "***************************************************************";
	public static String webBase = "http://tigerstudios.net/minecraft/";
	
	public static PermissionManager pexMan = null;
	public static boolean bCitizensLoaded = false;
	public static boolean bFactionsLoaded = false;
	
	// All items that need to have some sort of category associated to them.
	public static Map<String, String> itemCategories = new HashMap<String,String>();
		
	static FileConfiguration config = null;				

	@Override
	public void onDisable() {		
		// Shutdown all Manager classes
		try {
			mgr_Mob.shutdown();
			mgr_Player.SaveAllData();
			mgr_Player.logoutAllPlayers();
			MiningSystem.shutDown();
			SQLManager.closeConnection("RPGCraft");
		} catch (SQLException e1) { e1.printStackTrace();}		
	
		log.info(name + " " + version + " disabled.");
		log = null;
		this.setEnabled(false);
		super.onDisable();
	} // public void onDisable()
	
	
	@Override
	public void onEnable() {	
		super.onEnable();
		name = getDescription().getName();
		version = getDescription().getVersion();
					
		if(initializeRPGCraft() == false)
		{ 	// An error occurred while initiailizing the plugin.
			log.info(name + " " + version + " did not initialize.");
			log.info(name + " is not loaded.");
			log = null;
			this.setEnabled(false);
		}					
	} // public void onEnable()
	
	
	private boolean initializeRPGCraft()
	{
		log = Logger.getLogger("Minecraft.RPGCraft");
		mcServer = getBukkitServer();
		
		// Load the config file before anything else.  This way settings for other systems
		// can be added to the config
		loadConfig();
		
		new File(logDirectory).mkdirs();		// Make the log Directory
					
		setupCitizens();
		setupPermissions();	
		setupEconomy();
		if(!SQLManager.setupDatabase())
			return false;		
	
		// Load the Race data files
		RaceSystem.loadRaceFile("halfling.yml");	RaceSystem.loadRaceFile("human.yml");
		RaceSystem.loadRaceFile("elf.yml");			RaceSystem.loadRaceFile("dwarf.yml");
		
		SpoutFeatures.setup(this);
		copperCoin = new CCoin(this, "Copper Coin", config.getString("URL Images."+ "copperIcon"));
		silverCoin = new CCoin(this, "Silver Coin", config.getString("URL Images."+ "silverIcon"));
		goldCoin = new CCoin(this, "Gold Coin", config.getString("URL Images."+ "goldIcon"));
		aleMug = new CFood(this, "Cider Ale", webBase+"textures/mug1.png", 0);
						
		cp = copperCoin.getCustomId(); sp = silverCoin.getCustomId(); gp = goldCoin.getCustomId();
		aleID = aleMug.getCustomId();
		
		setupRecipies();
		mgr_Player.initialize(this);
		RPG_Character.initialize();
						
		currencyListener = new listener_Currency();
									
		rpgPlugin = this;
		MathMethods.setup();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new listener_Player(), this);
		pm.registerEvents(new listener_Block(rpgPlugin), this);
		pm.registerEvents(new listener_Entity(rpgPlugin), this);
		pm.registerEvents(new FarmSystem(), this);
		pm.registerEvents(new CombatSystem(), this);
		pm.registerEvents(new GUIListener(rpgPlugin), this);					
		
		// Bind the 'R' key to the RPGMainWindow interface.
		KeyBindingManager kMan = SpoutManager.getKeyBindingManager();
		kMan.registerBinding("net.tigerstudios.rpgcraft.mainwin",
			Keyboard.KEY_R, "Load the RPGCraft Main Window", new RPGMainWindow(), RPGCraft.getPlugin());
				
		return true;
	} // private boolean initializeRPGCraft()	
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,	String label, String[] args)
	{	if(sender instanceof Player)
		{	if(command.getName().equalsIgnoreCase("rpg"))
			{	if(CommandProcessor.rpgCommands((Player) sender, args))
					return true;
			}			
			if(currencyListener.currencyProcessor(sender, command, label, args))
				return true;
		} // if(sender instanceof Player)
	
		return false;
	} // public boolean onCommand(CommandSender sender, Command command, String label, String[] args)			
	
	
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
	
	
	public void setupCitizens()
	{	PluginManager pm = getServer().getPluginManager();
	    Plugin test = pm.getPlugin("Citizens");
	    if (test != null) {
	        System.out.println("[RPGCraft] ---> Successfully hooked into Citizens!");
	        bCitizensLoaded = true;
	    } else {
	        System.out.println("[RPGCraft] ---> Citizens isn't loaded.");
	        bCitizensLoaded = false;	    }
	    
	    test = pm.getPlugin("Factions");
	    if (test != null) {
	        System.out.println("[RPGCraft] ---> Successfully hooked into Factions!");
	        bFactionsLoaded = true;
	    } else {
	        System.out.println("[RPGCraft] ---> Factions isn't loaded.");
	        bFactionsLoaded = false;
	    }
	} // public void setupCitizens()
	
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    } //private boolean setupEconomy()
		
	public void loadConfig()
	{	config = this.getConfig();
				
		config.createSection("URL Images");
		config.addDefault("URL Images."+ "copperIcon", webBase+"textures/copper.png");
		config.addDefault("URL Images." + "silverIcon", webBase+"textures/silver.png");
		config.addDefault("URL Images." +"goldIcon", webBase+"textures/gold.png");
						
		config.options().copyDefaults(true);
		saveConfig();
	} // public void loadConfig()
	
	public static FileConfiguration getRPGConfig() { return config; }	
	
	private void setupRecipies()
	{
		ShapelessRecipe ciderAle = new ShapelessRecipe(new ItemStack(Material.FLINT, 1, (short)aleID));
		
		ciderAle.addIngredient(Material.WHEAT);
		ciderAle.addIngredient(Material.APPLE);
		ciderAle.addIngredient(Material.POTION);	
		ciderAle.addIngredient(Material.SUGAR);
				
		Bukkit.addRecipe(ciderAle);		
	} // private void setupRecipies()
	
	public static Plugin getPlugin() { return rpgPlugin; }
	public static Server getBukkitServer() { return mcServer; }	
} // public class RPGCraft extends JavaPlugin{
