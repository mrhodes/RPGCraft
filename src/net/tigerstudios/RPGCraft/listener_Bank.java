// Temporarily Disabled this entire class.  It was created before
// coins naturally dropped off mobs.  I am keeping the code here just in 
// case it needs to be used again, perhaps as a testing tool or a moderator
// feature.
// Michael Rhodes ~ May 25, 2012

package net.tigerstudios.RPGCraft;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;


public class listener_Bank implements Listener {
	private Server rpgServer = null;
	
	//private PropertiesFile playerBalances = null;
	//private PropertiesFile transactionHistory = null;
			
	//public void onPlayerCommandPreprocess(PlayerChatEvent event)
	public boolean bankProcessor(CommandSender sender, Command command,	String label, String[] cmd)
	{
		/*Player p = rpgServer.getPlayer(sender.getName());
						
		// Make sure the banking system is enabled, and then make sure the player
		// issuing the command indeed has permissions to do so.				
		
		if(command.getName().equalsIgnoreCase("banker"))
		{
			if(!RPGCraft.pexMan.has(p, "rpgcraft.bank.banker"))
			{	p.sendMessage("[§2RPG§f] §4You do not have access to this command.");
				return true;
			}
			
			if(cmd.length < 4)
			{	p.sendMessage("§aBanker Command Help");
				p.sendMessage(" ");
				p.sendMessage("Usage: /banker (§6g§f) (§7s§f) (§cc§f) <playername>");
				p.sendMessage(" ");
				p.sendMessage("The banker command will give a player specified some coin");
				p.sendMessage("You must put a value in for gold, silver, and copper.");
				return true;
			}
			
			// Command to give coin to a player
			int gold = Integer.parseInt(cmd[0]);
			int silver = Integer.parseInt(cmd[1]);
			int copper = Integer.parseInt(cmd[2]);
			
			Player pReceiver = rpgServer.matchPlayer(cmd[3]).get(0);
			if(pReceiver!=null)
			{
				RPG_Player rpgPlayer = mgr_Player.getPlayer(pReceiver.getName().hashCode());
				if(rpgPlayer != null)
				{
					rpgPlayer.setGold(rpgPlayer.getGold() + gold);
					rpgPlayer.setSilver(rpgPlayer.getSilver() + silver);
					rpgPlayer.setCopper(rpgPlayer.getCopper() + copper);
					p.sendMessage("[§2RPG§f] The transaction with "+pReceiver.getDisplayName()+" is now complete.");
					p.sendMessage("[§2RPG§f] §6"+gold+" Gold§f, §7"+silver+" Silver§f, and §c"+copper+" Copper §fhas been sent to "+pReceiver.getDisplayName()+".");
					pReceiver.sendMessage("[§2RPG§f] The banker has paid you §6"+gold+" Gold§f, §7"+silver+" Silver§f, and §c"+copper+" Copper§f.");
					pReceiver.sendMessage("[§2RPG§f] You now have §6"+rpgPlayer.getGold()+" gold§f, §7"+rpgPlayer.getSilver()+" silver§f, and §c"+rpgPlayer.getCopper()+" copper§f.");
					
					// Save transaction
					String transaction = "Banker: "+p.getName()+" Money: "+gold+"G, "+silver+"S, and "+copper+"C Receiver: "+rpgPlayer.getMCName();
					String date = Calendar.getInstance().getTime().toString();
					transactionHistory.setString(date, transaction, "Bank Transaction");
					transactionHistory.save();
					
					// Save updated player balance
					String receiverBalance = rpgPlayer.getGold()+","+rpgPlayer.getSilver()+","+rpgPlayer.getCopper();
					playerBalances.setString(rpgPlayer.getMCName(), receiverBalance, "Last updated: "+date);
					playerBalances.save();					
					
					return true;
				} // if(PlayerManager.validatePlayer(pReceiver, rpgPlayer, false))
			} // if(pReceiver!=null)					
		} // if(cmd[0].equalsIgnoreCase("/banker"))	
	*/
		return false;
	} // public void onPlayerCommandPreprocess(PlayerChatEvent event)
	
	
	public listener_Bank(Plugin p)
	{	this.rpgServer = p.getServer();
		
		//playerBalances = new PropertiesFile(RPGCraft.mainDirectory+"logs"+File.separatorChar+"playerBalances.log");
		//transactionHistory = new PropertiesFile(RPGCraft.mainDirectory+"logs"+File.separatorChar+"transactionHistory.log");
	} // public listener_Bank(Plugin p)
}
