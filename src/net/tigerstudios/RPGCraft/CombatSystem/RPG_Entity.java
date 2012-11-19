package net.tigerstudios.RPGCraft.CombatSystem;


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
		
	private int EntID = -1;
	
	// Getters and Setters
	public int getLevel() { return level; } 	public void setLevel(final int lvl) { level = lvl;}
	public float getSpeed() {return speed;}		public void setSpeed(final float spd) { this.speed = spd; }
	
	public int getHealth() {return this.health; }			public void setHealth(final int hp) { this.health = hp; }
	public int getDefense() { return this.defense; }		public void setDefense(final int def) { this.defense = def; }
	public int getAttack() { return attack; }				public void setAttack(final int atk) { this.attack = atk; }
	public int getParry() { return parry; }					public void setParry(final int par) { this.parry = par; }
	public float getArmorClass() {return armorClass; }		public void setArmorClass(final float ac) { this.armorClass = ac; }
	public float getWeaponDamage() {return weaponDamage;}	public void setWeaponDamage(final float damageValue) {this.weaponDamage = damageValue; }
	public int getStrength() { return strength; }			public void setStrength(final int str) { this.strength = str; }
	public int getIntelligence() { return intelligence; }	public void setIntelligence(final int intel) { this.intelligence = intel; }
	public int getDexterity() { return dexterity; }			public void setDexterity(final int dex) { this.dexterity = dex; }
	public int getConstitution() { return constitution; }	public void setConstitution(int con) { this.constitution = con; }
	public int getEntityID() {	return EntID;}				public void setEntityID(int entID) {EntID = entID; }
	
} // public abstract class RPG_Entity
