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
import Default.Door;
import Default.DoorFactory;
import Default.Dungeon;
import Default.DungeonController;
import Default.DungeonScannerInput;
import Default.Enemy;
import Default.Item;
import Default.Mage;
import Default.Player;
import Default.Warrior;

/**
 * Purpose: The reponsibility of TestBattle is ...
 *
 * TestBattle is-a ...
 * TestBattle is ...
 */
class TestBattle
{

	@Test
	void testBasicBattleTurnFlow()
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
		
		@Test
		void testDoorStoresEnemyEvent()
		{
			Enemy enemy = new Enemy("Goblin", 60, 30, 10);

			Door door = new Door("enemy", enemy, null);

			assertEquals("enemy", door.getEventType());
			assertEquals(enemy, door.getEnemy());
			assertNull(door.getItem());
		}

		@Test
		void testDoorStoresRewardEvent()
		{
			Item item = new Item("Health Potion", 25);

			Door door = new Door("reward", null, item);

			assertEquals("reward", door.getEventType());
			assertNull(door.getEnemy());
			assertEquals(item, door.getItem());
		}

		@Test
		void testHealthPotionHealsPlayer()
		{
			Player player = new Warrior();
			Item item = new Item("Health Potion", 25);

			player.takeDamage(40);
			item.use(player);

			assertEquals(105, player.getHealth());
		}

		@Test
		void testPlayerStoresItemsInInventory()
		{
			Player player = new Warrior();
			Item item = new Item("Health Potion", 25);

			player.addItem(item);

			assertEquals(1, player.getItemCount());
			assertEquals(item, player.getItem(0));
		}

		@Test
		void testPlayerUsesHealthPotionFromInventory()
		{
			Player player = new Warrior();
			Item item = new Item("Health Potion", 25);

			player.takeDamage(40);
			player.addItem(item);
			player.useItem(0);

			assertEquals(105, player.getHealth());
			assertEquals(0, player.getItemCount());
		}

		@Test
		void testScannerItemInput()
		{
			String input = "item\n";

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

			assertEquals("item", action);

			scanner.close();
		}

		@Test
		void testDoorFactoryGeneratesThreeDoors()
		{
			DoorFactory doorFactory = new DoorFactory();

			Door[] doors = doorFactory.generateDoors();

			assertEquals(3, doors.length);
			assertNotNull(doors[0]);
			assertNotNull(doors[1]);
			assertNotNull(doors[2]);
		}

		@Test
		void testDungeonGeneratesDoors()
		{
			Dungeon dungeon = new Dungeon();

			dungeon.generateDoors();

			Door[] doors = dungeon.getDoors();

			assertEquals(3, doors.length);
			assertNotNull(doors[0]);
			assertNotNull(doors[1]);
			assertNotNull(doors[2]);
		}

		@Test
		void testDungeonChooseDoor()
		{
			Dungeon dungeon = new Dungeon();

			dungeon.generateDoors();

			Door selectedDoor = dungeon.chooseDoor(1);

			assertNotNull(selectedDoor);
			assertEquals(dungeon.getDoors()[0], selectedDoor);
		}

		@Test
		void testDungeonStageStartsAtOneAndIncrements()
		{
			Dungeon dungeon = new Dungeon();

			assertEquals(1, dungeon.getStage());

			dungeon.nextStage();

			assertEquals(2, dungeon.getStage());
		}

		@Test
		void testDungeonControllerGeneratesDoorsAndChoosesDoor()
		{
			Dungeon dungeon = new Dungeon();

			DungeonController dungeonController =
					new DungeonController(dungeon);

			dungeonController.generateDoors();

			Door selectedDoor =
					dungeonController.handleDoorChoice(1);

			assertNotNull(selectedDoor);
			assertEquals(dungeon.getDoors()[0], selectedDoor);
		}

		@Test
		void testDungeonScannerInputValidChoice()
		{
			String input = "2\n";

			ByteArrayInputStream testInput =
					new ByteArrayInputStream(input.getBytes());

			Scanner scanner = new Scanner(testInput);

			Dungeon dungeon = new Dungeon();

			DungeonController dungeonController =
					new DungeonController(dungeon);

			Player player = new Warrior();

			DungeonScannerInput dungeonScannerInput =
					new DungeonScannerInput(dungeonController, player);

			int choice = dungeonScannerInput.getDoorChoice(scanner);

			assertEquals(2, choice);

			scanner.close();
		}

		@Test
		void testDungeonScannerInputInvalidThenValidChoice()
		{
			String input = "hello\n3\n";

			ByteArrayInputStream testInput =
					new ByteArrayInputStream(input.getBytes());

			Scanner scanner = new Scanner(testInput);

			Dungeon dungeon = new Dungeon();

			DungeonController dungeonController =
					new DungeonController(dungeon);

			Player player = new Warrior();

			DungeonScannerInput dungeonScannerInput =
					new DungeonScannerInput(dungeonController, player);

			int choice = dungeonScannerInput.getDoorChoice(scanner);

			assertEquals(3, choice);

			scanner.close();
		}
	}


