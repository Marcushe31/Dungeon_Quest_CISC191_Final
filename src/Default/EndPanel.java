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
import java.awt.Dimension;
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
 * Purpose: EndPanel shows the win or lose screen with custom pixel art.
 * Victory shows a crown + stars, defeat shows a gravestone. GameManagerView
 * calls showResult() before switching to this screen.
 *
 * EndPanel HAS-A GameManagerView to return home.
 */
public class EndPanel extends JPanel
{
	private GameManagerView gameManager;
	private JLabel titleLabel;
	private JLabel messageLabel;
	private ArtPanel artPanel;

	/**
	 * Builds the end screen.
	 *
	 * @param gameManager the main window
	 */
	public EndPanel(GameManagerView gameManager)
	{
		this.gameManager = gameManager;
		setBackground(GuiStyle.BACKGROUND);
		setLayout(new BorderLayout());

		titleLabel = new JLabel("", SwingConstants.CENTER);
		titleLabel.setForeground(new Color(255, 220, 80));
		titleLabel.setFont(new Font("Monospaced", Font.BOLD, 52));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

		messageLabel = new JLabel("", SwingConstants.CENTER);
		messageLabel.setForeground(GuiStyle.TEXT);
		messageLabel.setFont(GuiStyle.BIG_FONT);

		artPanel = new ArtPanel();
		artPanel.setOpaque(false);

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
		bottom.setBorder(BorderFactory.createEmptyBorder(10, 0, 40, 0));
		bottom.add(homeButton);

		JPanel center = new JPanel(new BorderLayout());
		center.setOpaque(false);
		center.add(artPanel, BorderLayout.CENTER);
		center.add(messageLabel, BorderLayout.SOUTH);

		add(titleLabel, BorderLayout.NORTH);
		add(center, BorderLayout.CENTER);
		add(bottom, BorderLayout.SOUTH);
	}

	/**
	 * Sets the result text and art type. Called before this screen is shown.
	 *
	 * @param title big heading text
	 * @param message smaller description text
	 */
	public void showResult(String title, String message)
	{
		titleLabel.setText(title);
		messageLabel.setText(message);

		// pick art based on whether title starts with V (Victory) or not (defeat)
		artPanel.victory = title.startsWith("V");

		// gold color for victory, red for defeat
		titleLabel.setForeground(artPanel.victory
				? new Color(255, 215, 60)
				: new Color(210, 60, 60));

		artPanel.repaint();
	}

	// -------------------------------------------------------------------------
	// Inner class: draws pixel art for the result screen
	// -------------------------------------------------------------------------
	private class ArtPanel extends JPanel
	{
		boolean victory = true;

		@Override
		protected void paintComponent(Graphics g)
		{
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			int W = getWidth(), H = getHeight();

			if (victory) drawVictory(g2, W, H);
			else         drawDefeat(g2, W, H);

			g2.dispose();
		}

