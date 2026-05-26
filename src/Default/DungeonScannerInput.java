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

import java.util.Scanner;

/**
 * Purpose: The responsibility of DungeonScannerInput is to handle
 * terminal input and output for dungeon exploration.
 *
 * DungeonScannerInput USES DungeonController.
 * DungeonScannerInput gets player door choices.
 */
public class DungeonScannerInput
{
	private DungeonController dungeonController;
	private Player player;

	public DungeonScannerInput(DungeonController dungeonController, Player player)
	{
		this.dungeonController = dungeonController;
		this.player = player;
	}

	public void runDungeon()
	{
		Scanner scanner = new Scanner(System.in);
		boolean gameRunning = true;

		while (gameRunning && player.isAlive())
		{
			System.out.println("\n========== Stage " + dungeonController.getStage() + " ==========");

			dungeonController.generateDoors();

			displayDoors();

			int choice = getDoorChoice(scanner);

			Door selectedDoor = dungeonController.handleDoorChoice(choice);

			handleDoorEvent(selectedDoor);

			dungeonController.moveToNextStage();
		}

		System.out.println("\nDungeon run ended.");
	}

	public void displayDoors()
	{
		System.out.println("Choose a door:");
		System.out.println("1. Door 1");
		System.out.println("2. Door 2");
		System.out.println("3. Door 3");
		System.out.print("--> ");
	}

	public int getDoorChoice(Scanner scanner)
	{
		String input = scanner.nextLine().trim();

		while (!input.equals("1")
				&& !input.equals("2")
				&& !input.equals("3"))
		{
			System.out.println("Invalid choice. Choose 1, 2, or 3.");
			System.out.print("--> ");
			input = scanner.nextLine().trim();
		}

		return Integer.parseInt(input);
	}

	private void handleDoorEvent(Door door)
	{
		if (door.getEventType().equals("enemy"))
		{
			System.out.println("\nAn enemy appeared!");

			Battle battle =
					new Battle(player, door.getEnemy());

			BattleController battleController =
					new BattleController(battle);

			BattleScannerInput battleScannerInput =
					new BattleScannerInput(
							battle,
							battleController);

			battle.startBattle();
			battleScannerInput.runBattle();
		}
		else if (door.getEventType().equals("reward"))
		{
			System.out.println("\nYou found a reward chest!");

			System.out.println("You received: " + door.getItem().getItemType());
			player.addItem(door.getItem());
			System.out.println("Item added to inventory. Items: "
					+ player.getItemCount());
		}
		else
		{
			System.out.println("\nNothing happened.");
			System.out.println("You recovered mana and stamina.");
		}
	}
}
