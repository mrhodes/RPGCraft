package net.tigerstudios.RPGCraft.CombatSystem;

import net.tigerstudios.RPGCraft.RPGCraft;
import net.tigerstudios.RPGCraft.RPG_Character;
import net.tigerstudios.RPGCraft.mgr_Player;
import net.tigerstudios.RPGCraft.utils.RandomGen;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

// 
public class CombatSystem implements Listener{
	// ------------------------------------------------------------------------------------
	// This method will be used to determine the amount of damage dealt by the attacker
	// and return the amount to the calling method.
	// 
	// Going to use D&D rules for combat, Comments will be provided throughout code.
	// type = 0 for a  melee attack, and 1 for range attack.
	public static int calculateDamage(RPG_Entity attacker, RPG_Entity defender, int type)
	{
		boolean bFail = false;
		if(attacker == null){	System.out.println("Attacker = Null"); bFail = true; }
		if(defender == null){	System.out.println("Defender = Null"); bFail = true; }
		if(bFail)
		{	RPGCraft.log.info(" Failed to aquire subjects in combat.");
						
			if(attacker != null) RPGCraft.log.info("  attacker = "+ attacker.toString());
			if(defender != null) RPGCraft.log.info("  defender = "+ defender.toString());
			
			return -1;
		}
		
		// First make sure the armor and weapons stats are updated
		if(attacker instanceof RPG_Character)
			updateWeaponStats(mgr_Player.getMCPlayer(((RPG_Character) attacker).getAccountID()));
		if(defender instanceof RPG_Character)
			updateArmorStats(mgr_Player.getMCPlayer(((RPG_Character) defender).getAccountID()));
				
		// Overall damage to be done to the entity
		int dmg = 0;
		int attackBonus = 1;	// This is the entities bonus to their attack based on other skills
		
		// Attacker rolls a D20, if a 1 auto miss, 20 is auto hit.
		int attackRoll = RandomGen.rollDice(20, 1, 0);
		if(attackRoll == 1)	{ return 0; }
					
		if(type == 0) attackBonus+=attacker.getAttack();	// Melee
		if(type == 1) attackBonus+=attacker.getAttack();	// Ranged
		
		attackBonus+=attacker.getWeaponDamage() + (attacker.getStrength() * 1.4);
		
		// Add Code to detect a Villager being attacked, animals 2
		float defenseBonus = (float) (defender.getArmorClass() + (defender.getDefense() * defender.getDexterity() * 0.75));
		
		if(attackRoll == 20)
		{	// Chance of Critical hit
			if( (RandomGen.rollDice(20, 1, 0) + attackBonus) > defenseBonus);		// 10 % chance to crit
				attackBonus*=1.75;	
		
		} // if(attackRoll == 20)	
		
		dmg = attackBonus - RandomGen.rollDice(8, 1, (int) (defenseBonus/2));
		
		// decrease damage a little if player is blocking.
		if(defender instanceof Player)
		{	if( ((Player)defender).isBlocking() )
			{	int blocking = RandomGen.rollDice(8, 1, defender.getParry());
				dmg -= blocking;
				mgr_Player.getMCPlayer(((RPG_Character) defender).getAccountID()).sendMessage("Blocking reduced Damage by: "+blocking);
			}
		}
		if(dmg < 1) dmg = 1;
		
		// Debugging messages
		
		Bukkit.broadcastMessage("Real Damage: "+dmg);		
			
		if(attacker instanceof RPG_Character){
			mgr_Player.getMCPlayer(((RPG_Character) attacker).getAccountID()).sendMessage("Your attackBonus is: "+attackBonus);
			mgr_Player.getMCPlayer(((RPG_Character) attacker).getAccountID()).sendMessage("Defenders defenseBonus is: "+defenseBonus);
			mgr_Player.getMCPlayer(((RPG_Character) attacker).getAccountID()).sendMessage("Damage done: "+dmg);
		}
		
		if(defender instanceof RPG_Character){
			mgr_Player.getMCPlayer(((RPG_Character) defender).getAccountID()).sendMessage("Their attackBonus is: "+attackBonus);
			mgr_Player.getMCPlayer(((RPG_Character) defender).getAccountID()).sendMessage("Your defenseBonus is: "+defenseBonus);
			mgr_Player.getMCPlayer(((RPG_Character) defender).getAccountID()).sendMessage("Damage taken: "+dmg);
		}
		
		return dmg;
	} // public void calculateDamage(RPG_Character rpgChar, RPG_Mob mob, boolean bcharHit)
		
