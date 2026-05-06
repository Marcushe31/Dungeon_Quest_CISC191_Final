/**
 * Lead Author(s):
 * 
 * @author Patrick Tran
 * @author Marcus Hernandez
 *
 *         Other Contributors:
 *
 *         References:
 *         Morelli, R., & Walde, R. (2016).
 *         Java, Java, Java: Object-Oriented Problem Solving
 *         https://open.umn.edu/opentextbooks/textbooks/java-java-java-object-oriented-problem-solving
 *
 *
 *         Version: 2026-05-04
 */
package Default;

import java.util.Scanner;

/**
 * Purpose: The reponsibility of Main is ...
 *
 * Main is-a ...
 * Main is ...
 */
public class Main
{
	public static void main(String[] args)
	{

		Player player = new Player("Warrior", 100, 50, 50, 15);
		Enemy enemy = new Enemy("Goblin", 60, 30, 10);

		Battle battle = new Battle(player, enemy);
		battle.startBattle();
		battle.runBattle();
		
		// TODO: move to startBattle
//		while (battle.isActive())
//		{
//			System.out.println("\nYour Turn! Choose (attack / block): ");
//			String action = scanner.nextLine().trim().toLowerCase();
//
//			if (!action.equals("attack") && !action.equals("block"))
//			{
//				System.out.println("Invalid action. Type 'attack' or 'block'.");
//				continue;
//			}
//			
//			Character currentCharacter = battle.takeTurn(action);
//			battle.takeTurn(action);
//		}
//
//		System.out.println("\nBattle over! Outcome: " + battle.getOutcome());
//		scanner.close();
	}
}
