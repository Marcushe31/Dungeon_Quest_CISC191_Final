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
 * Purpose: The reponsibility of Player is ...
 *
 * Player is-a ...
 * Player is ...
 */
public class Player
{
	private String characterClass;
	private int health;
	private int mana;
	private int stamina;
	private int damage;
	private ArrayList<Skill> skills;

	
	public void addSkill(Skill skill)
	{
		skills.add(skill);
	}
	
	// each profession has their own skill, which depends on the
	// index of the arraylist<Skill>
	public Skill getSkill(int index) {
		return skills.get(index);
	}

	public Player( String characterClass, int health, int mana, int stamina,
			int damage)
	{
		this.skills = new ArrayList<Skill>();
		this.characterClass = characterClass;
		this.health = health;
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

	public boolean isAlive()
	{
		return health > 0;
	}

	public int getHealth()
	{
		return health;
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
