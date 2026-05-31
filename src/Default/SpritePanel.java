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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/**
 * Purpose: SpritePanel draws a chibi retro sprite for a given character or
 * item type using Graphics2D shapes. It scales to whatever size its given,
 * so it works on both the home screen (medium) and battle screen (large).
 *
 * SpritePanel is part of the View. It reads no game data directly.
 */
public class SpritePanel extends JPanel
{
	// package-accessible so DungeonPanel can swap the sprite on reward events
	String type;

	// shared skin and palette constants used across sprites
	private static final Color SKIN      = new Color(255, 200, 150);
	private static final Color SKIN_SHD  = new Color(220, 165, 110);
	private static final Color CHEEK     = new Color(255, 150, 130);
	private static final Color EYE_DARK  = new Color(30, 20, 80);
	private static final Color BLACK     = Color.BLACK;
	private static final Color WHITE     = Color.WHITE;

	/**
	 * Creates a sprite panel for the given type.
	 * Valid types: Warrior, Mage, Archer, Rat, Goblin, Skeleton, Dragon, potion
	 *
	 * @param type the character or item name
	 */
	public SpritePanel(String type)
	{
		this.type = type;
		setBackground(GuiStyle.BACKGROUND);
		setOpaque(true);
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
		if (W < 20 || H < 20)
		{
			g2.dispose();
			return;
		}

		if (type.equalsIgnoreCase("Warrior"))       drawWarrior(g2, W, H);
		else if (type.equalsIgnoreCase("Mage"))     drawMage(g2, W, H);
		else if (type.equalsIgnoreCase("Archer"))   drawArcher(g2, W, H);
		else if (type.equalsIgnoreCase("Rat"))      drawRat(g2, W, H);
		else if (type.equalsIgnoreCase("Goblin"))   drawGoblin(g2, W, H);
		else if (type.equalsIgnoreCase("Skeleton")) drawSkeleton(g2, W, H);
		else if (type.equalsIgnoreCase("Dragon"))   drawDragon(g2, W, H);
		else if (type.equalsIgnoreCase("potion"))   drawPotion(g2, W, H);

		g2.dispose();
	}

	// -------------------------------------------------------------------------
	// drawing helpers
	// -------------------------------------------------------------------------

	private float stk(int W, int H)
	{
		return Math.max(1.5f, Math.min(W, H) / 60f);
	}

