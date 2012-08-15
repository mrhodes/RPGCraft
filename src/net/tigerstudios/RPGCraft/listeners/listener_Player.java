package net.tigerstudios.RPGCraft.listeners;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.tigerstudios.RPGCraft.RPGCraft;
import net.tigerstudios.RPGCraft.RPG_Character;
import net.tigerstudios.RPGCraft.RPG_Player;
import net.tigerstudios.RPGCraft.mgr_Player;
import net.tigerstudios.RPGCraft.skills.FarmSystem;
import net.tigerstudios.RPGCraft.utils.SQLManager;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;


public class listener_Player implements Listener {
	private Plugin rpgPlugin = null;
	
			
	@EventHandler
	public void onPlayerInteract(final PlayerInteractEvent event)
	{
		// I don't do anything if player has empty hand
		if(event.getItem() == null)
			return;
		
		ItemStack item = event.getItem();
				
		// Water the crops only if the targetd block is farmland
		if(item.getType() == Material.WATER_BUCKET)
		{
			Block bTarget = event.getClickedBlock();
			if(bTarget == null)
				return;			
			
			if((bTarget.getType() == Material.CROPS) || (bTarget.getType() == Material.SOIL))
			{
				Player p = event.getPlayer();
				p.sendMessage("Dumped water on crops or soil.");
				if(bTarget.getType() == Material.CROPS)
					bTarget = bTarget.getRelative(0, -1, 0);
				
				p.sendMessage("Soil Dryness was: "+bTarget.getData());
				if(bTarget.getData() < 0x7)
					bTarget.setData((byte) 7);						
				p.sendMessage("Soil dryness is now: "+bTarget.getData());
				p.setItemInHand(new ItemStack(Material.BUCKET));
				event.setCancelled(true);
				return;				
			} // if((bTarget.getType() == Material.CROPS) || (bTarget.getType() == Material.SOIL))
			return;
		} // if(item.getType() == Material.WATER_BUCKET)
			
				
		if(item.getDurability() > 1023)
		{	
			Player p = event.getPlayer();
			RPG_Character character = mgr_Player.getCharacter(p);
			
			if(character != null)
			{													
				short id = item.getDurability();
				
				// See if this item is the Cider Ale
				if(id == RPGCraft.aleID)
				{
					SpoutPlayer sp = SpoutManager.getPlayer(p);
					// Increase Drunkeness by 1
					character.drunkenLevel += 100;
					// Burp 75% chance....
					SpoutManager.getSoundManager().playGlobalCustomSoundEffect(rpgPlugin,  
							"http://tigerstudios.net/minecraft/sounds/Burp.ogg", false, sp.getLocation(), 6);
					sp.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 400, character.drunkenLevel + 300));
										
					return;
				} // if(id == RPGCraft.aleID)	
				
				RPG_Player rpgPlayer = mgr_Player.getPlayer(p.getName().hashCode());
				// Important, only do this if within the 10 second timer.
				if( (rpgPlayer.getTimer() + 10000) <= System.currentTimeMillis())
				{	// Reset Timer and then exit
					rpgPlayer.setTimer(0);
					return; 
				}				
				// Check for Copper, Silver, or Gold
				if(id == RPGCraft.cp)
					character.addCopper(item.getAmount());
				
				if(id == RPGCraft.sp)
					character.addSilver(item.getAmount());
	
				if(id == RPGCraft.gp)
					character.addGold(item.getAmount());
							
				p.setItemInHand(null);
				p.sendMessage("[§2RPG§f] Balance: §6"+rpgPlayer.getCharacter().getGold()+" Gold§f, §7"+rpgPlayer.getCharacter().getSilver()+" Silver§f, §c"+rpgPlayer.getCharacter().getCopper()+" Copper§f.");
			} // if(character != null)
		 }// if(item.getDurability() > 1023)
	} // public void onPlayerInteract(final PlayerInteractEvent event)
		
		
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
	{	if(event.getRightClicked() instanceof Animals)
		{
			FarmSystem.animalInteractions(event.getPlayer(), (Animals)event.getRightClicked());
			event.setCancelled(true);
			return;
		} // if(event.getRightClicked() instanceof Animals)
	
		/*if(RPGCraft.bCitizensLoaded)
		{	
			Entity ent = event.getRightClicked();
			if(CitizensManager.isNPC(ent))
			{
				NPC npc =   get(ent);
				//((SpoutPlayer) npc).setSkin("http://tigerstudios.net/minecraft/textures/bankerSkin1.png");
				SpoutPlayer sp = SpoutManager.getPlayer(event.getPlayer());
				sp.getMainScreen().closePopup();
				sp.getMainScreen().attachPopupScreen(new BankerWindow(event.getPlayer(), npc));				
			}
		}*/
	} // public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
	
		
	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		Player p = event.getPlayer();
		ResultSet rs = null;
		
		// See if this player is in the database.
		String query = "SELECT * from Accounts WHERE mc_Name='"+p.getName()+"'";
		rs = SQLManager.SQLQuery(query);
		
		try {
			if(!rs.next())
			{	// New player.  Need to register this as a new player before logging in.
				mgr_Player.playerRegister(p);
				rs.close();
			}			
		} catch (SQLException e) { e.printStackTrace();	}
		
		mgr_Player.playerLogin(p);		
		/*GUIListener.fullTwoWay(p);
		
		RPG_Character rpgChar = mgr_Player.getCharacter(p);
		if(rpgChar != null)
			SpoutFeatures.setWalkingSpeed(SpoutManager.getPlayer(p), rpgChar.fWalkSpeed);
		*/
		return;		
	} // public void onPlayerJoin(final PlayerJoinEvent event)
	
	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent event)
	{
		Player p = event.getPlayer();
		mgr_Player.playerLogout(p);
		return;
	} // public void onPlayerQuit(PlayerEvent event)
	

	@EventHandler
	public void onPlayerExpChange(PlayerExpChangeEvent event)
	{	RPG_Character rpgChar = mgr_Player.getCharacter(event.getPlayer());
		if(rpgChar == null)
		{	// Player has not chosen a race yet...  just let the event
			// go ahead
			return;
		}		
		Player p = event.getPlayer();		
		rpgChar.addExperience(0, SpoutManager.getPlayer(p));		
	} // public void onPlayerExpChange(PlayerExpChangeEvent event)
	
	
	public listener_Player(Plugin p)
	{
		rpgPlugin = p;
	} // public listener_Player(Plugin p)
			
} // public class listener_Player implements Listener
