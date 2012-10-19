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
import org.bukkit.inventory.ItemStack;

// 
public class CombatSystem implements Listener{
	private Player mcPlayer = null;
	
	
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
	} // public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	
	
	@EventHandler
	public void onEntityShootBow(EntityShootBowEvent event)
	{	// Find out if this is a player 
		if(event.getEntity() instanceof Player)
		{	mcPlayer = (Player) event.getEntity();
			RPG_Character rpgChar = mgr_Player.getCharacter(mcPlayer);
			if(rpgChar != null)
			{	if(rpgChar.race.equalsIgnoreCase("elf"))	// TODO: Remove Race specific reference
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
		if(attacker == null)
		{	System.out.println("Attacker = Null");
		return 0;
		}
		
		if(defender == null)
		{	System.out.println("Defender = Null");
		return 0;
		}
		
		// Overall damage to be done to the entity
		int dmg = 0;
		int attackBonus = 0;	// This is the entities bonus to their attack based on other skills
		
		// Attacker rolls a D20, if a 1 auto miss, 20 is auto hit.
		int attackRoll = MathMethods.rnd.nextInt(20) + 1;
		if(attackRoll == 1)	{ return 0; }
					
		if(type == 0) attackBonus+=attacker.getAttack();	// Melee
		if(type == 1) attackBonus+=attacker.getAttack();	// Ranged
		
		attackBonus+=attacker.getWeaponDamage();		
								
		if(attackRoll == 20)
		{	// Chance of Critical hit
			if((MathMethods.rnd.nextInt(20) + 1) > 18);		// 10 % chance to crit
				attackBonus*=1.5;						
		} // if(attackRoll == 20)		
				
		// Now calculate how much of this attack is going to be defended		
		// Defense, ArmorClass, Strength
		
		// Add Code to detect a Villager being attacked, animals 2
		int defenseBonus = defender.getArmorClass() + defender.getDefense();
		
		// Highest armor rating is 65
		// Lowest is 10
		dmg = attackBonus;		
		
		if(attacker instanceof RPG_Character){
			mgr_Player.getMCPlayer(((RPG_Character) attacker).getAccountID()).sendMessage("Your attackBonus is: "+attackBonus);
			mgr_Player.getMCPlayer(((RPG_Character) attacker).getAccountID()).sendMessage("Zombie's defenseBonus is: "+defenseBonus);
			mgr_Player.getMCPlayer(((RPG_Character) attacker).getAccountID()).sendMessage("Damage: "+dmg);
		}
		
		if(defender instanceof RPG_Character){
			mgr_Player.getMCPlayer(((RPG_Character) defender).getAccountID()).sendMessage("Zombie's attackBonus is: "+attackBonus);
			mgr_Player.getMCPlayer(((RPG_Character) defender).getAccountID()).sendMessage("Your defenseBonus is: "+defenseBonus);
			mgr_Player.getMCPlayer(((RPG_Character) defender).getAccountID()).sendMessage("Damage: "+dmg);
		}
		
		return dmg;
	} // public void calculateDamage(RPG_Character rpgChar, RPG_Mob mob, boolean bcharHit)
	
	
	public static void updateWeaponStats(Player p)
	{
		int DamageValue = 1;
		// Find out if the player has switched to a weapon
		// and update their weaponDamage value accordingly.  If not using 
		// a weapon, then weaponDamage must be set to 1
		int typeID = 0;
		if(p.getInventory().getItemInHand() != null) 
			typeID = p.getInventory().getItemInHand().getTypeId();
				
		// Set the Dmg value based on the sword the player is now
		// holding
		if(typeID == 268) DamageValue = 5;	// Wooden Sword
		if(typeID == 283) DamageValue = 6;	// Gold Sword
		if(typeID == 272) DamageValue = 8;	// Stone Sword
		if(typeID == 267) DamageValue = 12;	// Iron Sword
		if(typeID == 276) DamageValue = 17;	// Diamond Sword
		
		// Put in error detection to make sure player has a character made
		mgr_Player.getCharacter(p).setWeaponDamage(DamageValue);		
	} // public static void updateWeaponStats(Player p, int slot)
	
	public static void updateArmorStats(Player p)
	{
		int ac = 0;	// Default for wearing nothing
						
		// Check Helmet
		ItemStack armor = p.getInventory().getHelmet();
		if(armor != null)
		{	if(armor.getTypeId() == 298) { ac+= 3; }			// Leather
			if(armor.getTypeId() == 306) { ac+= 8; }			// Iron
			if(armor.getTypeId() == 310) { ac+= 10; }			// Diamond
		}else
			ac+=1;
		
		armor = p.getInventory().getChestplate();
		if(armor == null) ac+=4;
		else{
			if(armor.getTypeId() == 299) { ac+= 8; }		
			if(armor.getTypeId() == 307) { ac+= 14; }
			if(armor.getTypeId() == 311) { ac+= 15; }
		}
		
		armor = p.getInventory().getLeggings();
		if(armor == null) ac+=3;
		else{
			if(armor.getTypeId() == 300) { ac+= 6; }		
			if(armor.getTypeId() == 308) { ac+= 10; }
			if(armor.getTypeId() == 312) { ac+= 15; }
		}
		
		armor = p.getInventory().getBoots();
		if(armor == null) ac+=2;
		else{
			if(armor.getTypeId() == 301) { ac+= 3; }		
			if(armor.getTypeId() == 309) { ac+= 8; }		
			if(armor.getTypeId() == 313) { ac+= 10; }		
		}
		
		mgr_Player.getCharacter(p).setArmorClass(ac);
		p.sendMessage("DEBUG: Armor Rating: "+ac);		
	} // public static void updateArmorStats(Player p)
	
	public CombatSystem(){	} // public CombatSystem()	
	
} // public class CombatSystem implements Listener