	private void ov(Graphics2D g, Color c, int x, int y, int w, int h, float s)
	{
		g.setColor(c);
		g.fillOval(x, y, w, h);
		g.setColor(BLACK);
		g.setStroke(new BasicStroke(s, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.drawOval(x, y, w, h);
	}

	private void rc(Graphics2D g, Color c, int x, int y, int w, int h, float s)
	{
		g.setColor(c);
		g.fillRect(x, y, w, h);
		g.setColor(BLACK);
		g.setStroke(new BasicStroke(s));
		g.drawRect(x, y, w, h);
	}

	private void rr(Graphics2D g, Color c, int x, int y, int w, int h, int arc, float s)
	{
		g.setColor(c);
		g.fillRoundRect(x, y, w, h, arc, arc);
		g.setColor(BLACK);
		g.setStroke(new BasicStroke(s));
		g.drawRoundRect(x, y, w, h, arc, arc);
	}

	private void poly(Graphics2D g, Color c, int[] xs, int[] ys, float s)
	{
		g.setColor(c);
		g.fillPolygon(xs, ys, xs.length);
		g.setColor(BLACK);
		g.setStroke(new BasicStroke(s, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.drawPolygon(xs, ys, xs.length);
	}

	// eyes: two big chibi eyes centered at (cx, ey) with given radius
	private void eyes(Graphics2D g, int cx, int ey, int er, Color iris)
	{
		int gap = er * 2;
		int ex1 = cx - gap - er, ex2 = cx + gap - er;
		ov(g, WHITE, ex1, ey, er * 2, er * 2, 1.5f);
		ov(g, WHITE, ex2, ey, er * 2, er * 2, 1.5f);
		g.setColor(iris);
		g.fillOval(ex1 + er / 2, ey + er / 2, er, er);
		g.fillOval(ex2 + er / 2, ey + er / 2, er, er);
		g.setColor(WHITE);
		g.fillOval(ex1 + er / 2, ey + er / 3, er / 3, er / 3);
		g.fillOval(ex2 + er / 2, ey + er / 3, er / 3, er / 3);
	}

	// chibi smile arc
	private void smile(Graphics2D g, int cx, int y, int w, int h)
	{
		g.setColor(new Color(160, 80, 80));
		g.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.drawArc(cx - w / 2, y, w, h, 0, -180);
	}

	// -------------------------------------------------------------------------
	// WARRIOR: red chibi knight with big helmet, sword, and shield
	// -------------------------------------------------------------------------
	private void drawWarrior(Graphics2D g, int W, int H)
	{
		float s = stk(W, H);
		int cx = W / 2;

		Color RED      = new Color(190, 45, 45);
		Color DARK_RED = new Color(110, 20, 20);
		Color GOLD     = new Color(215, 175, 50);
		Color SILVER   = new Color(200, 210, 225);

		// legs
		int lw = W / 8, lh = H / 6, ly = H * 70 / 100;
		rc(g, DARK_RED, cx - lw - W/20, ly, lw, lh, s);
		rc(g, DARK_RED, cx + W/20,      ly, lw, lh, s);
		// boots
		rc(g, new Color(50, 30, 10), cx - lw - W/14, ly + lh - H/30, lw + W/14, H/25, s);
		rc(g, new Color(50, 30, 10), cx + W/20 - W/28, ly + lh - H/30, lw + W/14, H/25, s);

		// body
		int bw = W * 54 / 100, bh = H * 27 / 100, by = H * 44 / 100;
		rr(g, RED, cx - bw/2, by, bw, bh, 10, s);
		rc(g, DARK_RED, cx - bw/6, by + bh/4, bw/3, bh/2, s);
		g.setColor(GOLD); g.setStroke(new BasicStroke(2));
		g.drawLine(cx - bw/2 + 4, by, cx + bw/2 - 4, by);
		g.drawLine(cx - bw/2 + 4, by + bh, cx + bw/2 - 4, by + bh);
		// shoulder pads
		int sr = W / 9;
		ov(g, new Color(210, 65, 65), cx - bw/2 - sr/2, by - sr/4, sr*2, sr*2, s);
		ov(g, new Color(210, 65, 65), cx + bw/2 - sr*3/2, by - sr/4, sr*2, sr*2, s);

		// shield (left)
		int[] sxs = {cx - bw/2 - W/10, cx - bw/2 - W/6, cx - bw/2 - W/6, cx - bw/2 - W/10};
		int[] sys = {by + 5, by + bh/3, by + bh*2/3, by + bh - 5};
		poly(g, new Color(150, 100, 30), sxs, sys, s);
		ov(g, GOLD, cx - bw/2 - W*14/100, by + bh/3, W/14, H/18, s);

		// sword (right): blade, guard, hilt
		rc(g, SILVER, cx + bw/2 + W/25, by - H/6, W/14, bh + H/5, s);
		rc(g, GOLD,   cx + bw/2 - W/18, by - H/35, W/5, H/28, s);
		rc(g, new Color(80, 50, 15), cx + bw/2 + W/25 + W/30, by - H/35, W/14, bh/3, s);

		// big chibi head with full helmet
		int hw = W * 46 / 100, hh = H * 36 / 100, hy = H * 7 / 100;
		rr(g, RED, cx - hw/2, hy, hw, hh, hw/3, s);
		// crest on top
		rc(g, DARK_RED, cx - W/16, hy - H/18, W/8, H/18, s);
		// face opening
		int fw = hw * 58 / 100, fh = hh * 48 / 100, fx = cx - fw/2, fy = hy + hh*28/100;
		ov(g, SKIN, fx, fy, fw, fh, s);
		// visor slit
		rc(g, DARK_RED, cx - fw/3, hy + hh/5, fw*2/3, H/38, s);

		// eyes and face
		eyes(g, cx, fy + fh/5, fw/10, EYE_DARK);
		// cheeks
		g.setColor(new Color(CHEEK.getRed(), CHEEK.getGreen(), CHEEK.getBlue(), 150));
		g.fillOval(fx + fw/10, fy + fh/2, fw/4, fh/8);
		g.fillOval(fx + fw*6/10, fy + fh/2, fw/4, fh/8);
		smile(g, cx, fy + fh*58/100, fw/3, fh/5);
	}

	// -------------------------------------------------------------------------
	// MAGE: purple chibi wizard with pointed hat and glowing staff
	// -------------------------------------------------------------------------
	private void drawMage(Graphics2D g, int W, int H)
	{
		float s = stk(W, H);
		int cx = W / 2;

		Color PURPLE  = new Color(100, 55, 175);
		Color DPURPLE = new Color(55, 25, 110);
		Color CYAN    = new Color(80, 200, 220);
		Color STARCLR = new Color(255, 240, 100);

		// robe (wide trapezoid shape)
		int rx1 = cx - W*30/100, rx2 = cx + W*30/100;
		int ry = H * 43 / 100, rbot = H * 88 / 100;
		int rbw = W * 55 / 100;
		int[] rXs = {cx - rbw/2, cx + rbw/2, rx2, rx1};
		int[] rYs = {rbot, rbot, ry, ry};
		poly(g, PURPLE, rXs, rYs, s);
		// robe hem detail
		g.setColor(new Color(130, 80, 210));
		g.setStroke(new BasicStroke(2));
		g.drawLine(cx - rbw/2 + 4, rbot, cx + rbw/2 - 4, rbot);

		// feet poking out
		ov(g, new Color(40, 20, 70), cx - rbw/3, rbot - H/25, W/9, H/20, s);
		ov(g, new Color(40, 20, 70), cx + rbw/6, rbot - H/25, W/9, H/20, s);

		// upper body (inner robe)
		rr(g, DPURPLE, cx - W*18/100, ry, W*36/100, H * 26/100, 8, s);
		// star on chest
		g.setColor(STARCLR);
		g.setStroke(new BasicStroke(2));
		int ss = W/14;
		int scx = cx, scy = ry + H*13/100;
		g.drawLine(scx, scy - ss, scx, scy + ss);
		g.drawLine(scx - ss, scy, scx + ss, scy);
		g.drawLine(scx - ss*7/10, scy - ss*7/10, scx + ss*7/10, scy + ss*7/10);
		g.drawLine(scx + ss*7/10, scy - ss*7/10, scx - ss*7/10, scy + ss*7/10);

		// staff (left side, goes up high)
		rc(g, new Color(80, 55, 25), cx - W*38/100, H*5/100, W/14, H*60/100, s);
		ov(g, CYAN, cx - W*42/100, H*2/100, W/7, W/7, s);
		g.setColor(new Color(200, 255, 255, 120));
		g.fillOval(cx - W*40/100, H*3/100, W/10, W/10);

		// big chibi head
		int hw = W * 44 / 100, hh = H * 34 / 100, hy = H * 7 / 100;
		ov(g, SKIN, cx - hw/2, hy, hw, hh, s);

		// pointed hat
		int hbW = hw + W/8, hbX = cx - hbW/2, hbY = hy + hh*3/10;
		int[] hatX = {cx, hbX, hbX + hbW};
		int[] hatY = {hy - H*22/100, hbY, hbY};
		poly(g, DPURPLE, hatX, hatY, s);
		// hat brim
		rr(g, PURPLE, hbX, hbY - H/35, hbW, H/18, 5, s);
		// stars on hat
		g.setColor(STARCLR);
		g.fillOval(cx - W/9, hy - H*12/100, W/16, H/30);
		g.fillOval(cx + W/16, hy - H*18/100, W/20, H/38);

		// eyes (big sparkly mage eyes)
		int er = hw / 10;
		eyes(g, cx, hy + hh*30/100, er, new Color(80, 20, 150));
		// cheeks
		g.setColor(new Color(CHEEK.getRed(), CHEEK.getGreen(), CHEEK.getBlue(), 140));
		g.fillOval(cx - hw*38/100, hy + hh*56/100, hw/4, hh/10);
		g.fillOval(cx + hw*12/100, hy + hh*56/100, hw/4, hh/10);
		smile(g, cx, hy + hh*64/100, hw/3, hh/6);
	}

	// -------------------------------------------------------------------------
	// ARCHER: green chibi hooded ranger with bow
	// -------------------------------------------------------------------------
	private void drawArcher(Graphics2D g, int W, int H)
	{
		float s = stk(W, H);
		int cx = W / 2;

		Color GREEN   = new Color(55, 130, 55);
		Color DGREEN  = new Color(30, 80, 30);
		Color BROWN   = new Color(110, 68, 28);
		Color TAN     = new Color(195, 155, 90);

		// legs
		int lw = W/9, lh = H/5, ly = H*70/100;
		rc(g, BROWN, cx - lw - W/20, ly, lw, lh, s);
		rc(g, BROWN, cx + W/20,      ly, lw, lh, s);
		rc(g, new Color(60, 35, 10), cx - lw - W/14, ly + lh - H/30, lw + W/12, H/25, s);
		rc(g, new Color(60, 35, 10), cx + W/22 - W/28, ly + lh - H/30, lw + W/12, H/25, s);

		// body (slim tunic)
		int bw = W*42/100, bh = H*26/100, by = H*45/100;
		rr(g, GREEN, cx - bw/2, by, bw, bh, 8, s);
		// belt
		rc(g, BROWN, cx - bw/2, by + bh*7/10, bw, H/28, s);
		ov(g, TAN, cx - W/20, by + bh*68/100, W/10, H/25, s);

		// cloak behind (drawn before head so it goes behind)
		ov(g, DGREEN, cx - W*26/100, H*5/100, W*52/100, H*44/100, s);

		// quiver on back (right side)
		rc(g, BROWN, cx + bw/2 + W/30, by + H/20, W/10, bh*3/4, s);
		g.setColor(TAN);
		g.setStroke(new BasicStroke(2));
		g.drawLine(cx + bw/2 + W/30 + W/20, by + H/20 - H/20,
				   cx + bw/2 + W/30 + W/20, by + H/25);

		// bow (left side) - a curved arc
		g.setColor(BROWN);
		g.setStroke(new BasicStroke(s + 1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		int bx = cx - bw/2 - W*15/100;
		g.drawArc(bx, by - H/12, W/7, bh + H/6, 60, 240);
		// bowstring
		g.setColor(TAN);
		g.setStroke(new BasicStroke(1.5f));
		g.drawLine(bx + W/18, by - H/18, bx + W/18, by + bh + H/10);

		// head (medium sized)
		int hw = W*40/100, hh = H*33/100, hy = H*10/100;
		ov(g, SKIN, cx - hw/2, hy, hw, hh, s);
		// hood rim
		g.setColor(DGREEN);
		g.setStroke(new BasicStroke(s + 1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.drawArc(cx - hw/2 - 5, hy - 5, hw + 10, hh/2 + 10, 0, 180);

		// eyes (focused, slightly narrowed)
		int er = hw/10;
		eyes(g, cx, hy + hh*28/100, er, new Color(40, 80, 20));
		// determined eyebrow lines
		g.setColor(BROWN);
		g.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.drawLine(cx - er*3 - 2, hy + hh*24/100, cx - er - 2, hy + hh*22/100);
		g.drawLine(cx + er + 2, hy + hh*22/100, cx + er*3 + 2, hy + hh*24/100);
		// cheeks
		g.setColor(new Color(CHEEK.getRed(), CHEEK.getGreen(), CHEEK.getBlue(), 120));
		g.fillOval(cx - hw*35/100, hy + hh*52/100, hw/4, hh/9);
		g.fillOval(cx + hw*10/100, hy + hh*52/100, hw/4, hh/9);
		smile(g, cx, hy + hh*60/100, hw/4, hh/7);
	}

	// -------------------------------------------------------------------------
	// RAT: small chibi gray rat with big round ears and S-curve tail
	// -------------------------------------------------------------------------
	private void drawRat(Graphics2D g, int W, int H)
	{
		float s = stk(W, H);
		int cx = W / 2 + W/10;

		Color GRAY  = new Color(140, 135, 145);
		Color DGRAY = new Color(90, 85, 95);
		Color PINK  = new Color(255, 175, 185);
		Color RED   = new Color(220, 50, 50);

		// S-curve tail
		g.setColor(DGRAY);
		g.setStroke(new BasicStroke(s + 1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.drawArc(cx - W*40/100, H*40/100, W*25/100, H*30/100, 270, 180);
		g.drawArc(cx - W*52/100, H*55/100, W*20/100, H*25/100, 90, -180);

		// main egg-shaped body
		int bw = W*55/100, bh = H*42/100, by = H*42/100;
		ov(g, GRAY, cx - bw/2, by, bw, bh, s);
		// body shading
		g.setColor(DGRAY);
		g.fillOval(cx - bw/4, by + bh*55/100, bw/3, bh/5);

		// head (round, slightly smaller)
		int hw = W*40/100, hh = H*35/100, hy = H*22/100;
		ov(g, GRAY, cx - hw/2, hy, hw, hh, s);

		// big round ears
		int er = W/7;
		ov(g, GRAY, cx - hw/2 - er*2/3, hy - er*3/4, er*2, er*2, s);
		ov(g, PINK, cx - hw/2 - er*2/3 + er/4, hy - er*3/4 + er/4, er*3/2, er*3/2, s);
		ov(g, GRAY, cx + hw/2 - er*4/3, hy - er*3/4, er*2, er*2, s);
		ov(g, PINK, cx + hw/2 - er*4/3 + er/4, hy - er*3/4 + er/4, er*3/2, er*3/2, s);

		// snout (pointed)
		int[] snX = {cx - hw/4, cx + hw/4, cx};
		int[] snY = {hy + hh*65/100, hy + hh*65/100, hy + hh + H/20};
		poly(g, SKIN_SHD, snX, snY, s);
		// nose tip (pink)
		ov(g, PINK, cx - W/20, hy + hh + H/25, W/10, H/20, s);

		// eyes (red beady)
		int esz = hw/10;
		ov(g, RED, cx - hw/4, hy + hh*28/100, esz*2, esz*2, s);
		ov(g, RED, cx + hw/8, hy + hh*28/100, esz*2, esz*2, s);
		g.setColor(new Color(255, 100, 100));
		g.fillOval(cx - hw/4 + 2, hy + hh*28/100 + 2, esz, esz);
		g.fillOval(cx + hw/8 + 2, hy + hh*28/100 + 2, esz, esz);

		// whiskers
		g.setColor(new Color(200, 195, 205));
		g.setStroke(new BasicStroke(1f));
		for (int i = 0; i < 3; i++)
		{
			int wy = hy + hh*68/100 - i * H/30;
			g.drawLine(cx - W/6, wy, cx - hw/3, wy - H/50);
			g.drawLine(cx + W/6, wy, cx + hw/3, wy - H/50);
		}
	}

	// -------------------------------------------------------------------------
	// GOBLIN: toxic green chibi goblin with huge ears and toothy grin
	// -------------------------------------------------------------------------
	private void drawGoblin(Graphics2D g, int W, int H)
	{
		float s = stk(W, H);
		int cx = W / 2;

		Color GREEN  = new Color(80, 175, 65);
		Color DGREEN = new Color(45, 115, 35);
		Color YELLOW = new Color(230, 200, 50);
		Color BROWN  = new Color(105, 65, 25);

		// legs (short and stubby)
		int lw = W/8, lh = H/7, ly = H*75/100;
		rc(g, DGREEN, cx - lw - W/18, ly, lw, lh, s);
		rc(g, DGREEN, cx + W/18,      ly, lw, lh, s);
		rc(g, new Color(50, 30, 10), cx - lw - W/16, ly + lh - H/30, lw + W/12, H/25, s);
		rc(g, new Color(50, 30, 10), cx + W/20 - W/26, ly + lh - H/30, lw + W/12, H/25, s);

		// hunched barrel body
		int bw = W*50/100, bh = H*28/100, by = H*47/100;
		rr(g, DGREEN, cx - bw/2, by, bw, bh, 12, s);
		// tattered vest detail
		g.setColor(new Color(60, 40, 15));
		g.setStroke(new BasicStroke(2));
		g.drawLine(cx - bw/4, by + H/20, cx - bw/4, by + bh - H/20);
		g.drawLine(cx + bw/4, by + H/20, cx + bw/4, by + bh - H/20);

		// club in hand (right side)
		rc(g, BROWN, cx + bw/2 + W/20, by + bh/4, W/10, bh*3/4, s);
		ov(g, new Color(130, 85, 35), cx + bw/2 + W/25, by + bh/6, W*3/20, H/8, s);
		// studs on club
		g.setColor(new Color(160, 140, 50));
		g.fillOval(cx + bw/2 + W/22, by + bh*20/100, W/18, H/25);
		g.fillOval(cx + bw/2 + W/20, by + bh*10/100, W/18, H/25);

		// shoulders (hunched up high)
		ov(g, GREEN, cx - bw/2 - W/14, by - H/25, W*3/14, H/8, s);
		ov(g, GREEN, cx + bw/2 - W/7,  by - H/25, W*3/14, H/8, s);

		// big flat head
		int hw = W*50/100, hh = H*32/100, hy = H*15/100;
		ov(g, GREEN, cx - hw/2, hy, hw, hh, s);

		// HUGE pointed ears
		int[] leX = {cx - hw/2 + W/14, cx - hw/2 - W/4, cx - hw/2 - W*2/20};
		int[] leY = {hy + hh*3/10, hy + hh*2/10, hy + hh*7/10};
		poly(g, GREEN, leX, leY, s);
		int[] reX = {cx + hw/2 - W/14, cx + hw/2 + W/4, cx + hw/2 + W*2/20};
		int[] reY = {hy + hh*3/10, hy + hh*2/10, hy + hh*7/10};
		poly(g, GREEN, reX, reY, s);

		// eyes (beady yellow)
		int er = hw/12;
		ov(g, YELLOW, cx - hw/3, hy + hh*28/100, er*2, er*2, s);
		ov(g, YELLOW, cx + hw/6, hy + hh*28/100, er*2, er*2, s);
		g.setColor(new Color(50, 25, 0));
		g.fillOval(cx - hw/3 + er/2, hy + hh*28/100 + er/2, er, er);
		g.fillOval(cx + hw/6 + er/2, hy + hh*28/100 + er/2, er, er);

		// big warty nose
		ov(g, DGREEN, cx - W/20, hy + hh*46/100, W/10, H/18, s);

		// wide toothy grin
		int mw = hw*6/10, mx = cx - mw/2, my = hy + hh*62/100, mh = H/12;
		rc(g, new Color(20, 10, 5), mx, my, mw, mh, s);
		// teeth
		g.setColor(new Color(240, 235, 215));
		int tooth = mw / 5;
		for (int i = 0; i < 4; i++)
		{
			g.fillRect(mx + 2 + i * tooth + i, my + 2, tooth - 2, mh - 4);
		}
	}

	// -------------------------------------------------------------------------
	// SKELETON: bony chibi skeleton with exposed ribcage and rusty sword
	// -------------------------------------------------------------------------
	private void drawSkeleton(Graphics2D g, int W, int H)
	{
		float s = stk(W, H);
		int cx = W / 2;

		Color BONE  = new Color(230, 225, 200);
		Color BSHD  = new Color(185, 178, 155);
		Color RUST  = new Color(165, 100, 45);

		// thin leg bones
		int lw = W/12, lh = H/5, ly = H*70/100;
		rc(g, BONE, cx - lw - W/14, ly, lw, lh, s);
		rc(g, BONE, cx + W/14, ly, lw, lh, s);
		// feet bones
		rc(g, BSHD, cx - lw - W/10, ly + lh - H/30, W/6, H/30, s);
		rc(g, BSHD, cx + W/14 - W/30, ly + lh - H/30, W/6, H/30, s);

		// pelvis oval
		ov(g, BSHD, cx - W*18/100, H*62/100, W*36/100, H/10, s);

		// ribcage (oval chest with rib arcs)
		int chw = W*38/100, chh = H*24/100, chy = H*38/100;
		ov(g, BONE, cx - chw/2, chy, chw, chh, s);
		g.setColor(BSHD);
		g.setStroke(new BasicStroke(s, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		// spine line down center
		g.drawLine(cx, chy + H/30, cx, chy + chh - H/30);
		// rib arcs
		for (int i = 0; i < 4; i++)
		{
			int ry = chy + chh/5 + i * chh/5;
			g.drawArc(cx - chw/2 + chw/10, ry, chw/3, chh/6, 90, 180);
			g.drawArc(cx + chw/6, ry, chw/3, chh/6, 270, 180);
		}

		// arm bones (thin lines with joint circles)
		g.setColor(BONE);
		g.setStroke(new BasicStroke(s + 1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		// left arm
		g.drawLine(cx - chw/2, chy + H/20, cx - chw/2 - W/12, chy + chh/2);
		g.drawLine(cx - chw/2 - W/12, chy + chh/2, cx - chw/2 - W/10, chy + chh);
		ov(g, BSHD, cx - chw/2 - W/10, chy + chh/2 - H/40, W/12, H/20, s);
		// right arm holding sword
		g.drawLine(cx + chw/2, chy + H/20, cx + chw/2 + W/12, chy + chh/2);
		ov(g, BSHD, cx + chw/2 + W/16, chy + chh/2 - H/40, W/12, H/20, s);

		// rusty sword
		rc(g, RUST, cx + chw/2 + W/10, H*22/100, W/12, H*42/100, s);
		rc(g, new Color(140, 110, 60), cx + chw/2 + W/25, H*40/100, W/5, H/28, s);

		// skull
		int skw = W*42/100, skh = H*28/100, sky = H*9/100;
		ov(g, BONE, cx - skw/2, sky, skw, skh, s);
		// jaw (lower extension)
		rr(g, BSHD, cx - skw*4/10, sky + skh*7/10, skw*8/10, skh*3/10, 6, s);
		// eye sockets (dark square)
		rc(g, new Color(15, 12, 8), cx - skw*32/100, sky + skh*25/100, skw*22/100, skh*22/100, s);
		rc(g, new Color(15, 12, 8), cx + skw*10/100, sky + skh*25/100, skw*22/100, skh*22/100, s);
		// nasal cavity
		rc(g, new Color(15, 12, 8), cx - skw/16, sky + skh*52/100, skw/8, skh*16/100, s);
		// teeth
		g.setColor(BONE);
		int tw = skw / 6, ty = sky + skh*82/100;
		for (int i = 0; i < 4; i++)
		{
			g.fillRect(cx - skw*3/10 + i * (tw + 2), ty, tw, skh/8);
		}
		g.setColor(BLACK); g.setStroke(new BasicStroke(s));
		g.drawRect(cx - skw*3/10, ty, skw*6/10, skh/8);
	}

	// -------------------------------------------------------------------------
	// DRAGON: large imposing chibi dragon with wings and fire breath
	// -------------------------------------------------------------------------
	private void drawDragon(Graphics2D g, int W, int H)
	{
		float s = stk(W, H);
		int cx = W * 55 / 100;

		Color DRED   = new Color(160, 25, 25);
		Color BRED   = new Color(210, 55, 55);
		Color ORANGE = new Color(240, 130, 30);
		Color FIRE   = new Color(255, 220, 50);
		Color GOLD   = new Color(230, 185, 40);

		// wings (behind body -- drawn first)
		// left wing
		int[] lwX = {cx - W*30/100, cx - W*60/100, cx - W*20/100, cx - W*5/100};
		int[] lwY = {H*28/100, H*5/100, H*12/100, H*38/100};
		poly(g, DRED, lwX, lwY, s);
		// right wing
		int[] rwX = {cx + W*22/100, cx + W*60/100, cx + W*35/100, cx + W*15/100};
		int[] rwY = {H*25/100, H*3/100, H*10/100, H*35/100};
		poly(g, DRED, rwX, rwY, s);
		// wing membrane lines
		g.setColor(new Color(BRED.getRed(), BRED.getGreen(), BRED.getBlue(), 180));
		g.setStroke(new BasicStroke(s));
		g.drawLine(cx - W*30/100, H*28/100, cx - W*55/100, H*8/100);
		g.drawLine(cx - W*30/100, H*28/100, cx - W*48/100, H*15/100);
		g.drawLine(cx + W*22/100, H*25/100, cx + W*55/100, H*6/100);
		g.drawLine(cx + W*22/100, H*25/100, cx + W*45/100, H*13/100);

		// tail (curves to lower right)
		g.setColor(DRED);
		g.setStroke(new BasicStroke(s * 3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.drawArc(cx, H*60/100, W*30/100, H*25/100, 180, 90);
		// tail spike
		int[] tpX = {cx + W*30/100, cx + W*38/100, cx + W*22/100};
		int[] tpY = {H*72/100, H*88/100, H*88/100};
		poly(g, BRED, tpX, tpY, s);

		// main body (big oval)
		int bw = W*52/100, bh = H*40/100, by = H*38/100;
		ov(g, DRED, cx - bw/2, by, bw, bh, s);
		// belly scales (lighter center oval)
		ov(g, BRED, cx - bw*3/10, by + bh/4, bw*6/10, bh*5/8, s);
		// scale lines on body
		g.setColor(new Color(130, 15, 15));
		g.setStroke(new BasicStroke(1.5f));
		for (int i = 0; i < 3; i++)
		{
			g.drawArc(cx - bw*4/10 + i * bw/6, by + bh/5 + i * H/16,
					  bw/4, H/12, 0, 180);
		}

		// legs
		int lw = W/10, lh = H/8;
		ov(g, DRED, cx - bw/2 + W/20, by + bh - H/20, lw*2, lh, s);
		ov(g, DRED, cx + bw/2 - lw*3, by + bh - H/20, lw*2, lh, s);
		// claws
		g.setColor(new Color(200, 195, 185));
		g.setStroke(new BasicStroke(s, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		for (int i = 0; i < 3; i++)
		{
			g.drawLine(cx - bw/2 + W/20 + i*lw/2, by + bh + lh - H/30,
					   cx - bw/2 + W/30 + i*lw/2, by + bh + lh + H/30);
			g.drawLine(cx + bw/2 - lw*3 + W/20 + i*lw/2, by + bh + lh - H/30,
					   cx + bw/2 - lw*3 + W/30 + i*lw/2, by + bh + lh + H/30);
		}

		// neck going to upper-left
		int neckX = cx - bw/2;
		int neckY = by + bh/4;
		g.setColor(DRED);
		g.setStroke(new BasicStroke(W/6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.drawLine(neckX, neckY, cx - W*45/100, H*20/100);

		// head (pointed chibi dragon head)
		int hx = cx - W*58/100, hy = H*8/100, hw = W*40/100, hh = H*22/100;
		rr(g, DRED, hx, hy, hw, hh, hh/3, s);
		// horns
		int[] lhX = {hx + hw/4, hx + hw/5, hx + hw/2};
		int[] lhY = {hy, hy - H/10, hy - H/12};
		poly(g, BRED, lhX, lhY, s);
		int[] rhX = {hx + hw/2, hx + hw*2/3, hx + hw*3/4};
		int[] rhY = {hy, hy - H/12, hy};
		poly(g, BRED, rhX, rhY, s);
		// snout
		rr(g, BRED, hx, hy + hh/2, hw*6/10, hh/2, 8, s);
		// mouth (open, showing teeth)
		int[] mxL = {hx, hx - W*12/100, hx + W/20};
		int[] myL = {hy + hh*7/10, hy + hh*9/10, hy + hh};
		poly(g, DRED, mxL, myL, s);
		g.setColor(new Color(230, 220, 200));
		g.fillOval(hx - W/20, hy + hh*7/10, W/14, H/25);

		// fire breath (orange and yellow triangles)
		for (int i = 0; i < 3; i++)
		{
			int[] fxs = {hx - W/10, hx - W*(22 + i*10)/100, hx - W*(16 + i*8)/100};
			int[] fys = {hy + hh*80/100 - i*H/30, hy + hh + H/15 + i*H/20, hy + hh + H/25 + i*H/25};
			Color fc = i == 0 ? ORANGE : (i == 1 ? ORANGE : FIRE);
			poly(g, fc, fxs, fys, 1f);
		}

		// gold eyes
		eyes(g, hx + hw*55/100, hy + hh*16/100, hh/8, new Color(150, 100, 0));
		// make eyes gold instead
		g.setColor(GOLD);
		int eyeR = hh/8;
		g.fillOval(hx + hw*55/100 - eyeR*3, hy + hh*16/100, eyeR*2, eyeR*2);
		g.fillOval(hx + hw*55/100, hy + hh*16/100, eyeR*2, eyeR*2);
		g.setColor(new Color(80, 10, 10));
		g.fillOval(hx + hw*55/100 - eyeR*3 + eyeR/2, hy + hh*16/100 + eyeR/2, eyeR, eyeR);
		g.fillOval(hx + hw*55/100 + eyeR/2, hy + hh*16/100 + eyeR/2, eyeR, eyeR);
		g.setColor(Color.WHITE);
		g.fillOval(hx + hw*55/100 - eyeR*3 + 2, hy + hh*16/100 + 2, eyeR/2, eyeR/2);
		g.fillOval(hx + hw*55/100 + 2, hy + hh*16/100 + 2, eyeR/2, eyeR/2);
	}

	// -------------------------------------------------------------------------
	// HEALTH POTION: chibi ruby red potion bottle with glowing effect
	// -------------------------------------------------------------------------
	private void drawPotion(Graphics2D g, int W, int H)
	{
		float s = stk(W, H);
		int cx = W / 2;

		Color GLASS    = new Color(200, 240, 255, 180);
		Color LIQUID   = new Color(200, 30, 40);
		Color LIQUID_B = new Color(255, 80, 90);
		Color CORK     = new Color(150, 105, 45);

		// glow behind bottle
		g.setColor(new Color(200, 30, 40, 60));
		g.fillOval(cx - W*38/100, H*8/100, W*76/100, H*84/100);
		g.setColor(new Color(200, 30, 40, 30));
		g.fillOval(cx - W*46/100, H*4/100, W*92/100, H*92/100);

		// bottle bottom (round)
		int bw = W*56/100, bh = H*55/100, by = H*40/100;
		// liquid fill (slightly inside bottle bounds)
		ov(g, LIQUID, cx - bw/2 + 3, by + 3, bw - 6, bh - 6, 0f);
		// glass overlay
		ov(g, GLASS, cx - bw/2, by, bw, bh, s);

		// neck
		int nw = bw*35/100, nx = cx - nw/2;
		int nh = H*20/100, ny = by - nh + H/25;
		// neck liquid
		rc(g, LIQUID, nx + 3, ny + 3, nw - 6, nh - 3, 0f);
		// neck glass
		rc(g, GLASS, nx, ny, nw, nh, s);

		// cork
		rr(g, CORK, nx - W/20, ny - H/12, nw + W/10, H/10, 5, s);
		// cork lines
		g.setColor(new Color(110, 75, 25));
		g.setStroke(new BasicStroke(1.5f));
		g.drawLine(nx, ny - H/18, nx + nw + W/10, ny - H/18);

		// bubble inside liquid
		g.setColor(new Color(255, 150, 160, 180));
		g.fillOval(cx - bw/6, by + bh/3, bw/8, bh/10);
		g.fillOval(cx + bw/8, by + bh/2, bw/10, bh/12);

		// glass shine (white curved highlight)
		g.setColor(new Color(255, 255, 255, 200));
		g.setStroke(new BasicStroke(s + 1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.drawArc(cx - bw*36/100, by + bh/6, bw/4, bh/3, 30, 120);
		// neck shine
		g.drawArc(nx + nw/8, ny + nh/5, nw/3, nh/2, 30, 120);

		// cross symbol on bottle (health potion indicator)
		g.setColor(new Color(255, 255, 255, 200));
		g.setStroke(new BasicStroke(s + 1));
		int cross = bw / 5;
		g.drawLine(cx, by + bh*35/100, cx, by + bh*65/100);
		g.drawLine(cx - cross/2, by + bh/2, cx + cross/2, by + bh/2);
	}
}
