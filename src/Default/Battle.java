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
 * Purpose: The reponsibility of Battle is ...
 *
 * Battle is-a ...
 * Battle is ...
 */
public class Battle
{
	private Player player;
	private Enemy enemy;
	private boolean active;
	private String outcome;
	private String turn;
	private Scanner scanner;
	private boolean playerIsBlocking = false;

	public Battle(Player player, Enemy enemy)
	{
		this.player = player;
		this.enemy = enemy;
	}

	public void startBattle()
	{
		active = true;
		outcome = "In Progress";
		turn = "Player";
		System.out.println("Battle started!" + player.getCharacterClass()
				+ " vs " + enemy.getEnemyType());
	}
	
//	public void runBattle()
//	{
//		Scanner scanner = new Scanner(System.in);
//		
//		while(active)
//		{
//			if (turn.equals("Player"))
//			{
//				System.out.println("\nYour Turn! Choose (attack / block): ");
//				String action = scanner.nextLine().trim().toLowerCase();
//				
//				if(!action.equals("attack") && !action.equals("block"))
//				{
//					System.out.println("Invalid action. Type 'attack' or 'block'.");
//					continue;
//				}
//				takeTurn(action);
//			}
//			else
//			{
//				System.out.println("\nEnemy Turn...");
//				takeTurn("");
//			}
//		}
//		System.out.println("\nBattle over! Outcome: " + outcome);
//		scanner.close();
//	}
	
	/**
	 * 
	 * Purpose: takes the turn of player and enemy action
	 * @param action to be action performed
	 */
	public void takeTurn(String action)
	{
		if (!active)
		{
			System.out.println("Battle is already over");
			return;
		}

		if (turn.equals("Player"))
		{
			if (action.equals("attack"))
			{
				enemy.takeDamage(player.getDamage());
				System.out.println("Player attacks for " + player.getDamage()
						+ " damage! Enemy HP: " + enemy.getHealth());
			}
			else if (action.equals("block"))
			{
				playerIsBlocking = true;
				System.out.println("Player blocks! Damage reduced next turn");
			}
			turn = "Enemy";
		}
		else
		{
			int incomingDamage = enemy.getDamage();
			if (playerIsBlocking)
			{
				incomingDamage /= 2;
				System.out.println("Block reduced damage to " + incomingDamage + "!");
				playerIsBlocking = false;
				
			}
			player.takeDamage(incomingDamage);
			System.out.println("Enemy attacks for " + incomingDamage
			+ " damage! Player HP" + player.getHealth());
			turn = "Player";
		}
		checkBattleEnd();
	}

	/**
	 * 
	 * Purpose: Check if battle is completed
	 */
	private void checkBattleEnd()
	{
		if (!enemy.isAlive())
		{
			outcome = "Player Wins";
			active = false;
			System.out.println("Enemy defeated! Player Wins!");
		}
		else if (!player.isAlive())
		{
			outcome = "Enemy Wins";
			active = false;
			System.out.println("Player defeated! Game over");
		}
	}

	public boolean isActive()
	{
		return active;
	}

	public String getOutcome()
	{
		return outcome;
	}

	/**
	 * @return the turn
	 */
	public String getTurn()
	{
		return turn;
	}

	/**
	 * @param turn the turn to set
	 */
	public void setTurn(String turn)
	{
		this.turn = turn;
	}
	
	
	// public void playerAttack()
	// {
	// if (active && turn.equals("Player"))
	// {
	// player.attack();
	// enemy.takeDamage(player.getDamage());
	//
	// if (!enemy.isAlive())
	// {
	// active = false;
	// outcome = "Player Wins!";
	// }
	// else
	// {
	// turn = "Enemy";
	// }
	// }
	// }
}
