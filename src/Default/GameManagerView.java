/**
 * Lead Author(s):
 *
 * @author Marcus Hernandez
 * @author Patrick Tran
 *
 *         Other Contributors:
 *
 *         References:
 *         Morelli, R., & Walde, R. (2016).
 *         Java, Java, Java: Object-Oriented Problem Solving
 *         https://open.umn.edu/opentextbooks/textbooks/java-java-java-object-oriented-problem-solving
 *
 *
 *         Version: 2026-05-30
 */
package Default;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Purpose: GameManagerView is the top-level window for the GUI version of the
 * game. It is the View/coordinator in MVC. It holds the game state (player,
 * dungeon, controllers) and swaps between the different screens using a
 * CardLayout. The screen panels stay simple and just ask this class to move
 * the game forward.
 *
 * GameManagerView HAS-A Player, Dungeon, DungeonController, and the screens.
 */
public class GameManagerView extends JFrame
{
	private CardLayout cardLayout;
	// holds all the screens, only one is shown at a time
	private JPanel cardPanel;

	private HomePanel homePanel;
	private DungeonPanel dungeonPanel;
	private BattlePanel battlePanel;
	private BossDoorPanel bossDoorPanel;
	private EndPanel endPanel;

	// game state the view needs to keep track of between screens
	private Player player;
	private Dungeon dungeon;
	private DungeonController dungeonController;
	private SaveManager saveManager;
	private Battle battle;
	private BattleController battleController;
	private boolean bossBattle;
	// tracks how many regular enemies the player has beaten this run
	private int enemiesDefeated;

	/**
	 * Builds the window, sets up all the screens, and shows the home screen.
	 */
	public GameManagerView()
	{
		saveManager = new SaveManager();

		setTitle("Dungeon Quest");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);

		// each screen gets a reference back to this class so it can call us
		homePanel = new HomePanel(this);
		dungeonPanel = new DungeonPanel(this);
		battlePanel = new BattlePanel(this);
		bossDoorPanel = new BossDoorPanel(this);
		endPanel = new EndPanel(this);

		cardPanel.add(homePanel, "home");
		cardPanel.add(dungeonPanel, "dungeon");
		cardPanel.add(battlePanel, "battle");
		cardPanel.add(bossDoorPanel, "bossDoor");
		cardPanel.add(endPanel, "end");

		add(cardPanel);

		showScreen("home");

		// maximize the window so the game fills the whole screen
		pack();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	/**
	 * Switches which screen is showing.
	 *
	 * @param name the name we gave the screen when we added it
	 */
	public void showScreen(String name)
	{
		cardLayout.show(cardPanel, name);
	}

	/**
	 * Starts a brand new game with the chosen class.
	 *
	 * @param chosenPlayer the player object the home screen made
	 */
	public void startNewGame(Player chosenPlayer)
	{
		this.player = chosenPlayer;
		this.dungeon = new Dungeon();
		this.dungeonController = new DungeonController(dungeon);
		// reset kill counter for a fresh run
		this.enemiesDefeated = 0;
		enterDungeon();
	}

	/**
	 * Loads a saved game off the save file and drops the player back in.
	 */
	public void loadGame()
	{
		GameSave save = saveManager.loadGame();
		if (save == null)
		{
			// no save found, just stay on the home screen
			return;
		}

			this.player = makePlayer(save.getCharacterClass());
			player.setHealth(save.getHealth());
			if (save.getMana() >= 0)
			{
				player.setMana(save.getMana());
			}
			if (save.getStamina() >= 0)
			{
				player.setStamina(save.getStamina());
			}
			for (int i = 0; i < save.getItemCount(); i++)
			{
				player.addItem(new Item("Health Potion", 50));
			}

			this.dungeon = new Dungeon();
			dungeon.setStage(save.getStage());
			this.dungeonController = new DungeonController(dungeon);
			this.enemiesDefeated = save.getEnemiesDefeated();

			if (save.isInBattle())
			{
				Enemy savedEnemy = makeEnemy(save.getEnemyType());
				savedEnemy.setHealth(save.getEnemyHealth());
				startBattle(savedEnemy, save.isBossBattle());
			}
			else
			{
				enterDungeon();
			}
		}

	/**
	 * Rebuilds the right player subclass from a saved class name.
	 *
	 * @param characterClass the saved class name
	 * @return a new player of that class
	 */
	private Player makePlayer(String characterClass)
	{
		if (characterClass.equals("Warrior"))
		{
			return new Warrior();
		}
		else if (characterClass.equals("Mage"))
		{
			return new Mage();
		}
		else
		{
			return new Archer();
		}
	}

