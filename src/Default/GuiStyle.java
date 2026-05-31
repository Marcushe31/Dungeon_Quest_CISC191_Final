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

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;

/**
 * Purpose: GuiStyle holds the shared colors and fonts for the whole GUI so
 * every screen looks the same. It also has a couple helper methods so we
 * dont repeat the same styling code in every panel.
 *
 * GuiStyle is part of the View. It does not hold any game data.
 */
public class GuiStyle
{
	// dark dungeon background and panel colors for the retro look
	public static final Color BACKGROUND = new Color(28, 26, 34);
	public static final Color PANEL = new Color(48, 44, 56);
	public static final Color TEXT = new Color(235, 230, 215);

	// one color per class so each hero feels a little different
	public static final Color WARRIOR_RED = new Color(200, 70, 60);
	public static final Color MAGE_BLUE = new Color(90, 120, 210);
	public static final Color ARCHER_GREEN = new Color(90, 180, 100);
	public static final Color STAMINA_ORANGE = new Color(220, 130, 40);
	public static final Color ENEMY_RED = new Color(140, 40, 40);
	// stone/dungeon tones for door and dungeon backgrounds
	public static final Color STONE_DARK = new Color(50, 48, 58);
	public static final Color STONE_MID = new Color(72, 68, 80);
	public static final Color STONE_LIGHT = new Color(100, 95, 112);
	public static final Color WOOD_DARK = new Color(60, 38, 18);
	public static final Color WOOD_MID = new Color(88, 56, 28);

	// bold monospaced font gives us that pixel/retro feel without a custom ttf
	public static final Font TITLE_FONT = new Font("Monospaced", Font.BOLD, 36);
	public static final Font BIG_FONT = new Font("Monospaced", Font.BOLD, 18);
	public static final Font TEXT_FONT = new Font("Monospaced", Font.BOLD, 14);

	/**
	 * Styles a button to match our theme so we dont have to repeat this
	 * in every panel.
	 *
	 * @param button the button to style
	 */
	public static void styleButton(JButton button)
	{
		button.setBackground(PANEL);
		button.setForeground(TEXT);
		button.setFont(BIG_FONT);
		button.setFocusPainted(false);
		// chunky raised border to look retro instead of flat
		button.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	}

	/**
	 * Picks an HP bar color based on the player's class.
	 *
	 * @param characterClass the class name
	 * @return the color for that class
	 */
	public static Color colorForClass(String characterClass)
	{
		if (characterClass.equals("Warrior"))
		{
			return WARRIOR_RED;
		}
		else if (characterClass.equals("Mage"))
		{
			return MAGE_BLUE;
		}
		else if (characterClass.equals("Archer"))
		{
			return ARCHER_GREEN;
		}
		return TEXT;
	}
}
