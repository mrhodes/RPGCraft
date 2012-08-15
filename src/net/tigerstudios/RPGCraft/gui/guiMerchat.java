package net.tigerstudios.RPGCraft.gui;

import net.tigerstudios.RPGCraft.RPGCraft;
import net.tigerstudios.RPGCraft.SpoutFeatures.PlayerInventorySlot;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.getspout.spoutapi.event.screen.ScreenCloseEvent;
import org.getspout.spoutapi.gui.ContainerType;
import org.getspout.spoutapi.gui.GenericContainer;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTextField;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.WidgetAnchor;


public class guiMerchat extends GenericPopup implements Listener{
	protected GenericContainer labelContainer;
	private Player player;
	private GenericTexture guiTexture; 
	private GenericLabel lblGold, lblSilver, lblCopper;
	//private PlayerInventorySlot
			

	public guiMerchat(Player player) {

		this.player = player;
		
		GenericTexture border = new GenericTexture("machinegui.png");
		border.setX(-88).setY(-83);
		border.setPriority(RenderPriority.Highest);
		border.setWidth(176).setHeight(166);
		border.setFixed(true);
		border.setAnchor(WidgetAnchor.CENTER_CENTER);

		guiTexture = new GenericTexture("http://tigerstudios.net/minecraft/textures/guiWindow.png");
		guiTexture.setPriority(RenderPriority.Low);
		guiTexture.setWidth(256).setHeight(128);
		guiTexture.setFixed(true);
		guiTexture.setAnchor(WidgetAnchor.CENTER_CENTER);
		guiTexture.setX(-126).setY(-64);
		guiTexture.setVisible(false);

		createLabelWriter();
	
		
		this.setTransparent(true);
		this.attachWidgets(RPGCraft.getPlugin(), border, guiTexture);

		Inventory inventory = player.getInventory();

		int xposition = 0;
		int yposition = 58;

		//i know theres some kind of cool math equation to do this, but i cant remember how to figure it out. doin it the cheap way
		for (int i = 0; i < 36; i++) {

			PlayerInventorySlot slot = new PlayerInventorySlot(player, i);
			if (xposition == 9) xposition = 0;
			if (i > 8) yposition = 0;
			if (i > 17) yposition = 18;
			if (i > 26) yposition = 36;

			slot.setY(1 + yposition);
			slot.setX(-80 + (xposition*18));
			slot.setWidth(16).setHeight(16);
			slot.setPriority(RenderPriority.Normal);
			slot.setFixed(true);
			slot.setAnchor(WidgetAnchor.CENTER_CENTER);
			slot.setItem(inventory.getItem(i));
			this.attachWidget(RPGCraft.getPlugin(), slot);

			xposition++;
		}
	}

	protected void createLabelWriter() {

		if (this.labelContainer != null) {
			return;
		}

		labelContainer = new GenericContainer();
		labelContainer.setAnchor(WidgetAnchor.CENTER_CENTER);
		labelContainer.setPriority(RenderPriority.Lowest);
		labelContainer.setAlign(WidgetAnchor.CENTER_CENTER);
		labelContainer.setWidth(256);
		labelContainer.setHeight(64);
		labelContainer.setX(-128);
		labelContainer.setY(-64);

		GenericContainer bContainer = new GenericContainer();
		bContainer.setLayout(ContainerType.HORIZONTAL);
		bContainer.setAlign(WidgetAnchor.CENTER_CENTER);
		bContainer.setWidth(256);
		bContainer.setHeight(30);

		GenericTextField labelInput = new GenericTextField();
		labelInput.setMaximumCharacters(500);
		labelInput.setHeight(15).setWidth(200);
		//labelInput.setY(25);
		//labelInput.setX(5);
		labelInput.setMaximumLines(1);
		labelInput.setFocus(true);
		labelInput.setPriority(RenderPriority.Lowest);
		labelInput.setFixed(true);
		labelInput.setMarginTop(20);
		labelContainer.addChild(labelInput);

		labelContainer.addChild(bContainer);
		labelContainer.setVisible(false);

		this.attachWidget(RPGCraft.getPlugin(), labelContainer);
	}

	@Override
	public void onScreenClose(ScreenCloseEvent event) {
		

	}

	@Override
	public void handleItemOnCursor(org.bukkit.inventory.ItemStack itemOnCursor) {
		
	}

	@Override
	public void onTick() {
	}
	
}