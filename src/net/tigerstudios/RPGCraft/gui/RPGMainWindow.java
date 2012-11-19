/* RPGMainWindow - Interface hub for the RPGCraft plugin
 * 
 *  This window will be a main window for accessing the RPGCraft
 *  features.  It will provide basic info on the players race
 *  and have buttons for accessing other user interface options.
 *  
 *  An access key will be set for this window, Default will be 'R' 
 */

package net.tigerstudios.RPGCraft.gui;

import java.util.Iterator;

import net.tigerstudios.RPGCraft.RPGCraft;
import net.tigerstudios.RPGCraft.RPG_Character;
import net.tigerstudios.RPGCraft.RPG_Player;
import net.tigerstudios.RPGCraft.Race;
import net.tigerstudios.RPGCraft.RaceSystem;
import net.tigerstudios.RPGCraft.mgr_Player;
import net.tigerstudios.RPGCraft.SpoutFeatures.SpoutFeatures;

import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.event.input.KeyBindingEvent;
import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.ContainerType;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericContainer;
import org.getspout.spoutapi.gui.GenericEntityWidget;
import org.getspout.spoutapi.gui.GenericGradient;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericListWidget;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.ListWidgetItem;
import org.getspout.spoutapi.gui.Orientation;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.ScreenType;
import org.getspout.spoutapi.gui.ScrollBarPolicy;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.keyboard.BindingExecutionDelegate;
import org.getspout.spoutapi.player.SpoutPlayer;

public class RPGMainWindow extends GenericPopup implements BindingExecutionDelegate{
	Plugin rpgPlugin = null;
	SpoutPlayer sPlayer = null;
	int screenWidth, screenHeight;
	int screenXCenter, screenYCenter;
	int containerWidth = 0, containerHeight = 0;
	GenericContainer currentContainer = null;
	
	// Left and Right button panels
	GenericContainer mainButtons = null;
	GenericContainer subButtons = null;
	
	// Container for each possible window
	GenericContainer raceSelection = null;
	GenericContainer characterWindow = null;
	
	static int buttonWidth = 65, buttonHeight = 15;
	int x, y;
	
	public static void create(SpoutPlayer p)
	{	RPGMainWindow win = new RPGMainWindow(p);		
		win.init();
	} // public static void create(RPGCraft plug, Player p)
	
	public RPGMainWindow(SpoutPlayer p)
	{	if(p == null)
			return;
		sPlayer = p;
		rpgPlugin = RPGCraft.getPlugin(); 
		screenWidth = sPlayer.getMainScreen().getWidth(); screenHeight = sPlayer.getMainScreen().getHeight();
		screenXCenter = screenWidth / 2; screenYCenter = screenHeight / 2;
	} // public RPGMainWindow(SpoutPlayer p)
	
	public RPGMainWindow() {}

