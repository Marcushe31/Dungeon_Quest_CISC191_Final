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
 * Purpose: The reponsibility of BattleScannerInput is ...
 *
 * BattleScannerInput is-a ...
 * BattleScannerInput is ...
 */
public class BattleScannerInput
{
	private Battle battle;
	private BattleController battleController;
	
	public BattleScannerInput(Battle battle, BattleController battleController) {
		this.battle = battle;
		this.battleController = battleController;
	}

	
	public void runBattle()
	{
		Scanner scanner = new Scanner(System.in);
		
		while(battle.isActive())
		{
			if (battle.getTurn().equals("Player"))
			{
				System.out.println("\nYour Turn! Choose (attack / block / skill): ");
				String action = getPlayerAction(scanner);
				battleController.handlePlayerAction(action);
			}
			else
			{
				System.out.println("\nEnemy Turn...");
				battleController.handleEnemyTurn();
			}
		}
		System.out.println("\nBattle over! Outcome: " + battle.getOutcome());
	}

	public String getPlayerAction(Scanner scanner)
	{
		String action = scanner.nextLine().trim().toLowerCase();
		
		while (!action.equals("attack") && !action.equals("block") && !action.equals("skill"))
		{
			System.out.println("Invalid action. Type 'attack', 'block', or 'skill'.");
			action = scanner.nextLine().trim().toLowerCase();
		}
		
		return action;
	}
}