		// draws a gold crown with jewels and radiating stars
		private void drawVictory(Graphics2D g, int W, int H)
		{
			int cx = W / 2;
			int cy = H / 2;

			// radiating glow lines
			g.setColor(new Color(255, 215, 60, 50));
			g.setStroke(new BasicStroke(3));
			for (int i = 0; i < 12; i++)
			{
				double ang = i * Math.PI / 6;
				int x1 = cx + (int)(60  * Math.cos(ang));
				int y1 = cy + (int)(60  * Math.sin(ang));
				int x2 = cx + (int)(160 * Math.cos(ang));
				int y2 = cy + (int)(160 * Math.sin(ang));
				g.drawLine(x1, y1, x2, y2);
			}

			// crown body
			int cw = W * 40 / 100, ch = H * 40 / 100;
			int cx0 = cx - cw / 2, cy0 = cy - ch / 2;

			// band base
			g.setColor(new Color(220, 175, 50));
			g.fillRect(cx0, cy0 + ch * 55 / 100, cw, ch * 45 / 100);

			// three symmetric crown points
			int sideBaseY = cy0 + ch * 55 / 100;
			int sideTipY = cy0 + ch * 12 / 100;
			int[] pxL = {cx0, cx0 + cw / 5, cx0 + cw * 2 / 5};
			int[] pyL = {sideBaseY, sideTipY, sideBaseY};
			g.setColor(new Color(240, 195, 55));
			g.fillPolygon(pxL, pyL, 3);

			int[] pxC = {cx - cw / 5, cx, cx + cw / 5};
			int[] pyC = {sideBaseY, cy0 - ch * 18 / 100, sideBaseY};
			g.fillPolygon(pxC, pyC, 3);

			int[] pxR = {cx0 + cw * 3 / 5, cx0 + cw * 4 / 5, cx0 + cw};
			int[] pyR = {sideBaseY, sideTipY, sideBaseY};
			g.fillPolygon(pxR, pyR, 3);

			// inner band detail
			g.setColor(new Color(200, 150, 30));
			g.fillRect(cx0 + 4, cy0 + ch*58/100, cw - 8, ch*6/100);

			// outline
			g.setColor(new Color(180, 130, 20));
			g.setStroke(new BasicStroke(3));
			g.drawRect(cx0, cy0 + ch*55/100, cw, ch*45/100);

			// jewels on band
			g.setColor(new Color(220, 50, 50));
			g.fillOval(cx0 + cw/5, cy0 + ch*62/100, cw/8, ch/10);
			g.setColor(new Color(70, 150, 220));
			g.fillOval(cx - cw/16, cy0 + ch*62/100, cw/8, ch/10);
			g.setColor(new Color(90, 210, 90));
			g.fillOval(cx0 + cw*3/5, cy0 + ch*62/100, cw/8, ch/10);

			// star dots around crown
			drawStar(g, cx - cw * 65 / 100, cy - ch * 50 / 100, 18, new Color(255, 240, 100));
			drawStar(g, cx + cw * 55 / 100, cy - ch * 55 / 100, 14, new Color(255, 240, 100));
			drawStar(g, cx - cw * 55 / 100, cy + ch * 55 / 100, 12, new Color(255, 225, 80));
			drawStar(g, cx + cw * 50 / 100, cy + ch * 50 / 100, 16, new Color(255, 230, 90));
		}

		// draws an 8-pointed pixel star
		private void drawStar(Graphics2D g, int x, int y, int r, Color c)
		{
			g.setColor(c);
			int[] xs = new int[8], ys = new int[8];
			for (int i = 0; i < 8; i++)
			{
				double a = i * Math.PI / 4;
				int ri = (i % 2 == 0) ? r : r / 2;
				xs[i] = x + (int)(ri * Math.cos(a));
				ys[i] = y + (int)(ri * Math.sin(a));
			}
			g.fillPolygon(xs, ys, 8);
		}

		// draws a gravestone with RIP
		private void drawDefeat(Graphics2D g, int W, int H)
		{
			int cx = W / 2;
			int cy = H * 45 / 100;

			// eerie red background glow
			g.setColor(new Color(100, 15, 15, 60));
			g.fillOval(cx - W/3, cy - H/3, W * 2/3, H * 2/3);

			// gravestone body (rectangle + arch top)
			int sw = W * 25 / 100, sh = H * 50 / 100;
			int sx = cx - sw / 2, sy = cy - sh / 2;

			g.setColor(new Color(85, 82, 95));
			g.fillRect(sx, sy + sw/2, sw, sh - sw/2);
			g.fillArc(sx, sy, sw, sw, 0, 180);

			// stone detail
			g.setColor(new Color(105, 102, 118));
			g.fillRect(sx + sw/10, sy + sw/2 + sh/10, sw*8/10, sh/15);

			// RIP text carved in
			g.setColor(new Color(50, 48, 58));
			g.setFont(new Font("Monospaced", Font.BOLD, sw / 4));
			int tw = g.getFontMetrics().stringWidth("RIP");
			g.drawString("RIP", cx - tw / 2, sy + sw/2 + sh/3);

			// cracks
			g.setColor(new Color(40, 38, 50));
			g.setStroke(new BasicStroke(2));
			g.drawLine(sx + sw*3/5, sy + sw/3, sx + sw*3/5 + 8, sy + sw/2 + 20);

			// stone outline
			g.setColor(new Color(40, 38, 50));
			g.setStroke(new BasicStroke(3));
			g.drawRect(sx, sy + sw/2, sw, sh - sw/2);
			g.drawArc(sx, sy, sw, sw, 0, 180);

			// floating skull eyes (creepy effect)
			g.setColor(new Color(200, 30, 30, 180));
			g.fillOval(cx - W/4, cy - H/4, 8, 8);
			g.fillOval(cx + W/5, cy - H/5, 6, 6);
			g.fillOval(cx - W/6, cy + H/4, 5, 5);
		}
	}
}
