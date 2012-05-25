package net.tigerstudios.RPGCraft;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.tigerstudios.RPGCraft.utils.SQLiteManager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;


public class listener_Player implements Listener {
	private Plugin rpgPlugin = null;
	private Server rpgServer = null;
		
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		Player p = event.getPlayer();
		ResultSet rs = null;
		
		// See if this player is in the database.
		String query = "SELECT * from Accounts WHERE mc_Name='"+p.getName()+"';";
		rs = SQLiteManager.SQLQuery(query);
		if(rs != null)
		{
			try {
				if(!rs.next())
				{	// New player.  Need to register this as a new player before logging in.
					mgr_Player.playerRegister(p);	
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} // if(rs != null)
		
		
		RPG_Player rpgPlayer = mgr_Player.getPlayer(p.getName().hashCode());
		if(rpgPlayer!=null)
		{
			mgr_Player.playerLogin(p);
			p.sendMessage("Welcome back to the server "+p.getName()+".");
			return;
		}
		// If player has not been added to the RPG list then register and login the 
		// player now
		
		mgr_Player.playerLogin(p);			
		return;		
	} // public void onPlayerJoin(final PlayerJoinEvent event)
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(final PlayerQuitEvent event)
	{
		Player p = event.getPlayer();
		RPG_Player rpgPlayer = mgr_Player.getPlayer(p.getName().hashCode());
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
	 } // public listener_Player(Plugin p)
	
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(final PlayerInteractEvent event)
	{
		if(event.getItem() == null)
			return;
		
		ItemStack item = event.getItem();
		
		if(item.getType() == Material.BOW)
		{
			// Make sure player has ammo to shoot
			Player p = event.getPlayer();
			if(p.getInventory().contains(Material.ARROW))
			{	Projectile e = p.getWorld().spawnArrow(p.getLocation(), p.getVelocity(), (float) 0.6, 10); 
				Bukkit.getPluginManager().callEvent(new ProjectileLaunchEvent(e));
				event.setCancelled(true);
				p.sendMessage("Bow Fired");
			}
		}
		
		if(item.getDurability() > 1023)
		{	Player p = event.getPlayer();
			RPG_Player rpgPlayer = mgr_Player.getPlayer(p.getName().hashCode());
					
			// Important, only do this if within the 10 second timer.
			if( (rpgPlayer.lTimer + 10000) <= System.currentTimeMillis())
			{	// Reset Timer and then exit
				rpgPlayer.lTimer = 0;
				return; 
			}
			
			short id = item.getDurability();
			// Check for Copper, Silver, or Gold
			if(id == RPGCraft.cp)
				rpgPlayer.getCharacter().setCopper(rpgPlayer.getCharacter().getCopper() + item.getAmount());
				
			if(id == RPGCraft.sp)
				rpgPlayer.getCharacter().setSilver(rpgPlayer.getCharacter().getSilver() + item.getAmount());
	
			if(id == RPGCraft.gp)
				rpgPlayer.getCharacter().setGold(rpgPlayer.getCharacter().getGold() + item.getAmount());
			
			p.setItemInHand(null);
			p.sendMessage("[§2RPG§f] Balance: §6"+rpgPlayer.getCharacter().getGold()+" Gold§f, §7"+rpgPlayer.getCharacter().getSilver()+" Silver§f, §c"+rpgPlayer.getCharacter().getCopper()+" Copper§f.");
						
		} // if(item.getDurability() > 1023)
	} // public void onPlayerInteract(final PlayerInteractEvent event)
		
	
	
	public boolean displayHelp(CommandSender sender, Command command, String[] args)
	{
		if(command.getName().equalsIgnoreCase("rpg"))
		{
			Player p = rpgServer.getPlayer(sender.getName());
		
			p.sendMessage("§aRPGCraft Help System - Page 1");
			p.sendMessage(" ");
			p.sendMessage("Currency Commands:");
			p.sendMessage("    §2/balance §for §2/bal    §3Displays your current balance.");
			p.sendMessage("    §2/givecoin §for §2/gc    §3Type /givecoin for usage info.");
			p.sendMessage("    §2/deposit                §3Deposit Coins to the bank.");
			p.sendMessage("    §2/withdraw               §3Type /withdraw for usage info.");
			if(RPGCraft.pexMan.has(p, "rpgcraft.bank.banker"))
			{ p.sendMessage("Bank Commands:");
				p.sendMessage("Please type the following commands without options for");
				p.sendMessage("more detailed help.");
				p.sendMessage("    §2/banker §f<§6gold§f> §f<§7silver§f> §f<§ccopper§f> §f<§2receiver§f>");
			} // if(RPGCraft.Permissions.has(p, "rpg.bank.banker"))
			return true;
		} // if(command.getName().equalsIgnoreCase("rpg"))
		
		return false;		
	} // public boolean displayHelp(CommandSender sender, Command command, String[] args)
} // public class listener_Player implements Listener
