/**
* Lead Author(s):
* @author marcu_n7cwnq7; student ID
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
* Version: 2026-04-27
*/
package Default;

import java.util.Random;


/**
 * Purpose: The responsibility of Dungeon is ...
 *
 * Dungeon is-a ...
 * Dungeon is ...
 */
public class Dungeon
{
	private Door[] doors;
	private int stage;
	private DoorFactory doorFactory;

	public Dungeon()
	{
		doors = new Door[3];
		stage = 1;
		doorFactory = new DoorFactory();
	}

	public void generateDoors()
	{
		doors = doorFactory.generateDoors();
	}

	public Door chooseDoor(int doorNumber)
	{
		return doors[doorNumber - 1];
	}

	public Door[] getDoors()
	{
		return doors;
	}

	public int getStage()
	{
		return stage;
	}

	public void nextStage()
	{
		stage++;
	}
}











