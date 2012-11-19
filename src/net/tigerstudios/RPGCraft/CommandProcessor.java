package net.tigerstudios.RPGCraft;

import net.tigerstudios.RPGCraft.SpoutFeatures.SpoutFeatures;

import org.bukkit.entity.Player;

public class CommandProcessor{
		
	public static boolean rpgCommands(Player sender, String[] args)
    {
		// When a player chooses a race then they will have 
		// access to the main world
						
		if(args.length == 0)
		{	sender.sendMessage(RPGCraft.divider);
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
		{	if(args[0].equalsIgnoreCase("list"))
			{	sender.sendMessage(RPGCraft.divider);
				RaceSystem.listRaces(sender);
				sender.sendMessage(RPGCraft.divider);
				return true;
			} // if(args[0].equalsIgnoreCase("list"))
			
			if(args[0].equalsIgnoreCase("stats"))
			{				
				RPG_Character rc = mgr_Player.getCharacter(sender);
				
				sender.sendMessage("§5"+RPGCraft.divider);
				if(rc == null)
				{	sender.sendMessage("[§2RPG§f] You have not yet choosen a character race.");
					sender.sendMessage("[§2RPG§f] Please choose a race to begin your role playing");
					sender.sendMessage("[§2RPG§f] adventures.");					
				} // if(rc == null)
				else
				{	sender.sendMessage("§l§2Level §f"+rc.getLevel()+" §2"+rc.race);
					sender.sendMessage("§aArmor Rating§f: "+rc.getArmorClass()+"§f, §aCurrent Weapon Rating§f: "+rc.getWeaponDamage());
					sender.sendMessage("§aStrength§f: "+rc.getStrength()+"§f,  §aDexterity§f: "+rc.getDexterity()+"§f,  §aConstitution§f: "+rc.getConstitution()+"§f,  §aIntelligence§f: "+rc.getIntelligence());			
					sender.sendMessage("§aAttack§f: "+rc.getAttack()+"§f,  §aDefense§f: "+rc.getDefense()+"§f,  §aParry§f: "+rc.getParry());
					sender.sendMessage("§aFarming§f: "+rc.farming+"§f,  §aMining§f: "+rc.mining+"§f,  §aEnchanting§f: "+rc.enchanting+"§f, §aAlchemy§f: "+rc.alchemy);
					
					// If the player has any unspent points to use let them know
					if((rc.statPtsTotal - rc.statPtsUsed) > 0)
					{	sender.sendMessage("\nYou have a total of "+(rc.statPtsTotal - rc.statPtsUsed)+" unspent bonus points.");
						sender.sendMessage("To increase a stat, type '/§arpg §aincrease §f<§dstat§f>' Replace stat with one of the following:");
						sender.sendMessage("  1: §aStrength:     §dstr\n  §f2: §aDexterity:    §ddex\n");
						sender.sendMessage("  3: §aIntelligence: §dint\n  §f4: §aConstitution: §dcon");
					} // if((rc.statPtsTotal - rc.statPtsUsed) > 0)
				}				
				sender.sendMessage("§5"+RPGCraft.divider);
				
				return true;				
			} // if(args[0].equalsIgnoreCase("stats"))
		} // if(args.length == 1)
		
		if(args.length == 2)
		{
			if(args[0].equalsIgnoreCase("increase"))
			{	String stat = args[1];
				RPG_Character rpgChar = mgr_Player.getCharacter(sender);
				if(rpgChar == null)
				{	sender.sendMessage("[§2RPG§f] You have not yet choosen a character race.");
					sender.sendMessage("[§2RPG§f] Please choose a race to begin your role playing");
					sender.sendMessage("[§2RPG§f] adventures.");
					return true;
				}
				if((rpgChar.statPtsTotal - rpgChar.statPtsUsed) > 0)
				{
					if(stat.equalsIgnoreCase("str") || stat.equalsIgnoreCase("1")) 
					{ 	rpgChar.setStrength(rpgChar.getStrength() + 1);
						rpgChar.statPtsUsed++;
						sender.sendMessage("[§2RPG§f] You've increased your Strength! Strength is now "+rpgChar.getStrength());
						return true;				
					}
					
					if(stat.equalsIgnoreCase("dex") || stat.equalsIgnoreCase("2")) 
					{ 	rpgChar.setDexterity(rpgChar.getDexterity() + 1);
						rpgChar.statPtsUsed++;
						sender.sendMessage("[§2RPG§f] You've increased your Dexterity! It is now "+rpgChar.getDexterity());
						return true; 
					}					
					if(stat.equalsIgnoreCase("int") || stat.equalsIgnoreCase("3"))
					{	rpgChar.setIntelligence(rpgChar.getIntelligence() + 1); 
						rpgChar.statPtsUsed++;
						sender.sendMessage("[§2RPG§f] You've increased your Intelligence! It is now "+rpgChar.getIntelligence());
						return true; 						
					}
					
					if(stat.equalsIgnoreCase("con") || stat.equalsIgnoreCase("4"))
					{	rpgChar.setConstitution(rpgChar.getConstitution() + 1);
						rpgChar.statPtsUsed++;
						sender.sendMessage("[§2RPG§f] You've increased your Constitution! It is now "+rpgChar.getConstitution());
						return true; 
					}
				
					sender.sendMessage("[§2RPG§f] You need to type the correct option to increase your stats.");
					sender.sendMessage("  1: §aStrength:     §dstr\n  §f2: §aDexterity:    §ddex\n");
					sender.sendMessage("  3: §aIntelligence: §dint\n  §f4: §aConstitution: §dcon");				
					
					return true;
				} // if((rpgChar.statPtsTotal - rpgChar.statPtsUsed) > 0)
				
				sender.sendMessage("[§2RPG§f] You can't increase any stats right now.  You need to level up first.");
				
				return true;
			} // if(args[0].equalsIgnoreCase("increase"))
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
