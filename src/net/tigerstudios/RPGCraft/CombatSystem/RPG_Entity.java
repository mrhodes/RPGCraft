package net.tigerstudios.RPGCraft.CombatSystem;

import org.bukkit.entity.Entity;

// ---------------------------------------------------------------------------
// Base Entity interface for the Character and Mob classes to derive from
// This interface will hold the common properties of each type of entity
public interface RPG_Entity extends Entity{
	int level = 0;
	float speed = 0.0f;
	int health = 0;
	int maxHealth = 0;
	int defense = 0;
	int attack = 0;
	int parry = 0;
	int armorClass = 0;
	int weaponDamage = 0;
	int strength = 0, intelligence = 0, dexterity = 0, constitution = 0;
		
	int EntID = -1;	
	
	// Getters and Setters
	public int getLevel(){ return level; }                	public void setLevel(final int lvl) { this.level = lvl; }
	public float getSpeed() { return speed; }				public void setSpeed(final float spd) { this.speed = spd; }
	public int getHealth() { return health; }				public void setHealth(final int hp) { this.health = hp; }
	public int getDefense() { return defense; }				public void setDefense(final int def) { this.defense = def; }
	public int getAttack() { return attack; }				public void setAttack(final int atk) { this.attack = atk; }
	public int getParry() { return parry; }					public void setParry(final int par) { this.parry = par; }
	public int getArmorClass() {return armorClass; }		public void setArmorClass(final int ac) { this.armorClass = ac; }
	public int getWeaponDamage() {return weaponDamage;}		public void setWeaponDamage(final int wd) {this.weaponDamage = wd; }
	public int getStrength() { return strength; }			public void setStrength(final int str) { this.strength = str; }
	public int getIntelligence() { return intelligence; }	public void setIntelligence(final int intel) { this.intelligence = intel; }
	public int getDexterity() { return dexterity; }			public void setDexterity(final int dex) { this.dexterity = dex; }
	public int getConstitution() { return constitution; }	public void setConstitution(int con) { this.constitution = con; }
	
} // public abstract class RPG_Entity
