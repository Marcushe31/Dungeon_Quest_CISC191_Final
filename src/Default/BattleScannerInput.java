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
* Version: 2026-05-06
*/
package Default;

import java.util.Scanner;

/**
 * Purpose: BattleScannerInput handles terminal input/output for combat.
 * It reads player actions and prints messages from the battle log each turn.
 *
 * BattleScannerInput uses BattleController to process actions.
 */
public class BattleScannerInput
{
	private Battle battle;
	private BattleController battleController;

	/**
	 * Creates a BattleScannerInput for the given battle and controller.
	 *
	 * @param battle the current battle
	 * @param battleController the controller handling actions
	 */
	public BattleScannerInput(Battle battle, BattleController battleController) {
		this.battle = battle;
		this.battleController = battleController;
	}

	/**
	 * Runs the main battle loop until the battle ends.
	 * Prints the battle log after every turn.
	 */
	public void runBattle()
	{
		Scanner scanner = new Scanner(System.in);

		// print the opening message from startBattle()
		printAndClearLog();

		while (battle.isActive())
		{
			if (battle.getTurn().equals("Player"))
			{
				System.out.println("\nYour Turn! Choose (attack / block / skill / item): ");
				String action = getPlayerAction(scanner);
				battleController.handlePlayerAction(action);
			}
			else
			{
				System.out.println("\nEnemy Turn...");
				battleController.handleEnemyTurn();
			}

			// show what happened this turn then wipe the log
			printAndClearLog();
		}
		System.out.println("\nBattle over! Outcome: " + battle.getOutcome());
	}

	/**
	 * Reads and validates a player action from the terminal.
	 *
	 * @param scanner the active scanner
	 * @return a valid action string
	 */
	public String getPlayerAction(Scanner scanner)
	{
		String action = scanner.nextLine().trim().toLowerCase();

		while (!action.equals("attack") && !action.equals("block")
				&& !action.equals("skill") && !action.equals("item"))
		{
			System.out.println("Invalid action. Type 'attack', 'block', 'skill', or 'item'.");
			action = scanner.nextLine().trim().toLowerCase();
		}

		return action;
	}

	/**
	 * Prints every message in the battle log and then clears it.
	 */
	private void printAndClearLog()
	{
		for (String msg : battle.getBattleLog())
		{
			System.out.println(msg);
		}
		battle.clearLog();
	}
}
