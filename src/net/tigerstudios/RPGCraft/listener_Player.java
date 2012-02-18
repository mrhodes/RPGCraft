package net.tigerstudios.RPGCraft;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.inventory.SpoutItemStack;

public class listener_Player implements Listener {
	private Plugin rpgPlugin = null;
	private Server rpgServer = null;
	private List<World> rpgWorlds = null;
	private boolean bChatEnabled = false;
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		Player p = event.getPlayer();
			
		RPG_Player rpgPlayer = mgr_Player.getPlayer(p.getName());
		if(rpgPlayer!=null)
		{
			mgr_Player.playerLogin(p);
			if(rpgPlayer.getRpgName().equals("null"))
				p.sendMessage("Welcome back to the server "+p.getName()+".");
			else
				p.sendMessage("Welcome back to the server "+rpgPlayer.getRpgName()+".");
				
			return;
		}
		// If player has not been added to the RPG list then register and login the 
		// player now
		mgr_Player.playerRegister(p);
		mgr_Player.playerLogin(p);			
		return;		
	} // public void onPlayerJoin(final PlayerJoinEvent event)
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(final PlayerQuitEvent event)
	{
		Player p = event.getPlayer();
		RPG_Player rpgPlayer = mgr_Player.getPlayer(p.getName());
		if(rpgPlayer!=null)
		{
			if(rpgPlayer.bIsOnline)
				mgr_Player.playerLogout(p);
			
		} // if(rpgPlayer!=null)
		return;
	} // public void onPlayerQuit(PlayerEvent event)
	
	
	public listener_Player(Plugin p)
	{
		Bukkit.getServer().getPluginManager().registerEvents(this, p);
		rpgPlugin = p;
		rpgServer = rpgPlugin.getServer();
		rpgWorlds = rpgServer.getWorlds();
	 } // public listener_Player(Plugin p)
	
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(final PlayerInteractEvent event)
	{
		if(event.getItem() == null)
			return;
		
		ItemStack item = event.getItem();
		if(item.getDurability() > 1023)
		{	Player p = event.getPlayer();
			RPG_Player rpgPlayer = mgr_Player.getPlayer(p.getName());
			
			// Important, only do this if within the 15 second timer.
			if( (rpgPlayer.lTimer + 10000) <= System.currentTimeMillis())
			{	// Reset Timer and then exit
				rpgPlayer.lTimer = 0;
				return; 
			}
			
			short id = item.getDurability();
			// Check for Copper, Silver, or Gold
			if(id == RPGCraft.cp)
				rpgPlayer.setCopper(rpgPlayer.getCopper() + item.getAmount());
				
			if(id == RPGCraft.sp)
				rpgPlayer.setSilver(rpgPlayer.getSilver() + item.getAmount());
	
			if(id == RPGCraft.gp)
				rpgPlayer.setGold(rpgPlayer.getGold() + item.getAmount());
			
			rpgPlayer.optimizeCoin();
			p.setItemInHand(null);
			p.sendMessage("[�2RPG�f] Balance: �6"+rpgPlayer.getGold()+" Gold�f, �7"+rpgPlayer.getSilver()+" Silver�f, �c"+rpgPlayer.getCopper()+" Copper�f.");
						
		} // if(item.getDurability() > 1023)
	} // public void onPlayerInteract(final PlayerInteractEvent event)
	
	
	
	
	public boolean displayHelp(CommandSender sender, Command command, String[] args)
	{
		if(command.getName().equalsIgnoreCase("rpg"))
		{
			Player p = rpgServer.getPlayer(sender.getName());
		
			p.sendMessage("�aRPGCraft Help System - Page 1");
			p.sendMessage(" ");
			p.sendMessage("Currency Commands:");
			p.sendMessage("    �2/balance �for �2/bal    �3Displays your current balance.");
			p.sendMessage("    �2/givecoin �for �2/gc    �3Type /givecoin for usage info.");
			p.sendMessage("    �2/deposit                �3Deposit Coins to the bank.");
			p.sendMessage("    �2/withdraw               �3Type /withdraw for usage info.");
			if(RPGCraft.permissionHandler.has(p, "rpgcraft.bank.banker"))
			{ p.sendMessage("Bank Commands:");
				p.sendMessage("Please type the following commands without options for");
				p.sendMessage("more detailed help.");
				p.sendMessage("    �2/banker �f<�6gold�f> �f<�7silver�f> �f<�ccopper�f> �f<�2receiver�f>");
			} // if(RPGCraft.Permissions.has(p, "rpg.bank.banker"))
			return true;
		}
		
		return false;		
	} // private void displayHelp(Player p)
	
}