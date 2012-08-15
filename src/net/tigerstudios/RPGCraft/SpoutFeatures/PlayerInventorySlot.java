package net.tigerstudios.RPGCraft.SpoutFeatures;

import org.bukkit.entity.Player;
import org.getspout.spoutapi.gui.GenericSlot;

public class PlayerInventorySlot extends GenericSlot {
	int index;
	Player player;

	
	public PlayerInventorySlot(Player player, int index) {
		this.index = index;
		this.player = player;
	}

	public int getIndex() {
		return index;
	}

	public boolean onItemExchange(org.bukkit.inventory.ItemStack current, org.bukkit.inventory.ItemStack cursor) {
		player.getInventory().setItem(index, cursor);
		return true;
	}

	public boolean onItemPut(org.bukkit.inventory.ItemStack item) {
		player.getInventory().setItem(index, item);
		return true;
	}

	public boolean onItemTake(org.bukkit.inventory.ItemStack item) {
		player.getInventory().setItem(index, null);
		return true;
	}
}
