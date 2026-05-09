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
	private String turn;
	private boolean active;
	private String outcome;
	
	public void runBattle()
	{
		Scanner scanner = new Scanner(System.in);
		
		while(active)
		{
			if (turn.equals("Player"))
			{
				System.out.println("\nYour Turn! Choose (attack / block): ");
				String action = scanner.nextLine().trim().toLowerCase();
				
				if(!action.equals("attack") && !action.equals("block"))
				{
					System.out.println("Invalid action. Type 'attack' or 'block'.");
					continue;
				}
				takeTurn(action);
			}
			else
			{
				System.out.println("\nEnemy Turn...");
				takeTurn("");
			}
		}
		System.out.println("\nBattle over! Outcome: " + outcome);
		scanner.close();
	}
}
