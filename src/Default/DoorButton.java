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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

/**
 * Purpose: DoorButton is a custom painted JPanel that looks like a dungeon
 * door (stone arch, wooden door, metal studs, ring handle). Clicking it
 * calls GameManagerView.chooseDoor() with this door's number.
 *
 * DoorButton is part of the View. It uses a MouseListener for click events.
 */
public class DoorButton extends JPanel
{
	private int doorNumber;
	private GameManagerView gameManager;
	private boolean hovered;
	private boolean enabled;
	private boolean revealedPotion;

	// stone and wood colors shared across all doors
	private static final Color STONE_A = new Color(55, 52, 65);
	private static final Color STONE_B = new Color(78, 74, 90);
	private static final Color STONE_C = new Color(105, 100, 118);
	private static final Color WOOD_A  = new Color(65, 40, 18);
	private static final Color WOOD_B  = new Color(90, 58, 26);
	private static final Color METAL   = new Color(90, 88, 96);

	/**
	 * Creates a door button for the given door number.
	 *
	 * @param doorNumber which door this is (1, 2, or 3)
	 * @param gameManager the main window to notify on click
	 */
	public DoorButton(int doorNumber, GameManagerView gameManager)
	{
		this.doorNumber = doorNumber;
		this.gameManager = gameManager;
		this.enabled = true;
		this.hovered = false;

		setCursor(new Cursor(Cursor.HAND_CURSOR));

		addMouseListener(new MouseAdapter()
		{
			// use mousePressed so it fires immediately on click down
			// mouseClicked requires no movement between press and release which
			// makes it unreliable -- this was the bug
			public void mousePressed(MouseEvent e)
			{
				if (enabled)
				{
					// disable immediately so a second click cant sneak through
					enabled = false;
					repaint();
					gameManager.chooseDoor(DoorButton.this.doorNumber);
				}
			}

			public void mouseEntered(MouseEvent e)
			{
				hovered = true;
				repaint();
			}

			public void mouseExited(MouseEvent e)
			{
				hovered = false;
				repaint();
			}
		});
	}

