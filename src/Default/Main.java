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

//import java.util.Scanner;

/**
 * Purpose: The responsibility of Main is ...
 *
 * Main is-a ...
 * Main is ...
 */
public class Main
{
	public static void main(String[] args)
	{
		GameManagerView window = new GameManagerView();
		window.setVisible(true);

		// Moved to GameManager Class
//		Scanner scanner = new Scanner(System.in);
//		
//		System.out.println("Choose your Profession (Select Number):");
//		System.out.println("1. Warrior");
//		System.out.println("2. Mage");
//		System.out.println("3. Archer");
//		System.out.println("Enter Here:");
//		
//		String choice = scanner.nextLine().trim();
//		
//		Player player;
//		
//		if (choice.equals("1"))
//		{
//			player = new Warrior();
//		}
//		else if (choice.equals("2"))
//		{
//			player = new Mage();
//		}
//		else if (choice.equals("3"))
//		{
//			player = new Archer();
//		}
//		else
//		{
//			System.out.println("Not VALID Profession. Defaulting to Warrior");
//			player = new Warrior();
//		}
		
//		Player player = new Player("Mage", 100, 50, 50, 10);
//		Enemy enemy = new Enemy("Goblin", 60, 30, 10);

		// Version 1
//		Battle battle = new Battle(player, enemy);
//		BattleController battleController = new BattleController(battle);
//		BattleScannerInput battleScannerInput = new BattleScannerInput(battle, battleController);

//		battle.startBattle();
//		battleScannerInput.runBattle();
		
		// Moved to GameManager Class
//		Dungeon dungeon = new Dungeon();
//		DungeonController dungeonController = new DungeonController(dungeon);
//		DungeonScannerInput dungeonScannerInput = new DungeonScannerInput(dungeonController, player);
//
//		dungeonScannerInput.runDungeon();
	}
}
