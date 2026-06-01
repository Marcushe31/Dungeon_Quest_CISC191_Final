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
 *         Version: 2026-05-31
 */
package Default;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

/**
 * Purpose: DungeonPanel is the dungeon exploration screen (a View). It shows
 * the current stage, three door buttons, the player's HP/Mana/Stamina bars,
 * and save/quit buttons. Clicking a door disables all three and reports the
 * choice to GameManagerView.
 *
 * DungeonPanel HAS-A GameManagerView so it can report door choices.
 */
public class DungeonPanel extends JPanel
{
	private GameManagerView gameManager;

	private JLabel stageLabel;
	private JLabel enemyCountLabel;
	private JProgressBar hpBar;
	private JProgressBar manaBar;
	private JProgressBar staminaBar;

	// keep refs to the door buttons so we can enable/disable them together
	private DoorButton[] doorButtons;
	private Player currentPlayer;

	// shown after non-battle door events before player hits continue
	private JLabel eventLabel;
	private JPanel inventoryPanel;
	private JButton continueButton;

	/**
	 * Builds the dungeon screen layout.
	 *
	 * @param gameManager the main window
	 */
	public DungeonPanel(GameManagerView gameManager)
	{
		this.gameManager = gameManager;
		setBackground(GuiStyle.BACKGROUND);
		setLayout(new BorderLayout());

		add(buildTopBar(), BorderLayout.NORTH);
		add(buildDoors(), BorderLayout.CENTER);
		add(buildStats(), BorderLayout.SOUTH);
	}

	// stage label and enemy counter across the top
	private JPanel buildTopBar()
	{
		JPanel top = new JPanel(new BorderLayout());
		top.setBackground(GuiStyle.BACKGROUND);
		top.setBorder(BorderFactory.createEmptyBorder(18, 24, 8, 24));

		stageLabel = new JLabel("Stage 1", SwingConstants.CENTER);
		stageLabel.setForeground(GuiStyle.TEXT);
		stageLabel.setFont(GuiStyle.TITLE_FONT);

		enemyCountLabel = new JLabel("Enemies: 0 / 3", SwingConstants.RIGHT);
		enemyCountLabel.setForeground(GuiStyle.WARRIOR_RED);
		enemyCountLabel.setFont(GuiStyle.BIG_FONT);

		top.add(stageLabel, BorderLayout.CENTER);
		top.add(enemyCountLabel, BorderLayout.EAST);
		return top;
	}

