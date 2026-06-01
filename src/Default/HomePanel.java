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
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

/**
 * Purpose: HomePanel is the title screen (a View). It shows the game title,
 * a card for each class with their sprite and a short description, and a
 * Load Game button. Clicking a class card starts a new game as that class.
 *
 * HomePanel HAS-A GameManagerView so it can start or load a game.
 */
public class HomePanel extends JPanel
{
	private GameManagerView gameManager;
	private boolean selectionLocked;

	/**
	 * Builds the title screen layout.
	 *
	 * @param gameManager the main window
	 */
	public HomePanel(GameManagerView gameManager)
	{
		this.gameManager = gameManager;
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

		JLabel title = new JLabel("DUNGEON  QUEST", SwingConstants.CENTER);
		title.setForeground(GuiStyle.TEXT);
		title.setFont(GuiStyle.TITLE_FONT);
		top.add(title);

		JLabel sub = new JLabel("-- choose your class --", SwingConstants.CENTER);
		sub.setForeground(new Color(160, 155, 140));
		sub.setFont(GuiStyle.TEXT_FONT);
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setBackground(GuiStyle.BACKGROUND);
		wrapper.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
		wrapper.add(title, BorderLayout.NORTH);
		wrapper.add(sub, BorderLayout.CENTER);
		return wrapper;
	}

	// three class cards side by side
	private JPanel buildClassChoices()
	{
		JPanel choices = new JPanel(new GridLayout(1, 3, 20, 20));
		choices.setBackground(GuiStyle.BACKGROUND);
		choices.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 60));

		choices.add(makeClassCard("Warrior", GuiStyle.WARRIOR_RED,
				"High HP + Damage", "Skill: Rage"));
		choices.add(makeClassCard("Mage", GuiStyle.MAGE_BLUE,
				"Strong magic, lots of mana", "Skill: Fireball"));
		choices.add(makeClassCard("Archer", GuiStyle.ARCHER_GREEN,
				"Balanced fighter", "Skill: Double Shot"));

		return choices;
	}

	/**
	 * Builds a clickable class card with a sprite, name, and description.
	 *
	 * @param className the class to create when clicked
	 * @param accent the accent color for the card border and label
	 * @param desc1 first description line
	 * @param desc2 second description line (skill)
	 * @return the finished card panel
	 */
	private JPanel makeClassCard(final String className, Color accent, String desc1, String desc2)
	{
		JPanel card = new JPanel(new BorderLayout(0, 8));
		card.setBackground(GuiStyle.PANEL);
		card.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createBevelBorder(BevelBorder.RAISED),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		card.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// sprite takes up most of the card
		SpritePanel sprite = new SpritePanel(className);
		card.add(sprite, BorderLayout.CENTER);

		// label area at the bottom
		JPanel labels = new JPanel(new GridLayout(3, 1, 2, 2));
		labels.setBackground(GuiStyle.PANEL);

		JLabel nameLabel = new JLabel(className.toUpperCase(), SwingConstants.CENTER);
		nameLabel.setForeground(accent);
		nameLabel.setFont(GuiStyle.BIG_FONT);

		JLabel d1 = new JLabel(desc1, SwingConstants.CENTER);
		d1.setForeground(GuiStyle.TEXT);
		d1.setFont(GuiStyle.TEXT_FONT);

		JLabel d2 = new JLabel(desc2, SwingConstants.CENTER);
		d2.setForeground(accent);
		d2.setFont(GuiStyle.TEXT_FONT);

		labels.add(nameLabel);
		labels.add(d1);
		labels.add(d2);
		card.add(labels, BorderLayout.SOUTH);

		MouseAdapter clickHandler = new MouseAdapter()
		{
			public void mouseEntered(MouseEvent e)
			{
				card.setBackground(new Color(65, 60, 76));
				labels.setBackground(new Color(65, 60, 76));
				card.repaint();
			}

			public void mouseExited(MouseEvent e)
			{
				card.setBackground(GuiStyle.PANEL);
				labels.setBackground(GuiStyle.PANEL);
				card.repaint();
			}

			// mousePressed fires immediately and cant be spammed
			public void mousePressed(MouseEvent e)
			{
				if (!selectionLocked && e.getButton() == MouseEvent.BUTTON1)
				{
					selectionLocked = true;
					gameManager.startNewGame(makePlayer(className));
				}
			}
		};

		addClickHandler(card, clickHandler);

		return card;
	}

	private void addClickHandler(Component component, MouseAdapter clickHandler)
	{
		component.addMouseListener(clickHandler);
		if (component instanceof JPanel)
		{
			Component[] children = ((JPanel) component).getComponents();
			for (Component child : children)
			{
				addClickHandler(child, clickHandler);
			}
		}
	}

	public void resetSelectionLock()
	{
		selectionLocked = false;
	}

	/**
	 * Creates the chosen class player object.
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
		bottom.setBorder(BorderFactory.createEmptyBorder(10, 0, 40, 0));

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