	private Enemy makeEnemy(String enemyType)
	{
		if (enemyType.equals("Rat"))
		{
			return new Enemy("Rat", 35, 20, 6);
		}
		else if (enemyType.equals("Goblin"))
		{
			return new Enemy("Goblin", 65, 30, 11);
		}
		else if (enemyType.equals("Skeleton"))
		{
			return new Enemy("Skeleton", 90, 40, 16);
		}
		else
		{
			return new Enemy("Dragon", 150, 50, 20);
		}
	}

	/**
	 * Makes a fresh set of doors and shows the dungeon screen.
	 * If 3 enemies have already been defeated, skips straight to the boss.
	 */
	public void enterDungeon()
	{
		if (!dungeonController.isGameRunning())
		{
			return;
		}

		// once 3 enemies are down, force the boss encounter
		if (enemiesDefeated >= 3)
		{
			showScreen("bossDoor");
			return;
		}

		dungeonController.generateDoors();
		dungeonPanel.refresh(player, dungeonController.getStage(), enemiesDefeated);
		showScreen("dungeon");
	}

	/**
	 * Starts the dragon fight after the player clicks the boss door.
	 */
	public void enterBossFight()
	{
		DoorFactory factory = new DoorFactory();
		Door bossDoor = factory.generateBossDoor();
		startBattle(bossDoor.getEnemy(), true);
	}

	/**
	 * Handles whatever is behind the door the player picked. For enemy and boss
	 * events the battle screen opens. For reward and nothing events we show a
	 * message on the dungeon screen and wait for the player to hit continue.
	 *
	 * @param choice which door (1, 2, or 3)
	 */
	public void chooseDoor(int choice)
	{
		// lock all doors right away so no second click can go through
		dungeonPanel.setDoorsEnabled(false);
		Door door = dungeonController.handleDoorChoice(choice);
		String type = door.getEventType();

		if (type.equals("enemy") || type.equals("boss"))
		{
			startBattle(door.getEnemy(), type.equals("boss"));
		}
		else if (type.equals("reward"))
		{
			// add item first, then show feedback and wait for continue click
			player.addItem(door.getItem());
			dungeonPanel.showEvent("You found a " + door.getItem().getItemType() + "!", "potion", choice);
		}
		else
		{
			// nothing happened, let the player see that before moving on
			player.heal(20);
			if (player.getCharacterClass().equals("Mage"))
			{
				player.restoreMana(15);
			}
			else
			{
				player.restoreStamina(15);
			}
			dungeonPanel.showEvent("You rest and recover: +20 HP, +15 "
					+ (player.getCharacterClass().equals("Mage") ? "mana." : "stamina."), null, choice);
		}
	}

	/**
	 * Called when the player clicks CONTINUE on the dungeon screen after a
	 * non-battle event. Advances to the next stage.
	 */
	public void onDungeonContinue()
	{
		dungeonController.moveToNextStage();
		enterDungeon();
	}

	/**
	 * Sets up a new battle and shows the battle screen.
	 *
	 * @param enemy the enemy to fight
	 * @param isBoss true if this is the dragon boss
	 */
	private void startBattle(Enemy enemy, boolean isBoss)
	{
		this.battle = new Battle(player, enemy);
		this.battleController = new BattleController(battle);
		this.bossBattle = isBoss;
		battle.startBattle();
		battlePanel.startBattle(battle, battleController, isBoss);
		showScreen("battle");
	}

	/**
	 * Decides what happens once a battle is finished. Called by the battle
	 * screen when the player clicks continue.
	 */
	public void onBattleEnd()
	{
		if (!player.isAlive())
		{
			endPanel.showResult("YOU DIED", "The dungeon claims another hero...");
			showScreen("end");
		}
		else if (bossBattle)
		{
			endPanel.showResult("VICTORY!", "You slayed the Dragon and cleared the dungeon!");
			showScreen("end");
		}
		else
		{
			// count the kill then move on, boss triggers automatically at 3
			enemiesDefeated++;
			dungeonController.moveToNextStage();
			enterDungeon();
		}
	}

	/**
	 * Saves the current run. Called by the save button on the dungeon screen.
	 */
	public void saveGame()
	{
		saveManager.saveGame(player, dungeon, enemiesDefeated, battle, bossBattle);
	}

	/**
	 * Sends the player back to the title screen.
	 */
	public void goHome()
	{
		homePanel.resetSelectionLock();
		showScreen("home");
	}

	/**
	 * Starts the GUI version of the game.
	 *
	 * @param args not used
	 */
	public static void main(String[] args)
	{
		GameManagerView window = new GameManagerView();
		window.setVisible(true);
	}
}
