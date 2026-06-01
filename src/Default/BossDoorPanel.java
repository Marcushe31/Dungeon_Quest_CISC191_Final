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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Purpose: BossDoorPanel shows a single large menacing door before the boss
 * fight. It replaces the normal 3-door screen when the player has defeated 3
 * enemies. Clicking ENTER THE LAIR triggers the Dragon fight.
 *
 * BossDoorPanel HAS-A GameManagerView to trigger the boss battle.
 */
public class BossDoorPanel extends JPanel
{
	private GameManagerView gameManager;

	/**
	 * Builds the boss door screen.
	 *
	 * @param gameManager the main window
	 */
	public BossDoorPanel(GameManagerView gameManager)
	{
		this.gameManager = gameManager;
		setBackground(new Color(12, 6, 8));
		setLayout(new BorderLayout());

		// warning text at top
		JLabel warningLabel = new JLabel("-- 3 ENEMIES DEFEATED --", SwingConstants.CENTER);
		warningLabel.setForeground(new Color(180, 60, 60));
		warningLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
		warningLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

		// door painting in center
		JPanel doorPainter = new JPanel()
		{
			@Override
			protected void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				drawBossDoor(g2, getWidth(), getHeight());
				g2.dispose();
			}
		};
		doorPainter.setBackground(new Color(12, 6, 8));

		// enter button at bottom
		JLabel subLabel = new JLabel("THE DRAGON AWAITS BEYOND...", SwingConstants.CENTER);
		subLabel.setForeground(new Color(200, 60, 60));
		subLabel.setFont(new Font("Monospaced", Font.BOLD, 22));

