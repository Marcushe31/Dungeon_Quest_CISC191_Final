/**
* Lead Author(s):
* @author patri; student ID
* @author Full name; student ID
* <<Add additional lead authors here>>
*
* Other Contributors:
* Full name; student ID or contact information if not in class
* <<Add additional contributors (mentors, tutors, friends) here, with contact information>>
*
* References:
* Morelli, R., & Walde, R. (2016).
* Java, Java, Java: Object-Oriented Problem Solving
* https://open.umn.edu/opentextbooks/textbooks/java-java-java-object-oriented-problem-solving
*
* <<Add more references here>>
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
		System.out.println("Player attacks with " + damage + " damage!");
	}
	
	public void takeDamage(int damage)
	{
		health -= damage;
		System.out.println("Player takes " + damage + " damage! Remaining health: " + health);
	}
}
