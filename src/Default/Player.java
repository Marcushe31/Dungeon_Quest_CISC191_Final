/**
* Lead Author(s):
* @author Patrick Tran
* @author Marcus Hernandez
*
* Other Contributors:
*
* References:
* Morelli, R., & Walde, R. (2016).
* Java, Java, Java: Object-Oriented Problem Solving
* https://open.umn.edu/opentextbooks/textbooks/java-java-java-object-oriented-problem-solving
*
*
* Version: 2026-05-04
*/
package Default;

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
	
	public Player(String characterClass, int health, int mana, int stamina, int damage)
	{
		this.characterClass = characterClass;
		this.health = health;
		this.mana = mana;
		this.stamina = stamina;
		this.damage = damage;
	}
	
	public void attack()
	{
		System.out.println("Player attacks with " + damage + " damage! Remaining enemy HP is");
	}
	
	public void takeDamage(int damage)
	{
		health -= damage;
		
		if (health < 0)
		{
			health = 0;
		}
//		System.out.println("Player takes " + damage + " damage! Remaining health: " + health);
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