		JButton enterButton = new JButton("ENTER THE LAIR");
		enterButton.setBackground(new Color(100, 20, 20));
		enterButton.setForeground(new Color(255, 200, 100));
		enterButton.setFont(new Font("Monospaced", Font.BOLD, 28));
		enterButton.setFocusPainted(false);
		enterButton.setBorder(BorderFactory.createLineBorder(new Color(200, 60, 60), 3));
		enterButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				gameManager.enterBossFight();
			}
		});

		JPanel bottom = new JPanel(new BorderLayout(0, 12));
		bottom.setBackground(new Color(12, 6, 8));
		bottom.setBorder(BorderFactory.createEmptyBorder(10, 80, 40, 80));
		bottom.add(subLabel, BorderLayout.NORTH);
		bottom.add(enterButton, BorderLayout.CENTER);

		add(warningLabel, BorderLayout.NORTH);
		add(doorPainter, BorderLayout.CENTER);
		add(bottom, BorderLayout.SOUTH);
	}

	/**
	 * Draws the large menacing boss door with dragon symbols.
	 */
	private void drawBossDoor(Graphics2D g, int W, int H)
	{
		int cx = W / 2;

		// background: pulsing dark red atmosphere
		g.setColor(new Color(30, 5, 5));
		g.fillRect(0, 0, W, H);

		// dungeon stone floor
		g.setColor(new Color(40, 36, 44));
		g.fillRect(0, H * 78 / 100, W, H * 22 / 100);
		g.setColor(new Color(50, 46, 56));
		for (int x = 0; x < W; x += W / 8)
		{
			g.fillRect(x + 2, H * 78 / 100 + 2, W / 8 - 4, H / 18);
		}

		// torches on sides
		drawTorch(g, W / 8, H * 35 / 100);
		drawTorch(g, W * 7 / 8, H * 35 / 100);

		// atmospheric red glow from door
		for (int i = 5; i > 0; i--)
		{
			g.setColor(new Color(180, 20, 20, i * 12));
			int gw = (int)(W * 0.55 * i / 5), gh = (int)(H * 0.85 * i / 5);
			g.fillOval(cx - gw / 2, H / 2 - gh / 2, gw, gh);
		}

		// door dimensions
		int dw = W * 35 / 100;
		int dh = H * 72 / 100;
		int dx = cx - dw / 2;
		int dy = H * 6 / 100;

		// outer stone frame
		g.setColor(new Color(40, 35, 45));
		g.fillRect(dx - dw / 8, dy - dh / 8, dw + dw / 4, dh + dh / 8);

		// stone arch above door
		g.setColor(new Color(55, 50, 62));
		g.fillArc(dx - dw / 6, dy - dh / 4, dw + dw / 3, dh / 2, 0, 180);

		// arch keystone (center top)
		g.setColor(new Color(75, 68, 85));
		int ks = dw / 8;
		g.fillRect(cx - ks / 2, dy - dh / 4 - ks / 2, ks, ks + dh / 12);
		g.setColor(new Color(200, 60, 60));
		// skull carved on keystone
		g.fillOval(cx - ks / 3, dy - dh / 4, ks * 2 / 3, ks * 2 / 3);

		// main door body
		g.setColor(new Color(18, 8, 8));
		g.fillRect(dx, dy, dw, dh);

		// door vertical planks
		g.setColor(new Color(30, 14, 14));
		g.setStroke(new BasicStroke(3));
		for (int i = 1; i < 4; i++)
		{
			g.drawLine(dx + dw * i / 4, dy + 10, dx + dw * i / 4, dy + dh - 10);
		}

		// horizontal reinforcement bars (iron/chain)
		g.setColor(new Color(80, 72, 88));
		g.setStroke(new BasicStroke(6));
		g.drawLine(dx + 8, dy + dh / 5, dx + dw - 8, dy + dh / 5);
		g.drawLine(dx + 8, dy + dh * 4 / 5, dx + dw - 8, dy + dh * 4 / 5);

		// metal studs (spikes)
		g.setColor(new Color(120, 110, 130));
		for (int row = 0; row < 4; row++)
		{
			for (int col = 0; col < 3; col++)
			{
				int sx = dx + dw * (col + 1) / 4 - 6;
				int sy = dy + dh / 6 + row * dh / 4;
				g.fillOval(sx, sy, 12, 12);
				g.setColor(new Color(160, 150, 170));
				g.fillOval(sx + 2, sy + 2, 5, 5);
				g.setColor(new Color(120, 110, 130));
			}
		}

		// dragon skull emblem in center of door
		drawDragonSkull(g, cx, dy + dh / 2, dw * 30 / 100);

		// door outline
		g.setColor(new Color(80, 20, 20));
		g.setStroke(new BasicStroke(4));
		g.drawRect(dx, dy, dw, dh);
	}

	// draws a flame torch at the given position
	private void drawTorch(Graphics2D g, int x, int y)
	{
		// handle
		g.setColor(new Color(110, 75, 35));
		g.fillRect(x - 5, y, 10, 30);
		// flame glow
		g.setColor(new Color(255, 140, 30, 60));
		g.fillOval(x - 20, y - 40, 40, 50);
		g.setColor(new Color(255, 180, 50, 100));
		g.fillOval(x - 14, y - 32, 28, 36);
		// flame
		g.setColor(new Color(255, 200, 50));
		g.fillOval(x - 8, y - 22, 16, 24);
		g.setColor(new Color(255, 130, 30));
		g.fillOval(x - 5, y - 14, 10, 16);
	}

	// draws a simple dragon skull symbol
	private void drawDragonSkull(Graphics2D g, int cx, int cy, int r)
	{
		g.setColor(new Color(160, 30, 30));
		// skull outline circle
		g.setStroke(new BasicStroke(3));
		g.drawOval(cx - r / 2, cy - r / 2, r, r);
		// eye sockets
		g.fillOval(cx - r / 3, cy - r / 8, r / 5, r / 5);
		g.fillOval(cx + r / 8, cy - r / 8, r / 5, r / 5);
		// horns
		int[] hx1 = {cx - r / 3, cx - r / 2, cx - r / 6};
		int[] hy1 = {cy - r / 2, cy - r, cy - r / 2};
		g.fillPolygon(hx1, hy1, 3);
		int[] hx2 = {cx + r / 3, cx + r / 2, cx + r / 6};
		int[] hy2 = {cy - r / 2, cy - r, cy - r / 2};
		g.fillPolygon(hx2, hy2, 3);
	}
}
