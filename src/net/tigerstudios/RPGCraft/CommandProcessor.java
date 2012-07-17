package net.tigerstudios.RPGCraft;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.tigerstudios.RPGCraft.utils.SQLiteManager;
import net.tigerstudios.RPGCraft.utils.SpoutFeatures;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandProcessor{
	
	public static boolean rpgCommands(Player sender, String[] args)
     	{
		// When a player chooses a race then they will have 
		// access to the main world
		if(args.length == 0)
		{
			sender.sendMessage(RPGCraft.divider);
			sender.sendMessage("RPGCraft Help");
			sender.sendMessage("The options for the rpg command are:");
			sender.sendMessage("   - list:  Lists the available races.");
			sender.sendMessage("   - choose <race>: Choose a race to become.");
			sender.sendMessage("   - stats: Displays your character stats.");
			sender.sendMessage("Example:  '/rpg list' or '/rpg choose elf'");
			sender.sendMessage(RPGCraft.divider);
			return true;
		} // if(args.length == 0)q
		
		if(args.length == 1)
		{
			if(args[0].equalsIgnoreCase("list"))
			{
				sender.sendMessage(RPGCraft.divider);
				RaceSystem.listRaces(sender);
				sender.sendMessage(RPGCraft.divider);
				return true;
			} // if(args[0].equalsIgnoreCase("list"))
			
			if(args[0].equalsIgnoreCase("choose"))
			{
				sender.sendMessage(RPGCraft.divider);
				sender.sendMessage("[§2RPG§f] You must select a race to choose.");
				sender.sendMessage("[§2RPG§f] use '/rpg list' to get a list of races and");
				sender.sendMessage("[§2RPG§f] '/rpg choose <race name>' to choose your race.\n");
				sender.sendMessage(RPGCraft.divider);
				return true;
			} // if(args[0].equalsIgnoreCase("choose"))
			
			if(args[0].equalsIgnoreCase("stats"))
			{
				RPG_Character rc = mgr_Player.getCharacter(sender);
				
				sender.sendMessage(RPGCraft.divider);
				if(rc == null)
				{
					sender.sendMessage("[§2RPG§f] You have not yet choosen a character race.");
					sender.sendMessage("[§2RPG§f] Please choose a race to begin your role playing");
					sender.sendMessage("[§2RPG§f] adventures.");					
				} // if(rc == null)
				else
				{
					sender.sendMessage("Level "+rc.level+" "+rc.race);
					sender.sendMessage("Strength: "+rc.strength+",  Dexterity: "+rc.dexterity+",  Constitution: "+rc.constitution+",  Intelligence: "+rc.intelligence);			
					sender.sendMessage("Attack: "+rc.attack+",  Defense: "+rc.defense+",  Parry: "+rc.parry);
					sender.sendMessage("Farming: "+rc.farming+",  Mining: "+rc.mining+",  Enchanting: "+rc.enchanting+", Alchemy: "+rc.alchemy);
				}				
				sender.sendMessage(RPGCraft.divider);
				
				return true;				
			} // if(args[0].equalsIgnoreCase("stats"))
		} // if(args.length == 1)
		
		if(args.length == 2)
		{	if(args[0].equalsIgnoreCase("choose"))
			{
				String raceName = args[1];
				
				Race race = RaceSystem.getRace(raceName.toLowerCase());
				
				sender.sendMessage(RPGCraft.divider);
				
				if(race == null)
				{
					sender.sendMessage("[§2RPG§f] That race cannot be found.  Try '/rpg list' for a list");
					sender.sendMessage("[§2RPG§f] of available races.");
					sender.sendMessage(RPGCraft.divider);
					return true;
				} // if(race == null)
				
				// Now setup a RPG_Character for this Race and assign it to the RPG_Player.
				RPG_Player rpgPlayer = mgr_Player.getPlayer(sender.getName().hashCode());
				if(rpgPlayer.getCharacter() != null)
				{
					sender.sendMessage("[§2RPG§f] You have already selected a race.");
					sender.sendMessage("[§2RPG§f] Currently you are a "+rpgPlayer.getCharacter().race);
					sender.sendMessage(RPGCraft.divider);
					return true;
				}
				
				RPG_Character rc = new RPG_Character(race, rpgPlayer.AccountID);
								
				rpgPlayer.setCharacter(rc);
				SpoutFeatures.updateTitle(sender, null);
				
				sender.sendMessage("[§2RPG§f] You are now a "+raceName);
				sender.sendMessage("[§2RPG§f] Have fun!");
				sender.sendMessage(RPGCraft.divider);
				return true;				
			} // if(args[0].equalsIgnoreCase("choose"))
		
			// /rpg reset <player> allows Mods and above the ability to reset someones
			// race.
			if(args[0].equalsIgnoreCase("reset") && RPGCraft.pexMan.has(sender, "rpgcraft.mod"))
			{
				String sName = args[1];
				Player p = Bukkit.getPlayer(sName);
				if(p == null)
				{
					sender.sendMessage("[§2RPG§f] Are you sure this player is online?");
					return true;
				}
				
				// Find this players entry in the database, and remove their character data.
				String query = "Select account_id from Accounts WHERE mc_Name = '"+sName+"'";
				ResultSet rs;
				
				int AccID = 0;
				rs = SQLiteManager.SQLQuery(query);
				try{
					if(rs.next())
					{	AccID = rs.getInt("account_id");
					}
				}catch (SQLException e) {e.printStackTrace();}
				
				if(AccID > 0)
				{	query = "delete from Characters Where account_id = "+AccID;
					SQLiteManager.SQLUpdate(query);
					sender.sendMessage("[§2RPG§f] Players character has been reset.");
					
					RPG_Player rpgPlayer = mgr_Player.getPlayer(p.getName().hashCode());
					p.sendMessage("[§2RPG§f] Your race has been reset by "+sender.getDisplayName());
					p.sendMessage("[§2RPG§f] You can now choose a new race.");
					rpgPlayer.resetCharacter();		
					return true;
				}	
				// reset player's experience bar
				p.setExp(0);
				p.setLevel(0);
			} // if(args[0].equalsIgnoreCase("reset") && RPGCraft.pexMan.has(sender, "rpgcraft.mod"))q
		
		} // if(args.length == 2)	
		
		return true;
	} // public static boolean rpgCommands(Player sender, String[] args)
} // public class CommandProcessor
