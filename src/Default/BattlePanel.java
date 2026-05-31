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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

/**
 * Purpose: BattlePanel is the battle screen (the View for a fight). It shows
 * the player and enemy, their HP bars, a scrolling battle log, and the action
 * buttons. When a button is clicked it tells the BattleController what to do,
 * it does NOT do any battle math itself. After each action it reads the new
 * numbers and log lines back out of the Battle object and shows them.
 *
 * BattlePanel HAS-A GameManagerView so it can ask to change screens.
 */
public class BattlePanel extends JPanel
{
	private GameManagerView gameManager;
	private Battle battle;
	private BattleController battleController;

	// enemy display (top right of the field)
	private JLabel enemyName;
	private SpritePanel enemySprite;
	private JProgressBar enemyHpBar;

	// player display (bottom left of the field)
	private JLabel playerName;
	private SpritePanel playerSprite;
	private JProgressBar playerHpBar;

	private JLabel promptLabel;
	private JTextArea logArea;

	private JButton fightButton;
	private JButton skillsButton;
	private JButton bagButton;
	private JButton blockButton;
	// only shows up once the fight is over
	private JButton continueButton;
	// always visible so the player can bail or save at any point
	private JButton saveQuitButton;
	private JButton quitHomeButton;

	/**
	 * Builds the battle screen layout.
	 *
	 * @param gameManager the main window
	 */
	public BattlePanel(GameManagerView gameManager)
	{
		this.gameManager = gameManager;
		// no fixed size -- let it fill the fullscreen window
		setBackground(GuiStyle.BACKGROUND);
		setLayout(new BorderLayout());

		add(buildBattlefield(), BorderLayout.CENTER);
		add(buildControls(), BorderLayout.SOUTH);
	}

	/**
	 * Builds the top area where the two fighters stand. Uses a 2x2 grid so the
	 * enemy sits top-right and the player sits bottom-left, like an old rpg.
	 *
	 * @return the battlefield panel
	 */
	private JPanel buildBattlefield()
	{
		JPanel field = new JPanel(new GridLayout(2, 2));
		field.setBackground(GuiStyle.BACKGROUND);

		field.add(emptyCell());
		field.add(buildEnemyCell());
		field.add(buildPlayerCell());
		field.add(emptyCell());

		return field;
	}

	private JPanel emptyCell()
	{
		JPanel cell = new JPanel();
		cell.setBackground(GuiStyle.BACKGROUND);
		return cell;
	}

	private JPanel buildEnemyCell()
	{
		JPanel cell = new JPanel(new BorderLayout());
		cell.setBackground(GuiStyle.BACKGROUND);

		enemyName = makeLabel("ENEMY");
		enemyHpBar = makeHpBar(GuiStyle.ENEMY_RED);
		enemySprite = new SpritePanel("Goblin"); // updated in startBattle()

		JPanel info = new JPanel(new BorderLayout());
		info.setBackground(GuiStyle.BACKGROUND);
		info.add(enemyName, BorderLayout.NORTH);
		info.add(enemyHpBar, BorderLayout.SOUTH);

		cell.add(info, BorderLayout.NORTH);
		cell.add(enemySprite, BorderLayout.CENTER);
		return cell;
	}

	private JPanel buildPlayerCell()
	{
		JPanel cell = new JPanel(new BorderLayout());
		cell.setBackground(GuiStyle.BACKGROUND);

		playerName = makeLabel("HERO");
		playerHpBar = makeHpBar(GuiStyle.TEXT);
		playerSprite = new SpritePanel("Warrior"); // updated in startBattle()

		JPanel info = new JPanel(new BorderLayout());
		info.setBackground(GuiStyle.BACKGROUND);
		info.add(playerName, BorderLayout.NORTH);
		info.add(playerHpBar, BorderLayout.SOUTH);

		cell.add(playerSprite, BorderLayout.CENTER);
		cell.add(info, BorderLayout.SOUTH);
		return cell;
	}

	/**
	 * Builds the bottom area with the battle log on the left and the action
	 * buttons on the right.
	 *
	 * @return the controls panel
	 */
	private JPanel buildControls()
	{
		int sh = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
		JPanel controls = new JPanel(new BorderLayout());
		controls.setBackground(GuiStyle.PANEL);
		// scale controls height to screen so it doesnt look tiny on 1080p
		controls.setPreferredSize(new Dimension(0, sh * 22 / 100));

		logArea = new JTextArea();
		logArea.setEditable(false);
		logArea.setBackground(new Color(20, 18, 24));
		logArea.setForeground(GuiStyle.TEXT);
		logArea.setFont(GuiStyle.TEXT_FONT);
		logArea.setLineWrap(true);
		logArea.setWrapStyleWord(true);

		JScrollPane scroll = new JScrollPane(logArea);
		int sw = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
		scroll.setPreferredSize(new Dimension(sw * 35 / 100, 0));

		controls.add(scroll, BorderLayout.WEST);
		controls.add(buildActionArea(), BorderLayout.CENTER);
		return controls;
	}

