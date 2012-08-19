package net.tigerstudios.RPGCraft;

import net.tigerstudios.RPGCraft.SpoutFeatures.SpoutFeatures;

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
					sender.sendMessage("Level "+rc.getLevel()+" "+rc.race);
					sender.sendMessage("Strength: "+rc.getStrength()+",  Dexterity: "+rc.getDexterity()+",  Constitution: "+rc.getConstitution()+",  Intelligence: "+rc.getIntelligence());			
					sender.sendMessage("Attack: "+rc.getAttack()+",  Defense: "+rc.getDefense()+",  Parry: "+rc.getParry());
					sender.sendMessage("Farming: "+rc.farming+",  Mining: "+rc.mining+",  Enchanting: "+rc.enchanting+", Alchemy: "+rc.alchemy);
				}				
				sender.sendMessage(RPGCraft.divider);
				
				return true;				
			} // if(args[0].equalsIgnoreCase("stats"))
		} // if(args.length == 1)
		
		if(args.length == 2)
		{
			if(args[0].equalsIgnoreCase("choose"))
			{
				String race = args[1];
				
				RPG_Player play = mgr_Player.getPlayer(sender.getName().hashCode());
				RPG_Character rc = new RPG_Character(RaceSystem.getRace(race), play.getAccountID());
								
				play.setCharacter(rc);
			
				SpoutFeatures.updateTitle(sender, null);
				
				sender.sendMessage("[§2RPG§f] You are now a "+race);
				sender.sendMessage("[§2RPG§f] Have fun!");				
			}
			return true;
		} // if(args.length == 2)
		
		return true;
	} // public static boolean rpgCommands(Player sender, String[] args)
	
} // public class CommandProcessor
