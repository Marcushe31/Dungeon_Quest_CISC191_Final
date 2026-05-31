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
 * Purpose: Enemy holds the stats for any enemy the player fights.
 * isBoss can be set to flag a boss encounter.
 *
 * Enemy is a model class used by Battle.
 * Enemy implements Attackable so it can be used in combat.
 */
public class Enemy implements Attackable
{
	private String enemyType;
	private int health;
	private int maxHealth;
	private int stamina;
	private boolean isBoss;
	private int damage;

	/**
	 * Creates an Enemy with the given stats. maxHealth is set from the starting health value.
	 *
	 * @param enemyType name of the enemy (e.g. "Goblin")
	 * @param health starting and max health
	 * @param stamina starting stamina
	 * @param damage base attack damage
	 */
	public Enemy(String enemyType, int health, int stamina, int damage)
	{
		this.enemyType = enemyType;
		this.health = health;
		// save max so the view can show a health bar later
		this.maxHealth = health;
		this.stamina = stamina;
		this.damage = damage;
	}
	
	public void attack()
	{
		System.out.println("Enemy attacks with " + damage + " damage!");
	}
	
	public void takeDamage(int damage)
	{
		health -= damage;
		
		if (health < 0)
		{
			health = 0;
		}
//		System.out.println("Enemy takes " + damage + " damage! Remaining health: " + health);
	}
	
	public boolean isAlive()
	{
		return health > 0;
	}
	
	/**
	 * Returns the enemy's current health.
	 *
	 * @return current health
	 */
	public int getHealth()
	{
		return health;
	}

	/**
	 * Returns the enemy's max health.
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
	
	public String getEnemyType()
	{
		return enemyType;
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
