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

import net.milkbowl.vault.economy.Economy;
import net.tigerstudios.RPGCraft.CombatSystem.CombatSystem;
import net.tigerstudios.RPGCraft.SpoutFeatures.GUIListener;
import net.tigerstudios.RPGCraft.SpoutFeatures.SpoutFeatures;
import net.tigerstudios.RPGCraft.gui.RPGMainWindow;
import net.tigerstudios.RPGCraft.listeners.listener_Block;
import net.tigerstudios.RPGCraft.listeners.listener_Entity;
import net.tigerstudios.RPGCraft.listeners.listener_Player;
import net.tigerstudios.RPGCraft.skills.FarmSystem;
import net.tigerstudios.RPGCraft.skills.MiningSystem;
import net.tigerstudios.RPGCraft.utils.MathMethods;
import net.tigerstudios.RPGCraft.utils.RPGCraftTimer;
import net.tigerstudios.RPGCraft.utils.RandomGen;
import net.tigerstudios.RPGCraft.utils.SQLManager;
import net.tigerstudios.RPGCraft.utils.custom.CCoin;


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
	private static Server mcServer;	
	private static int timerID = 0;
	private static RPGCraftTimer timer;	
	public static Economy econ = null;
	
	public static Logger log = null;		
			
	// Listener Classes to handle the events
	public static listener_Currency currencyListener = null;	
	public static CustomItem copperCoin, silverCoin, goldCoin;
	public static GenericCustomFood aleMug;
	public static int cp, sp, gp, aleID;
			
	public static String mainDirectory = "plugins" + File.separatorChar + "RPGCraft" + File.separatorChar;
	public static String logDirectory = mainDirectory + "logs" + File.separatorChar;
	public static String divider = "�5***************************************************************";
	public static String webBase = "http://tigerstudios.net/minecraft/";
	
	public static PermissionManager pexMan = null;		
	public static FileConfiguration config = null;	
	
	public static Server getBukkitServer() { return mcServer; }		
	public static Plugin getPlugin() { return rpgPlugin; }	
	public static FileConfiguration getRPGConfig() { return config; }
	
	public static boolean bDebugMessages = false;
	
	
	@SuppressWarnings("deprecation")
	private boolean initializeRPGCraft()
	{
		log = this.getLogger();
		mcServer = getServer();
		
		// Load the config file before anything else.  This way settings for other systems
		// can be added to the config
		loadConfig();
		bDebugMessages = config.getBoolean("Misc.Debug");
		log.info("Debug Messages are "+ (bDebugMessages ? "on" :"off" ));
		
		new File(logDirectory).mkdirs();		// Make the log Directory
					
		setupPermissions();	
		if(!SQLManager.setupDatabase())
			return false;	
		
		// Setup the Vault Economy feature
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            econ = economyProvider.getProvider();
        }
		
	/*	if(getServer().getPluginManager().getPlugin("Citizens") == null || getServer().getPluginManager().getPlugin("Citizens").isEnabled() == false)
		{	log.log(Level.SEVERE, "Citizens 2.0 not found or not enabled");
		
		}*/
	
		// Load the Race data files
		RaceSystem.loadRaces();
		
		SpoutFeatures.setup(this);
		
		copperCoin = new CCoin(this, "Copper Coin", config.getString("URL Images."+ "copperIcon"));
		silverCoin = new CCoin(this, "Silver Coin", config.getString("URL Images."+ "silverIcon"));
		goldCoin = new CCoin(this, "Gold Coin", config.getString("URL Images."+ "goldIcon"));
		//aleMug = new CFood(this, "Cider Ale", webBase+"textures/mug1.png", 0);
						
		cp = copperCoin.getCustomId(); sp = silverCoin.getCustomId(); gp = goldCoin.getCustomId();
		//aleID = aleMug.getCustomId();
		
		setupRecipies();
		mgr_Entity.initialize();
		RPG_Character.initialize();
		mgr_Player.initialize(this);		
						
		currencyListener = new listener_Currency();
									
		rpgPlugin = this;
		MathMethods.setup();
		RandomGen.initialize(System.currentTimeMillis());
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new listener_Player(), this);
		pm.registerEvents(new listener_Block(), this);
		pm.registerEvents(new listener_Entity(), this);
		pm.registerEvents(new FarmSystem(), this);
		pm.registerEvents(new CombatSystem(), this);
		pm.registerEvents(new GUIListener(rpgPlugin), this);					
		
		// Bind the 'R' key to the RPGMainWindow interface.
		KeyBindingManager kMan = SpoutManager.getKeyBindingManager();
		kMan.registerBinding("net.tigerstudios.rpgcraft.mainwin",
			Keyboard.KEY_R, "Load the RPGCraft Main Window", new RPGMainWindow(), RPGCraft.getPlugin());
				
		// Set up the repeating 'tick listener'
		timer = new RPGCraftTimer();
		mcServer.getScheduler().scheduleAsyncRepeatingTask(this, timer, 20L, 100L);
		
		return true;
	} // private boolean initializeRPGCraft()	
	
	
	public void loadConfig()
	{	config = this.getConfig();
				
		config.createSection("URL Images");
		config.addDefault("URL Images."+ "copperIcon", webBase+"textures/copper.png");
		config.addDefault("URL Images." + "silverIcon", webBase+"textures/silver.png");
		config.addDefault("URL Images." +"goldIcon", webBase+"textures/gold.png");
						
		config.options().copyDefaults(true);
		saveConfig();
	} // public void loadConfig()
	
			
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
	
	@Override
	public void onDisable() {		
		// Shutdown all Manager classes
		try {
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
			return;
		}		
	} // public void onEnable()	
	
	public void setupPermissions() {
		Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("PermissionsEx");

		if (RPGCraft.pexMan == null) {
			if (permissionsPlugin != null) {
				RPGCraft.pexMan = PermissionsEx.getPermissionManager();
				log.info(" --> Permissions plugin detected.");
			} else {
				log.info(" --> Permissions plugin not detected, defaulting to Bukkit's built-in system.");
			}
		}
	} // public void setupPermissions()	
	private void setupRecipies()
	{
		ShapelessRecipe ciderAle = new ShapelessRecipe(new ItemStack(Material.FLINT, 1, (short)aleID));
		
		ciderAle.addIngredient(Material.WHEAT);
		ciderAle.addIngredient(Material.APPLE);
		ciderAle.addIngredient(Material.POTION);	
		ciderAle.addIngredient(Material.SUGAR);
				
		Bukkit.addRecipe(ciderAle);		
	} // private void setupRecipies()	
} // public class RPGCraft extends JavaPlugin{
