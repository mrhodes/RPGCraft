package net.tigerstudios.RPGCraft;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;


public class listener_Block implements Listener {
	private Random rndSeed = null;
	
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event)
	{		
		// If the player is in Creative mode then completely ignore this
		if(event.getPlayer().getGameMode() == GameMode.CREATIVE)
			return;
		
		Block b = event.getBlock();	
		int id = b.getTypeId();
		
		// If the block being broke is Wheat then call the harvest method
		if((id == 59) || (id == 83) || (id == 86) || (id == 103))
		{	Player p = event.getPlayer();	
			ItemStack item = p.getItemInHand();
			if(item != null)
			{	if((item.getTypeId() >= 290) && (item.getTypeId() <= 294))
				{		
					// Player tryinq to harvest a crop
					if(FarmSystem.harvest(p, b, item))
						event.setCancelled(true);					
					return;
				} // if((item.getTypeId() >= 290) && (item.getTypeId() <= 294))
				p.sendMessage("[§2RPG§f] You need to use a hoe to harvest these crops.");
				event.setCancelled(true);
				return;			
			} // if(item != null)
			
			// Player is breaking wheat with hand.... 
			p.sendMessage("[§2RPG§f] For better results, try using a hoe to harvest crops.");
			event.setCancelled(true);
			return;			
		} // if((id == 59) || (id == 86) || (id == 103))		
		
		// Player is either trying to till the soil, or get info on the crop and soil
		// If this is soil check if there are crops above it.  If so
		// then destroy crops without dropping anything.
		if(id == 60)
		{	// Don't care about this if not player related.
			// However we want to manually set the block to dirt, and
			// cancel the drops.
			if(event.getPlayer() != null)
			{	Block bCrops = event.getBlock().getRelative(BlockFace.UP);
				if(bCrops.getType() == Material.CROPS)
					bCrops.setTypeId(0);
				return;
			}
			//event.setCancelled(true);			
			return;
		} // if(id == 60)
		
		// This is going to be very crude code... but it will work for now.
		// In the future find a way to group all these stones into a collection
		// Now for the Mining Features of the plugin
		/* Stone 01
		 * CobbleStone 04
		 * BedRock 07
		 * Gold Ore 14
		 * Iron Ore 15
		 * Coal Ore 16
		 * Lapis Lazuli Ore
		 * Moss Stone 48
		 * Obsidian 49
		 * Diamond Ore 56
		 * Redstone Ore 73
		 * Netherrack 87
		 * Emerald Ore 129 
		 */
		if((id == 01) || (id == 04) || (id == 07) || ((id >= 14) && (id <= 16)) || (id == 21) || (id == 48) || (id == 49) || (id == 56) || (id == 73) || (id==87))
		{	Player p = event.getPlayer();	
			ItemStack item = p.getItemInHand();
			if(item != null)
			{	int itemId = item.getTypeId();
				// Item must equal a pickaxe
				if((itemId == 270) || (itemId == 274) || (itemId == 257)||(itemId==285) || (itemId == 278))
				{	MiningSystem.mine(b, p, item);
					event.setCancelled(true);
					return;
				}
				p.sendMessage("[§2RPG§f] You can't mine without a pickaxe.");
				event.setCancelled(true);
				return;
			} // if(item != null)
		}
				
		return;
	} // public void onBlockBreak(BlockBreakEvent event)
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		
		// first check if player is in creative more
		if(event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
			return;
		
		
		int bId = event.getBlock().getTypeId();	
				
		if((bId == 14) || (bId == 15) || (bId == 48) || (bId == 49) || (bId == 87) || (bId == 112))
		{	MiningSystem.placedBlocks.put(event.getBlock().getLocation(), mgr_Player.getCharacter(event.getPlayer()).AccountID);
			return;
		}		
	} // public void onBlockPlace(BlockPlaceEvent event)
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockForm(BlockFormEvent event)
	{
		if(event.getBlock().getType() == Material.SNOW_BLOCK)
			event.setCancelled(true);
	}
	
	
	public listener_Block(Plugin p)
	{
		Bukkit.getServer().getPluginManager().registerEvents(this, p);
		rndSeed = new Random(System.currentTimeMillis());
	 } // public listener_Player(Plugin p)
}
