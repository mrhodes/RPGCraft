package net.tigerstudios.RPGCraft.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import net.tigerstudios.RPGCraft.RPGCraft;

import org.bukkit.configuration.file.YamlConfiguration;

public class SQLManager {
	static String dbName;
	static Boolean bIsConnected = false;	
	static Connection conn = null;
	static Statement statement = null;
	static YamlConfiguration config;
	
	static public void closeConnection(String pluginName) throws SQLException
	{		
		if(conn != null)
		{	if(conn.isClosed() == false)
			{	statement.close();
				conn.close();
				
				System.out.println("["+pluginName+"] --->   Closed SQLite connection.");
				return;
			}
		}
	} // static void closeConnection()
	
	
	static public boolean initialize(final String name)
	{	try {
			Class.forName("org.sqlite.JDBC");
			System.out.println("["+ name + "] --->   Successfully loaded 'org.sqlite.JDBC' driver.");
		} catch (ClassNotFoundException e) {
			System.out.println("["+ name + "] --->   Error loading sqlite library.  Please make sure you have the");
			System.out.println("["+ name + "] --->   correct sqlite library installed.");
			e.printStackTrace();
			return false;
		}		
	
		/*config = new YamlConfiguration();
		try {
			config.load(new File(RPGCraft.mainDirectory+"db.yml"));
		} catch (FileNotFoundException e) {	e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();}
		catch (InvalidConfigurationException e) {e.printStackTrace();}
		*/
		
		return true;		
	} // static public boolean initialize(String name)
	
	
	static public void newConnection(String filename, String pluginName)
	{
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:"+filename);
			statement = conn.createStatement();
			statement.setQueryTimeout(30);
			
			dbName = filename;
			bIsConnected = true;
			System.out.println("["+pluginName+"] --->   Opened SQLite connection.");
		} catch (SQLException e) {	e.printStackTrace(); }			
	} // void newConnection(String filename)
	
	// ------------------------------------------------------------------------------------------
	// Setup the Database tables and if needed make alterations to existing tables
	public static boolean setupDatabase()
	{
		Logger log = RPGCraft.log;
		if(SQLManager.initialize("RPGCraft")==false)
		{	log.info("[RPGCraft]   Error when loading the SQLite library. RPGCraft cannot");
			log.info("[RPGCraft]   run without this.  Please make sure you have sqlite-jdbc-3.7.2.jar");
			log.info("[RPGCraft]   in the root of the server directory.");
			return false;
		} // if(SQLiteManager.initialize()==false)		
		
		SQLManager.newConnection(RPGCraft.mainDirectory+"RPGCraftDB.db", "RPGCraft");
		
		// Setup the account Table...
		// When a new user joins the server this table will be updated with
		// that new users info.  This is not character related.
		if(TableExists("Accounts", "RPGCraft") == false)
		{	log.info("[RPGCraft] --->   Creating Accounts table.");
			SQLUpdate("create table Accounts ("+
					"account_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
					"mc_Name VARCHAR(24), "+
					"joined DATE, "+
					"lastOnline DATE, "+
					"totalTimeOnline TIME"+
					");");
		} // if(SQLiteManager.TableExists("accounts", "RPGCraft") == false)
		
		// Setup the playerData Table..
		if(TableExists("Characters", "RPGCraft") == false)
		{	log.info("[RPGCraft] --->   Creating Characters table.");
			SQLUpdate("create table Characters ("+
					"char_id INTEGER PRIMARY KEY AUTOINCREMENT," + 	// Primary key
					"account_id SMALLINT UNSIGNED NOT NULL," +		// Foreign key
					"name varchar(16) NOT NULL," +
					"namePrefix varchar(32), 	nameSuffix varchar(64), "+
					"race varchar(10), level tinyint, experience int, " +
					"strength int, dexterity int, constitution int, intelligence int, " +
					"statPointsUsed int, statPointsTotal int, " +
					"attack int, defense int, parry int," +
					"mine int, mineSkillBar float, " +
					"farm int, farmSkillBar float, " +
					"blacksmith int, blacksmithSkillBar float, "+
					"enchant int, enchantSkillBar float, " +
					"alchemy int, alchemySkillBar float, " +
					"cook int, cookSkillBar float, " +
					"fish int, fishSkillBar float, " +
					"trade int, tradeSkillBar float, "+
					"alcoholTolerance int, thrist int"+										
					");");
		} // if(SQLiteManager.TableExists("characters", "RPGCraft") == false)
				
	// ----------------------------------------------------------------------
	// The following 2 data tables will hold custom information about the
	// Items available ingame.  These tables can be added to, changed, and 
	// have items removed from them completely within game by someone with
	// the "rpgcraft.admin" permission.
		// Table needed to store "ItemType" ids
		if(TableExists("itemListing", "RPGCraft") == false)
		{	log.info("[RPGCraft] --->   Creating itemListing.");
			SQLUpdate("create table itemListing ("+
					"id INTEGER PRIMARY KEY AUTOINCREMENT,"+
					"itemID INTEGER, itemData INTEGER, catID INTEGER"+
					");");
		} // if(SQLManager.TableExists("itemListing", "RPGCraft") == false)		
		
		// Table to store Item Categories
		// Consist of Id, Category Name, Caegory Description
		if(TableExists("itemCategory", "RPGCraft") == false)
		{	log.info("[RPGCraft] --->   Creating itemCategory table.");
			SQLUpdate("create table itemCategory ("+
					"id INTEGER PRIMARY KEY AUTOINCREMENT,"+
					"name VARCHAR(16), description VARCHAR(128));");
		} // if(SQLManager.TableExists("itemCategory", "RPGCraft") == false)
	// ------------------------------------------------------------------------
				
		/* --------------------------------------------------------------------
		 * Spawn Groups
		 * An Admin or Mod can create groups of LivingEntities to be spawned 
		 * at once.  These groups will consist of just the Entities themselves
		 * and not contain lvl information.  */
		if(TableExists("spawnGroup", "RPGCraft") == false)
		{	log.info("[SQL] --->   Creating spawnGroup table.");
			SQLUpdate("create table spawnGroup("+
					"id INTEGER PRIMARY KEY AUTOINCREMENT,"+
					"name VARCHAR(16), description VARCHAR(128));");
		} // if(SQLManager.TableExists("spawnGroup", "RPGCraft") == false)
		 
		
		
								
		return true;
	} // private void setupDatabase()		
	
	static public ResultSet SQLQuery(final String query)
	{
		ResultSet rs = null;
				
		if(bIsConnected)
		{ 	try {
				rs = statement.executeQuery(query);
			} catch (SQLException e) { System.out.println(e.getLocalizedMessage()); rs = null; }			
		} // if(bIsConnected)
		
		return rs;
	} // static public ResultSet SQLQuery(String query)
	
	
	static public void SQLUpdate(final String query)
	{	if(bIsConnected)
		{ 	try {
				statement.executeUpdate(query);			
			} catch (SQLException e) { System.out.println(e.getLocalizedMessage());	}			
		} // if(bIsConnected)
		
	} // static public ResultSet SQLQuery(String query)	
	
	
	// Check for the existance of the given table
	static public boolean TableExists(String id, String pluginName)
	{	String query = "select * from " + id;
		try {
			statement.executeQuery(query);
		} catch (SQLException e) {
			System.out.println("["+pluginName+"] --->   Table: "+id+" does not exist.");			
			return false;
		}		
		return true;
	} // static public boolean TableExists(String id)
} // public class SQLiteManager
