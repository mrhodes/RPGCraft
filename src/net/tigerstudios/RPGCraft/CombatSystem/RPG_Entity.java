package net.tigerstudios.RPGCraft.CombatSystem;



// ---------------------------------------------------------------------------
// Base Entity interface for the Character and Mob classes to derive from
// This interface will hold the common properties of each type of entity
public abstract class RPG_Entity {
	protected int level;
	protected float speed;
	protected int health, maxHealth;
	protected int defense, attack, parry;
	protected int strength, intelligence, dexterity, constitution;
	
	protected int EntID;	
	
	// Getters and Setters
	public int getLevel(){ return level; }                	public void setLevel(int lvl) { this.level = lvl; }
	public float getSpeed() { return speed; }				public void setSpeed(float spd) { this.speed = spd; }
	public int getHealth() { return health; }				public void setHealth(int hp) { this.health = hp; }
	public int getDefense() { return defense; }				public void setDefense(int def) { this.defense = def; }
	public int getAttack() { return attack; }				public void setAttack(int atk) { this.attack = atk; }
	public int getParry() { return parry; }					public void setParry(int par) { this.parry = par; }
	public int getStrength() { return strength; }			public void setStrength(int str) { this.strength = str; }
	public int getIntelligence() { return intelligence; }	public void setIntelligence(int intel) { this.intelligence = intel; }
	public int getDexterity() { return dexterity; }			public void setDexterity(int dex) { this.dexterity = dex; }
	public int getConstitution() { return constitution; }	public void setConstitution(int con) { this.constitution = con; }
		
	
} // public abstract class RPG_Entity
