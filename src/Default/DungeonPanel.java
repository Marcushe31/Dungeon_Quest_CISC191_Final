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

	// the three doors the player chooses between
	private JPanel buildDoors()
	{
		JPanel doors = new JPanel(new GridLayout(1, 3, 12, 12));
		doors.setBackground(GuiStyle.BACKGROUND);
		doors.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

		doors.add(makeDoorButton("Door 1", 1));
		doors.add(makeDoorButton("Door 2", 2));
		doors.add(makeDoorButton("Door 3", 3));

		return doors;
	}

	/**
	 * Makes a door button that reports its number when clicked.
	 *
	 * @param text the button label
	 * @param doorNumber which door this is (1, 2, or 3)
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
				gameManager.chooseDoor(doorNumber);
			}
		});
		return button;
	}

	// the stat bars plus the save button along the bottom
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

		JButton saveButton = new JButton("SAVE GAME");
		GuiStyle.styleButton(saveButton);
		saveButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				gameManager.saveGame();
			}
		});
		stats.add(saveButton);

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
	 * Updates the stage number and all the stat bars from the player. Called
	 * every time we enter the dungeon screen.
	 *
	 * @param player the current player
	 * @param stage the current stage number
	 */
	public void refresh(Player player, int stage)
	{
		stageLabel.setText("Stage " + stage);

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
