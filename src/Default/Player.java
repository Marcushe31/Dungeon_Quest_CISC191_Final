/**
 * Lead Author(s):
 * 
 * @author Patrick Tran
 * @author Marcus Hernandez
 *
 *         Other Contributors:
 *
 *         References:
 *         Morelli, R., & Walde, R. (2016).
 *         Java, Java, Java: Object-Oriented Problem Solving
 *         https://open.umn.edu/opentextbooks/textbooks/java-java-java-object-oriented-problem-solving
 *
 *
 *         Version: 2026-05-04
 */
package Default;

import java.util.ArrayList;

/**
 * Purpose: Player holds all the stats and inventory for a playable character.
 * Subclasses (Archer, Mage, Warrior) set their own starting values.
 *
 * Player is a base class extended by each character class.
 */
public class Player
{
	private String characterClass;
	private int health;
	private int maxHealth;
	private int mana;
	private int stamina;
	private int damage;
	private ArrayList<Skill> skills;
	private ArrayList<Item> inventory;

	
	public void addSkill(Skill skill)
	{
		skills.add(skill);
	}

	public void addItem(Item item)
	{
		inventory.add(item);
	}
	
	// each profession has their own skill, which depends on the
	// index of the arraylist<Skill>
	public Skill getSkill(int index) {
		return skills.get(index);
	}
	
	public int getSkillCount()
	{
		return skills.size();
	}

	public Item getItem(int index)
	{
		return inventory.get(index);
	}

	public int getItemCount()
	{
		return inventory.size();
	}

	public Item removeItem(int index)
	{
		return inventory.remove(index);
	}

	/**
	 * Creates a Player with the given stats. maxHealth is set from the starting health value.
	 *
	 * @param characterClass the player's class name (e.g. "Archer")
	 * @param health starting and max health
	 * @param mana starting mana
	 * @param stamina starting stamina
	 * @param damage base attack damage
	 */
	public Player(String characterClass, int health, int mana, int stamina, int damage)
	{
		this.skills = new ArrayList<Skill>();
		this.inventory = new ArrayList<Item>();
		this.characterClass = characterClass;
		this.health = health;
		// save max so we can cap healing later
		this.maxHealth = health;
		this.mana = mana;
		this.stamina = stamina;
		this.damage = damage;
	}

	public void attack()
	{
		System.out.println("Player attacks with " + damage
				+ " damage! Remaining enemy HP is");
	}

	public void takeDamage(int damage)
	{
		health -= damage;

		if (health < 0)
		{
			health = 0;
		}
		// System.out.println("Player takes " + damage + " damage! Remaining
		// health: " + health);
	}

	/**
	 * Heals the player by the given amount without going over maxHealth.
	 *
	 * @param amount how much health to restore
	 */
	public void heal(int amount)
	{
		if (amount > 0)
		{
			health += amount;
			// dont let health go above the max
			if (health > maxHealth)
			{
				health = maxHealth;
			}
		}
	}

	public void useItem(int index)
	{
		Item item = removeItem(index);
		item.use(this);
	}

	public boolean isAlive()
	{
		return health > 0;
	}

	/**
	 * Returns the player's current health.
	 *
	 * @return current health
	 */
	public int getHealth()
	{
		return health;
	}

	/**
	 * Returns the player's max health.
	 *
	 * @return max health
	 */
	public int getMaxHealth()
	{
		return maxHealth;
	}

	public int getDamage()
	{
		return damage;
	}

	public String getCharacterClass()
	{
		return characterClass;
	}
}