	public static void updateArmorStats(Player p)
	{
		float ac = 0;	// Default for wearing nothing
		
/* These are the Default durability max for armor
Material	Helmet	Chestplate	Leggings	Boots
Leather		56		81			76			66
Gold		78		113			106			92
Chain/Iron	166		241			226			196
Diamond		364		529			496			430
*/
		// Check Helmet
		ItemStack armor;
		int type, dur;
		float acValue = 0;
				
		armor = p.getInventory().getHelmet();
		if(armor != null)
		{	type = armor.getTypeId();   dur = armor.getDurability();
			if(type == 298) { acValue = 2 * (56f - dur) / 56; }			// Leather
			if(type == 302) { acValue = 3.5f * (166f - dur) / 166; }	// Chain
			if(type == 306) { acValue = 5f * (166f - dur) / 166; }		// Iron
			if(type == 310) { acValue = 7f * (364f - dur) / 364; }		// Diamond
			if(acValue < 1)	ac+=1;
			else ac += acValue;		
		}else ac+=1;		
		
		armor = p.getInventory().getChestplate(); acValue = 0;
		if(armor != null) 
		{	type = armor.getTypeId();   dur = armor.getDurability();
			if(type == 299) { acValue = 4  * (81f - dur) / 81 ;}
			if(type == 303) { acValue = 5.75f * (241f - dur) / 241 ;}
			if(type == 307) { acValue = 8 * (241f - dur) / 241 ;}
			if(type == 311) { acValue = 11.5f * (529f - dur) / 529 ;}
			if(acValue < 2)	ac+=2;
			else ac += acValue;		
		}else ac+=2;
				
		armor = p.getInventory().getLeggings(); acValue = 0;
		if(armor != null) 
		{	type = armor.getTypeId();   dur = armor.getDurability();
			if(type == 300) { acValue = 3.5f * (76f - dur) / 76 ;}
			if(type == 304) { acValue = 4.0f * (226f - dur) / 226 ;}
			if(type == 308) { acValue = 6.75f * (226f - dur) / 226;}
			if(type == 312) { acValue = 9.5f * (496f - dur) / 496 ;}
			if(acValue < 2)	ac+=2;
			else ac += acValue;			
		}else ac+=2;		
		
		armor = p.getInventory().getBoots(); acValue = 0;
		if(armor != null)
		{	type = armor.getTypeId();   dur = armor.getDurability();
			if(type == 301) { acValue = 3.25f * (66f - dur) / 66 ;}
			if(type == 305) { acValue = 4.75f * (196f - dur) / 196 ;}
			if(type == 309) { acValue = 5.75f * (196f - dur) / 196 ;}	
			if(type == 313) { acValue = 8 * (430f - dur) / 430 ;}
			if(acValue < 2)	ac+=2;
			else ac += acValue;		
		}else ac+=2;
		
		mgr_Player.getCharacter(p).setArmorClass(ac);
		
		p.sendMessage("DEBUG: Armor Rating: "+ac);		
	} // public static void updateArmorStats(Player p)
	
	
	public static void updateWeaponStats(Player p)
	{
		float DamageValue = 1;
		// Find out if the player has switched to a weapon
		// and update their weaponDamage value accordingly.  If not using 
		// a weapon, then weaponDamage must be set to 1
		int typeID = 0;	int dur = 0;
		
		if(p.getItemInHand() != null) 
		{	typeID = p.getItemInHand().getTypeId();
			dur = p.getItemInHand().getDurability();
		}
		// Set the Dmg value based on the sword the player is now
		// holding and the damage value of the sword
		if(typeID == 268) DamageValue = 10 * (60 - dur) / 60;	// Wooden Sword
		if(typeID == 283) DamageValue = 10 * (33 - dur) / 33;	// Gold Sword
		if(typeID == 272) DamageValue = 15 * (132 - dur) / 132;	// Stone Sword
		if(typeID == 267) DamageValue = 20 * (251 - dur) / 251;	// Iron Sword
		if(typeID == 276) DamageValue = 35 * (1562 - dur) / 1562;	// Diamond Sword
		
		// Put in error detection to make sure player has a character made
		mgr_Player.getCharacter(p).setWeaponDamage(DamageValue);		
	} // public static void updateWeaponStats(Player p, int slot)
		
	
	public CombatSystem(){	} // public CombatSystem()	
		

	
} // public class CombatSystem implements Listener
