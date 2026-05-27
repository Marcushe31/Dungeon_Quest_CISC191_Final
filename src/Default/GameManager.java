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
* Version: 2026-05-27
*/
package Default;

import java.util.Scanner;

/**
 * Purpose: The reponsibility of GameManager is ...
 *
 * GameManager is-a ...
 * GameManager is ...
 */
public class GameManager
{
	private Player player;
	private Dungeon dungeon;
	private DungeonController dungeonController;
	private DungeonScannerInput dungeonScannerInput;

	public void startGame()
	{
		Scanner scanner =
				new Scanner(System.in);

		player = chooseProfession(scanner);

		dungeon = new Dungeon();

		dungeonController =
				new DungeonController(dungeon);

		dungeonScannerInput =
				new DungeonScannerInput(
						dungeonController,
						player);

		dungeonScannerInput.runDungeon();
	}

	private Player chooseProfession(Scanner scanner)
	{
		Player player = null;

		while (player == null)
		{
			System.out.println(
					"Choose your Profession:");

			System.out.println("1. Warrior");
			System.out.println("2. Mage");
			System.out.println("3. Archer");

			System.out.print("--> ");

			String choice =
					scanner.nextLine().trim();

			if (choice.equals("1"))
			{
				player = new Warrior();
			}
			else if (choice.equals("2"))
			{
				player = new Mage();
			}
			else if (choice.equals("3"))
			{
				player = new Archer();
			}
			else
			{
				System.out.println(
						"Invalid choice.");
			}
		}

		return player;
	}
}
