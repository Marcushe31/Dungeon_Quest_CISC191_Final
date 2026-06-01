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
* Version: 2026-05-28
*/
package Default;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Purpose: The reponsibility of SaveManager is ...
 *
 * SaveManager is-a ...
 * SaveManager is ...
 */
public class SaveManager
{
	private final String SAVE_FILE = "saveData.txt";

	public void saveGame(Player player, Dungeon dungeon)
	{
		saveGame(player, dungeon, 0);
	}

	public void saveGame(Player player, Dungeon dungeon, int enemiesDefeated)
	{
		saveGame(player, dungeon, enemiesDefeated, null, false);
	}

	public void saveGame(Player player, Dungeon dungeon, int enemiesDefeated,
			Battle battle, boolean bossBattle)
	{
		try
		{
			PrintWriter writer = new PrintWriter(new FileWriter(SAVE_FILE));

			writer.println(player.getCharacterClass());
			writer.println(player.getHealth());
			writer.println(player.getMana());
			writer.println(player.getStamina());
			writer.println(dungeon.getStage());
			writer.println(player.getItemCount());
			writer.println(enemiesDefeated);
			boolean inBattle = battle != null && battle.isActive();
			writer.println(inBattle);
			if (inBattle)
			{
				writer.println(battle.getEnemy().getEnemyType());
				writer.println(battle.getEnemy().getHealth());
				writer.println(bossBattle);
			}

			writer.close();

			System.out.println("Game saved successfully.");
		}
		catch (IOException e)
		{
			System.out.println("Error saving game.");
		}
	}
	
	public GameSave loadGame()
	{
		try
		{
			Scanner fileScanner = new Scanner(new File(SAVE_FILE));

			String characterClass = fileScanner.nextLine();

			int health = Integer.parseInt(fileScanner.nextLine());
			int mana = -1;
			int stamina = -1;
			int stage;
			int itemCount = 0;
			int enemiesDefeated = 0;
			boolean inBattle = false;
			String enemyType = "";
			int enemyHealth = 0;
			boolean bossBattle = false;

			if (fileScanner.hasNextLine())
			{
				String thirdLine = fileScanner.nextLine();
				if (fileScanner.hasNextLine())
				{
					mana = Integer.parseInt(thirdLine);
					stamina = Integer.parseInt(fileScanner.nextLine());
					stage = Integer.parseInt(fileScanner.nextLine());
					if (fileScanner.hasNextLine())
					{
						itemCount = Integer.parseInt(fileScanner.nextLine());
					}
					if (fileScanner.hasNextLine())
					{
						enemiesDefeated = Integer.parseInt(fileScanner.nextLine());
					}
					if (fileScanner.hasNextLine())
					{
						inBattle = Boolean.parseBoolean(fileScanner.nextLine());
					}
					if (inBattle && fileScanner.hasNextLine())
					{
						enemyType = fileScanner.nextLine();
						enemyHealth = Integer.parseInt(fileScanner.nextLine());
						bossBattle = Boolean.parseBoolean(fileScanner.nextLine());
					}
				}
				else
				{
					stage = Integer.parseInt(thirdLine);
				}
			}
			else
			{
				stage = 1;
			}

			fileScanner.close();

			return new GameSave(characterClass, health, mana, stamina, stage,
					itemCount, enemiesDefeated, inBattle, enemyType, enemyHealth,
					bossBattle);
		}
		catch (Exception e)
		{
			System.out.println("Could not load save file.");

			System.out.println(e.getMessage());

			return null;
		}
	}
}
