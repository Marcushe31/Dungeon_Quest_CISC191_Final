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
* Version: 2026-05-20
*/
package Default;

import java.util.Random;

/**
 * Purpose: The reponsibility of DoorFactory is ...
 *
 * DoorFactory is-a ...
 * DoorFactory is ...
 */
public class DoorFactory
{
	private Random random;

	public DoorFactory()
	{
		random = new Random();
	}

	public Door[] generateDoors()
	{
		Door[] doors = new Door[3];

		for (int i = 0; i < doors.length; i++)
		{
			doors[i] = generateDoor();
		}

		return doors;
	}

	private Door generateDoor()
	{
		// 60% enemy, 20% reward, 20% nothing
		int randomEvent = random.nextInt(10);

		if (randomEvent < 6)
		{
			Enemy enemy = generateEnemy();
			return new Door("enemy", enemy, null);
		}
		else if (randomEvent < 8)
		{
			Item item = new Item("Health Potion", 50);
			return new Door("reward", null, item);
		}
		else
		{
			return new Door("nothing", null, null);
		}
	}

	private Enemy generateEnemy()
	{
		int randomEnemy = random.nextInt(3);

		if (randomEnemy == 0)
		{
			// Rat: easy warmup fight
			return new Enemy("Rat", 35, 20, 6);
		}
		else if (randomEnemy == 1)
		{
			// Goblin: medium threat
			return new Enemy("Goblin", 65, 30, 11);
		}
		else
		{
			// Skeleton: hardest regular enemy
			return new Enemy("Skeleton", 90, 40, 16);
		}
	}

	/**
	 * Generates the boss door. Called by GameManagerView once 3 enemies are down.
	 * Dragon is tough but beatable if the player uses skills and items well.
	 *
	 * @return a boss door with the Dragon enemy
	 */
	public Door generateBossDoor()
	{
		// 150 HP and 20 dmg -- dangerous but not a one-shot machine
		Enemy boss = new Enemy("Dragon", 150, 50, 20);
		return new Door("boss", boss, null);
	}
}
