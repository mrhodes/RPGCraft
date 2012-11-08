package net.tigerstudios.RPGCraft.SpoutFeatures;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.tigerstudios.RPGCraft.RPGCraft;
import net.tigerstudios.RPGCraft.RPG_Character;
import net.tigerstudios.RPGCraft.mgr_Player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import org.getspout.spoutapi.player.SpoutPlayer;
import org.getspout.spoutapi.ClientOnly;
import org.getspout.spoutapi.SpoutManager;


import ru.tehkode.permissions.PermissionUser;

public class SpoutFeatures {
	private static Plugin rpgPlugin = null;
	private static boolean enabled = false;
	public static boolean isEnabled() { return enabled; }

	public static boolean setup(Plugin plug)
	{
		Plugin plugin = Bukkit.getPluginManager().getPlugin("Spout");
				
		rpgPlugin = plug;
		if (plugin == null || !plugin.isEnabled())
		{	if (enabled == false) return false;
			enabled = false;
			return false;
		}
		if (enabled == true) return true;
		enabled = true;

		RPGCraft.log.info("---> Found and will use features of "+plugin.getDescription().getFullName());
		
		// Set the files to be preloaded for new players.
		preLoadResources();		
		return true;
	}	
		
	@ClientOnly
	public static void preLoadResources()
	{	// Precache all needed resources for the plugin.
		SpoutManager.getFileManager().addToPreLoginCache(rpgPlugin, "http://tigerstudios.net/minecraft/textures/guiWindow.png");
		SpoutManager.getFileManager().addToPreLoginCache(rpgPlugin, "http://tigerstudios.net/minecraft/textures/copper.png");
		SpoutManager.getFileManager().addToPreLoginCache(rpgPlugin, "http://tigerstudios.net/minecraft/textures/silver.png");
		SpoutManager.getFileManager().addToPreLoginCache(rpgPlugin, "http://tigerstudios.net/minecraft/textures/gold.png");
		SpoutManager.getFileManager().addToPreLoginCache(rpgPlugin, "http://tigerstudios.net/minecraft/textures/mug1.png");
		SpoutManager.getFileManager().addToPreLoginCache(rpgPlugin, "http://tigerstudios.net/minecraft/sounds/Burp.ogg");
		SpoutManager.getFileManager().addToPreLoginCache(rpgPlugin, "http://tigerstudios.net/minecraft/sounds/levelup.ogg");
		SpoutManager.getFileManager().addToPreLoginCache(rpgPlugin, "http://tigerstudios.net/minecraft/sounds/ZombieComeHere.ogg");
	} // public void preLoadResources()
		
	public static void updateTitle(final Object ofrom, final Object oto, boolean onlyIfDifferent)
	{	// Enabled and non-null?
		if ( ! isEnabled()) return;
		
		Set<Player> fromPlayers = getPlayersFromObject(ofrom);
		Set<Player> toPlayers = getPlayersFromObject(oto);

		for (Player player : fromPlayers)
		{
			RPG_Character rpgChar = mgr_Player.getCharacter(player);
			SpoutPlayer splayer = SpoutManager.getPlayer(player);
						
			for (Player playerTo : toPlayers)
			{
				SpoutPlayer splayerTo = SpoutManager.getPlayer(playerTo);
				String title = generateTitle(player, rpgChar);

				boolean skip = onlyIfDifferent && title.equals(splayer.getTitleFor(splayerTo));
				if (skip) continue;
				
				splayer.setTitleFor(splayerTo, title);
			} // for (Player playerTo : toPlayers)
		} // for (Player player : fromPlayers)
	} // public static void updateTitle(final Object ofrom, final Object oto, boolean onlyIfDifferent)
	
	public static void updateTitle(final Object ofrom, final Object oto)
	{
		updateTitle(ofrom, oto, true);
	} // public static void updateTitle(final Object ofrom, final Object oto)
	
	public static void updateTitleShortly(final Object ofrom, final Object oto)
	{
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(rpgPlugin, new Runnable()
		{	@Override
			public void run()	{	updateTitle(ofrom, oto, false);	}	}, 5);
	} // public static void updateTitleShortly(final Object ofrom, final Object oto)
		
	
	public static String generateTitle(Player player, RPG_Character rpgChar)
	{
		String ret = "";
	
		// First set Players group (only if Mod or above, and for Donators)
		PermissionUser pUser = RPGCraft.pexMan.getUser(player);
		if(pUser.has("tigerstudios.donator") || pUser.has("tigerstudios.mod"))
			ret = pUser.getPrefix()+ "\n";
		
		String color = null;		
		int health = player.getHealth();
		if(health >= 0) color = "&4";	if(health >= 5) color = "&6";
		if(health >= 10) color = "&e";	if(health >= 15) color = "&2";		
		
		ret = ret + color + player.getDisplayName();
		
		if(rpgChar == null)
		{	return ret.replace('&', '§');	}
		
		ret = ret +",\n&a"+rpgChar.getDisplaySuffix();		
		
		return ret.replace('&', '§');
	} // public static String generateTitle(Player player, RPG_Character rpgChar)
	
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //

	public static Set<Player> getPlayersFromObject(Object o)
	{
		Set<Player> ret = new HashSet<Player>();
		if (o instanceof Player)
		{
			ret.add((Player)o);
		}
		else if (o instanceof RPG_Character)
		{
			RPG_Character rpgPlayer = (RPG_Character)o;
			Player player = mgr_Player.getMCPlayer(rpgPlayer.getAccountID());
			if (player != null)
			{
				ret.add(player);
			}
		}
		else
		{
			ret.addAll(Arrays.asList(Bukkit.getOnlinePlayers()));
		}
		return ret;
	} // public static Set<Player> getPlayersFromObject(Object o)	
}
