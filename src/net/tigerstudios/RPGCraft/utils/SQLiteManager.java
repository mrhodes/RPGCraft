package net.tigerstudios.RPGCraft.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteManager {
	static String dbName;
	static Boolean bIsConnected;
	
	static Connection conn = null;
	static Statement statement = null;
	
	static public boolean initialize(String name)
	{	try {
			Class.forName("org.sqlite.JDBC");
			System.out.println("["+ name + "] --->   Successfully loaded 'org.sqlite.JDBC' driver.");
		} catch (ClassNotFoundException e) {
			System.out.println("["+ name + "] --->   Error loading sqlite library.  Please make sure you have the");
			System.out.println("["+ name + "] --->   correct sqlite library installed.");
			e.printStackTrace();
			return false;
		}
				
		return true;		
	} // static boolean initialize()
	
	
	static public void newConnection(String filename, String pluginName)
	{
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:"+filename);
			statement = conn.createStatement();
			statement.setQueryTimeout(30);
			
			dbName = filename;
			bIsConnected = true;
			System.out.println("["+pluginName+"] --->   Opened SQLite connection.");
		} catch (SQLException e) {
			
			e.printStackTrace();
		}		
		
	} // void newConnection(String filename)
	
	static public void closeConnection(String pluginName) throws SQLException
	{
		
		if(conn != null)
		{
			if(conn.isClosed() == false)
			{
				statement.close();
				conn.close();
				
				System.out.println("["+pluginName+"] --->   Closed SQLite connection.");
				return;
			}
		}
	} // static void closeConnection()
	
	static public ResultSet SQLQuery(String query)
	{
		ResultSet rs = null;
				
		if(bIsConnected)
		{ 
			try {
				rs = statement.executeQuery(query);
			} catch (SQLException e) {
				rs = null;
			}			
		}	
		
		return rs;
	} // static public ResultSet SQLQuery(String query)
	
	static public void SQLUpdate(String query)
	{
		if(bIsConnected)
		{ 
			try {
				statement.executeUpdate(query);
				
			} catch (SQLException e) {
				System.out.println(e.getLocalizedMessage());
			}			
		}	
		
	} // static public ResultSet SQLQuery(String query)	
	
	
	// Check for the existance of the given table
	static public boolean TableExists(String id, String pluginName)
	{
		String query = "select * from " + id;
		try {
			statement.executeQuery(query);
		} catch (SQLException e) {
			System.out.println("["+pluginName+"] --->   Table: "+id+" does not exist.");			
			return false;
		}
		
		return true;
	} // static public boolean TableExists(String id)
	
	
}