	/**
	 * Builds the center area: three DoorButton panels across the top and an
	 * event result area in the middle that stays hidden until needed.
	 *
	 * @return the center panel
	 */
	private JPanel buildDoors()
	{
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setBackground(GuiStyle.BACKGROUND);

		// three DoorButton panels side by side -- they fill the available space
		JPanel doorRow = new JPanel(new GridLayout(1, 3, 14, 0));
		doorRow.setBackground(GuiStyle.BACKGROUND);
		doorRow.setBorder(BorderFactory.createEmptyBorder(10, 28, 10, 28));
		// give the door row most of the height so doors look tall and dramatic
		int sh = Toolkit.getDefaultToolkit().getScreenSize().height;
		doorRow.setPreferredSize(new Dimension(0, sh * 60 / 100));

		doorButtons = new DoorButton[3];
		for (int i = 0; i < 3; i++)
		{
			doorButtons[i] = new DoorButton(i + 1, gameManager);
			doorRow.add(doorButtons[i]);
		}

		// event result area (hidden until a door is picked)
		eventLabel = new JLabel("", SwingConstants.CENTER);
		eventLabel.setForeground(GuiStyle.TEXT);
		eventLabel.setFont(GuiStyle.BIG_FONT);
		eventLabel.setVisible(false);

		inventoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
		inventoryPanel.setOpaque(false);

		continueButton = new JButton("CONTINUE");
		GuiStyle.styleButton(continueButton);
		continueButton.setVisible(false);
		continueButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				gameManager.onDungeonContinue();
			}
		});

		JPanel eventPanel = new JPanel(new BorderLayout(0, 10));
		eventPanel.setBackground(GuiStyle.BACKGROUND);
		eventPanel.setBorder(BorderFactory.createEmptyBorder(16, 100, 16, 100));
		eventPanel.add(inventoryPanel, BorderLayout.WEST);
		eventPanel.add(eventLabel, BorderLayout.CENTER);
		eventPanel.add(continueButton, BorderLayout.SOUTH);

		wrapper.add(doorRow, BorderLayout.NORTH);
		wrapper.add(eventPanel, BorderLayout.CENTER);
		return wrapper;
	}

	/**
	 * Shows the event result and optionally a sprite.
	 * Called by GameManagerView after a non-battle door is chosen.
	 *
	 * @param message what to show the player
	 * @param spriteName sprite type to show, or null for no sprite
	 */
	public void showEvent(String message, String spriteName, int doorChoice)
	{
		eventLabel.setText(message);
		eventLabel.setVisible(true);
		continueButton.setVisible(true);
		updateInventoryTally();

		if (spriteName != null)
		{
			doorButtons[doorChoice - 1].showPotion();
		}
	}

	/**
	 * Enables or disables all three door buttons.
	 *
	 * @param enabled true to allow clicking
	 */
	public void setDoorsEnabled(boolean enabled)
	{
		for (DoorButton b : doorButtons)
		{
			b.setEnabled(enabled);
		}
	}

	private void updateInventoryTally()
	{
		inventoryPanel.removeAll();
		if (currentPlayer != null)
		{
			for (int i = 0; i < currentPlayer.getItemCount(); i++)
			{
				SpritePanel potion = new SpritePanel("potion");
				potion.setPreferredSize(new Dimension(34, 34));
				inventoryPanel.add(potion);
			}
		}
		inventoryPanel.revalidate();
		inventoryPanel.repaint();
	}

	// stat bars and save/home buttons at the bottom
	private JPanel buildStats()
	{
		int sh = Toolkit.getDefaultToolkit().getScreenSize().height;
		JPanel stats = new JPanel(new GridLayout(4, 1, 4, 4));
		stats.setBackground(GuiStyle.PANEL);
		// scale the stats bar height proportionally to the screen
		stats.setPreferredSize(new Dimension(0, sh * 16 / 100));

		hpBar      = makeBar("HP");
		manaBar    = makeBar("MANA");
		staminaBar = makeBar("STAMINA");

		stats.add(hpBar);
		stats.add(manaBar);
		stats.add(staminaBar);

		JPanel buttonRow = new JPanel(new GridLayout(1, 2, 6, 6));
		buttonRow.setBackground(GuiStyle.PANEL);

		JButton saveButton = new JButton("SAVE GAME");
		GuiStyle.styleButton(saveButton);
		saveButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				gameManager.saveGame();
			}
		});

		JButton homeButton = new JButton("QUIT TO HOME");
		GuiStyle.styleButton(homeButton);
		homeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				gameManager.goHome();
			}
		});

		buttonRow.add(saveButton);
		buttonRow.add(homeButton);
		stats.add(buttonRow);

		return stats;
	}

	private JProgressBar makeBar(String label)
	{
		JProgressBar bar = new JProgressBar();
		bar.setStringPainted(true);
		bar.setString(label);
		bar.setForeground(GuiStyle.TEXT);
		return bar;
	}

	/**
	 * Updates the whole dungeon screen from current player state.
	 * Resets event area, re-enables doors, updates all stat bars.
	 *
	 * @param player the current player
	 * @param stage the current stage number
	 * @param enemiesDefeated how many enemies beaten this run
	 */
	public void refresh(Player player, int stage, int enemiesDefeated)
	{
		currentPlayer = player;
		stageLabel.setText("Stage " + stage);
		enemyCountLabel.setText("Enemies: " + enemiesDefeated + " / 3");

		eventLabel.setVisible(false);
		continueButton.setVisible(false);
		for (DoorButton doorButton : doorButtons)
		{
			doorButton.clearReveal();
		}
		setDoorsEnabled(true);
		updateInventoryTally();

		hpBar.setForeground(GuiStyle.colorForClass(player.getCharacterClass()));
		hpBar.setMaximum(player.getMaxHealth());
		hpBar.setValue(player.getHealth());
		hpBar.setString("HP " + player.getHealth() + "/" + player.getMaxHealth());

		manaBar.setForeground(GuiStyle.MAGE_BLUE);
		manaBar.setMaximum(player.getMaxMana());
		manaBar.setValue(player.getMana());
		manaBar.setString("MANA " + player.getMana() + "/" + player.getMaxMana());
		manaBar.setVisible(player.getCharacterClass().equals("Mage"));

		// stamina is orange now
		staminaBar.setForeground(GuiStyle.STAMINA_ORANGE);
		staminaBar.setMaximum(player.getMaxStamina());
		staminaBar.setValue(player.getStamina());
		staminaBar.setString("STAMINA " + player.getStamina() + "/" + player.getMaxStamina());
		staminaBar.setVisible(!player.getCharacterClass().equals("Mage"));
	}
}
