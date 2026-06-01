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
		setOpaque(false);
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
		Color RED = new Color(92, 108, 130), RED_S = new Color(48, 58, 76), RED_H = new Color(185, 198, 215);
		Color SLV = new Color(205, 212, 228), SLV_S = new Color(150, 158, 180), SLV_H = new Color(240, 245, 255);
		Color GLD = new Color(222, 182, 70),  GLD_S = new Color(175, 135, 40);
		Color BLU = new Color(55, 90, 175), BLU_S = new Color(25, 45, 105);
		Color BRN = new Color(95, 60, 28);

		// steel crest
		shade(g, SLV, SLV_H, SLV_S, 17, 0, 6, 4);
		// sword (raised, right side): blade, guard, grip
		shade(g, SLV, SLV_H, SLV_S, 33, 4, 3, 21);
		r(g, SLV_H, 34, 5, 1, 17);
		shade(g, GLD, GLD, GLD_S, 30, 23, 9, 2);
		shade(g, BRN, new Color(120,80,40), BRN, 34, 25, 2, 5);
		// shield (left side)
		shade(g, GLD, GLD, GLD_S, 2, 17, 6, 13);
		shade(g, BLU, new Color(80,120,220), BLU_S, 3, 20, 4, 7);
		r(g, GLD, 4, 21, 2, 5);

		// legs + boots
		shade(g, RED_S, RED, RED_S, 14, 40, 5, 9);
		shade(g, RED_S, RED, RED_S, 21, 40, 5, 9);
		shade(g, BRN, new Color(120,80,40), new Color(60,38,16), 13, 48, 7, 3);
		shade(g, BRN, new Color(120,80,40), new Color(60,38,16), 20, 48, 7, 3);

		// plate armor, tucked under helmet so the head does not float
		shade(g, SLV, SLV_H, SLV_S, 11, 20, 18, 21);
		shade(g, RED, RED_H, RED_S, 15, 27, 10, 9);
		r(g, GLD, 11, 20, 18, 1);
		shade(g, SLV_S, SLV, RED_S, 13, 18, 14, 4);
		r(g, SLV_H, 13, 23, 14, 2);
		r(g, RED_S, 19, 24, 2, 14);
		shade(g, GLD, GLD, GLD_S, 11, 38, 18, 2);
		// shoulder pauldrons
		shade(g, SLV, SLV_H, SLV_S, 7, 21, 6, 8);
		shade(g, SLV, SLV_H, SLV_S, 27, 21, 6, 8);

		// steel helmet with visor
		shade(g, SLV, SLV_H, SLV_S, 10, 3, 20, 16);
		r(g, SLV_S, 10, 17, 20, 2);
		// face slot -- bigger opening
		shade(g, SKIN, SKIN_H, SKIN_S, 13, 7, 14, 10);
		r(g, RED_S, 13, 7, 14, 2);                     // visor brow
		r(g, SLV_S, 10, 11, 3, 7);
		r(g, SLV_S, 27, 11, 3, 7);
		// eyes, cheeks, mouth
		eyes(g, 14, 22, 10, new Color(40, 30, 90));
		r(g, CHEEK, 13, 14, 3, 1);
		r(g, CHEEK, 24, 14, 3, 1);
		r(g, new Color(150,70,70), 18, 15, 4, 1);
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

		// robe, slim and flat like the other player classes
		shade(g, PUR, PUR_H, PUR_S, 14, 24, 12, 19);
		r(g, PUR_H, 15, 24, 2, 14);
		shade(g, DPU, PUR, PUR_S, 14, 40, 12, 3);
		// robe star emblem
		r(g, GLD, 19, 28, 2, 8);
		r(g, GLD, 16, 31, 8, 2);
		// feet
		shade(g, DPU, PUR, PUR_S, 14, 43, 5, 3);
		shade(g, DPU, PUR, PUR_S, 21, 43, 5, 3);

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
		// jagged toothy grin
		r(g, new Color(20,10,5), 10, 23, 20, 5);
		r(g, WHT, 11, 23, 3, 3);
		r(g, WHT, 15, 23, 2, 4);
		r(g, WHT, 19, 23, 3, 3);
		r(g, WHT, 24, 23, 2, 4);
		r(g, new Color(230,230,210), 13, 26, 3, 2);
		r(g, new Color(230,230,210), 21, 26, 3, 2);

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

		// curved silver dagger gripped in the left hand, raised in front of the arm
		Color SIL = new Color(200, 205, 215), SIL_H = new Color(240, 244, 252), SIL_S = new Color(130, 135, 148);
		// blade angles up to a point above the fist
		shade(g, SIL, SIL_H, SIL_S, 3, 18, 4, 11);
		r(g, SIL_S, 3, 18, 1, 11);
		r(g, SIL_H, 5, 19, 1, 9);
		// sharpened tip
		r(g, SIL_H, 4, 16, 2, 2);
		r(g, SIL, 4, 15, 1, 1);
		// gold crossguard
		shade(g, YLW, new Color(250,225,90), STD, 1, 29, 8, 2);
		// wrapped grip in the fist
		shade(g, BRN, BRN_H, new Color(70,40,20), 3, 31, 4, 5);
		r(g, new Color(70,40,20), 4, 32, 1, 3);
		// round gold pommel
		r(g, YLW, 3, 36, 4, 2);
		r(g, new Color(250,225,90), 4, 36, 1, 1);
	}

	// -------------------------------------------------------------------------
	// SKELETON -- bone white skull, ribcage, thin bones, rusty sword
	// -------------------------------------------------------------------------
	private void drawSkeleton(Graphics2D g)
	{
		Color BON = new Color(235, 230, 210), BON_S = new Color(185, 178, 155), BON_H = new Color(255, 252, 238);
		Color DRK = new Color(22, 16, 12);
		Color RST = new Color(155, 95, 55), RST_H = new Color(210, 160, 100);
		Color GLD = new Color(150, 115, 60);
		Color BLU = new Color(80, 190, 230);

		// domed cranium -- rounded top so it isn't flat against the ceiling
		r(g, BON, 15, 2, 10, 1);
		r(g, BON, 13, 3, 14, 1);
		shade(g, BON, BON_H, BON_S, 11, 4, 18, 11);
		// bright highlight across the top of the dome
		r(g, BON_H, 14, 3, 9, 1);
		r(g, BON_H, 12, 5, 4, 2);
		// hairline crack down the right side of the skull
		r(g, BON_S, 24, 3, 1, 4);
		r(g, BON_S, 23, 5, 1, 2);
		// cheekbones
		r(g, BON_S, 10, 8, 2, 5);
		r(g, BON_S, 28, 8, 2, 5);
		// deep eye sockets with glowing blue pupils
		r(g, DRK, 12, 7, 6, 5);
		r(g, DRK, 22, 7, 6, 5);
		r(g, BLU, 14, 8, 3, 3);
		r(g, BLU, 24, 8, 3, 3);
		r(g, BON_H, 15, 8, 1, 1);
		r(g, BON_H, 25, 8, 1, 1);
		// triangular nose cavity
		r(g, DRK, 19, 11, 3, 2);
		r(g, DRK, 20, 13, 1, 1);
		// jaw + gritted teeth with dark gaps
		shade(g, BON_S, BON, BON_S, 13, 14, 14, 4);
		r(g, DRK, 13, 14, 14, 1);
		r(g, BON, 14, 15, 2, 3);
		r(g, BON, 17, 15, 2, 3);
		r(g, BON, 20, 15, 2, 3);
		r(g, BON, 23, 15, 2, 3);

		// spine + ribcage
		r(g, BON_S, 19, 18, 2, 4);
		shade(g, BON, BON_H, BON_S, 12, 22, 16, 12);
		r(g, DRK, 14, 23, 2, 10);
		r(g, DRK, 18, 23, 2, 10);
		r(g, DRK, 22, 23, 2, 10);
		r(g, DRK, 26, 23, 1, 10);
		r(g, BON_H, 13, 24, 14, 1);
		r(g, BON_H, 13, 28, 14, 1);
		r(g, BLU, 19, 27, 2, 2);

		// arms (thin bones)
		shade(g, BON, BON_H, BON_S, 6, 22, 7, 3);
		shade(g, BON, BON_H, BON_S, 5, 24, 3, 10);
		shade(g, BON, BON_H, BON_S, 27, 22, 7, 3);
		shade(g, BON, BON_H, BON_S, 30, 24, 3, 9);

		// pelvis + legs + feet
		shade(g, BON_S, BON, BON_S, 13, 35, 14, 4);
		shade(g, BON, BON_H, BON_S, 14, 39, 4, 8);
		shade(g, BON, BON_H, BON_S, 22, 39, 4, 8);
		r(g, BON_S, 12, 47, 7, 2);
		r(g, BON_S, 21, 47, 7, 2);

		// curved rusty blade and handle in front of the right arm
		shade(g, RST, RST_H, new Color(120,70,30), 32, 7, 4, 25);
		r(g, RST_H, 31, 8, 2, 8);
		r(g, new Color(230,170,95), 33, 8, 1, 21);
		shade(g, GLD, GLD, new Color(110,85,40), 27, 22, 12, 3);
		shade(g, new Color(80,48,24), new Color(125,75,35), new Color(55,32,14), 34, 25, 3, 10);
	}

	// -------------------------------------------------------------------------
	// DRAGON -- red boss, spread wings, horns, fire breath, gold eyes
	// -------------------------------------------------------------------------
	private void drawDragon(Graphics2D g)
	{
		Color RED = new Color(175, 28, 28), RED_S = new Color(105, 12, 12), RED_H = new Color(230, 65, 55);
		Color BEL = new Color(235, 155, 85), BEL_S = new Color(175, 95, 50);
		Color DRK = new Color(70, 8, 8);
		Color ORN = new Color(245, 140, 35);
		Color FIR = new Color(255, 225, 70);
		Color GLD = new Color(225, 185, 55);

		// jagged wings with dark membranes, inset so the outline is not clipped
		shade(g, DRK, RED, RED_S, 2, 8, 11, 20);
		shade(g, DRK, RED, RED_S, 27, 8, 11, 20);
		r(g, RED_H, 4, 10, 3, 14);
		r(g, RED_H, 33, 10, 3, 14);
		r(g, RED_S, 8, 12, 2, 15);
		r(g, RED_S, 30, 12, 2, 15);
		r(g, new Color(45,5,5), 2, 25, 5, 4);
		r(g, new Color(45,5,5), 33, 25, 5, 4);
		r(g, GLD, 5, 7, 3, 3);
		r(g, GLD, 32, 7, 3, 3);

		// tail curls to the side with a spade tip
		r(g, RED, 25, 39, 8, 3);
		r(g, RED, 29, 42, 5, 3);
		shade(g, RED_H, RED_H, RED_S, 33, 42, 4, 5);

		// body and belly scales
		shade(g, RED, RED_H, RED_S, 8, 21, 24, 19);
		shade(g, BEL, BEL, BEL_S, 13, 25, 14, 12);
		r(g, BEL_S, 13, 28, 14, 1);
		r(g, BEL_S, 13, 31, 14, 1);
		r(g, BEL_S, 13, 34, 14, 1);

		// legs + claws
		shade(g, RED, RED_H, RED_S, 10, 38, 8, 7);
		shade(g, RED, RED_H, RED_S, 22, 38, 8, 7);
		r(g, WHT, 10, 44, 2, 2);
		r(g, WHT, 14, 44, 2, 2);
		r(g, WHT, 22, 44, 2, 2);
		r(g, WHT, 27, 44, 2, 2);

		// head (dropped 1px so the horns have room to point up)
		shade(g, RED, RED_H, RED_S, 9, 8, 22, 14);

		// swept-back ivory horns with gold tips, tapering to points
		Color IVR = new Color(245, 230, 200), IVR_S = new Color(195, 175, 140);
		// left horn sweeps up and out
		shade(g, IVR, IVR, IVR_S, 10, 6, 4, 3);
		shade(g, IVR, IVR, IVR_S, 8, 3, 4, 3);
		shade(g, IVR, IVR, IVR_S, 7, 1, 3, 3);
		r(g, GLD, 7, 1, 2, 2);
		// right horn sweeps up and out
		shade(g, IVR, IVR, IVR_S, 26, 6, 4, 3);
		shade(g, IVR, IVR, IVR_S, 28, 3, 4, 3);
		shade(g, IVR, IVR, IVR_S, 30, 1, 3, 3);
		r(g, GLD, 31, 1, 2, 2);
		// jagged crown spikes between the horns
		r(g, RED_S, 15, 5, 2, 4);
		r(g, RED_H, 19, 4, 2, 5);
		r(g, RED_S, 23, 5, 2, 4);
		// snout + mouth
		shade(g, RED_H, RED_H, RED_S, 11, 17, 18, 6);
		r(g, new Color(25,8,8), 13, 19, 14, 4);
		r(g, WHT, 14, 19, 2, 3);
		r(g, WHT, 18, 19, 2, 3);
		r(g, WHT, 22, 19, 2, 3);
		r(g, WHT, 26, 19, 2, 3);
		// gold eyes with slit pupils
		r(g, GLD, 12, 10, 5, 4);
		r(g, GLD, 23, 10, 5, 4);
		r(g, new Color(60,5,5), 14, 10, 1, 4);
		r(g, new Color(60,5,5), 25, 10, 1, 4);
		r(g, RED_S, 10, 15, 20, 1);
		// flame cheeks and smoke/fire under mouth
		shade(g, ORN, FIR, ORN, 8, 20, 7, 4);
		r(g, FIR, 10, 21, 3, 2);
		shade(g, ORN, FIR, ORN, 26, 20, 7, 4);
		r(g, FIR, 28, 21, 3, 2);
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
