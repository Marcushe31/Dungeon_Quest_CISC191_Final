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

/**
 * Purpose: HomePanel is the title screen (a View). It shows the game title,
 * a button for each class to start a new game, and a load game button. Picking
 * a class makes that player object and hands it to GameManagerView to start.
 *
 * HomePanel HAS-A GameManagerView so it can start or load a game.
 */
public class HomePanel extends JPanel
{
	private GameManagerView gameManager;

	/**
	 * Builds the title screen layout.
	 *
	 * @param gameManager the main window
	 */
	public HomePanel(GameManagerView gameManager)
	{
		this.gameManager = gameManager;
		setPreferredSize(new Dimension(760, 540));
		setBackground(GuiStyle.BACKGROUND);
		setLayout(new BorderLayout());

		add(buildTitle(), BorderLayout.NORTH);
		add(buildClassChoices(), BorderLayout.CENTER);
		add(buildLoad(), BorderLayout.SOUTH);
	}

	private JPanel buildTitle()
	{
		JPanel top = new JPanel();
		top.setBackground(GuiStyle.BACKGROUND);
		top.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));

		JLabel title = new JLabel("DUNGEON QUEST");
		title.setForeground(GuiStyle.TEXT);
		title.setFont(GuiStyle.TITLE_FONT);
		top.add(title);
		return top;
	}

	// one button per class, each starts a new game as that class
	private JPanel buildClassChoices()
	{
		JPanel choices = new JPanel(new GridLayout(1, 3, 16, 16));
		choices.setBackground(GuiStyle.BACKGROUND);
		choices.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

		choices.add(makeClassButton("WARRIOR", "High HP and damage. Skill: Rage", "Warrior"));
		choices.add(makeClassButton("MAGE", "Strong magic, lots of mana. Skill: Fireball", "Mage"));
		choices.add(makeClassButton("ARCHER", "Balanced fighter. Skill: Double Shot", "Archer"));

		return choices;
	}

	/**
	 * Makes a class button with the name and a short description.
	 *
	 * @param name the class name shown big
	 * @param description a one line summary of the class
	 * @param className the class name we pass to make the player
	 * @return the finished button
	 */
	private JButton makeClassButton(String name, String description, final String className)
	{
		// a little html lets us stack the name and description on two lines
		JButton button = new JButton("<html><center>" + name + "<br><br>" + description + "</center></html>");
		GuiStyle.styleButton(button);
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Player player = makePlayer(className);
				gameManager.startNewGame(player);
			}
		});
		return button;
	}

	/**
	 * Creates the chosen class object.
	 *
	 * @param className which class to make
	 * @return the new player
	 */
	private Player makePlayer(String className)
	{
		if (className.equals("Warrior"))
		{
			return new Warrior();
		}
		else if (className.equals("Mage"))
		{
			return new Mage();
		}
		else
		{
			return new Archer();
		}
	}

	private JPanel buildLoad()
	{
		JPanel bottom = new JPanel();
		bottom.setBackground(GuiStyle.BACKGROUND);
		bottom.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));

		JButton loadButton = new JButton("LOAD GAME");
		GuiStyle.styleButton(loadButton);
		loadButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				gameManager.loadGame();
			}
		});
		bottom.add(loadButton);
		return bottom;
	}
}
