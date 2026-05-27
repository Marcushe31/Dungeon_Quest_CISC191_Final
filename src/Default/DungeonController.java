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

/**
 * Purpose: The responsibility of DungeonController is to control
 * dungeon actions by telling the Dungeon model what to do.
 *
 * DungeonController HAS-A Dungeon.
 * DungeonController controls door choices and dungeon progression.
 */
public class DungeonController
{
	private Dungeon dungeon;
	private boolean gameRunning;

	public DungeonController(Dungeon dungeon)
	{
		this.dungeon = dungeon;
		this.gameRunning = true;
	}

	public void generateDoors()
	{
		dungeon.generateDoors();
	}

	public Door handleDoorChoice(int choice)
	{
		return dungeon.chooseDoor(choice);
	}

	public void moveToNextStage()
	{
		dungeon.nextStage();
	}

	public int getStage()
	{
		return dungeon.getStage();
	}

	public Door[] getDoors()
	{
		return dungeon.getDoors();
	}
	
	public boolean isGameRunning()
	{
		return gameRunning;
	}
	
	public void endGame()
	{
		gameRunning = false;
	}
}