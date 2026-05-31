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
 * Player implements Attackable so it can be used in combat.
 */
public class Player implements Attackable
{
	private String characterClass;
	private int health;
	private int maxHealth;
	private int mana;
	private int maxMana;
	private int stamina;
	private int maxStamina;
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
	
	/**
	 * Returns the skill at the given index.
	 * Returns null if the index is out of range.
	 *
	 * @param index position in the skills list
	 * @return the skill, or null if index is invalid
	 */
	// each profession has their own skill, which depends on the index of the ArrayList
	public Skill getSkill(int index) {
		try {
			return skills.get(index);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("No skill at that index.");
			return null;
		}
	}
	
	public int getSkillCount()
	{
		return skills.size();
	}

	/**
	 * Returns the item at the given index.
	 * Returns null if the index is out of range.
	 *
	 * @param index position in the inventory list
	 * @return the item, or null if index is invalid
	 */
	public Item getItem(int index)
	{
		try {
			return inventory.get(index);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("No item at that index.");
			return null;
		}
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
	/**
	 * Creates a Player with the given stats.
	 * Max values are saved from the starting values so we can track them later.
	 *
	 * @param characterClass the player's class (e.g. "Archer")
	 * @param health starting health
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
		// same thing for mana and stamina
		this.maxMana = mana;
		this.stamina = stamina;
		this.maxStamina = stamina;
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
	
	public void setHealth(int health)
	{
		this.health = health;
	}

	/**
	 * Returns the player's current mana.
	 *
	 * @return current mana
	 */
	public int getMana()
	{
		return mana;
	}

	/**
	 * Returns the player's max mana.
	 *
	 * @return max mana
	 */
	public int getMaxMana()
	{
		return maxMana;
	}

	/**
	 * Returns the player's current stamina.
	 *
	 * @return current stamina
	 */
	public int getStamina()
	{
		return stamina;
	}

	/**
	 * Returns the player's max stamina.
	 *
	 * @return max stamina
	 */
	public int getMaxStamina()
	{
		return maxStamina;
	}

	/**
	 * Deducts mana by the given amount. Wont go below 0.
	 *
	 * @param amount how much mana to use
	 */
	public void useMana(int amount)
	{
		mana -= amount;
		if (mana < 0)
		{
			mana = 0;
		}
	}

	/**
	 * Required by Attackable. Passes damage to takeDamage.
	 *
	 * @param damage incoming damage
	 */
	@Override
	public void receivedDamage(int damage)
	{
		takeDamage(damage);
	}
}
