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
		try
		{
			PrintWriter writer = new PrintWriter(new FileWriter(SAVE_FILE));

			writer.println(player.getCharacterClass());

			writer.println(player.getHealth());

			writer.println(dungeon.getStage());

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

			int stage =
					Integer.parseInt(fileScanner.nextLine());

			fileScanner.close();

			return new GameSave(characterClass, health, stage);
		}
		catch (Exception e)
		{
			System.out.println("Could not load save file.");

			System.out.println(e.getMessage());

			return null;
		}
	}
}