	private JPanel buildActionArea()
	{
		JPanel area = new JPanel(new BorderLayout());
		area.setBackground(GuiStyle.PANEL);

		promptLabel = new JLabel("What will HERO do?", SwingConstants.CENTER);
		promptLabel.setForeground(GuiStyle.TEXT);
		promptLabel.setFont(GuiStyle.BIG_FONT);

		// 2x2 grid of the four battle actions
		JPanel buttons = new JPanel(new GridLayout(2, 2, 6, 6));
		buttons.setBackground(GuiStyle.PANEL);

		fightButton = makeActionButton("FIGHT", "attack");
		skillsButton = makeActionButton("SKILLS", "skill");
		bagButton = makeActionButton("BAG", "item");
		blockButton = makeActionButton("BLOCK", "block");

		buttons.add(fightButton);
		buttons.add(skillsButton);
		buttons.add(bagButton);
		buttons.add(blockButton);

		continueButton = new JButton("CONTINUE");
		GuiStyle.styleButton(continueButton);
		continueButton.setVisible(false);
		continueButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				gameManager.onBattleEnd();
			}
		});

		// saves current HP and stage then returns to the title screen
		saveQuitButton = new JButton("SAVE & QUIT");
		GuiStyle.styleButton(saveQuitButton);
		saveQuitButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				gameManager.saveGame();
				gameManager.goHome();
			}
		});

		// returns home without saving
		quitHomeButton = new JButton("QUIT TO HOME");
		GuiStyle.styleButton(quitHomeButton);
		quitHomeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				gameManager.goHome();
			}
		});

		// bottom row holds the quit buttons and the post-battle continue button
		JPanel bottomRow = new JPanel(new GridLayout(1, 3, 6, 6));
		bottomRow.setBackground(GuiStyle.PANEL);
		bottomRow.add(saveQuitButton);
		bottomRow.add(quitHomeButton);
		bottomRow.add(continueButton);

		area.add(promptLabel, BorderLayout.NORTH);
		area.add(buttons, BorderLayout.CENTER);
		area.add(bottomRow, BorderLayout.SOUTH);
		return area;
	}

	/**
	 * Makes one action button that sends its action into the battle.
	 *
	 * @param text what the button says
	 * @param action the action string the battle understands
	 * @return the finished button
	 */
	private JButton makeActionButton(String text, final String action)
	{
		JButton button = new JButton(text);
		GuiStyle.styleButton(button);
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				doPlayerAction(action);
			}
		});
		return button;
	}

	/**
	 * Sets up the screen for a brand new fight. Called by GameManagerView.
	 *
	 * @param battle the new battle
	 * @param battleController the controller for that battle
	 */
	public void startBattle(Battle battle, BattleController battleController)
	{
		this.battle = battle;
		this.battleController = battleController;

		logArea.setText("");

		Player player = battle.getPlayer();
		Enemy enemy = battle.getEnemy();

		playerName.setText(player.getCharacterClass());
		enemyName.setText(enemy.getEnemyType());
		// swap sprite panels to the right character for this fight
		playerSprite.type = player.getCharacterClass();
		enemySprite.type  = enemy.getEnemyType();
		playerSprite.repaint();
		enemySprite.repaint();
		promptLabel.setText("What will " + player.getCharacterClass() + " do?");

		// color the player hp bar to match their class
		playerHpBar.setForeground(GuiStyle.colorForClass(player.getCharacterClass()));

		setActionsEnabled(true);
		continueButton.setVisible(false);

		refresh();
	}

	/**
	 * Runs the player's action, then lets the enemy take its turn, then updates
	 * everything on screen. If the fight ended it swaps in a continue button.
	 *
	 * @param action attack, block, skill, or item
	 */
	private void doPlayerAction(String action)
	{
		if (!battle.isActive())
		{
			return;
		}

		battleController.handlePlayerAction(action);

		// let the enemy hit back if the fight is still going
		if (battle.isActive() && battle.getTurn().equals("Enemy"))
		{
			battleController.handleEnemyTurn();
		}

		refresh();

		// if the fight is over, hide the actions and show continue
		if (!battle.isActive())
		{
			setActionsEnabled(false);
			continueButton.setVisible(true);
		}
	}

	/**
	 * Pulls fresh HP numbers and any new log lines out of the battle and puts
	 * them on the screen.
	 */
	private void refresh()
	{
		Player player = battle.getPlayer();
		Enemy enemy = battle.getEnemy();

		playerHpBar.setMaximum(player.getMaxHealth());
		playerHpBar.setValue(player.getHealth());
		playerHpBar.setString("HP " + player.getHealth() + "/" + player.getMaxHealth());

		enemyHpBar.setMaximum(enemy.getMaxHealth());
		enemyHpBar.setValue(enemy.getHealth());
		enemyHpBar.setString("HP " + enemy.getHealth() + "/" + enemy.getMaxHealth());

		// copy the new log lines into the text area then clear them out
		ArrayList<String> lines = battle.getBattleLog();
		for (String line : lines)
		{
			logArea.append(line + "\n");
		}
		battle.clearLog();

		// auto scroll down to the newest message
		logArea.setCaretPosition(logArea.getDocument().getLength());
	}

	private void setActionsEnabled(boolean enabled)
	{
		fightButton.setEnabled(enabled);
		skillsButton.setEnabled(enabled);
		bagButton.setEnabled(enabled);
		blockButton.setEnabled(enabled);
	}

	private JLabel makeLabel(String text)
	{
		JLabel label = new JLabel(text, SwingConstants.CENTER);
		label.setForeground(GuiStyle.TEXT);
		label.setFont(GuiStyle.TEXT_FONT);
		return label;
	}

private JProgressBar makeHpBar(Color color)
	{
		JProgressBar bar = new JProgressBar();
		bar.setStringPainted(true);
		bar.setForeground(color);
		bar.setBackground(new Color(20, 18, 24));
		return bar;
	}
}