	// Create the window for the player to interact with
	public void init()	
	{
		// width and height are part of GenericPopup
		width = 295; height = 200;
		x = screenXCenter - (width / 2);
		y = screenYCenter - (height / 2);
		
		// These values set the starting points for the primary buttons
		int btnPosX = x - 65;  int btnPosY = y;
		
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
		texture.setDrawAlphaChannel(false);
		this.attachWidget(rpgPlugin, texture);
		
		label = new GenericLabel("RPGCraft Feature Menu");
		label.setWidth((int)(width * 0.75f)).setHeight(20);
		label.setX(screenXCenter - (label.getWidth() / 2));
		label.setY(y - (label.getHeight() / 2) - 1);
		this.attachWidget(rpgPlugin, label);		
		
		// If player has already choosen a Race do not show this button
		if(mgr_Player.getCharacter(sPlayer.getPlayer()) == null)
		{
			button = new GenericButton("Choose Race"){
				@Override
				public void onButtonClick(ButtonClickEvent event) { 
					currentContainer = raceSelection;
					showContainer(raceSelection, true);				
				}
			};
			button.setX(btnPosX).setY(btnPosY);
			button.setWidth(buttonWidth).setHeight(buttonHeight);
			this.attachWidget(rpgPlugin, button);
			btnPosY+=20;
		}
		
		button = new GenericButton("Character"){
			@Override
			public void onButtonClick(ButtonClickEvent event) { 
				currentContainer = raceSelection;
				showContainer(characterWindow, true);				
			}
		};
		button.setX(btnPosX).setY(btnPosY);
		button.setWidth(buttonWidth).setHeight(buttonHeight);
		this.attachWidget(rpgPlugin, button);		
		
		// Setup the advanced users buttons.
		// Only admin buttons available for now
		if(RPGCraft.pexMan.has(sPlayer.getPlayer(), "rpgcraft.admin"))
		{	button = new GenericButton("Admin Config"){
				@Override
				public void onButtonClick(ButtonClickEvent ev)
				{ AdminConfig.create(rpgPlugin, sPlayer); } // public void onButtonClick(ButtonClickEvent ev)			
			};
			
			btnPosY+=20;
			button.setX(btnPosX).setY(btnPosY);
			button.setWidth(buttonWidth).setHeight(buttonHeight);
			this.attachWidget(rpgPlugin, button);
			
		} // if(sPlayer.hasPermission("rpgcraft.admin"))
		
		// Setup the close button
		button = new GenericButton("Close"){
			@Override
			public void onButtonClick(ButtonClickEvent event){	exit();	}
		};
		button.setWidth(width).setHeight(buttonHeight);
		button.setX(x);
		button.setY(y + height + 5);
		this.attachWidget(rpgPlugin, button);
		
		createContainers();	
		
		sPlayer.getMainScreen().attachPopupScreen(this);
	}// public void init()	
		
	
	/* --------------------------------------------------------------
	 *  createContainers is a method to premake all the needed 
	 *  containers for the main UI
	 */
	public void createContainers()
	{
		createRaceSelection();
		createCharacterWindow();
		
	} // public void createContainers()
		
	
	private void createCharacterWindow()
	{
		GenericLabel label;
		GenericGradient grad = null;
		GenericEntityWidget entityWid;
		characterWindow = new GenericContainer();
		characterWindow.setLayout(ContainerType.OVERLAY);
		characterWindow.setWidth(width).setHeight(height);
		
		// Set the Title of this window
		grad = new GenericGradient();
		grad.setWidth(100).setHeight(20);
		grad.setX(x + 7).setY(y + 8);
		grad.setTopColor(new Color(0, 10, 100)).setBottomColor(new Color(0, 0, 0));
		characterWindow.addChild(grad);		
		
		label = new GenericLabel();
		label.setTextColor(new Color(255, 0, 0));
		label.setText(sPlayer.getDisplayName().toUpperCase() + ", the "+ mgr_Player.getCharacter(sPlayer).race);
		label.setX(x + 10).setY(y + 10);
		label.setWidth(100).setHeight(20);
		characterWindow.addChild(label);		
		
		grad = new GenericGradient();
		grad.setWidth(50).setHeight(75);
		grad.setX(width / 2).setY(y);
		grad.setTopColor(new Color(0, 0, 0)).setBottomColor(new Color(0, 0, 255));
		characterWindow.addChild(grad);	
		
		entityWid = new GenericEntityWidget();
		entityWid.setEntityId(sPlayer.getEntityId());
		entityWid.setWidth(60).setHeight(90);
		entityWid.setX(width / 2).setY(y);
		characterWindow.addChild(entityWid);
		
		
		// Show Players Level
		label = new GenericLabel("Level: ");
		label.setX(x).setY(y); 
		label.setTextColor(new Color(255, 150, 150));
		characterWindow.addChild(label);
		
		
		// Make a close button to close this window
		GenericButton button = new GenericButton("Close"){
			@Override
			public void onButtonClick(ButtonClickEvent event)
			{	showContainer(characterWindow, false);	}			
		};
		button.setWidth(buttonWidth).setHeight(buttonHeight);
		button.setX(x + (width - button.getWidth()) - 3).setY(y + (height - button.getHeight() - 3));
		characterWindow.addChild(button);		
	}	
	
	
	private void createRaceSelection()
	{
		final GenericListWidget lstRaces;
		GenericButton btnSelect;
		
		raceSelection = new GenericContainer();
		raceSelection.setWidth(width).setHeight(height);
		raceSelection.setPriority(RenderPriority.Highest);
		raceSelection.setLayout(ContainerType.OVERLAY);		
				
		GenericLabel label = new GenericLabel("Changing your race will RESET\nall your skills and you will\nlose all banked money!");
		label.setWidth((int)(width * 0.8f)).setHeight(1);
		label.setTextColor(new Color(190, 0, 0, 0)).setShadow(false);
		label.setX(screenWidth/2).setY(y + 7);
		label.setAlign(WidgetAnchor.TOP_CENTER);
		raceSelection.addChild(label);
		
		// Player does have a character.
		lstRaces = new GenericListWidget();
		
		Iterator<Race> iRace = RaceSystem.races.values().iterator();
		Race r;
		while(iRace.hasNext())
		{	r = iRace.next();							
			lstRaces.addItem(new ListWidgetItem(r.Name, r.Description));
		}
		lstRaces.setSelection(0);
		lstRaces.setWidth(100).setHeight(60);
		lstRaces.setX(x + 30).setY(y + 40);	
		lstRaces.setScrollBarPolicy(Orientation.VERTICAL, ScrollBarPolicy.SHOW_IF_NEEDED);
		
		btnSelect = new GenericButton("Select Race")
		{	@Override
			public void onButtonClick(ButtonClickEvent event)
			{
				String race = lstRaces.getSelectedItem().getTitle();
				// Get character info.				
				
				RPG_Player play = mgr_Player.getPlayer(sPlayer.getName().hashCode());
				RPG_Character rc = new RPG_Character(RaceSystem.getRace(race), play.getAccountID());
						
				play.setCharacter(rc);
								
				SpoutFeatures.updateTitle(sPlayer, null);
				
				sPlayer.sendMessage("[§2RPG§f] You are now a "+race);
				sPlayer.sendMessage("[§2RPG§f] Have fun!");
				
				showContainer(raceSelection, false);
			}			
		};
		btnSelect.setWidth(buttonWidth).setHeight(buttonHeight);
		btnSelect.setX(x + (width - btnSelect.getWidth()) - 3).setY(y + (height - btnSelect.getHeight() - 3));
							
		raceSelection.addChild(lstRaces);
		raceSelection.addChild(btnSelect);		
		
		raceSelection.setContainer(this.getContainer());
		raceSelection.setDirty(true);
	}
	
	
	private void showContainer(GenericContainer c, boolean bShow)
	{
		if(currentContainer != null)
		{
			currentContainer.setVisible(false); currentContainer = null;
		}
		if(c != null)
			if(bShow == false)
				c.setVisible(false);
			else
			{	this.attachWidgets(rpgPlugin, c.getChildren());
				c.setVisible(true);
				currentContainer = c;
			}
		return;
	} // private void showContainer(GenericContainer c, boolean bShow)
	
	
	
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