	/**
	 * Enables or disables click handling. Disabled doors are drawn grayed out.
	 *
	 * @param enabled true to allow clicking
	 */
	@Override
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
		repaint();
	}

	public void clearReveal()
	{
		revealedPotion = false;
		repaint();
	}

	public void showPotion()
	{
		revealedPotion = true;
		enabled = false;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		int W = getWidth();
		int H = getHeight();

		// dim everything if disabled
		if (!enabled)
		{
			g2.setColor(new Color(30, 28, 36));
			g2.fillRect(0, 0, W, H);
			if (revealedPotion)
			{
				drawOpenDoorReward(g2, W, H);
			}
			g2.dispose();
			return;
		}

		// subtle hover brightness
		Color bg = hovered ? STONE_B : STONE_A;
		g2.setColor(bg);
		g2.fillRect(0, 0, W, H);

		drawStoneBricks(g2, W, H);
		drawArch(g2, W, H);
		drawDoor(g2, W, H);
		g2.dispose();
	}

	private void drawOpenDoorReward(Graphics2D g, int W, int H)
	{
		g.setColor(new Color(10, 8, 14));
		g.fillRoundRect(W * 18 / 100, H * 10 / 100, W * 64 / 100, H * 80 / 100,
				W / 15, W / 15);
		g.setColor(new Color(80, 74, 90));
		g.setStroke(new BasicStroke(5));
		g.drawRoundRect(W * 18 / 100, H * 10 / 100, W * 64 / 100, H * 80 / 100,
				W / 15, W / 15);
		g.setColor(new Color(160, 25, 35, 90));
		g.fillOval(W * 28 / 100, H * 24 / 100, W * 44 / 100, H * 50 / 100);
		drawBigPotion(g, W / 2, H * 50 / 100, Math.min(W, H) * 42 / 100);
	}

	private void drawBigPotion(Graphics2D g, int cx, int cy, int s)
	{
		int x = cx - s / 2;
		int y = cy - s / 2;
		g.setColor(new Color(120, 70, 30));
		g.fillRect(cx - s / 8, y, s / 4, s / 7);
		g.setColor(new Color(210, 235, 245));
		g.fillRect(cx - s / 6, y + s / 8, s / 3, s / 5);
		g.setColor(new Color(210, 40, 50));
		g.fillOval(x + s / 8, y + s / 4, s * 3 / 4, s * 2 / 3);
		g.setColor(new Color(245, 90, 100));
		g.fillOval(x + s / 5, y + s / 3, s / 4, s / 5);
		g.setColor(Color.WHITE);
		g.fillRect(cx - s / 12, cy - s / 10, s / 6, s / 3);
		g.fillRect(cx - s / 5, cy, s * 2 / 5, s / 7);
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(3));
		g.drawOval(x + s / 8, y + s / 4, s * 3 / 4, s * 2 / 3);
		g.drawRect(cx - s / 6, y + s / 8, s / 3, s / 5);
	}

	/**
	 * Draws a staggered stone brick pattern behind the door.
	 */
	private void drawStoneBricks(Graphics2D g, int W, int H)
	{
		int brickH = H / 10;
		int brickW = W / 3;

		for (int row = 0; row < 11; row++)
		{
			int offsetX = (row % 2 == 0) ? 0 : brickW / 2;
			int y = row * brickH;
			for (int col = -1; col < 4; col++)
			{
				int x = col * brickW + offsetX;
				// alternate between two stone shades for texture
				Color c = ((row + col) % 2 == 0) ? STONE_B : STONE_A;
				g.setColor(c);
				g.fillRect(x + 1, y + 1, brickW - 2, brickH - 2);
				g.setColor(new Color(25, 22, 30));
				g.setStroke(new BasicStroke(1));
				g.drawRect(x, y, brickW, brickH);
			}
		}
	}

	/**
	 * Draws the stone arch framing the top of the door.
	 */
	private void drawArch(Graphics2D g, int W, int H)
	{
		int doorW = W * 60 / 100;
		int archX = (W - doorW) / 2;
		int archTopY = H * 8 / 100;
		int archH = H * 15 / 100;

		// outer arch (dark stone frame)
		g.setColor(new Color(30, 28, 36));
		g.fillArc(archX - W/15, archTopY - H/20, doorW + W*2/15, archH * 2, 0, 180);

		// inner arch fill (dark opening)
		g.setColor(new Color(10, 8, 14));
		g.fillArc(archX, archTopY, doorW, archH * 2, 0, 180);

		// arch keystone (center top)
		int ks = W / 10;
		g.setColor(STONE_C);
		g.fillRect(W / 2 - ks / 2, archTopY - H / 20, ks, H / 12);
		g.setColor(new Color(25, 22, 30));
		g.setStroke(new BasicStroke(1.5f));
		g.drawRect(W / 2 - ks / 2, archTopY - H / 20, ks, H / 12);
	}

	/**
	 * Draws the wooden door itself with planks, metal studs, and a ring handle.
	 */
	private void drawDoor(Graphics2D g, int W, int H)
	{
		int doorW = W * 60 / 100;
		int doorX = (W - doorW) / 2;
		int doorY = H * 18 / 100;
		int doorH = H * 72 / 100;
		int arc   = doorW / 10;

		// door frame (dark border)
		g.setColor(new Color(35, 20, 8));
		g.fillRoundRect(doorX - W/25, doorY - H/40, doorW + W*2/25, doorH + H/25, arc, arc);

		// door face (wood)
		Color doorCol = hovered ? new Color(100, 64, 28) : WOOD_B;
		g.setColor(doorCol);
		g.fillRoundRect(doorX, doorY, doorW, doorH, arc, arc);

		// vertical plank lines (gives it a wooden look)
		g.setColor(WOOD_A);
		g.setStroke(new BasicStroke(1.5f));
		int planks = 4;
		for (int i = 1; i < planks; i++)
		{
			int px = doorX + doorW * i / planks;
			g.drawLine(px, doorY + arc/2, px, doorY + doorH - arc/2);
		}

		// horizontal cross brace near top and bottom
		int braceY1 = doorY + doorH / 5;
		int braceY2 = doorY + doorH * 4 / 5;
		g.setColor(METAL);
		g.setStroke(new BasicStroke(3));
		g.drawLine(doorX + arc/2, braceY1, doorX + doorW - arc/2, braceY1);
		g.drawLine(doorX + arc/2, braceY2, doorX + doorW - arc/2, braceY2);

		// metal studs in a 2-column grid
		int studR = W / 24;
		int cols = 2, rows = 4;
		for (int r = 0; r < rows; r++)
		{
			for (int c = 0; c < cols; c++)
			{
				int sx = doorX + doorW * (c + 1) / (cols + 1) - studR;
				int sy = doorY + doorH / 6 + r * (doorH * 4 / 5) / (rows + 1);
				g.setColor(METAL);
				g.fillOval(sx, sy, studR * 2, studR * 2);
				g.setColor(new Color(130, 128, 135));
				g.fillOval(sx + studR/3, sy + studR/3, studR/2, studR/2);
				g.setColor(new Color(50, 48, 55));
				g.setStroke(new BasicStroke(1));
				g.drawOval(sx, sy, studR * 2, studR * 2);
			}
		}

		// ring handle in center
		int rx = W / 2 - W/12;
		int ry = doorY + doorH / 2 - W/12;
		g.setColor(new Color(100, 95, 50));
		g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.drawOval(rx, ry, W/6, W/6);
		// handle mount (small plate behind ring)
		g.setColor(METAL);
		g.fillOval(W/2 - W/20, doorY + doorH/2 - W/20, W/10, W/10);
		g.setColor(new Color(50, 48, 55));
		g.setStroke(new BasicStroke(1));
		g.drawOval(W/2 - W/20, doorY + doorH/2 - W/20, W/10, W/10);

		// door outline
		g.setColor(new Color(20, 12, 5));
		g.setStroke(new BasicStroke(2));
		g.drawRoundRect(doorX, doorY, doorW, doorH, arc, arc);
	}

	/**
	 * Draws the door number label at the bottom of the button.
	 */
	private void drawDoorLabel(Graphics2D g, int W, int H)
	{
		g.setColor(GuiStyle.TEXT);
		g.setFont(new Font("Monospaced", Font.BOLD, Math.max(12, W / 8)));
		String label = "DOOR " + doorNumber;
		int sw = g.getFontMetrics().stringWidth(label);
		g.drawString(label, (W - sw) / 2, H * 95 / 100);
	}
}
