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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
	private boolean isBossBattle = false;
	private JPanel battlefield;   // keep ref so we can repaint background
	private JPanel enemyCell;
	private JPanel playerCell;

	// enemy display (top right of the field)
	private JLabel enemyName;
	private SpritePanel enemySprite;
	private JProgressBar enemyHpBar;
	private JProgressBar enemyResourceBar;

	// player display (bottom left of the field)
	private JLabel playerName;
	private SpritePanel playerSprite;
	private JProgressBar playerHpBar;
	private JProgressBar playerResourceBar;

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
		battlefield = new JPanel(null)
		{
			@Override
			protected void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g.create();
				if (isBossBattle) drawDragonLair(g2, getWidth(), getHeight());
				else              drawDungeonBg(g2, getWidth(), getHeight());
				g2.dispose();
			}

			@Override
			public void doLayout()
			{
				int W = getWidth();
				int H = getHeight();
				if (playerCell != null)
				{
					playerCell.setBounds(0, H * 48 / 100, W / 2, H * 52 / 100);
				}
				if (enemyCell != null)
				{
					enemyCell.setBounds(W / 2, H * 28 / 100, W / 2, H * 44 / 100);
				}
			}
		};
		battlefield.setOpaque(true);

		playerCell = buildPlayerCell();
		enemyCell = buildEnemyCell();
		battlefield.add(playerCell);
		battlefield.add(enemyCell);

		return battlefield;
	}

	// dungeon stone background for normal battles
	private void drawDungeonBg(Graphics2D g, int W, int H)
	{
		// dark stone background
		g.setColor(new Color(32, 28, 38));
		g.fillRect(0, 0, W, H);
		// stone block rows
		g.setColor(new Color(44, 40, 52));
		int bw = W / 10, bh = H / 12;
		for (int row = 0; row < 14; row++)
		{
			int off = (row % 2 == 0) ? 0 : bw / 2;
			for (int col = -1; col < 12; col++)
			{
				g.fillRect(col * bw + off + 1, row * bh + 1, bw - 2, bh - 2);
			}
		}
		// dark floor
		g.setColor(new Color(22, 19, 28));
		g.fillRect(0, H * 55 / 100, W, H * 45 / 100);
		g.setColor(new Color(35, 30, 42));
		for (int x = 0; x < W; x += W / 8)
		{
			g.fillRect(x + 2, H * 55 / 100 + 2, W / 8 - 4, H / 16);
		}
		// torches
		drawBgTorch(g, W / 10, H * 30 / 100);
		drawBgTorch(g, W * 9 / 10, H * 30 / 100);
	}

	// dragon lair background for boss fight
	private void drawDragonLair(Graphics2D g, int W, int H)
	{
		// deep red/black cave
		g.setColor(new Color(18, 6, 4));
		g.fillRect(0, 0, W, H);
		// orange cave glow from sides
		for (int i = 3; i > 0; i--)
		{
			g.setColor(new Color(200, 80, 20, i * 18));
			g.fillOval(-W / 3, H / 4 - H / 3 * i / 3, W * 2 / 3, H / 2);
			g.fillOval(W * 2 / 3, H / 4 - H / 3 * i / 3, W * 2 / 3, H / 2);
		}
		// cave ceiling stalactites
		g.setColor(new Color(40, 28, 32));
		for (int x = W / 12; x < W; x += W / 8)
		{
			int h = H / 10 + (x % (W / 5)) / 3;
			int[] xs = {x - W/20, x + W/20, x};
			int[] ys = {0, 0, h};
			g.fillPolygon(xs, ys, 3);
		}
		// dark cave floor
		g.setColor(new Color(28, 18, 14));
		g.fillRect(0, H * 55 / 100, W, H * 45 / 100);
		// gold coin piles on floor
		g.setColor(new Color(200, 165, 30));
		int[] coinX = {W/12, W/5, W*3/10, W*7/10, W*4/5, W*9/10};
		int[] coinY = {84, 74, 88, 86, 72, 84};
		int[] coinS = {20, 18, 17, 18, 19, 21};
		for (int i = 0; i < coinX.length; i++)
		{
			for (int j = 0; j < 7; j++)
			{
				g.fillOval(coinX[i] + j * 7 - 20, H * coinY[i] / 100 + j % 2 * 4,
						coinS[i], Math.max(4, coinS[i] / 3));
			}
		}
		g.setColor(new Color(235, 195, 55));
		for (int i = 0; i < 10; i++)
		{
			int x = W / 14 + i * W / 11;
			if (x > W * 43 / 100 && x < W * 66 / 100)
			{
				x += W / 5;
			}
			int y = H * (58 + (i * 7) % 32) / 100;
			g.fillOval(x, y, 10 + (i % 3) * 3, 4);
		}
	}

	private void drawBgTorch(Graphics2D g, int x, int y)
	{
		g.setColor(new Color(110, 75, 35));
		g.fillRect(x - 4, y, 8, 20);
		g.setColor(new Color(255, 160, 40, 80));
		g.fillOval(x - 14, y - 28, 28, 34);
		g.setColor(new Color(255, 200, 60));
		g.fillOval(x - 7, y - 18, 14, 20);
	}

	private JPanel emptyCell()
	{
		JPanel cell = new JPanel();
		cell.setOpaque(false);
		return cell;
	}

	private JPanel buildEnemyCell()
	{
		JPanel cell = new JPanel(new BorderLayout());
		cell.setOpaque(false);

		enemyName = makeLabel("ENEMY");
		enemyHpBar = makeHpBar(GuiStyle.ENEMY_RED);
		enemyResourceBar = makeHpBar(GuiStyle.STAMINA_ORANGE);
		enemyResourceBar.setVisible(false);
		enemySprite = new SpritePanel("Goblin"); // updated in startBattle()

		JPanel bars = new JPanel(new GridLayout(2, 1, 0, 2));
		bars.setOpaque(false);
		bars.add(enemyHpBar);
		bars.add(enemyResourceBar);

		JPanel info = new JPanel(new BorderLayout());
		info.setOpaque(false);
		info.add(enemyName, BorderLayout.NORTH);
		info.add(bars, BorderLayout.SOUTH);

		cell.add(info, BorderLayout.NORTH);
		cell.add(makeSpriteStand(enemySprite, true), BorderLayout.CENTER);
		return cell;
	}

	private JPanel buildPlayerCell()
	{
		JPanel cell = new JPanel(new BorderLayout());
		cell.setOpaque(false);

		playerName = makeLabel("HERO");
		playerHpBar = makeHpBar(GuiStyle.TEXT);
		playerResourceBar = makeHpBar(GuiStyle.STAMINA_ORANGE);
		playerSprite = new SpritePanel("Warrior"); // updated in startBattle()

		JPanel bars = new JPanel(new GridLayout(2, 1, 0, 2));
		bars.setOpaque(false);
		bars.add(playerHpBar);
		bars.add(playerResourceBar);

		JPanel info = new JPanel(new BorderLayout());
		info.setOpaque(false);
		info.add(playerName, BorderLayout.NORTH);
		info.add(bars, BorderLayout.SOUTH);

		cell.add(makeSpriteStand(playerSprite, false), BorderLayout.CENTER);
		cell.add(info, BorderLayout.SOUTH);
		return cell;
	}

	private JPanel makeSpriteStand(SpritePanel sprite, boolean enemy)
	{
		JPanel stand = new JPanel(new BorderLayout());
		stand.setOpaque(false);

		JPanel anchor = new JPanel(new BorderLayout())
		{
			@Override
			protected void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setColor(new Color(0, 0, 0, 70));
					int y = getHeight() - 44;
					g2.fillOval(getWidth() / 2 - 78, y, 156, 18);
				g2.dispose();
			}
		};
		anchor.setOpaque(false);
		anchor.setPreferredSize(new Dimension(0, 285));
		anchor.add(sprite, BorderLayout.CENTER);
		stand.add(anchor, BorderLayout.SOUTH);
		stand.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		return stand;
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
		continueButton.setPreferredSize(new Dimension(220, 55));
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
		saveQuitButton.setPreferredSize(new Dimension(220, 55));
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
		quitHomeButton.setPreferredSize(new Dimension(220, 55));
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
	public void startBattle(Battle battle, BattleController battleController, boolean isBossBattle)
	{
		this.battle = battle;
		this.battleController = battleController;
		this.isBossBattle = isBossBattle;
		battlefield.repaint();

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
		String resourceName = player.getCharacterClass().equals("Mage") ? "MANA" : "STAMINA";
		int skillCost = player.getSkillCount() > 0 ? player.getSkill(0).getCost() : 0;
		skillsButton.setText("SKILLS (" + skillCost + " " + resourceName + ")");
		promptLabel.setText("What will " + player.getCharacterClass() + " do?");

		// color the player hp bar to match their class
		playerHpBar.setForeground(GuiStyle.colorForClass(player.getCharacterClass()));
		updatePlayerResourceBar(player);
		enemyResourceBar.setVisible(false);

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

		// Finished battles move straight to the next screen.
		if (!battle.isActive())
		{
			setActionsEnabled(false);
			continueButton.setVisible(false);
			gameManager.onBattleEnd();
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

		updatePlayerResourceBar(player);

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

	private void updatePlayerResourceBar(Player player)
	{
		if (player.getCharacterClass().equals("Mage"))
		{
			playerResourceBar.setVisible(true);
			playerResourceBar.setForeground(GuiStyle.MAGE_BLUE);
			playerResourceBar.setMaximum(player.getMaxMana());
			playerResourceBar.setValue(player.getMana());
			playerResourceBar.setString("MANA " + player.getMana() + "/" + player.getMaxMana());
		}
		else
		{
			playerResourceBar.setVisible(true);
			playerResourceBar.setForeground(GuiStyle.STAMINA_ORANGE);
			playerResourceBar.setMaximum(player.getMaxStamina());
			playerResourceBar.setValue(player.getStamina());
			playerResourceBar.setString("STAMINA " + player.getStamina() + "/" + player.getMaxStamina());
		}
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
