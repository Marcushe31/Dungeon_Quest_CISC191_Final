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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * Purpose: SpritePanel draws a retro pixel-art sprite onto a small 40x56
 * BufferedImage using flat colored blocks, gives each part light/shadow shading
 * for depth, then adds a black outline around the whole shape so it pops. The
 * finished image is scaled up with nearest-neighbor so the pixels stay sharp.
 * The sprite is always centered and capped in size so it stays small even in
 * a big battle cell.
 *
 * SpritePanel is part of the View. It reads no game data.
 */
public class SpritePanel extends JPanel
{
	// package access so DungeonPanel and BattlePanel can swap the type
	String type;

	// the little canvas all sprites are drawn on
	private static final int SW = 40;
	private static final int SH = 56;

	// cap the scale so sprites never fill a whole battle cell
	private static final int MAX_SCALE = 7;

	// shared skin tones (base, shadow, highlight)
	private static final Color SKIN   = new Color(255, 206, 160);
	private static final Color SKIN_S = new Color(225, 165, 120);
	private static final Color SKIN_H = new Color(255, 226, 190);
	private static final Color CHEEK  = new Color(255, 150, 140);
	private static final Color WHT    = Color.WHITE;
	private static final Color OUT     = Color.BLACK;

	/**
	 * Creates a sprite panel for the given character or item type.
	 *
	 * @param type Warrior, Mage, Archer, Rat, Goblin, Skeleton, Dragon, or potion
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
		int W = getWidth(), H = getHeight();
		if (W < 10 || H < 10) return;

		// draw on a tiny transparent canvas with no anti-aliasing
		BufferedImage img = new BufferedImage(SW, SH, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = img.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);

		drawSprite(g2);
		g2.dispose();

		// add a black outline around the whole silhouette so it stands out
		addOutline(img);

		// scale up with nearest-neighbor to keep the blocky pixel look
		int scale = Math.max(1, Math.min(MAX_SCALE, Math.min(W / SW, H / SH)));
		int dw = SW * scale, dh = SH * scale;
		int dx = (W - dw) / 2, dy = (H - dh) / 2;

		Graphics2D out = (Graphics2D) g.create();
		out.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		out.drawImage(img, dx, dy, dw, dh, null);
		out.dispose();
	}

	private void drawSprite(Graphics2D g)
	{
		if      (type.equalsIgnoreCase("Warrior"))  drawWarrior(g);
		else if (type.equalsIgnoreCase("Mage"))     drawMage(g);
		else if (type.equalsIgnoreCase("Archer"))   drawArcher(g);
		else if (type.equalsIgnoreCase("Rat"))      drawRat(g);
		else if (type.equalsIgnoreCase("Goblin"))   drawGoblin(g);
		else if (type.equalsIgnoreCase("Skeleton")) drawSkeleton(g);
		else if (type.equalsIgnoreCase("Dragon"))   drawDragon(g);
		else if (type.equalsIgnoreCase("potion"))   drawPotion(g);
	}

	// -------------------------------------------------------------------------
	// drawing helpers
	// -------------------------------------------------------------------------

	// flat block of one color
	private void r(Graphics2D g, Color c, int x, int y, int w, int h)
	{
		g.setColor(c);
		g.fillRect(x, y, w, h);
	}

	// shaded block: base fill, light strip on top+left, dark strip on bottom+right
	private void shade(Graphics2D g, Color base, Color hi, Color sh, int x, int y, int w, int h)
	{
		g.setColor(base); g.fillRect(x, y, w, h);
		g.setColor(hi);   g.fillRect(x, y, w, 1);          // top highlight
		g.setColor(hi);   g.fillRect(x, y, 1, h);          // left highlight
		g.setColor(sh);   g.fillRect(x, y + h - 1, w, 1);  // bottom shadow
		g.setColor(sh);   g.fillRect(x + w - 1, y, 1, h);  // right shadow
	}

	// two big chibi eyes with white, pupil, and a shine dot
	private void eyes(Graphics2D g, int lx, int rx, int y, Color iris)
	{
		r(g, WHT, lx, y, 3, 4);
		r(g, WHT, rx, y, 3, 4);
		r(g, iris, lx + 1, y + 1, 2, 2);
		r(g, iris, rx + 1, y + 1, 2, 2);
		r(g, WHT, lx + 1, y + 1, 1, 1);   // shine
		r(g, WHT, rx + 1, y + 1, 1, 1);
	}

	// scans the image and turns transparent pixels next to filled ones black
	private void addOutline(BufferedImage img)
	{
		int w = img.getWidth(), h = img.getHeight();
		int[] src = img.getRGB(0, 0, w, h, null, 0, w);
		int black = 0xFF000000;

		for (int y = 0; y < h; y++)
		{
			for (int x = 0; x < w; x++)
			{
				int i = y * w + x;
				// only outline empty pixels
				if ((src[i] >>> 24) != 0) continue;

				boolean touching = false;
				if (x > 0     && (src[i - 1] >>> 24) != 0) touching = true;
				if (x < w - 1 && (src[i + 1] >>> 24) != 0) touching = true;
				if (y > 0     && (src[i - w] >>> 24) != 0) touching = true;
				if (y < h - 1 && (src[i + w] >>> 24) != 0) touching = true;

				if (touching) img.setRGB(x, y, black);
			}
		}
	}

	// -------------------------------------------------------------------------
	// WARRIOR -- red knight, plumed helmet, sword raised, shield
	// -------------------------------------------------------------------------
	private void drawWarrior(Graphics2D g)
	{
		Color RED = new Color(200, 60, 55), RED_S = new Color(150, 35, 35), RED_H = new Color(230, 95, 90);
		Color SLV = new Color(205, 212, 228), SLV_S = new Color(150, 158, 180), SLV_H = new Color(240, 245, 255);
		Color GLD = new Color(222, 182, 70),  GLD_S = new Color(175, 135, 40);
		Color BRN = new Color(95, 60, 28);

		// plume on helmet
		shade(g, RED_H, RED_H, RED_S, 18, 1, 4, 4);
		// sword (raised, right side): blade, guard, grip
		shade(g, SLV, SLV_H, SLV_S, 32, 4, 3, 20);
		r(g, SLV_H, 33, 5, 1, 16);
		shade(g, GLD, GLD, GLD_S, 29, 23, 9, 2);
		shade(g, BRN, new Color(120,80,40), BRN, 33, 25, 2, 5);
		// shield (left side)
		shade(g, GLD, GLD, GLD_S, 3, 16, 6, 13);
		shade(g, RED, RED_H, RED_S, 4, 19, 4, 7);
		r(g, GLD, 5, 21, 2, 3);

		// legs + boots
		shade(g, RED_S, RED, RED_S, 14, 40, 5, 8);
		shade(g, RED_S, RED, RED_S, 21, 40, 5, 8);
		shade(g, BRN, new Color(120,80,40), new Color(60,38,16), 13, 47, 7, 3);
		shade(g, BRN, new Color(120,80,40), new Color(60,38,16), 20, 47, 7, 3);

		// torso armor
		shade(g, RED, RED_H, RED_S, 11, 24, 18, 17);
		shade(g, RED_S, RED, RED_S, 15, 27, 10, 9);   // chest plate inset
		r(g, GLD, 11, 24, 18, 1);                      // top trim
		shade(g, GLD, GLD, GLD_S, 11, 38, 18, 2);      // belt
		// shoulder pauldrons
		shade(g, RED_H, RED_H, RED_S, 7, 24, 6, 5);
		shade(g, RED_H, RED_H, RED_S, 27, 24, 6, 5);

		// helmet
		shade(g, RED, RED_H, RED_S, 12, 5, 16, 9);
		r(g, RED_S, 12, 12, 16, 2);                    // helmet brim shadow
		// face slot
		shade(g, SKIN, SKIN_H, SKIN_S, 15, 9, 10, 6);
		r(g, RED_S, 15, 7, 10, 2);                      // visor bar shadow
		// eyes, cheeks, mouth
		eyes(g, 16, 21, 10, new Color(40, 30, 90));
		r(g, CHEEK, 15, 13, 2, 1);
		r(g, CHEEK, 23, 13, 2, 1);
		r(g, new Color(150,70,70), 18, 13, 4, 1);
	}

	// -------------------------------------------------------------------------
	// MAGE -- purple robe, tall pointed hat, glowing staff
	// -------------------------------------------------------------------------
	private void drawMage(Graphics2D g)
	{
		Color PUR = new Color(120, 70, 195), PUR_S = new Color(80, 40, 140), PUR_H = new Color(150, 100, 220);
		Color DPU = new Color(70, 35, 120);
		Color CYN = new Color(110, 220, 235), CYN_S = new Color(60, 160, 190), CYN_H = new Color(190, 250, 255);
		Color GLD = new Color(230, 200, 90);
		Color BRN = new Color(95, 60, 28);

		// staff with glowing orb (left)
		shade(g, BRN, new Color(120,80,40), new Color(60,38,16), 5, 14, 3, 30);
		shade(g, CYN, CYN_H, CYN_S, 2, 6, 8, 8);
		r(g, CYN_H, 3, 7, 3, 3);
		r(g, WHT, 4, 8, 1, 1);

		// robe (wide at bottom)
		shade(g, PUR, PUR_H, PUR_S, 11, 24, 18, 14);
		shade(g, PUR, PUR_H, PUR_S, 9, 36, 22, 8);
		r(g, PUR_S, 9, 42, 22, 2);
		// robe star emblem
		r(g, GLD, 19, 28, 2, 8);
		r(g, GLD, 16, 31, 8, 2);
		// feet
		shade(g, DPU, PUR, PUR_S, 12, 43, 6, 3);
		shade(g, DPU, PUR, PUR_S, 22, 43, 6, 3);

		// head
		shade(g, SKIN, SKIN_H, SKIN_S, 12, 14, 16, 11);
		// pointed hat (stacked, leans slightly)
		shade(g, DPU, PUR, PUR_S, 20, 0, 3, 4);
		shade(g, DPU, PUR, PUR_S, 18, 3, 5, 4);
		shade(g, DPU, PUR, PUR_S, 16, 6, 7, 4);
		shade(g, PUR, PUR_H, PUR_S, 10, 9, 20, 5);   // brim
		r(g, GLD, 18, 4, 2, 1);                        // hat star
		r(g, GLD, 24, 7, 2, 1);

		// eyes, cheeks, smile
		eyes(g, 14, 23, 17, new Color(70, 30, 140));
		r(g, CHEEK, 13, 21, 2, 1);
		r(g, CHEEK, 25, 21, 2, 1);
		r(g, new Color(150,70,70), 17, 22, 6, 1);
	}

	// -------------------------------------------------------------------------
	// ARCHER -- green hood + tunic, tall longbow, quiver
	// -------------------------------------------------------------------------
	private void drawArcher(Graphics2D g)
	{
		Color GRN = new Color(60, 140, 60), GRN_S = new Color(35, 90, 35), GRN_H = new Color(90, 175, 90);
		Color DGR = new Color(30, 75, 30);
		Color BRN = new Color(120, 75, 32), BRN_S = new Color(85, 52, 22), BRN_H = new Color(150, 100, 50);
		Color TAN = new Color(200, 160, 95);

		// longbow (left, tall curved)
		shade(g, BRN, BRN_H, BRN_S, 4, 10, 3, 32);
		r(g, BRN_S, 3, 12, 1, 6);
		r(g, BRN_S, 3, 34, 1, 6);
		r(g, TAN, 7, 12, 1, 28);          // bowstring

		// quiver + arrows (right back)
		shade(g, BRN, BRN_H, BRN_S, 28, 22, 5, 13);
		r(g, TAN, 29, 17, 1, 6);
		r(g, TAN, 31, 16, 1, 7);
		r(g, new Color(220,220,220), 29, 16, 1, 1);
		r(g, new Color(220,220,220), 31, 15, 1, 1);

		// hood (frames the head)
		shade(g, DGR, GRN, GRN_S, 11, 9, 18, 14);
		// head
		shade(g, SKIN, SKIN_H, SKIN_S, 13, 12, 14, 11);
		r(g, DGR, 11, 9, 18, 3);          // hood top shadow

		// eyes (focused), brows, cheeks
		eyes(g, 15, 22, 15, new Color(40, 80, 30));
		r(g, DGR, 15, 14, 3, 1);
		r(g, DGR, 22, 14, 3, 1);
		r(g, CHEEK, 14, 19, 2, 1);
		r(g, CHEEK, 24, 19, 2, 1);
		r(g, new Color(150,80,70), 18, 20, 4, 1);

		// tunic body
		shade(g, GRN, GRN_H, GRN_S, 12, 23, 16, 11);
		shade(g, BRN, BRN_H, BRN_S, 12, 31, 16, 2);   // belt
		r(g, TAN, 18, 31, 4, 2);                       // buckle

		// legs + boots
		shade(g, BRN, BRN_H, BRN_S, 13, 34, 5, 9);
		shade(g, BRN, BRN_H, BRN_S, 22, 34, 5, 9);
		shade(g, DGR, GRN_S, DGR, 12, 42, 7, 3);
		shade(g, DGR, GRN_S, DGR, 21, 42, 7, 3);
	}

	// -------------------------------------------------------------------------
	// RAT -- gray rodent, big pink ears, long tail, beady red eyes
	// -------------------------------------------------------------------------
	private void drawRat(Graphics2D g)
	{
		Color GRY = new Color(150, 145, 155), GRY_S = new Color(105, 100, 112), GRY_H = new Color(180, 175, 188);
		Color PNK = new Color(255, 175, 185), PNK_S = new Color(220, 130, 145);
		Color RED = new Color(220, 50, 50);

		// tail (curls left and down)
		r(g, GRY_S, 2, 40, 7, 3);
		r(g, GRY_S, 6, 35, 3, 6);
		r(g, GRY_S, 8, 31, 3, 6);

		// body (round, sits low)
		shade(g, GRY, GRY_H, GRY_S, 9, 26, 22, 16);
		r(g, GRY_S, 11, 38, 18, 3);       // belly shadow

		// feet
		shade(g, PNK, PNK, PNK_S, 12, 41, 5, 3);
		shade(g, PNK, PNK, PNK_S, 23, 41, 5, 3);

		// head (round)
		shade(g, GRY, GRY_H, GRY_S, 11, 13, 16, 15);
		// big ears
		shade(g, GRY, GRY_H, GRY_S, 9, 7, 8, 8);
		shade(g, GRY, GRY_H, GRY_S, 23, 7, 8, 8);
		shade(g, PNK, PNK, PNK_S, 11, 9, 4, 4);
		shade(g, PNK, PNK, PNK_S, 25, 9, 4, 4);

		// snout (juts right) + pink nose
		shade(g, GRY_H, GRY_H, GRY_S, 25, 18, 8, 6);
		shade(g, PNK, PNK, PNK_S, 31, 20, 3, 3);

		// beady red eyes with shine
		r(g, RED, 15, 16, 3, 3);
		r(g, RED, 22, 16, 3, 3);
		r(g, new Color(255,120,120), 15, 16, 1, 1);
		r(g, new Color(255,120,120), 22, 16, 1, 1);

		// whiskers
		r(g, new Color(210,205,215), 20, 21, 11, 1);
		r(g, new Color(210,205,215), 20, 23, 10, 1);
	}

	// -------------------------------------------------------------------------
	// GOBLIN -- green brute, huge ears, toothy grin, spiked club
	// -------------------------------------------------------------------------
	private void drawGoblin(Graphics2D g)
	{
		Color GRN = new Color(90, 180, 70), GRN_S = new Color(55, 125, 45), GRN_H = new Color(125, 205, 95);
		Color DGR = new Color(45, 110, 35);
		Color YLW = new Color(235, 205, 60);
		Color BRN = new Color(110, 70, 30), BRN_H = new Color(140, 95, 45);
		Color STD = new Color(170, 150, 60);

		// spiked club (right hand)
		shade(g, BRN, BRN_H, new Color(75,45,18), 29, 22, 4, 14);
		shade(g, new Color(130,88,40), new Color(160,110,55), BRN, 26, 15, 9, 7);
		r(g, STD, 28, 16, 2, 2);
		r(g, STD, 31, 18, 2, 2);

		// huge pointed ears
		shade(g, GRN, GRN_H, GRN_S, 1, 15, 9, 8);
		shade(g, GRN, GRN_H, GRN_S, 30, 15, 9, 8);
		r(g, DGR, 2, 17, 6, 4);
		r(g, DGR, 31, 17, 6, 4);

		// wide head
		shade(g, GRN, GRN_H, GRN_S, 6, 13, 28, 14);
		// brow ridge
		r(g, GRN_S, 9, 16, 22, 1);
		// nose
		shade(g, DGR, GRN_S, DGR, 18, 21, 5, 3);
		// yellow eyes
		r(g, YLW, 10, 17, 5, 4);
		r(g, YLW, 25, 17, 5, 4);
		r(g, OUT, 11, 18, 3, 2);
		r(g, OUT, 26, 18, 3, 2);
		r(g, WHT, 11, 18, 1, 1);
		r(g, WHT, 26, 18, 1, 1);
		// big toothy grin
		r(g, new Color(20,10,5), 10, 24, 20, 4);
		r(g, WHT, 11, 24, 3, 3);
		r(g, WHT, 15, 24, 3, 3);
		r(g, WHT, 19, 24, 3, 3);
		r(g, WHT, 23, 24, 3, 3);

		// hunched body
		shade(g, DGR, GRN, GRN_S, 9, 28, 22, 13);
		// arms
		shade(g, GRN, GRN_H, GRN_S, 3, 29, 7, 8);
		shade(g, GRN, GRN_H, GRN_S, 30, 29, 7, 8);
		// legs + feet
		shade(g, DGR, GRN, GRN_S, 12, 41, 6, 7);
		shade(g, DGR, GRN, GRN_S, 22, 41, 6, 7);
		shade(g, new Color(55,33,12), BRN, new Color(40,24,10), 11, 47, 8, 3);
		shade(g, new Color(55,33,12), BRN, new Color(40,24,10), 21, 47, 8, 3);
	}

	// -------------------------------------------------------------------------
	// SKELETON -- bone white skull, ribcage, thin bones, rusty sword
	// -------------------------------------------------------------------------
	private void drawSkeleton(Graphics2D g)
	{
		Color BON = new Color(235, 230, 210), BON_S = new Color(185, 178, 155), BON_H = new Color(255, 252, 238);
		Color DRK = new Color(22, 16, 12);
		Color RST = new Color(170, 105, 50), RST_H = new Color(200, 135, 70);
		Color GLD = new Color(150, 115, 60);

		// rusty sword (right)
		shade(g, RST, RST_H, new Color(120,70,30), 30, 6, 4, 22);
		shade(g, GLD, GLD, new Color(110,85,40), 26, 17, 9, 3);
		r(g, new Color(90,55,25), 31, 20, 2, 5);

		// skull
		shade(g, BON, BON_H, BON_S, 12, 1, 16, 13);
		// eye sockets
		r(g, DRK, 13, 4, 5, 4);
		r(g, DRK, 22, 4, 5, 4);
		r(g, new Color(120,40,40), 14, 5, 2, 2);   // faint red glow
		r(g, new Color(120,40,40), 23, 5, 2, 2);
		// nose + jaw + teeth
		r(g, DRK, 19, 8, 3, 3);
		shade(g, BON_S, BON, BON_S, 13, 11, 14, 4);
		r(g, BON, 14, 12, 2, 3);
		r(g, BON, 17, 12, 2, 3);
		r(g, BON, 20, 12, 2, 3);
		r(g, BON, 23, 12, 2, 3);

		// spine + ribcage
		r(g, BON_S, 19, 15, 2, 4);
		shade(g, BON, BON_H, BON_S, 12, 19, 16, 14);
		r(g, DRK, 14, 20, 2, 12);   // rib gaps
		r(g, DRK, 17, 20, 2, 12);
		r(g, DRK, 20, 20, 2, 12);
		r(g, DRK, 23, 20, 2, 12);

		// arms (thin bones)
		shade(g, BON, BON_H, BON_S, 6, 19, 7, 3);
		shade(g, BON, BON_H, BON_S, 6, 21, 3, 9);
		shade(g, BON, BON_H, BON_S, 27, 19, 7, 3);
		shade(g, BON, BON_H, BON_S, 29, 21, 3, 9);

		// pelvis + legs + feet
		shade(g, BON_S, BON, BON_S, 13, 33, 14, 4);
		shade(g, BON, BON_H, BON_S, 14, 37, 4, 10);
		shade(g, BON, BON_H, BON_S, 22, 37, 4, 10);
		r(g, BON_S, 12, 47, 7, 3);
		r(g, BON_S, 21, 47, 7, 3);
	}

	// -------------------------------------------------------------------------
	// DRAGON -- red boss, spread wings, horns, fire breath, gold eyes
	// -------------------------------------------------------------------------
	private void drawDragon(Graphics2D g)
	{
		Color RED = new Color(170, 35, 35), RED_S = new Color(120, 18, 18), RED_H = new Color(210, 70, 70);
		Color BEL = new Color(225, 150, 90), BEL_S = new Color(185, 110, 60);   // belly
		Color ORN = new Color(245, 140, 35);
		Color FIR = new Color(255, 225, 70);
		Color GLD = new Color(225, 185, 55);

		// wings (spread wide, behind body)
		shade(g, RED_S, RED, new Color(95,12,12), 0, 7, 12, 19);
		shade(g, RED_S, RED, new Color(95,12,12), 28, 7, 12, 19);
		r(g, RED, 2, 9, 8, 13);          // inner membrane lighter
		r(g, RED, 30, 9, 8, 13);
		r(g, RED_S, 5, 9, 1, 13);        // wing ribs
		r(g, RED_S, 8, 9, 1, 13);
		r(g, RED_S, 31, 9, 1, 13);
		r(g, RED_S, 34, 9, 1, 13);

		// tail (curls right, with spade tip)
		r(g, RED, 26, 38, 8, 3);
		r(g, RED, 30, 41, 5, 3);
		shade(g, RED_H, RED_H, RED_S, 33, 42, 4, 4);

		// body
		shade(g, RED, RED_H, RED_S, 9, 22, 22, 17);
		shade(g, BEL, BEL, BEL_S, 13, 25, 14, 11);   // belly scales
		r(g, BEL_S, 13, 28, 14, 1);
		r(g, BEL_S, 13, 31, 14, 1);

		// legs + claws
		shade(g, RED, RED_H, RED_S, 11, 37, 7, 7);
		shade(g, RED, RED_H, RED_S, 22, 37, 7, 7);
		r(g, new Color(230,225,215), 11, 43, 2, 2);
		r(g, new Color(230,225,215), 15, 43, 2, 2);
		r(g, new Color(230,225,215), 22, 43, 2, 2);
		r(g, new Color(230,225,215), 26, 43, 2, 2);

		// head
		shade(g, RED, RED_H, RED_S, 10, 7, 20, 14);
		// horns
		shade(g, BEL, BEL, BEL_S, 11, 2, 4, 6);
		shade(g, BEL, BEL, BEL_S, 25, 2, 4, 6);
		// snout + mouth
		shade(g, RED_H, RED_H, RED_S, 12, 17, 16, 5);
		r(g, new Color(25,8,8), 13, 19, 14, 3);
		r(g, WHT, 14, 19, 2, 2);          // fangs
		r(g, WHT, 17, 19, 2, 2);
		r(g, WHT, 20, 19, 2, 2);
		r(g, WHT, 23, 19, 2, 2);
		// gold eyes with slit pupils
		r(g, GLD, 12, 10, 5, 4);
		r(g, GLD, 23, 10, 5, 4);
		r(g, new Color(80,10,10), 14, 11, 2, 2);
		r(g, new Color(80,10,10), 24, 11, 2, 2);
		r(g, WHT, 12, 10, 1, 1);
		r(g, WHT, 23, 10, 1, 1);

		// fire breath under the mouth
		shade(g, ORN, FIR, ORN, 8, 21, 7, 4);
		r(g, FIR, 10, 22, 3, 2);
		shade(g, ORN, FIR, ORN, 25, 21, 7, 4);
		r(g, FIR, 27, 22, 3, 2);
	}

	// -------------------------------------------------------------------------
	// POTION -- red health flask with cork, shine, and cross
	// -------------------------------------------------------------------------
	private void drawPotion(Graphics2D g)
	{
		Color RED = new Color(210, 40, 50), RED_S = new Color(160, 22, 30), RED_H = new Color(245, 90, 100);
		Color GLS = new Color(205, 235, 245), GLS_S = new Color(150, 185, 205);
		Color CRK = new Color(155, 110, 50), CRK_H = new Color(185, 140, 70);

		// soft red glow behind bottle
		r(g, new Color(150, 25, 35), 10, 20, 20, 28);
		r(g, new Color(110, 15, 25), 8, 24, 24, 20);

		// round bottle body
		shade(g, GLS, GLS, GLS_S, 11, 24, 18, 22);
		shade(g, GLS, GLS, GLS_S, 9, 28, 22, 14);
		// red liquid inside
		shade(g, RED, RED_H, RED_S, 12, 27, 16, 18);
		shade(g, RED, RED_H, RED_S, 10, 30, 20, 11);
		r(g, RED_H, 13, 28, 7, 2);        // liquid surface highlight

		// neck
		shade(g, GLS, GLS, GLS_S, 16, 16, 8, 9);
		r(g, RED, 17, 18, 6, 6);

		// cork
		shade(g, CRK, CRK_H, new Color(120,85,35), 15, 10, 10, 6);

		// glass shine
		r(g, WHT, 13, 28, 2, 10);
		r(g, WHT, 13, 27, 4, 2);

		// white health cross
		r(g, WHT, 19, 32, 2, 8);
		r(g, WHT, 16, 35, 8, 2);

		// bottle base
		shade(g, GLS_S, GLS, GLS_S, 11, 44, 18, 2);
	}
}
