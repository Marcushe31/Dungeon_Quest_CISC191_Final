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
		BattleController battleController = new BattleController(battle);
		BattleScannerInput battleScannerInput = new BattleScannerInput(battle, battleController);

		battle.startBattle();
		battleScannerInput.runBattle();
		
		
	}
}
