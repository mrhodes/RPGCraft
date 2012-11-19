package net.tigerstudios.RPGCraft.gui;


import net.tigerstudios.RPGCraft.RPGCraft;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.gui.ContainerType;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericComboBox;
import org.getspout.spoutapi.gui.GenericContainer;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTextField;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.player.SpoutPlayer;

public class AdminConfig extends GenericPopup{
	Plugin rpgPlugin = null;
	SpoutPlayer sPlayer = null;
	
	GenericButton button = null;
	GenericTextField catInput = null;
	GenericComboBox catList = null;
	GenericLabel label = null;
	GenericButton btnBack = null; GenericButton btnClose = null;
	
	GenericContainer currentContainer = null;
	
	GenericContainer categoryConfig = null;
	GenericContainer playerConfig = null;
	GenericContainer rpgStats = null;
	
	GenericContainer optionButtons = null;
	GenericContainer mainScreen = null;
	GenericContainer subButtons = null;
	
	int screenWidth, screenHeight;
	int width, height;
	int x, y;	
	
	public static void create(Plugin plug, Player p)
	{	AdminConfig gui = new AdminConfig(plug, p);
		gui.init();
	} // public static void create(RPGCraft plug, Player p)
	
	public AdminConfig(Plugin instance, Player p)
	{
		if(p == null)
			return;
		sPlayer = SpoutManager.getPlayer(p);
		if(sPlayer == null)
			return;
		rpgPlugin = instance;
	} // public AdminConfig(Plugin instance, Player p)
	
		
	// Create the window for the player to interact with
	public void init()
	{	
		sPlayer.getMainScreen().closePopup();
		screenWidth = sPlayer.getMainScreen().getWidth();
		screenHeight = sPlayer.getMainScreen().getHeight();
		width = 250; height = 175;
		x = screenWidth / 2 - (width / 2);	y = screenHeight / 2 - (height / 2);
		this.setX(x).setY(y);
		this.setWidth(width).setHeight(height);				
		
		// Setup the 3 Containers to hold the GUI
// --------------------------
		mainScreen = new GenericContainer();
		mainScreen.setX(x).setY(y);
		mainScreen.setWidth(width).setHeight(height);
		mainScreen.setLayout(ContainerType.OVERLAY);
		
		//Set up the windows background image
		GenericTexture texture = new GenericTexture(RPGCraft.webBase+"textures/guiWindow.png");
		texture.setX(0).setY(0).setWidth(width).setHeight(height).setPriority(RenderPriority.Highest);
		label = new GenericLabel("Admin Control Panel");
		label.setTooltip("title");
		label.setWidth((int)(width * 0.8f)).setHeight(30);
		label.setX(0).setY(-10);
		
		btnBack = new GenericButton("Back"){
			@Override
			public void onButtonClick(ButtonClickEvent ev) 
			{ 	// see if the subButtons panel is open, is so, close it
				if(subButtons != null)
					if(subButtons.isVisible())
						subButtons.setVisible(false);
				
				mainScreen.setVisible(true);
				btnBack.setEnabled(false);							
			} // public void onButtonClick(ButtonClickEvent ev) 
		};
		btnBack.setEnabled(false);
		btnBack.setWidth(60).setHeight(10); btnBack.setX(width - 80).setY(height - 20);
		btnBack.setTooltip("Return to main admin screen");
		
		btnClose = new GenericButton("Close"){
			@Override
			public void onButtonClick(ButtonClickEvent ev) { exit();} 
		};
		btnClose.setWidth(60).setHeight(10); btnClose.setX(5).setY(height - 20);
		btnClose.setTooltip("Return to main admin screen");
		mainScreen.addChildren(label, texture, btnBack, btnClose);
		this.attachWidgets(rpgPlugin, mainScreen.getChildren());		
// ------------------------
		// Setup the Buttons.
		optionButtons = new GenericContainer();
		optionButtons.setX(x - 75).setY(y);
		optionButtons.setWidth(75).setHeight(height);
		optionButtons.setLayout(ContainerType.OVERLAY);
				
		button = new GenericButton("Set Categories"){
			@Override
			public void onButtonClick(ButtonClickEvent ev) {
				openContainer(categoryConfig);			
			};
		};
		button.setX(0).setY(0);
		button.setWidth(75).setHeight(15);
		button.setTooltip("Create and Edit item categories.");
		optionButtons.addChild(button);		
		
		button = new GenericButton("Player Config"){
			@Override
			public void onButtonClick(ButtonClickEvent ev)	{  }		
		};
		button.setX(0).setY(20);
		button.setWidth(75).setHeight(15);
		button.setTooltip("Access Player settings and Control Panel.");
		optionButtons.addChild(button);
		
		button = new GenericButton("Race Config"){
			
		};
		button.setX(0).setY(40).setWidth(75).setHeight(15);
		button.setTooltip("Set the many Racial Config Options");
		optionButtons.addChild(button);
		
		button = new GenericButton("Skills Config"){
			@Override
			public void onButtonClick(ButtonClickEvent ev)	{ skillsConfig(); }		
		};
		button.setX(0).setY(60);
		button.setWidth(75).setHeight(15);
		button.setTooltip("Configure the many different skill options.");
		optionButtons.addChild(button);
		this.attachWidgets(rpgPlugin, optionButtons.getChildren());
		
		
		setupCategoryConfig();				
		sPlayer.getMainScreen().attachPopupScreen(this);
	} // public void init()
	
	
	private void skillsConfig()
	{
		subButtons = new GenericContainer();
		subButtons.setLayout(ContainerType.OVERLAY);
		subButtons.setX(x + width + 5).setY(y).setWidth(75).setHeight(height);
		
		btnBack.setEnabled(true);	
		button = new GenericButton("Farming"){
			
		};
		button.setX(0).setY(0);
		button.setWidth(75).setHeight(15);
		button.setTooltip("Farming related options.");
		subButtons.addChild(button);		
		
		button = new GenericButton("Mining"){			
		};
		button.setX(0).setY(20);
		button.setWidth(75).setHeight(15);
		button.setTooltip("Mining related options.");
		subButtons.addChild(button);		
		
		button = new GenericButton("Enchanting"){
			
		};
		button.setX(0).setY(40);
		button.setWidth(75).setHeight(15);
		button.setTooltip("Enchanting related options.");
		subButtons.addChild(button);	
		
		button = new GenericButton("Alchemy"){
			
		};
		button.setX(0).setY(60);
		button.setWidth(75).setHeight(15);
		button.setTooltip("Alchemy related options.");
		subButtons.addChild(button);	
		
		button = new GenericButton("Blacksmithing"){
			
		};
		button.setX(0).setY(80);
		button.setWidth(75).setHeight(15);
		button.setTooltip("Blacksmithing related options.");
		subButtons.addChild(button);	
		
		button = new GenericButton("Cooking"){
			
		};
		button.setX(0).setY(100);
		button.setWidth(75).setHeight(15);
		button.setTooltip("Cooking related options.");
		subButtons.addChild(button);
		
		button = new GenericButton("Fishing"){
			
		};
		button.setX(0).setY(120);
		button.setWidth(75).setHeight(15);
		button.setTooltip("Farming related options.");
		subButtons.addChild(button);	
		
		this.attachWidgets(rpgPlugin, subButtons.getChildren());		
		return;
	} // private void skillsConfig()
	
	
	// Setup Methods for individual admin pages.
	// A page will be a Container that is simply 
	// switch from on to off.
	private void setupCategoryConfig()
	{
		categoryConfig = new GenericContainer();
		categoryConfig.setLayout(ContainerType.OVERLAY);
		categoryConfig.setWidth(width - 10).setHeight(height - 25);
		//categoryConfig.setPriority(RenderPriority.Highest);
		categoryConfig.setX(x + 5).setY(y);
		
		label = new GenericLabel("Item Types Creation");
		label.setWidth(width).setHeight(15);	
		label.setX(10).setY(10);
		categoryConfig.addChild(label);			
		
		catInput = new GenericTextField();
		catInput.setPlaceholder("<Enter New Category>");
		
		catList = new GenericComboBox(){
			@Override
			public void onSelectionChanged(int i, String text)
			{	catInput.setText(text);				
			}			
		};
		
		catInput.setX(10).setY(catList.getHeight() + catList.getY() + 1).setWidth(80).setHeight(15);
		categoryConfig.addChild(catInput);
		
		// Here we need to load the categories from the RPGClass
		// and populate the box with them, if any.
		/*if(!RPGCraft.itemCategories.isEmpty())
		{	catList.setItems((List<String>) RPGCraft.itemCategories.values());			
		}else
		{
			catList.setText("Nothing Here Yet");
		}*/
		catList.setSelection(0);
		catList.setWidth(80).setHeight(15);
		catList.setX(10).setY(30);
		categoryConfig.addChild(catList);
		
		
			
				
		categoryConfig.setVisible(false);
		this.attachWidgets(rpgPlugin, categoryConfig.getChildren());
	}
	
	public void openContainer(GenericContainer con)
	{	if(con.isVisible())
			return;
	
	/*	for(Widget w: mainScreen.getChildren())
		{	if( (w.getTooltip().equalsIgnoreCase("title")) || (w instanceof Texture))
				w.setVisible(false);
		}*/
		
		// Now open the container we want to open
		con.setVisible(true);		
		btnBack.setEnabled(true);		
	} // public void openContainer(GenericContainer con)
	
	
	public void exit(){	sPlayer.getMainScreen().closePopup(); }
} // public class AdminConfig extends GenericPopup