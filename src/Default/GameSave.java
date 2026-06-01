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
	private int itemCount;
	private int enemiesDefeated;
	private boolean inBattle;
	private String enemyType;
	private int enemyHealth;
	private boolean bossBattle;

	public GameSave(String characterClass, int health, int stage)
	{
		this(characterClass, health, 0, 0, stage, 0, 0);
	}

	public GameSave(String characterClass, int health, int mana, int stamina,
			int stage, int itemCount, int enemiesDefeated)
	{
		this(characterClass, health, mana, stamina, stage, itemCount,
				enemiesDefeated, false, "", 0, false);
	}

	public GameSave(String characterClass, int health, int mana, int stamina,
			int stage, int itemCount, int enemiesDefeated, boolean inBattle,
			String enemyType, int enemyHealth, boolean bossBattle)
	{
		this.characterClass = characterClass;
		this.health = health;
		this.mana = mana;
		this.stamina = stamina;
		this.stage = stage;
		this.itemCount = itemCount;
		this.enemiesDefeated = enemiesDefeated;
		this.inBattle = inBattle;
		this.enemyType = enemyType;
		this.enemyHealth = enemyHealth;
		this.bossBattle = bossBattle;
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

	public int getItemCount()
	{
		return itemCount;
	}

	public int getEnemiesDefeated()
	{
		return enemiesDefeated;
	}

	public boolean isInBattle()
	{
		return inBattle;
	}

	public String getEnemyType()
	{
		return enemyType;
	}

	public int getEnemyHealth()
	{
		return enemyHealth;
	}

	public boolean isBossBattle()
	{
		return bossBattle;
	}
}
