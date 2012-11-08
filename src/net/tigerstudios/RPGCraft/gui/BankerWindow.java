package net.tigerstudios.RPGCraft.gui;


import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericEntityWidget;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTextField;
import org.getspout.spoutapi.gui.GenericTexture;


public class BankerWindow extends GenericPopup implements Listener{
	private Plugin rpgPlugin;
	
	private GenericButton button = null;
	
	private GenericTexture guiBackground;
	private GenericTextField txtGold, txtSilver, txtCopper;
	private GenericEntityWidget entBanker = null;
	
	/*
	public void openInterface(SpoutPlayer p, NPC n)
	{
		RPG_Character rpgChar = mgr_Player.getCharacter(p.getPlayer());
		if(rpgChar == null)
		{
			//n.callTargetEvent(new EntityTargetEvent((Entity) n, p, TargetReason.CUSTOM));
			p.sendMessage("You need to select a race first.");
			return;			
		}
		
		// Setup Interface
		p.getMainScreen().removeWidgets(rpgPlugin);		
		int screenWidth = p.getMainScreen().getWidth();
		int screenHeight = p.getMainScreen().getHeight();
				
		this.setWidth(250).setHeight(150);
		this.setX((screenWidth / 2) - (this.getWidth() / 2))
				.setY((screenHeight / 2) - (this.getHeight() / 2));
		
		// Icons and Labels for balance
		GenericTexture goldTex = new GenericTexture("http://tigerstudios.net/minecraft/textures/gold.png");
		GenericTexture silverTex = new GenericTexture("http://tigerstudios.net/minecraft/textures/silver.png");
		GenericTexture copperTex = new GenericTexture("http://tigerstudios.net/minecraft/textures/copper.png");
		txtGold = new GenericTextField();
		txtSilver = new GenericTextField();
		txtCopper = new GenericTextField();
		
		// Buttons
		entBanker = new GenericEntityWidget();
		entBanker.setEntityId(n.getId());
			
		entBanker.setWidth(50).setHeight(80);
		entBanker.setX(getX() + 10).setY(getY()+ getHeight() - entBanker.getHeight() - 20);
		entBanker.setPriority(RenderPriority.Highest);
		entBanker.setDirty(true);
		
		guiBackground.setUrl("http://tigerstudios.net/minecraft/texture/guiWindow.png");
		guiBackground.setX(0).setY(0);
		guiBackground.setWidth(getWidth()).setHeight(getHeight());
		guiBackground.setPriority(RenderPriority.Highest);
		
		int bottomY = getY() + getHeight();
		txtGold.setWidth(8); txtSilver.setWidth(8); txtCopper.setWidth(8);
		txtGold.setHeight(10); txtSilver.setHeight(10); txtCopper.setHeight(10);
		txtGold.setX(getWidth() - (txtGold.getWidth() + 5) * 5).setY(10);
		txtSilver.setX(getWidth() - (txtGold.getWidth() + 5) * 5).setY(10);
		txtCopper.setX(getWidth() - (txtGold.getWidth() + 5) * 5).setY(10);
		
		txtGold.setText(""+rpgChar.getGold()); txtSilver.setText(""+rpgChar.getSilver()); 
		txtCopper.setText(""+rpgChar.getCopper()); 
		
		setTransparent(true);
		attachWidgets(rpgPlugin, guiBackground, txtGold, txtSilver, txtCopper, entBanker);
						
		setVisible(true);	
		setDirty(true);	
	} //public void openInterface(SpoutPlayer p, NPC npcTeller)
	
		
	public BankerWindow(Player p, NPC n)
	{
		rpgPlugin = RPGCraft.getPlugin();
		guiBackground = new GenericTexture();	
	
		openInterface(SpoutManager.getPlayer(p), n);
	}*/
} // public class guiBankInterface extends GenericPopup
