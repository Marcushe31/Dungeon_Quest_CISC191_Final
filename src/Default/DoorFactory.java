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
		int randomEvent = random.nextInt(3);

		if (randomEvent == 0)
		{
			Enemy enemy = generateEnemy();

			return new Door("enemy", enemy, null);
		}
		else if (randomEvent == 1)
		{
			Item item = new Item("Health Potion", 25);

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
			return new Enemy("Rat", 30, 20, 5);
		}
		else if (randomEnemy == 1)
		{
			return new Enemy("Goblin", 60, 30, 10);
		}
		else
		{
			return new Enemy("Skeleton", 80, 40, 15);
		}
	}
}
