package net.tigerstudios.RPGCraft.utils.custom;

import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.material.MaterialData;
import org.getspout.spoutapi.material.item.GenericCustomFood;

public class CFood extends GenericCustomFood {

	public CFood(Plugin plugin, String name, String texture, int hungerRestored) {
		super(plugin, name, texture, hungerRestored);
		MaterialData.addCustomItem(this);
	}
}
