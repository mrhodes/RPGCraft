package net.tigerstudios.RPGCraft.gui;

import net.tigerstudios.RPGCraft.RPGCraft;

import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.player.SpoutPlayer;

public class EnchantTable extends GenericPopup{
	RPGCraft rpgPlugin = null;
	SpoutPlayer sPlayer = null;	
	
	public static void create(RPGCraft plug, Player p)
	{
		EnchantTable gui = new EnchantTable(plug, p);
		gui.init();
	} // public static void create(RPGCraft plug, Player p)
	
	public EnchantTable(RPGCraft instance, Player p)
	{
		if(p == null)
			return;
		sPlayer = SpoutManager.getPlayer(p);
		if(sPlayer == null)
			return;
	} // public EnchantTable(RPGCraft instance, Player p)
	
	// Create the window for the player to interact with
	public void init()
	{	
		int screenWidth = sPlayer.getMainScreen().getWidth();
		int screenHeight = sPlayer.getMainScreen().getHeight();
		int width = 250; int height = 175;
		int x = screenWidth / 2 - (width / 2);
		int y = screenHeight / 2 - (height / 2);
		
		this.setX(x).setY(y);
		this.setWidth(width).setHeight(height);
		
		//Set up the windows background image
		GenericTexture texture = new GenericTexture(RPGCraft.webBase+"textures/guiWindow.png");
		texture.setX(x).setY(y);
		this.attachWidget(rpgPlugin, texture);
				
		sPlayer.getMainScreen().attachPopupScreen(this);
	} // public void init()
	
	
	public void exit(){	sPlayer.getMainScreen().closePopup(); }
} // public class <ClassName> extends GenericPopup