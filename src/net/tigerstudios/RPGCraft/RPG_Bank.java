// ----------------------------------------------------------------------------
// RPG_Bank()
//
// 
// ----------------------------------------------------------------------------

package net.tigerstudios.RPGCraft;

import net.tigerstudios.RPGCraft.utils.PropertiesFile;

public class RPG_Bank {
	private String owner = null;
	private String bankName = null;
	private String town = "";
	private String welcomeMessage = "";
	private String farewellMessage = "";
	
	// Banks Current Balance
	int gold = 0;
	int silver = 0;
	int copper = 0;	
	
	public RPG_Bank(String owner, String name)
	{
		this.owner = owner;
		bankName = name;
	} // public RPG_Bank(String owner, String name)
	
	
	public String getName() { return bankName; }
	public void setName(String name)
	{
// TODO: program some checks to make sure this bank name isn't taken already
		this.bankName = name;
	}
	
	public void saveBank(PropertiesFile pf)
	{
		
		
	}

	public void setTown(String town) {this.town = town;}
	public String getTown() {return town;}	
}
