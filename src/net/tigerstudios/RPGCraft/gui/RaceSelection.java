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
import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericListWidget;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.ListWidgetItem;
import org.getspout.spoutapi.gui.Orientation;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.ScrollBarPolicy;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

public class RaceSelection extends GenericPopup{
	private Plugin plugin;
	private SpoutPlayer sPlayer;			
	private GenericListWidget lstRaces;
	private GenericButton btnSelect;
	
	int screenWidth, screenHeight;
	int width, height;		
	int x, y;	
	
	public static void create(Plugin plugin, SpoutPlayer p)
	{		
		RaceSelection charWin = new RaceSelection(plugin, p);	
		charWin.init();
	} // public static void create(RPGCraft plugin, Player p)
	
	public RaceSelection(Plugin instance, SpoutPlayer p)
	{	
		if(p == null)
			return;
		sPlayer = p;
		plugin = instance;	
	} // public guiCharacter(RPGCraft instance, Player p)
	
	private void init()
	{		
		sPlayer.getMainScreen().closePopup();
		screenWidth = sPlayer.getMainScreen().getWidth();
		screenHeight = sPlayer.getMainScreen().getHeight();
		width = 250; height = 175;
		x = screenWidth / 2 - (width / 2);	y = screenHeight / 2 - (height / 2);
		this.setX(x).setY(y);
		this.setWidth(width).setHeight(height);
		
		GenericTexture background = new GenericTexture();		
		background.setX(x).setY(y).setWidth(width).setHeight(height);
		background.setUrl(RPGCraft.webBase+"textures/guiWindow.png");
		background.setPriority(RenderPriority.Highest);
		this.attachWidget(plugin, background);
		
		GenericLabel label = new GenericLabel("Changing your race will RESET\nall your skills and you will\nlose all banked money!");
		label.setWidth((int)(width * 0.8f)).setHeight(1);
		label.setTextColor(new Color(190, 0, 0, 0)).setShadow(false);
		label.setX(screenWidth/2).setY(y + 7);
		label.setAlign(WidgetAnchor.TOP_CENTER);
		this.attachWidget(plugin, label);
		
		// Player does have a character.
		lstRaces = new GenericListWidget(){
			@Override
			public void onSelected(int item, boolean dbl)
			{	// Get the race name and populate the info box below
			//	choosenRace = lstRaces.getSelectedItem().getTitle();
							
			} // public void onSelected(int item, boolean dbl)
			
			@Override
			public void onAnimate()
			{return;}			
		};
		
		Iterator<Race> iRace = RaceSystem.races.values().iterator();
		Race r;
		while(iRace.hasNext())
		{	r = iRace.next();							
			lstRaces.addItem(new ListWidgetItem(r.Name, r.Description));
		}
		lstRaces.setSelection(0);
		lstRaces.setWidth(100).setHeight(60);
		lstRaces.setX(x+30).setY(y+40);	
		lstRaces.setScrollBarPolicy(Orientation.VERTICAL, ScrollBarPolicy.SHOW_IF_NEEDED);
		
		btnSelect = new GenericButton("Select Race")
		{	@Override
			public void onButtonClick(ButtonClickEvent event)
			{
				selectRace(lstRaces.getSelectedItem().getTitle());
			}			
		};
		btnSelect.setWidth(50).setHeight(15);
		btnSelect.setX(x + (width - 45)).setY(y + (height - 15));
					
		this.attachWidget(plugin, lstRaces);
		this.attachWidget(plugin, btnSelect);			
							
		sPlayer.getMainScreen().attachPopupScreen(this);
	} // private void init()
		
	public void selectRace(String race )
	{
		// Get character info.				
		
		RPG_Player play = mgr_Player.getPlayer(sPlayer.getName().hashCode());
		RPG_Character rc = new RPG_Character(RaceSystem.getRace(race), play.getAccountID());
				
		play.setCharacter(rc);
						
		SpoutFeatures.updateTitle(sPlayer, null);
		
		sPlayer.sendMessage("[§2RPG§f] You are now a "+race);
		sPlayer.sendMessage("[§2RPG§f] Have fun!");
		exit();
	} // public void selectRace()
	
	public void exit(){	sPlayer.getMainScreen().closePopup(); }
}