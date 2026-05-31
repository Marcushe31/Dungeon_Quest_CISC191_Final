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


/**
 * Purpose: Dungeon tracks the current stage, generates doors, and keeps a
 * 2D map of every door event across all stages. Rows = stages, columns = doors.
 *
 * Dungeon is a model class used by DungeonController.
 */
public class Dungeon
{
	private Door[] doors;
	private int stage;
	private DoorFactory doorFactory;
	private final int FINAL_STAGE = 5;

	// 2D array to track what event was behind each door at each stage
	// dungeonMap[stage-1][doorIndex] = event type string
	private String[][] dungeonMap;

	public boolean isFinalStage()
	{
		return stage == FINAL_STAGE;
	}

	/**
	 * Creates a new Dungeon. Sets up the door array, the door factory,
	 * and the 2D dungeon map (5 stages x 3 doors).
	 */
	public Dungeon()
	{
		doors = new Door[3];
		stage = 1;
		doorFactory = new DoorFactory();
		// 5 stages, 3 doors each -- rows are stages, columns are door slots
		dungeonMap = new String[FINAL_STAGE][3];
	}

	/**
	 * Generates 3 random doors for the current stage and records them in the map.
	 * Boss triggering is handled by GameManagerView based on enemies defeated,
	 * not by stage number.
	 */
	public void generateDoors()
	{
		doors = doorFactory.generateDoors();

		// record what's behind each door in the 2D map for this stage
		for (int i = 0; i < doors.length; i++)
		{
			// guard against going past the map size if stages run long
			if (stage - 1 < dungeonMap.length)
			{
				dungeonMap[stage - 1][i] = doors[i].getEventType();
			}
		}
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
	
	public void setStage(int stage)
	{
		this.stage = stage;
	}

	/**
	 * Returns the full dungeon map. Each row is a stage, each column is a door.
	 * Useful for displaying a stage history or debug info.
	 *
	 * @return the 2D array of event types
	 */
	public String[][] getDungeonMap()
	{
		return dungeonMap;
	}
}











