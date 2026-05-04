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
 * Purpose: The reponsibility of Enemy is ...
 *
 * Enemy is-a ...
 * Enemy is ...
 */
public class Enemy
{
	private String enemyType;
	private int health;
	private int stamina;
	private int isBoss;
	private int damage;
	
	public Enemy(String enemyType, int health, int stamina, int damage)
	{
		this.enemyType = enemyType;
		this.health = health;
		this.stamina = stamina;
		this.damage = damage;
	}
	
	public void attack()
	{
	
	}
	
	public void takeDamage(int damage)
	{
		health -= damage;
		
		if (health >0)
		{
			health = 0;
		}
	}
	
	public boolean isAlive()
	{
		return health >0;
	}
	
	public int getHealth()
	{
		return health;
	}
	
	public int getDamage()
	{
		return damage;
	}
	
	public String enemyType()
	{
		return enemyType;
	}
}
