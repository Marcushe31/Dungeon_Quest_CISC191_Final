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

import java.util.ArrayList;

/**
 * Purpose: Battle handles the turn-based combat logic between a Player and an Enemy.
 * All combat messages go into a battleLog instead of printing directly.
 *
 * Battle is a model class used by BattleController.
 */
public class Battle
{
	private Player player;
	private Enemy enemy;
	private boolean active;
	private String outcome;
	private String turn;
	private boolean playerIsBlocking = false;
	// store all combat messages here instead of printing directly
	private ArrayList<String> battleLog;

	/**
	 * Creates a Battle between the given player and enemy.
	 *
	 * @param player the player
	 * @param enemy the enemy
	 */
	public Battle(Player player, Enemy enemy)
	{
		this.player = player;
		this.enemy = enemy;
		this.battleLog = new ArrayList<String>();
	}

	/**
	 * Sets up the battle and logs the opening message.
	 */
	public void startBattle()
	{
		active = true;
		outcome = "In Progress";
		turn = "Player";
		battleLog.add("Battle started! " + player.getCharacterClass()
				+ " vs " + enemy.getEnemyType());
	}

	/**
	 * Purpose: Processes the current turn for either the player or the enemy.
	 *
	 * @param action the action to perform (attack, block, skill, item)
	 */
	public void takeTurn(String action)
	{
		if (!active)
		{
			battleLog.add("Battle is already over");
			return;
		}

		if (turn.equals("Player"))
		{
			if (action.equals("attack"))
			{
				enemy.takeDamage(player.getDamage());
				battleLog.add("Player attacks for " + player.getDamage()
						+ " damage! Enemy HP: " + enemy.getHealth());
			}
			else if (action.equals("block"))
			{
				playerIsBlocking = true;
				battleLog.add("Player blocks! Damage reduced next turn");
			}
			else if (action.equals("skill"))
			{
				if (player.getSkillCount() > 0)
				{
					Skill skill = player.getSkill(0);
					// check mana before letting the skill go off
					if (player.getMana() >= skill.getCost())
					{
						player.useMana(skill.getCost());
						int enemyHealthBefore = enemy.getHealth();
						skill.activate(player, enemy);
						int damageDone = enemyHealthBefore - enemy.getHealth();
						battleLog.add("Player uses " + skill.getName()
								+ " for " + damageDone
								+ " damage! Enemy HP: " + enemy.getHealth());
						battleLog.add("Mana remaining: " + player.getMana());
					}
					else
					{
						battleLog.add("Not enough mana to use " + skill.getName() + "!");
					}
				}
				else
				{
					battleLog.add("Player has no skill to use.");
				}
			}
			else if (action.equals("item"))
			{
				if (player.getItemCount() > 0)
				{
					Item item = player.getItem(0);
					player.useItem(0);
					battleLog.add("Player uses " + item.getItemType()
							+ ". Player HP: " + player.getHealth());
				}
				else
				{
					battleLog.add("Player has no items to use.");
				}
			}
			turn = "Enemy";
		}
		else
		{
			int incomingDamage = enemy.getDamage();
			if (playerIsBlocking)
			{
				incomingDamage /= 2;
				battleLog.add("Block reduced damage to " + incomingDamage + "!");
				playerIsBlocking = false;
			}
			player.takeDamage(incomingDamage);
			battleLog.add("Enemy attacks for " + incomingDamage
					+ " damage! Player HP: " + player.getHealth());
			turn = "Player";
		}
		checkBattleEnd();
	}

	/**
	 * Purpose: Checks if the battle is over and logs the result.
	 */
	private void checkBattleEnd()
	{
		if (!enemy.isAlive())
		{
			outcome = "Player Wins";
			active = false;
			battleLog.add("Enemy defeated! Player Wins!");
		}
		else if (!player.isAlive())
		{
			outcome = "Enemy Wins";
			active = false;
			battleLog.add("Player defeated! Game over");
		}
	}

	/**
	 * Returns the battle log so the view can display messages.
	 *
	 * @return list of combat messages
	 */
	public ArrayList<String> getBattleLog()
	{
		return battleLog;
	}

	/**
	 * Clears the battle log. Call this after printing the messages.
	 */
	public void clearLog()
	{
		battleLog.clear();
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
