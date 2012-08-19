package net.tigerstudios.RPGCraft.CombatSystem;

import net.tigerstudios.RPGCraft.RPG_Character;
import net.tigerstudios.RPGCraft.RPG_Player;
import net.tigerstudios.RPGCraft.mgr_Player;
import net.tigerstudios.RPGCraft.utils.MathMethods;

import org.bukkit.Material;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.getspout.spoutapi.player.SpoutPlayer;

// 
public class CombatSystem implements Listener{
	private Player mcPlayer = null;
	private SpoutPlayer sPlayer = null;
	
	@EventHandler
	public void onEntityDamageByEntity(final EntityDamageByEntityEvent event)
	{
		// If a player is not involved, exit now
		if(!(event.getEntity() instanceof Player) && !(event.getDamager() instanceof Player))
			return;	
		
		RPG_Entity attacker = null; RPG_Entity defender = null;
		
		if(event.getDamager() instanceof Projectile)
		{	if(event.getEntity() instanceof Player)
			{	//defender = mgr_Player.getCharacter((Player)event.getEntity());
				mcPlayer = (Player) event.getEntity();
				RPG_Player rpgP = mgr_Player.getPlayer(mcPlayer.getName().hashCode());
				
				if(rpgP != null)
					rpgP.getSpoutPlayer().sendNotification("Damage", "You've been hit for "+event.getDamage(), Material.APPLE);
				
				return;
			} // if(event.getEntity() instanceof Player)			
		} // if(event.getDamager() instanceof Projectile)	
				
		// Get the Attacker and Defender
		if(event.getDamager() instanceof Player) attacker = mgr_Player.getCharacter((Player)event.getDamager());
		if(event.getDamager() instanceof Monster)attacker = mgr_Mob.getMob(event.getDamager().getEntityId());
		if(event.getEntity() instanceof Player)	defender = mgr_Player.getCharacter((Player)event.getEntity());
		if(event.getEntity() instanceof Monster)defender = mgr_Mob.getMob(event.getEntity().getEntityId());
		
		event.setDamage(calculateDamage(attacker, defender, 0));
				
		/*if(event.getDamager() instanceof Zombie)
		{
			// Play random sound effect...
			if(MathMethods.rnd.nextInt(1000 + 1) <= 100)
				SpoutManager.getSoundManager().playCustomSoundEffect(RPGCraft.getPlugin(),
					SpoutManager.getPlayer((Player)event.getEntity()), 
					RPGCraft.webBase+"sounds/ZombieComeHere.ogg", false);			
		} // if(event.getDamager() instanceof Monster)*/
		
	} // public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	
	
	@EventHandler
	public void onEntityShootBow(EntityShootBowEvent event)
	{	// Find out if this is a player 
		if(event.getEntity() instanceof Player)
		{	mcPlayer = (Player) event.getEntity();
			RPG_Character rpgChar = mgr_Player.getCharacter(mcPlayer);
			if(rpgChar != null)
			{	if(rpgChar.race.equalsIgnoreCase("elf"))
				{	event.getProjectile().setVelocity(event.getProjectile().getVelocity().multiply(1.5f));
					return;
				}
				
				if(!event.getBow().getEnchantments().isEmpty())
				{	mcPlayer.sendMessage("[§2RPG§f] Only Elves are able to use enchanted bows.");
					event.setCancelled(true);
					return;
				}
			} // if(rpgChar != null)			
		} // if(event.getEntity() instanceof Player)
	} // public void onEntityShootBow(EntityShootBowEvent event)
	
	
	// ------------------------------------------------------------------------------------
	// This method will be used to determine the amount of damage dealt by the attacker
	// and return the amount to the calling method.
	// 
	// Going to use D&D rules for combat, Comments will be provided throughout code.
	// type = 0 for a  melee attack, and 1 for range attack.
	public static int calculateDamage(RPG_Entity attacker, RPG_Entity defender, int type)
	{
		int dmg = 0;
		int attackBonus = 0;
		
		// Temporary values for debugging
		Player p = null;
		if(attacker instanceof RPG_Character) p = mgr_Player.getMCPlayer(((RPG_Character) attacker).getAccountID());
		if(defender instanceof RPG_Character) p = mgr_Player.getMCPlayer(((RPG_Character) defender).getAccountID());
		
		// Attacker rolls a D20, if a 1 auto miss, 20 is auto hit.
		int attackRoll = MathMethods.rnd.nextInt(20) + 1;
		if(attackRoll == 1)
		{	p.sendMessage("Missed!");
			return 0;
		}
		if(attackRoll == 20)
		{	// Chance of Critical hit
			p.sendMessage("Crit!");
			dmg+=2;
		}
		
		if(type == 0)		// Melee Attack
			attackBonus+=attacker.getStrength();
		if(type == 1)		// Range Attack
			attackBonus+=attacker.getDexterity();
		
		// Calculate the damage done to the defender
		dmg+=attackBonus;		
		
		if(attacker instanceof RPG_Character) {p.sendMessage("Hit for "+dmg+" HP"); }
		return dmg;
	} // public void calculateDamage(RPG_Character rpgChar, RPG_Mob mob, boolean bcharHit)
	
	
	public CombatSystem(){	} // public CombatSystem(Plugin p)	
	
} // public class CombatSystem implements Listener
