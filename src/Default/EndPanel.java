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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Purpose: EndPanel is the win/lose screen (a View). It shows a big result
 * title and a short message, plus a button to go back to the title screen.
 * GameManagerView sets the text before showing this screen.
 *
 * EndPanel HAS-A GameManagerView so it can return to the home screen.
 */
public class EndPanel extends JPanel
{
	private GameManagerView gameManager;
	private JLabel titleLabel;
	private JLabel messageLabel;

	/**
	 * Builds the end screen layout.
	 *
	 * @param gameManager the main window
	 */
	public EndPanel(GameManagerView gameManager)
	{
		this.gameManager = gameManager;
		setPreferredSize(new Dimension(760, 540));
		setBackground(GuiStyle.BACKGROUND);
		setLayout(new BorderLayout());

		titleLabel = new JLabel("", SwingConstants.CENTER);
		titleLabel.setForeground(GuiStyle.TEXT);
		titleLabel.setFont(GuiStyle.TITLE_FONT);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(80, 0, 0, 0));

		messageLabel = new JLabel("", SwingConstants.CENTER);
		messageLabel.setForeground(GuiStyle.TEXT);
		messageLabel.setFont(GuiStyle.BIG_FONT);

		JButton homeButton = new JButton("BACK TO TITLE");
		GuiStyle.styleButton(homeButton);
		homeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				gameManager.goHome();
			}
		});

		JPanel bottom = new JPanel();
		bottom.setBackground(GuiStyle.BACKGROUND);
		bottom.setBorder(BorderFactory.createEmptyBorder(20, 0, 50, 0));
		bottom.add(homeButton);

		add(titleLabel, BorderLayout.NORTH);
		add(messageLabel, BorderLayout.CENTER);
		add(bottom, BorderLayout.SOUTH);
	}

	/**
	 * Sets the win or lose text. Called right before this screen is shown.
	 *
	 * @param title the big result line (like VICTORY!)
	 * @param message the smaller line underneath
	 */
	public void showResult(String title, String message)
	{
		titleLabel.setText(title);
		messageLabel.setText(message);
	}
}
