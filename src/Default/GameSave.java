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
* Version: 2026-05-29
*/
package Default;

/**
 * Purpose: The reponsibility of GameSave is ...
 *
 * GameSave is-a ...
 * GameSave is ...
 */
public class GameSave
{
	private String characterClass;
	private int health;
	private int mana;
	private int stamina;
	private int stage;

	public GameSave(String characterClass, int health, int stage)
	{
		this.characterClass = characterClass;
		this.health = health;
		this.stage = stage;
	}

	public String getCharacterClass()
	{
		return characterClass;
	}

	public int getHealth()
	{
		return health;
	}

	public int getMana()
	{
		return mana;
	}

	public int getStamina()
	{
		return stamina;
	}

	public int getStage()
	{
		return stage;
	}
}