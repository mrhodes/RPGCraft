/* RPGMainWindow - Interface hub for the RPGCraft plugin
 * 
 *  This window will be a main window for accessing the RPGCraft
 *  features.  It will provide basic info on the players race
 *  and have buttons for accessing other user interface options.
 *  
 *  An access key will be set for this window, Default will be 'R' 
 */

package net.tigerstudios.RPGCraft.gui;

import net.tigerstudios.RPGCraft.RPGCraft;

import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.event.input.KeyBindingEvent;
import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.ScreenType;
import org.getspout.spoutapi.keyboard.BindingExecutionDelegate;
import org.getspout.spoutapi.player.SpoutPlayer;

public class RPGMainWindow extends GenericPopup implements BindingExecutionDelegate{
	Plugin rpgPlugin = null;
	SpoutPlayer sPlayer = null;
	int screenWidth, screenHeight;
	int screenXCenter, screenYCenter;
	
	public static void create(SpoutPlayer p)
	{
		RPGMainWindow win = new RPGMainWindow(RPGCraft.getPlugin(), p);		
		win.init();
	} // public static void create(RPGCraft plug, Player p)
	
	public RPGMainWindow(Plugin instance, SpoutPlayer p)
	{
		if(p == null)
			return;
		sPlayer = SpoutManager.getPlayer(p);
		if(sPlayer == null)
			return;
		rpgPlugin = RPGCraft.getPlugin(); 
		screenWidth = sPlayer.getMainScreen().getWidth(); screenHeight = sPlayer.getMainScreen().getHeight();
		screenXCenter = screenWidth / 2; screenYCenter = screenHeight / 2;
	}
	
	public RPGMainWindow() {}

	// Create the window for the player to interact with
	public void init()	
	{
		int width = 175; int height = 150;
		int x = screenXCenter - (width / 2);
		int y = screenYCenter - (height / 2);
		
		this.setX(x).setY(y);
		this.setWidth(width).setHeight(height);
		
		// Components needed:
		GenericTexture texture;
		GenericButton button;
		GenericLabel label;
		
		//Set up the windows background image
		texture = new GenericTexture(RPGCraft.webBase+"textures/guiWindow.png");
		texture.setX(x).setY(y).setWidth(width).setHeight(height);
		texture.setPriority(RenderPriority.Highest);
		texture.setUrl(RPGCraft.webBase+"textures/guiWindow.png");
		this.attachWidget(rpgPlugin, texture);
		
		label = new GenericLabel("RPGCraft Feature Menu");
		label.setWidth((int)(width * 0.75f)).setHeight(20);
		label.setX(screenXCenter - (label.getWidth() / 2));
		label.setY(label.getHeight() + 1);
		this.attachWidget(rpgPlugin, label);
		
		// Setup the close button
		button = new GenericButton("Close"){
			@Override
			public void onButtonClick(ButtonClickEvent event)	{	exit();	}
		};
		button.setWidth(30).setHeight(10);
		button.setX(screenXCenter - (button.getWidth() / 2));
		button.setY(y + height - button.getHeight() - 5);
		this.attachWidget(rpgPlugin, button);
		
		button = new GenericButton("Choose Race"){
			@Override
			public void onButtonClick(ButtonClickEvent event) { 
				exit();
				RaceSelection.create(rpgPlugin, sPlayer);}
		};
		button.setX(x - 80).setY(y);
		button.setWidth(75).setHeight(15);
		this.attachWidget(rpgPlugin, button);	
		
		
		// Setup the advanced users buttons.
		// Only admin buttons available for now
		if(RPGCraft.pexMan.has(sPlayer.getPlayer(), "rpgcraft.admin"))
		{	button = new GenericButton("Admin Config"){
				@Override
				public void onButtonClick(ButtonClickEvent ev)
				{	exit();
					AdminConfig.create(RPGCraft.getPlugin(), sPlayer.getPlayer());
				} // public void onButtonClick(ButtonClickEvent ev)			
			};
			button.setX(x - 80).setY(y + 20);
			button.setWidth(75).setHeight(15);
			this.attachWidget(rpgPlugin, button);
			
		} // if(sPlayer.hasPermission("rpgcraft.admin"))
		
		sPlayer.getMainScreen().attachPopupScreen(this);
	}// public void init()	
	
	
	public void exit(){	sPlayer.getMainScreen().closePopup(); }

	@Override
	public void keyPressed(KeyBindingEvent event) {
		//Only toggle state when not on the gamescreen
		if (!event.getScreenType().equals(ScreenType.GAME_SCREEN)) {
			return;
		}		
		create(event.getPlayer());		
	}

	@Override
	public void keyReleased(KeyBindingEvent event) {}
}
