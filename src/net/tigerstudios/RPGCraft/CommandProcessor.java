package net.tigerstudios.RPGCraft;

import org.bukkit.entity.Player;

public class CommandProcessor{
	
	public static boolean rpgCommands(Player sender, String[] args)
	{
		// When a player chooses a race then they will have 
		// access to the main world
		if(args.length == 0)
		{
			sender.sendMessage("Not enough arguments...");
			return true;
		} // if(args.length == 0)
		
		if(args.length == 1)
		{
			if(args[0].equalsIgnoreCase("list"))
			{
				RaceSystem.listRaces(sender);
				return true;
			} // if(args[0].equalsIgnoreCase("list"))
			
			if(args[0].equalsIgnoreCase("choose"))
			{
				sender.sendMessage("You must select a race to choose.");
				sender.sendMessage("use '/rpg list' to get a list of races and");
				sender.sendMessage("'/rpg choose <race name>' to choose your race.");
				return true;
			} // if(args[0].equalsIgnoreCase("choose"))
		} // if(args.length == 1)
		
		if(args.length == 2)
		{	if(args[0].equalsIgnoreCase("choose"))
			{
				String raceName = args[1];
				Race race = RaceSystem.getRace(raceName);
				
				if(race == null)
				{
					sender.sendMessage("That race cannot be found.  Try '/rpg list' for a list");
					sender.sendMessage("of available races.");
					return true;
				} // if(race == null)
				
				// Now setup a RPG_Character for this Race and assign it to the RPG_Player.
				RPG_Player rpgPlayer = mgr_Player.getPlayer(sender.getName().hashCode());
				RPG_Character rc = new RPG_Character(race, rpgPlayer.AccountID);
								
				rpgPlayer.setCharacter(rc);
				
				sender.sendMessage("You chose: " + raceName);
				return true;				
			} // if(args[0].equalsIgnoreCase("race"))
		} // if(args.length == 2)
		
		
		return true;
	} // public static boolean rpgCommands(Player sender, String[] args)
} // public class CommandProcessor
