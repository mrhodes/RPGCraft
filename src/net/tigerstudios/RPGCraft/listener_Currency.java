package net.tigerstudios.RPGCraft;

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
			RPGCraft.log.info(command.getName());
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
			
			if(mgr_Player.getCharacter(p) == null)
			{
				p.sendMessage("[§2RPG§f] You don't have a race selected.");
				p.sendMessage("[§2RPG§f] Select a race with the '/rpg' command.");
				return true;
			}
			
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
			
			if(mgr_Player.getCharacter(p) == null)
			{
				p.sendMessage("[§2RPG§f] You don't have a race selected.");
				p.sendMessage("[§2RPG§f] Select a race with the '/rpg' command");
				p.sendMessage("[§2RPG§f] before trying to withdraw money.");
				return true;
			}
			
			if(cmd.length == 0 || cmd.length > 3)
			{	p.sendMessage("§5"+RPGCraft.divider);
				//p.sendMessage(message)
				p.sendMessage("[§2RPG§f] Usage -> /withdraw <§6gold§f> <§7silver§f> <§ccopper§f>.");
				p.sendMessage("§5"+RPGCraft.divider);
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
			
		return false;
	} // public boolean currencyProcessor(CommandSender sender, Command command, String label, String[] cmd)	
	
	public listener_Currency(Plugin p)
	{
		Bukkit.getServer().getPluginManager().registerEvents(this, p);
		this.rpgServer = p.getServer();
	} // public listener_Currency(Plugin p)
}
