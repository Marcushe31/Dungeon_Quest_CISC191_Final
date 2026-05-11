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
	
//	
	
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
			+ " damage! Player HP: " + player.getHealth());
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

	public Player getPlayer() {
		return player;
	}
	
	public Enemy getEnemy() {
		return enemy; 
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
