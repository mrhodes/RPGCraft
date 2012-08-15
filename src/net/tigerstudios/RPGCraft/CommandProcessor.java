package net.tigerstudios.RPGCraft;


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
		} // if(args.length == 0)
		
		if(args.length == 1)
		{
			if(args[0].equalsIgnoreCase("list"))
			{
				sender.sendMessage(RPGCraft.divider);
				RaceSystem.listRaces(sender);
				sender.sendMessage(RPGCraft.divider);
				return true;
			} // if(args[0].equalsIgnoreCase("list"))
			
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
		
		return true;
	} // public static boolean rpgCommands(Player sender, String[] args)
	
} // public class CommandProcessor
