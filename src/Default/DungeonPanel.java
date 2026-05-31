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
import java.awt.Dimension;
import java.awt.GridLayout;
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
 * and a save button. Clicking a door just tells GameManagerView which door
 * was picked, it does not decide what is behind it.
 *
 * DungeonPanel HAS-A GameManagerView so it can report door choices.
 */
public class DungeonPanel extends JPanel
{
	private GameManagerView gameManager;

	private JLabel stageLabel;
	private JProgressBar hpBar;
	private JProgressBar manaBar;
	private JProgressBar staminaBar;

	// keep a reference to all 3 door buttons so we can enable/disable them together
	private JButton[] doorButtons;

	// shows what happened behind the door before the player moves on
	private JLabel eventLabel;
	private JButton continueButton;

	/**
	 * Builds the dungeon screen layout.
	 *
	 * @param gameManager the main window
	 */
	public DungeonPanel(GameManagerView gameManager)
	{
		this.gameManager = gameManager;
		setPreferredSize(new Dimension(760, 540));
		setBackground(GuiStyle.BACKGROUND);
		setLayout(new BorderLayout());

		add(buildTopBar(), BorderLayout.NORTH);
		add(buildDoors(), BorderLayout.CENTER);
		add(buildStats(), BorderLayout.SOUTH);
	}

	// shows the stage number across the top
	private JPanel buildTopBar()
	{
		JPanel top = new JPanel();
		top.setBackground(GuiStyle.BACKGROUND);
		top.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

		stageLabel = new JLabel("Stage 1");
		stageLabel.setForeground(GuiStyle.TEXT);
		stageLabel.setFont(GuiStyle.TITLE_FONT);
		top.add(stageLabel);
		return top;
	}

	/**
	 * Builds the door buttons and the event result area below them.
	 * The event area is hidden until a non-battle door is chosen.
	 *
	 * @return the center panel
	 */
	private JPanel buildDoors()
	{
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setBackground(GuiStyle.BACKGROUND);

		// three door buttons across the top of the center area
		JPanel doorRow = new JPanel(new GridLayout(1, 3, 12, 12));
		doorRow.setBackground(GuiStyle.BACKGROUND);
		doorRow.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

		doorButtons = new JButton[3];
		for (int i = 0; i < 3; i++)
		{
			doorButtons[i] = makeDoorButton("DOOR " + (i + 1), i + 1);
			doorRow.add(doorButtons[i]);
		}

		// event label and continue button shown after picking a non-battle door
		eventLabel = new JLabel("", SwingConstants.CENTER);
		eventLabel.setForeground(GuiStyle.TEXT);
		eventLabel.setFont(GuiStyle.BIG_FONT);
		eventLabel.setVisible(false);

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

		JPanel eventPanel = new JPanel(new BorderLayout(0, 12));
		eventPanel.setBackground(GuiStyle.BACKGROUND);
		eventPanel.setBorder(BorderFactory.createEmptyBorder(20, 80, 20, 80));
		eventPanel.add(eventLabel, BorderLayout.CENTER);
		eventPanel.add(continueButton, BorderLayout.SOUTH);

		wrapper.add(doorRow, BorderLayout.NORTH);
		wrapper.add(eventPanel, BorderLayout.CENTER);

		return wrapper;
	}

	/**
	 * Makes a door button that disables all doors and reports the choice.
	 *
	 * @param text the button label
	 * @param doorNumber which door (1, 2, or 3)
	 * @return the finished button
	 */
	private JButton makeDoorButton(String text, final int doorNumber)
	{
		JButton button = new JButton(text);
		GuiStyle.styleButton(button);
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// lock all doors immediately so the player cant click twice
				setDoorsEnabled(false);
				gameManager.chooseDoor(doorNumber);
			}
		});
		return button;
	}

	/**
	 * Shows what happened behind the door and reveals the continue button.
	 * Called by GameManagerView for reward and nothing events.
	 *
	 * @param message what to show the player
	 */
	public void showEvent(String message)
	{
		eventLabel.setText(message);
		eventLabel.setVisible(true);
		continueButton.setVisible(true);
	}

	/**
	 * Enables or disables all three door buttons.
	 *
	 * @param enabled true to allow clicking
	 */
	public void setDoorsEnabled(boolean enabled)
	{
		for (JButton b : doorButtons)
		{
			b.setEnabled(enabled);
		}
	}

	// the stat bars plus the action buttons along the bottom
	private JPanel buildStats()
	{
		JPanel stats = new JPanel(new GridLayout(4, 1, 4, 4));
		stats.setBackground(GuiStyle.PANEL);
		stats.setPreferredSize(new Dimension(760, 150));

		hpBar = makeBar("HP");
		manaBar = makeBar("MANA");
		staminaBar = makeBar("STAMINA");

		stats.add(hpBar);
		stats.add(manaBar);
		stats.add(staminaBar);

		// put save and home side by side in the last row
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
	 * Updates everything on the dungeon screen from the current player state.
	 * Resets the event area and re-enables doors. Called each time we enter
	 * this screen.
	 *
	 * @param player the current player
	 * @param stage the current stage number
	 */
	public void refresh(Player player, int stage)
	{
		stageLabel.setText("Stage " + stage);

		// reset event area for the new stage
		eventLabel.setVisible(false);
		continueButton.setVisible(false);
		setDoorsEnabled(true);

		hpBar.setForeground(GuiStyle.colorForClass(player.getCharacterClass()));
		hpBar.setMaximum(player.getMaxHealth());
		hpBar.setValue(player.getHealth());
		hpBar.setString("HP " + player.getHealth() + "/" + player.getMaxHealth());

		manaBar.setForeground(GuiStyle.MAGE_BLUE);
		manaBar.setMaximum(player.getMaxMana());
		manaBar.setValue(player.getMana());
		manaBar.setString("MANA " + player.getMana() + "/" + player.getMaxMana());

		staminaBar.setForeground(GuiStyle.ARCHER_GREEN);
		staminaBar.setMaximum(player.getMaxStamina());
		staminaBar.setValue(player.getStamina());
		staminaBar.setString("STAMINA " + player.getStamina() + "/" + player.getMaxStamina());
	}
}
