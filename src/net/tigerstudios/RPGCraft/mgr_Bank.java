package net.tigerstudios.RPGCraft;

import java.util.ArrayList;
import java.util.List;

import net.tigerstudios.RPGCraft.utils.PropertiesFile;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class mgr_Bank {
	private static List<RPG_Bank> bankList = new ArrayList<RPG_Bank>();
	private static Plugin rpgPlugin = null;
	private static Server rpgServer = null;
	
	public static boolean newBank(String owner, String bankName)
	{
		// Make sure the owner meets the criteria for setting up a 
		// new bank.
				
		// find out if this bank name exists already
		for(RPG_Bank bank: bankList)
		{
			if(bank.getName().equalsIgnoreCase(bankName))
			{
				// This name already exists... Tell the player and 
				// return
				RPG_Player p = mgr_Player.getPlayer(owner);
				if(p!=null)
				{	p.GetPlayer().sendMessage("A bank with this name already exists.");
					p.GetPlayer().sendMessage("Please try again with another name.");
				}
				return false;
			} // if(bank.getName().equalsIgnoreCase(bankName))
		}
		// The bank does not exist.  Now create a new one
		RPG_Bank newBank = new RPG_Bank(owner, bankName);
		
		bankList.add(newBank);
		return true;
	}
	public static RPG_Bank getBank(Player p, String owner, String name)
	{
		RPG_Bank	rpgBank = null;
		
		return rpgBank;
	} // public static RPG_Bank getBank(Player p, String owner, String name)
	public void saveAllBanks()
	{
		PropertiesFile pfBank = null;
				
		for(RPG_Bank rpgBank: bankList)
		{
			pfBank = new PropertiesFile(rpgBank.getName()+".txt");
			rpgBank.saveBank(pfBank);
			
			pfBank = null;			
		} // for(RPG_Bank rpgBank: bankList)				
	} // public void saveAllBanks()
	
	public mgr_Bank()
	{
		
	}
		
}
