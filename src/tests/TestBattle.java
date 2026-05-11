/**
 * Lead Author(s):
 * 
 * @author patri; student ID
 * @author Full name; student ID
 *         <<Add additional lead authors here>>
 *
 *         Other Contributors:
 *         Full name; student ID or contact information if not in class
 *         <<Add additional contributors (mentors, tutors, friends) here, with
 *         contact information>>
 *
 *         References:
 *         Morelli, R., & Walde, R. (2016).
 *         Java, Java, Java: Object-Oriented Problem Solving
 *         https://open.umn.edu/opentextbooks/textbooks/java-java-java-object-oriented-problem-solving
 *
 *         <<Add more references here>>
 *
 *         Version: 2026-05-04
 */
package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import org.junit.jupiter.api.Test;
import Default.Battle;
import Default.BattleController;
import Default.BattleScannerInput;
import Default.Enemy;
import Default.Player;

/**
 * Purpose: The reponsibility of TestBattle is ...
 *
 * TestBattle is-a ...
 * TestBattle is ...
 */
class TestBattle
{

	@Test
	void test()
	{
		// fail("Not yet implemented");

		Player p = new Player("Warrior", 100, 50, 50, 15);
		Enemy e = new Enemy("Goblin", 60, 30, 10);
		Battle b = new Battle(p, e);
		b.startBattle();

		int initialPlayerHealth = p.getHealth();

		assertEquals(b.getTurn(), "Player");
		b.takeTurn("attack");

		int enemyHealth = e.getHealth();
		assertEquals(enemyHealth, 45);
		assertEquals(p.getHealth(), initialPlayerHealth);

		// Enemy now attacks
		assertEquals(b.getTurn(), "Enemy");
		b.takeTurn("");

		int playerHealth = p.getHealth();
		assertEquals(playerHealth, 90);
		assertEquals(e.getHealth(), enemyHealth);
	}

	@Test
	void testBattleTurnsAndDamage()
	{
		Player player = new Player("Warrior", 100, 50, 50, 15);

		Enemy enemy = new Enemy("Goblin", 60, 30, 10);

		Battle battle = new Battle(player, enemy);

		BattleController controller = new BattleController(battle);

		battle.startBattle();

		assertEquals("Player", battle.getTurn());

		controller.handlePlayerAction("attack");

		assertEquals(45, enemy.getHealth());
		assertEquals("Enemy", battle.getTurn());

		controller.handleEnemyTurn();

		assertEquals(90, player.getHealth());
		assertEquals("Player", battle.getTurn());
	}


		@Test
		void testScannerAttackInput()
		{
			String input = "attack\n";

			ByteArrayInputStream testInput = new ByteArrayInputStream(
					input.getBytes());

			Scanner scanner = new Scanner(testInput);

			Player player = new Player("Warrior", 100, 50, 50, 15);

			Enemy enemy = new Enemy("Goblin", 60, 30, 10);

			Battle battle = new Battle(player, enemy);

			BattleController controller = new BattleController(battle);

			BattleScannerInput inputHandler = new BattleScannerInput(battle,
					controller);

			String action = inputHandler.getPlayerAction(scanner);

			assertEquals("attack", action);

			scanner.close();
		}

		@Test
		void testScannerBlockInput()
		{
			String input = "block\n";

			ByteArrayInputStream testInput = new ByteArrayInputStream(
					input.getBytes());

			Scanner scanner = new Scanner(testInput);

			Player player = new Player("Warrior", 100, 50, 50, 15);

			Enemy enemy = new Enemy("Goblin", 60, 30, 10);

			Battle battle = new Battle(player, enemy);

			BattleController controller = new BattleController(battle);

			BattleScannerInput inputHandler = new BattleScannerInput(battle,
					controller);

			String action = inputHandler.getPlayerAction(scanner);

			assertEquals("block", action);

			scanner.close();
		}
	}


