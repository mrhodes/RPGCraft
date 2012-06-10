package net.tigerstudios.RPGCraft;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

public class listener_Block implements Listener {
	private Random rndSeed = null;
	
	
	/*@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockGrow(BlockGrowEvent event)
	{
		Block b = event.getBlock();
		if(b.getType() == Material.CROPS)
		{
			System.out.println("Crops changed state.");
			BlockState oldState = b.getState();
			BlockState newState = event.getNewState();
		
		}
		
		// Find out if this block is skipping stages.
		
		
	} // public void onBlockGrow(BlockGrowEvent event)	
	*/
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event)
	{
		// Don't care about this if not player related.
		if(event.getPlayer() == null)
			return;
		
		Block b = event.getBlock();	
		int id = b.getTypeId();
		// Handle special cases.  First case, Farming.
		// Crops, Pumpkin, or Melon
		if((id == 59) || (id == 86) || (id == 103))
		{	Player p = event.getPlayer();
			RPG_Player rp = mgr_Player.getPlayer(p.getName().hashCode());
			SpoutPlayer sp;
			if(rp == null)
			{
				p.sendMessage("rpgplayer == null");
			}
			//RPG_Character cp = rp.getCharacter();
			 sp = SpoutManager.getPlayer(p);
					
			// Make sure player is holding the proper tool for destroying crops
			ItemStack item = p.getItemInHand();
			if(item == null || !((item.getTypeId() >= 290) && (item.getTypeId() <= 294)))
			{	sp.sendNotification("Harvest", "Wrong tool for harvesting", b.getType());
				event.setCancelled(true);
				return;
			} // if(item == null)	
			
			// Now check that the player can farm, and if so has enough skill for the tool
			// they are using.
			
			b.setTypeId(3);
		} // if(b.getType() == Material.CROPS)		
	} // public void onBlockBreak(BlockBreakEvent event)
	
	public listener_Block(Plugin p)
	{
		Bukkit.getServer().getPluginManager().registerEvents(this, p);
		rndSeed = new Random(System.currentTimeMillis());
	 } // public listener_Player(Plugin p)
}
