package net.tigerstudios.RPGCraft.CombatSystem;

import org.bukkit.entity.EntityType;


// ---------------------------------------------------------------------------
// Base Entity interface for the Character and Mob classes to derive from
// This interface will hold the common properties of each type of entity
public abstract class RPG_Entity {
	protected int level = 1;
	protected float speed = 0.0f;
	protected int health = 0;
	protected int maxHealth = 0;
	protected int defense = 0;
	protected int attack = 0;
	protected int parry = 0;
	protected float armorClass = 0;
	protected float weaponDamage = 0;
	protected int strength = 0;
	protected int intelligence = 0;
	protected int dexterity = 0;
	protected int constitution = 0;
	
	public EntityType entType;
	
	private int EntID = -1;
	
	public float getArmorClass() {return armorClass; } 		public int getAttack() { return attack; }
	public int getConstitution() { return constitution; }	public int getDefense() { return this.defense; }
	public int getDexterity() { return dexterity; }			public int getEntityID() {	return EntID;}
	public int getHealth() {return this.health; }			public int getIntelligence() { return intelligence; }
	// Getters and Setters
	public int getLevel() { return level; }					public int getParry() { return parry; }
	public float getSpeed() {return speed;}					public int getStrength() { return strength; }
	public float getWeaponDamage() {return weaponDamage;}	public void setArmorClass(final float ac) { this.armorClass = ac; }
	public void setAttack(final int atk) { this.attack = atk; }		public void setConstitution(int con) { this.constitution = con; }
	public void setDefense(final int def) { this.defense = def; }	public void setDexterity(final int dex) { this.dexterity = dex; }
	public void setEntityID(int entID) {EntID = entID; }	public void setHealth(final int hp) { this.health = hp; }
	public void setIntelligence(final int intel) { this.intelligence = intel; }		public void setLevel(final int lvl) { level = lvl;}
	public void setParry(final int par) { this.parry = par; }	public void setSpeed(final float spd) { this.speed = spd; }
	public void setStrength(final int str) { this.strength = str; }				public void setWeaponDamage(final float damageValue) {this.weaponDamage = damageValue; }
	
	
	// Methods all Entities share
	
	
	
	
} // public abstract class RPG_Entity
