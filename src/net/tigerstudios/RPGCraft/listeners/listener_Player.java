package net.tigerstudios.RPGCraft.listeners;


import net.tigerstudios.RPGCraft.RPGCraft;
import net.tigerstudios.RPGCraft.RPG_Character;
import net.tigerstudios.RPGCraft.RPG_Player;
import net.tigerstudios.RPGCraft.mgr_Player;
import net.tigerstudios.RPGCraft.CombatSystem.CombatSystem;
import net.tigerstudios.RPGCraft.skills.FarmSystem;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class listener_Player implements Listener {		
			
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event)
	{
		if(mgr_Player.getCharacter((Player) event.getPlayer()) != null)
		{
			CombatSystem.updateArmorStats((Player) event.getPlayer());
			CombatSystem.updateWeaponStats((Player) event.getPlayer());
			return;
		}
		Player p = (Player)event.getPlayer();
		if(p != null)
		{	p.sendMessage(RPGCraft.divider);
			p.sendMessage("Please select a race for yourself.");
			p.sendMessage("Type /�arpg list �fto see race choices.");
			p.sendMessage("and then type /�arpg choose �f<�drace�f> to choose the race.");
			p.sendMessage(RPGCraft.divider);
		}		
	} // public void onInventoryClose(InventoryCloseEvent event)
	
	@EventHandler
	public void onPlayerItemBreak(PlayerItemBreakEvent event)
	{
		// When an Item breaks, update the players armor and weapon
		// values to reflect the changes.
		if(mgr_Player.getCharacter(event.getPlayer()) != null)
		{	CombatSystem.updateArmorStats(event.getPlayer());
			CombatSystem.updateWeaponStats(event.getPlayer());
			return;
		}
		Player p = (Player)event.getPlayer();
		if(p != null)
		{	p.sendMessage(RPGCraft.divider);
			p.sendMessage("Please select a race for yourself.");
			p.sendMessage("Type /�arpg list �fto see race choices.");
			p.sendMessage("and then type /�arpg choose �f<�drace�f> to choose the race.");
			p.sendMessage(RPGCraft.divider);
		}		
	}// public void onPlayerItemBreak(PlayerItemBreakEvent event)
	
	
	@EventHandler
	public void onPlayerInteract(final PlayerInteractEvent event)
	{
		// I don't do anything if player has empty hand
		if(event.getItem() == null)
			return;
		
		ItemStack item = event.getItem();
				
		if(item.getDurability() > 1023)
		{	
			Player p = event.getPlayer();
			RPG_Character character = mgr_Player.getCharacter(p);
			
			if(character != null)
			{													
				short id = item.getDurability();
					
				RPG_Player rpgPlayer = mgr_Player.getPlayer(p.getName().hashCode());
				// Important, only do this if within the 10 second timer.
				if( (rpgPlayer.getTimer() + 10000) <= System.currentTimeMillis())
				{	// Reset Timer and then exit
					rpgPlayer.setTimer(0);
					return; 
				}				
				// Check for Copper, Silver, or Gold
				double total = 0;
				if(id == RPGCraft.cp)	total += item.getAmount();
				if(id == RPGCraft.sp)	total += item.getAmount() * 100;
				if(id == RPGCraft.gp)	total += item.getAmount() * 10000;
							
				p.setItemInHand(null);
				//RPGCraft.econ.depositPlayer(p.getName(), total);
				double g = 0, s = 0, c;
				//c = RPGCraft.econ.getBalance(p.getName());
				//while(c >= 100){ s+=1; c-=100;}
				while(s >= 100){ g+=1; s-=100;}
				//p.sendMessage("[�2RPG�f] Balance: �6"+g+" Gold�f, �7"+s+" Silver�f, �c"+c+" Copper�f.");	
			} // if(character != null)
		 }// if(item.getDurability() > 1023)
	} // public void onPlayerInteract(final PlayerInteractEvent event)
		
		
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
	{	if(event.getRightClicked() instanceof Animals)
		{	FarmSystem.animalInteractions(event.getPlayer(), (Animals)event.getRightClicked());
			event.setCancelled(true);
			return;
		} // if(event.getRightClicked() instanceof Animals)
	
		if(event.getRightClicked() instanceof Villager)
		{			
			
		} // if(event.getRightClicked() instanceof Villager)
		
	} // public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
	
	
	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		mgr_Player.playerLogin(event.getPlayer());		
		return;		
	} // public void onPlayerJoin(final PlayerJoinEvent event)
	
	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent event)
	{
		mgr_Player.playerLogout(event.getPlayer());
		return;
	} // public void onPlayerQuit(PlayerEvent event)
	
	public listener_Player(){} // public listener_Player(Plugin p)			
} // public class listener_Player implements Listener
