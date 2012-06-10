package net.tigerstudios.RPGCraft;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.inventory.SpoutItemStack;

public class listener_Currency implements Listener {
	private Server rpgServer = null;
	private RPG_Player rpgPlayer = null;
		
	// public void onPlayerCommandPreprocess(PlayerChatEvent event)
	
	public boolean currencyProcessor(CommandSender sender, Command command, String label, String[] cmd)
	{
		Player p = rpgServer.getPlayer(sender.getName());
		
		if(command.getName().equalsIgnoreCase("balance") || command.getName().equalsIgnoreCase("bal"))
		{
			String playerName = p.getName();
						
			if(!(RPGCraft.pexMan.has(p, "rpgcraft.money")))
			{
				p.sendMessage("[§2RPG§f] Sorry, but you do not have access to the currency");
				p.sendMessage("[§2RPG§f] commands. Speak to a server admin to gain access.");
				return true;				
			} // if(!(RPGCraft.Permissions.has(p, "rpgcraft.money")))
			
			if(cmd.length == 1 && RPGCraft.pexMan.has(p, "rpgcraft.money.mods"))
			{
				playerName = cmd[0];
				rpgPlayer = mgr_Player.getPlayer(playerName.hashCode());
				if(rpgPlayer == null)
				{	p.sendMessage("[§2RPG§f] The player cannot be found.  Either they are not online");
					p.sendMessage("[§2RPG§f] or there may be a spelling mistake.");
					return true;
				} // if(rpgPlayer == null)
			} // if(cmd.length == 2 && RPGCraft.Permissions.has(p, "rpgcraft.money.mods"))
			
			if(cmd.length == 0)
			{	playerName = p.getName();
				rpgPlayer = mgr_Player.getPlayer(playerName.hashCode());
			} // if(cmd.length == 1)			
			
			p.sendMessage("[§2RPG§f] Balance: §6"+rpgPlayer.getCharacter().getGold()+" Gold§f, §7"+rpgPlayer.getCharacter().getSilver()+" Silver§f, §c"+rpgPlayer.getCharacter().getCopper()+" Copper§f.");
			if(cmd.length == 0 && RPGCraft.pexMan.has(p, "rpgcraft.money.mods"))
			{
				p.sendMessage("[§2RPG§f] You may also check other players balances by adding");
				p.sendMessage("[§2RPG§f] their name.");
				p.sendMessage("[§2RPG§f] Ex: §2/balance §f<§bplayername§f>");
			}
			return true;
		} // if(cmd[0].equalsIgnoreCase("/balance") || cmd[0].equalsIgnoreCase("/bal"))
		
		
		if(command.getName().equalsIgnoreCase("deposit"))
		{
			if(!(RPGCraft.pexMan.has(p, "rpgcraft.money")))
			{
				p.sendMessage("[§2RPG§f] Sorry, but you do not have access to the currency");
				p.sendMessage("[§2RPG§f] commands. Speak to a server admin to gain access.");
				return true;				
			} // if(!(RPGCraft.Permissions.has(p, "rpgcraft.money")))			
			
			
			if(p.getGameMode() == GameMode.CREATIVE)
			{
				p.sendMessage("[§2RPG§f] Nice try " + p.getDisplayName() + ". ");
				p.sendMessage("[§2RPG§f] This attempt will be logged, and admins will");
				p.sendMessage("[§2RPG§f] be notified.");
				return true;
			} // if(p.getGameMode() == GameMode.CREATIVE)
			
			
			p.sendMessage("[§2RPG§f] To use the Deposit command please \"Use\" the");
			p.sendMessage("[§2RPG§f] coin you want to deposit.");
			
			// Set the timer for 15 seconds
			rpgPlayer = mgr_Player.getPlayer(p.getName().hashCode());
			rpgPlayer.lTimer = System.currentTimeMillis(); 
			p.sendMessage("[§2RPG§f] Right click with the coins in your hand that you want");
			p.sendMessage("[§2RPG§f] to deposit");		
			
			return true;
		} // if(command.getName().equalsIgnoreCase("deposit"))
		
				
		if(command.getName().equalsIgnoreCase("withdraw"))
		{
			if(!(RPGCraft.pexMan.has(p, "rpgcraft.money")))
			{
				p.sendMessage("[§2RPG§f] Sorry, but you do not have access to the currency");
				p.sendMessage("[§2RPG§f] commands. Speak to a server admin to gain access.");
				return true;				
			} // if(!(RPGCraft.Permissions.has(p, "rpgcraft.money")))
			
			if(p.getGameMode() == GameMode.CREATIVE)
			{
				p.sendMessage("[§2RPG§f] Sorry, but no withdraws allowed while");
				p.sendMessage("[§2RPG§f] in creative mode.");
				return true;
			} // if(p.getGameMode() == GameMode.CREATIVE)
			
			if(cmd.length == 0 || cmd.length > 3)
			{	// TODO: Give a more detailed help description
				p.sendMessage("[§2RPG§f] Usage -> /withdraw <§6gold§f> <§7silver§f> <§ccopper§f>.");
				return true;
			}
			
			rpgPlayer = mgr_Player.getPlayer(p.getName().hashCode());
			
// TODO: Add error checking here to make sure all values are valid
// TODO: Add ability to append G, S, or C to amount and let player enter what they want instead
			//of requiring all 3 values.  Eg. /withdraw 10S
			int gp, sp, cp;
			gp = Integer.parseInt(cmd[0]);
			sp = Integer.parseInt(cmd[1]);
			cp = Integer.parseInt(cmd[2]);
			
			if(rpgPlayer.getCharacter().getTotalCopper() < ( cp + (sp * 100) + (gp * 10000)))
			{ 
				p.sendMessage("[§2RPG§f] Sorry, but you do not have enough coin.");
				p.sendMessage("[§2RPG§f] Balance: §6"+rpgPlayer.getCharacter().getGold()+" Gold§f, §7"+rpgPlayer.getCharacter().getSilver()+" Silver§f, §c"+rpgPlayer.getCharacter().getCopper()+" Copper§f.");						
				return true;
			}
			if(cp > 0)
			{				
				p.getWorld().dropItem(p.getLocation(), new SpoutItemStack(RPGCraft.copperCoin, cp));
								
			}
			if(sp > 0)
				p.getWorld().dropItem(p.getLocation(), new SpoutItemStack(RPGCraft.silverCoin, sp));
			if(gp > 0)
				p.getWorld().dropItem(p.getLocation(), new SpoutItemStack(RPGCraft.goldCoin, gp));
			
			
			int totalcp = cp + (sp * 100) + (gp * 10000);
			rpgPlayer.getCharacter().removeCopper(totalcp, p);
			p.sendMessage("[§2RPG§f] Balance: §6"+rpgPlayer.getCharacter().getGold()+" Gold§f, §7"+rpgPlayer.getCharacter().getSilver()+" Silver§f, §c"+rpgPlayer.getCharacter().getCopper()+" Copper§f.");						
			return true;
		} // if(command.getName().equalsIgnoreCase("withdraw"))
		
		
		if(command.getName().equalsIgnoreCase("givecoin") || command.getName().equalsIgnoreCase("gc"))
		{
			
			if(!(RPGCraft.pexMan.has(p, "rpgcraft.money")))
			{
				p.sendMessage("[§2RPG§f] Sorry, but you do not have access to the currency");
				p.sendMessage("[§2RPG§f] commands. Speak to a server admin to gain access.");
				return true;				
			} // if(!(RPGCraft.Permissions.has(p, "rpgcraft.money")))
			// example /givecoin 1g 1s 1c mrhodes						
			if(cmd.length == 0 || cmd.length > 4)
			{
				// TODO: Give a more detailed help description
				p.sendMessage("[§2RPG§f] Usage -> /givecoin <§6gold§f> <§7silver§f> <§ccopper§f> <player>.");
				return true;
			}
			// Make sure player is logged in to RPGCraft
			RPG_Player pSender = mgr_Player.getPlayer(p.getName().hashCode());
			if(pSender == null)
				return true;
			
			// Get the receivers name and validate that player too
			List<Player> receivList = rpgServer.matchPlayer(cmd[cmd.length-1]);
			if(receivList.size() != 1)
			{
				if(receivList.size() == 0)
				{
					p.sendMessage("[§2RPG§f] "+cmd[cmd.length-1]+" is not online at the moment, or");
					p.sendMessage("[§2RPG§f] there may be a spelling mistake in the name.");
					return true;
				}
				p.sendMessage("[§2RPG§f] Please spell out more of that players name to");
				p.sendMessage("[§2RPG§f] send some coin.");
								
				return true;
			} // if(receivList.size() != 1)
			Player pReceiver = receivList.get(0);
			RPG_Player receiver = mgr_Player.getPlayer(pReceiver.getName().hashCode());
			if(receiver == null)
			{
				p.sendMessage("[§2RPG§f] Cannot give coin to "+receivList.get(0).getName()+".");
				p.sendMessage("[§2RPG§f] They may not be set up yet as an RPG character");
				p.sendMessage("[§2RPG§f] or they are not logged in.");
				return true;
			}
			
			int gold = pSender.getCharacter().getGold();
			int silver = pSender.getCharacter().getSilver();
			int copper = pSender.getCharacter().getCopper();
			int totalCopper = (gold*100*100) + (silver*100) + copper;
			
			int sendGold = 0;		int sendSilver = 0;
			int sendCopper = 0;		int sendTotalCopper = 0;
			
			// Now get the coin values...
			
			switch (cmd.length)
			{
			case 4:
				sendGold = Integer.parseInt(cmd[0]);
				sendSilver = Integer.parseInt(cmd[1]);
				sendCopper = Integer.parseInt(cmd[2]);
				break;
			case 3:
				sendSilver = Integer.parseInt(cmd[0]);
				sendCopper = Integer.parseInt(cmd[1]);
				break;
			case 2:	
				sendCopper = Integer.parseInt(cmd[0]);
				break;
			} // switch (cmd.length)
			
			sendTotalCopper = (sendGold*100*100) + (sendSilver*100) + sendCopper;
			if(sendTotalCopper < 0)
			{	p.sendMessage("[§2RPG§f] You cannot send a negative amount of coin.");
				p.sendMessage("[§2RPG§f] Your transaction has been cancelled.");
				return true;
			} // if(sendTotalCopper < 0)
			
			if(sendTotalCopper == 0)
			{	p.sendMessage("[§2RPG§f] You cannot send nothing, that would be a waste of time.");
				p.sendMessage("[§2RPG§f] Your transaction has been cancelled.");
				return true;
			} // if(sendTotalCopper == 0)
				
			if(totalCopper < sendTotalCopper)
			{	p.sendMessage("[§2RPG§f] You do not have enough coin to send to "+pReceiver.getDisplayName()+".");
				p.sendMessage("[§2RPG§f] You only have §6"+gold+" gold§f, §7"+silver+" silver§f, and §c"+copper+" copper§f.");
				return true;	
			} // if(totalCopper < sendTotalCopper)
				
			receiver.getCharacter().setCopper(receiver.getCharacter().getCopper() + sendTotalCopper);
			pReceiver.sendMessage("[§2RPG§f] "+p.getName()+" has sent you §6"+sendGold+" Gold§f, §7"+sendSilver+" Silver§f, and §c"+sendCopper+" Copper§f.");
			
			pSender.getCharacter().removeCopper(sendTotalCopper, p);
			p.sendMessage("[§2RPG§f] You sent §6"+sendGold+" Gold§f, §7"+sendSilver+" Silver§f, and §c"+sendCopper+" Copper §fto "+receiver.getPlayer().getName()+".");
			p.sendMessage("[§2RPG§f] You have §6"+pSender.getCharacter().getGold()+" Gold§f, §7"+pSender.getCharacter().getSilver()+" Silver§f, and §C"+pSender.getCharacter().getCopper()+" Copper §fleft.");
			
// TODO: Save a record of the transaction in the database
			return true;
		} // if(cmd[0].equalsIgnoreCase("/givecoin") || cmd[0].equalsIgnoreCase("/gc"))	
			
		return false;
	} // public boolean currencyProcessor(CommandSender sender, Command command, String label, String[] cmd)	
	
	public listener_Currency(Plugin p)
	{
		Bukkit.getServer().getPluginManager().registerEvents(this, p);
		this.rpgServer = p.getServer();
	} // public listener_Currency(Plugin p)
}
