package net.tigerstudios.RPGCraft.CombatSystem;

import org.bukkit.entity.LivingEntity;


public class RPG_Animal extends RPG_Entity {

	//
	public RPG_Animal(LivingEntity ent, int level) 	
	{
		setEntityID(ent.getEntityId());
		entType = ent.getType();
		
		this.intelligence = 3;
		this.dexterity = 3;
		this.constitution = 3;
			
		this.weaponDamage = 2;
		this.armorClass = 5;
		this.attack = 2;
		this.defense = 2;		
		
		setLevel(level);		
	} // public Mob(LivingEntity ent)	
}
