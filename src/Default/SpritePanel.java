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
 * Purpose: SpritePanel draws a retro pixel-art sprite by painting onto a small
 * 40x56 BufferedImage using plain filled rectangles only, then scaling it up
 * with nearest-neighbor interpolation. This gives the authentic blocky pixel-art
 * look. The sprite is always centered in the panel and never exceeds a max size,
 * so it stays small even in large battle-screen cells.
 *
 * SpritePanel is part of the View. It reads no game data.
 */
public class SpritePanel extends JPanel
{
	// package access so DungeonPanel and BattlePanel can swap the type
	String type;

	// source canvas size for all sprites
	private static final int SW = 40;
	private static final int SH = 56;

	// cap at 6x so sprites never fill the whole battle cell
	private static final int MAX_SCALE = 6;

	// shared colors used across multiple sprites
	private static final Color SKIN  = new Color(255, 200, 150);
	private static final Color PINK  = new Color(255, 155, 140);
	private static final Color BLK   = Color.BLACK;
	private static final Color WHT   = Color.WHITE;

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

		// paint onto a tiny canvas -- no anti-aliasing for pixel art look
		BufferedImage img = new BufferedImage(SW, SH, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = img.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);

		drawSprite(g2);
		g2.dispose();

		// scale up with nearest-neighbor so pixels stay sharp and blocky
		int scale = Math.max(1, Math.min(MAX_SCALE, Math.min(W / SW, H / SH)));
		int dw = SW * scale;
		int dh = SH * scale;
		int dx = (W - dw) / 2;
		int dy = (H - dh) / 2;

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

	// short fill helper
	private void p(Graphics2D g, Color c, int x, int y, int w, int h)
	{
		g.setColor(c); g.fillRect(x, y, w, h);
	}

	// -------------------------------------------------------------------------
	// WARRIOR -- blocky red knight, sword right, shield left
	// -------------------------------------------------------------------------
	private void drawWarrior(Graphics2D g)
	{
		Color RED   = new Color(190, 45, 45);
		Color DRD   = new Color(110, 20, 20);
		Color SLV   = new Color(200, 210, 225);
		Color GLD   = new Color(215, 175, 50);
		Color BRN   = new Color(80, 50, 20);
		Color LRED  = new Color(210, 70, 70);

		// --- shield (left) ---
		p(g, GLD,  1, 14,  5, 12);
		p(g, RED,  2, 18,  3,  4);

		// --- sword (right) ---
		p(g, SLV, 33,  8,  3, 22);   // blade
		p(g, GLD, 29, 17,  9,  3);   // guard
		p(g, BRN, 34, 20,  2,  5);   // hilt

		// --- boots ---
		p(g, BRN, 12, 34,  8,  2);
		p(g, BRN, 20, 34,  8,  2);

		// --- legs ---
		p(g, DRD, 13, 24,  6,  10);
		p(g, DRD, 21, 24,  6,  10);

		// --- body armor ---
		p(g, RED,  9, 13, 22,  11);
		p(g, LRED, 4, 13,  6,   5);   // left shoulder
		p(g, LRED, 30,13,  6,   5);   // right shoulder
		p(g, DRD, 14, 15, 12,   7);   // chest detail
		p(g, GLD,  9, 13, 22,   1);   // top trim
		p(g, GLD,  9, 23, 22,   1);   // bottom trim

		// --- helmet ---
		p(g, DRD, 17,  0,  6,  3);   // crest
		p(g, RED, 12,  3, 16,  9);   // helmet block
		p(g, SKIN,14,  5, 12,  5);   // face opening
		p(g, DRD, 14,  5, 12,  2);   // visor bar
		// eyes
		p(g, BLK, 15,  7,  3,  2);
		p(g, BLK, 22,  7,  3,  2);
		// cheeks
		p(g, PINK,14,  9,  3,  1);
		p(g, PINK,23,  9,  3,  1);
		// mouth
		p(g, DRD, 18, 10,  4,  1);
	}

	// -------------------------------------------------------------------------
	// MAGE -- tall pointy hat, wide robe, glowing staff
	// -------------------------------------------------------------------------
	private void drawMage(Graphics2D g)
	{
		Color PUR  = new Color(100, 55, 175);
		Color DPUR = new Color(55,  25, 110);
		Color CYN  = new Color(80, 200, 220);
		Color GLD  = new Color(215, 175, 50);
		Color BRN  = new Color(80, 50, 20);

		// --- staff (behind, left side) ---
		p(g, BRN,  5, 12,  3, 30);   // pole
		p(g, CYN,  3,  6,  7,  7);   // crystal orb

		// --- hat (pointy, built in layers) ---
		p(g, DPUR,19,  0,  2,  3);
		p(g, DPUR,17,  3,  6,  3);
		p(g, DPUR,15,  6,  10, 3);
		p(g, DPUR,10, 10, 20,  4);   // brim
		// hat stars
		p(g, GLD, 17,  4,  2,  1);
		p(g, GLD, 23,  6,  2,  1);

		// --- head ---
		p(g, SKIN,12, 14, 16, 10);
		// eyes (big round pixel eyes)
		p(g, BLK, 14, 17,  4,  4);
		p(g, BLK, 22, 17,  4,  4);
		p(g, new Color(80,20,150), 15,18,  2,  2);
		p(g, new Color(80,20,150), 23,18,  2,  2);
		p(g, WHT, 14, 17,  2,  1);   // eye shine
		p(g, WHT, 22, 17,  2,  1);
		// cheeks and smile
		p(g, PINK,13, 20,  3,  1);
		p(g, PINK,24, 20,  3,  1);
		p(g, DPUR,17, 22,  6,  1);

		// --- robe ---
		p(g, PUR, 11, 24, 18, 14);   // upper robe
		p(g, PUR,  8, 34, 24,  8);   // lower robe wider
		// star on robe
		p(g, GLD, 19, 27,  2,  8);   // vertical
		p(g, GLD, 16, 30,  8,  2);   // horizontal
		// feet
		p(g, DPUR,12, 42,  6,  3);
		p(g, DPUR,22, 42,  6,  3);
	}

	// -------------------------------------------------------------------------
	// ARCHER -- green hooded ranger with bow
	// -------------------------------------------------------------------------
	private void drawArcher(Graphics2D g)
	{
		Color GRN  = new Color(55, 130, 55);
		Color DGRN = new Color(30,  80, 30);
		Color BRN  = new Color(110, 68, 28);
		Color TAN  = new Color(195,155, 90);

		// --- bow (left side) ---
		p(g, BRN,  3, 14,  3, 24);   // bow limb
		p(g, BRN,  2, 16,  2, 20);   // curve left
		p(g, TAN,  5, 14,  1, 24);   // bowstring

		// --- quiver (right side) ---
		p(g, BRN, 28, 22,  4, 12);
		p(g, TAN, 30, 18,  1,  6);   // arrow

		// --- hood (behind head) ---
		p(g, DGRN,10, 10, 20, 14);

		// --- head ---
		p(g, SKIN,13, 12, 14, 11);
		// focused narrow eyes
		p(g, BLK, 14, 16,  4,  1);
		p(g, BLK, 22, 16,  4,  1);
		// brows
		p(g, DGRN,14, 15,  4,  1);
		p(g, DGRN,22, 15,  4,  1);
		// cheeks
		p(g, PINK,13, 19,  3,  1);
		p(g, PINK,24, 19,  3,  1);
		p(g, new Color(140,80,60),18, 21,  4,  1);

		// --- body ---
		p(g, GRN, 12, 23, 16, 10);
		p(g, BRN, 12, 30, 16,  3);   // belt
		p(g, TAN, 18, 30,  4,  3);   // buckle

		// --- legs ---
		p(g, BRN, 13, 33,  5,  9);
		p(g, BRN, 22, 33,  5,  9);

		// --- boots ---
		p(g, new Color(60,35,10), 12, 42,  7,  3);
		p(g, new Color(60,35,10), 21, 42,  7,  3);
	}

	// -------------------------------------------------------------------------
	// RAT -- round gray rat, big pink ears, long tail
	// -------------------------------------------------------------------------
	private void drawRat(Graphics2D g)
	{
		Color GRAY = new Color(140, 135, 145);
		Color DGRY = new Color(90,   85,  95);
		Color PNK  = new Color(255, 175, 185);
		Color RED  = new Color(220,  50,  50);

		// --- tail (left, curves away) ---
		p(g, DGRY, 0, 36,  6,  3);
		p(g, DGRY, 4, 32,  4,  6);
		p(g, DGRY, 6, 28,  3,  6);

		// --- body (fat egg) ---
		p(g, GRAY, 10, 22, 22, 16);
		p(g, GRAY,  8, 25, 26, 10);
		p(g, GRAY, 12, 36, 16,  5);

		// --- head ---
		p(g, GRAY, 12, 10, 16, 14);

		// --- ears (top of head) ---
		p(g, GRAY, 10,  4,  8,  8);   // left ear
		p(g, GRAY, 22,  4,  8,  8);   // right ear
		p(g, PNK,  11,  5,  6,  6);   // left inner
		p(g, PNK,  23,  5,  6,  6);   // right inner

		// --- snout (extends right) ---
		p(g, GRAY, 26, 15,  8,  6);
		p(g, PNK,  32, 17,  4,  3);   // nose

		// --- eyes (red beady) ---
		p(g, RED,  14, 13,  3,  3);
		p(g, RED,  22, 13,  3,  3);
		p(g, new Color(255,100,100), 14, 13, 1, 1);
		p(g, new Color(255,100,100), 22, 13, 1, 1);

		// --- whiskers ---
		p(g, new Color(200,200,210), 19, 17, 10,  1);
		p(g, new Color(200,200,210), 19, 19,  9,  1);
		p(g, new Color(200,200,210), 19, 21,  8,  1);
	}

	// -------------------------------------------------------------------------
	// GOBLIN -- wide head, giant ears, toothy grin, club
	// -------------------------------------------------------------------------
	private void drawGoblin(Graphics2D g)
	{
		Color GRN  = new Color(80, 175, 65);
		Color DGRN = new Color(45, 115, 35);
		Color YLW  = new Color(230, 200, 50);
		Color BRN  = new Color(105, 65, 25);

		// --- club (right side) ---
		p(g, BRN, 29, 20,  4, 14);   // handle
		p(g, new Color(130,85,35), 26, 16,  9,  6);   // club head
		p(g, new Color(160,140,50),28, 17,  3,  2);   // stud

		// --- huge ears ---
		p(g, GRN,  0, 16,  9,  9);
		p(g, DGRN, 1, 17,  7,  7);
		p(g, GRN, 31, 16,  9,  9);
		p(g, DGRN,32, 17,  7,  7);

		// --- wide flat head ---
		p(g, GRN,  6, 14, 28, 13);
		// eyes
		p(g, YLW, 10, 17,  5,  4);
		p(g, YLW, 25, 17,  5,  4);
		p(g, BLK, 11, 18,  3,  2);   // pupils
		p(g, BLK, 26, 18,  3,  2);
		// nose
		p(g, DGRN,18, 22,  5,  3);
		// big grin -- dark gap
		p(g, BLK,  9, 25, 22,  5);
		// teeth (white blocks in the gap)
		p(g, WHT, 10, 25,  3,  4);
		p(g, WHT, 14, 25,  3,  4);
		p(g, WHT, 18, 25,  3,  4);
		p(g, WHT, 22, 25,  3,  4);

		// --- body ---
		p(g, DGRN, 9, 29, 22, 13);

		// --- arms ---
		p(g, DGRN, 3, 30,  7,  8);
		p(g, DGRN,30, 30,  7,  8);

		// --- legs ---
		p(g, DGRN,12, 42,  6,  8);
		p(g, DGRN,22, 42,  6,  8);

		// --- boots ---
		p(g, new Color(50,30,10), 11, 50,  8,  3);
		p(g, new Color(50,30,10), 21, 50,  8,  3);
	}

	// -------------------------------------------------------------------------
	// SKELETON -- skull, ribcage, thin bones, sword
	// -------------------------------------------------------------------------
	private void drawSkeleton(Graphics2D g)
	{
		Color BONE = new Color(230, 225, 200);
		Color BSHD = new Color(185, 178, 155);
		Color RUST = new Color(165, 100, 45);
		Color DARK = new Color(20,  15,  10);
		Color GLD  = new Color(140, 110, 60);
		Color BRN  = new Color(80,  50,  20);

		// --- sword (right) ---
		p(g, RUST, 29,  8,  4, 22);   // blade
		p(g, GLD,  25, 18,  9,  3);   // guard
		p(g, BRN,  30, 21,  2,  5);   // hilt

		// --- skull ---
		p(g, BONE, 12,  0, 16, 13);
		p(g, DARK, 13,  3,  5,  4);   // left eye socket
		p(g, DARK, 22,  3,  5,  4);   // right eye socket
		p(g, DARK, 18,  5,  4,  3);   // nasal gap
		// jaw
		p(g, BSHD,13, 11, 14,  5);
		// teeth
		p(g, BONE, 14, 13,  2,  3);
		p(g, BONE, 17, 13,  2,  3);
		p(g, BONE, 20, 13,  2,  3);
		p(g, BONE, 23, 13,  2,  3);

		// --- neck ---
		p(g, BSHD,18, 16,  4,  3);

		// --- ribcage (stripes suggesting ribs) ---
		p(g, BONE, 12, 19, 16, 14);
		p(g, DARK, 14, 19,  2, 14);   // rib gaps
		p(g, DARK, 17, 19,  2, 14);
		p(g, DARK, 20, 19,  2, 14);
		p(g, DARK, 23, 19,  2, 14);

		// --- arms ---
		p(g, BONE,  5, 19,  8,  3);   // left arm upper
		p(g, BONE,  5, 22,  3,  8);   // left arm lower
		p(g, BONE, 27, 19,  8,  3);   // right arm
		p(g, BONE, 29, 22,  3,  8);

		// --- pelvis ---
		p(g, BSHD,14, 33, 12,  5);

		// --- leg bones ---
		p(g, BONE, 14, 38,  5, 10);
		p(g, BONE, 21, 38,  5, 10);

		// --- feet ---
		p(g, BSHD,13, 48,  7,  3);
		p(g, BSHD,20, 48,  7,  3);
	}

	// -------------------------------------------------------------------------
	// DRAGON -- front-facing boss, wings spread, fire breath
	// -------------------------------------------------------------------------
	private void drawDragon(Graphics2D g)
	{
		Color DRED  = new Color(160,  25,  25);
		Color BRED  = new Color(210,  55,  55);
		Color ORNGE = new Color(240, 130,  30);
		Color FIRE  = new Color(255, 220,  50);
		Color DGLD  = new Color(215, 175,  40);

		// --- wings (behind body) ---
		p(g, DRED,  0,  8, 11, 18);   // left wing
		p(g, DRED,  0,  6,  8,  4);
		p(g, DRED, 29,  8, 11, 18);   // right wing
		p(g, DRED, 32,  6,  8,  4);
		// wing detail (slightly lighter inner)
		p(g, BRED,  2, 10,  7, 12);
		p(g, BRED, 31, 10,  7, 12);

		// --- body ---
		p(g, DRED,  9, 22, 22, 16);
		p(g, BRED, 13, 24, 14, 10);   // belly (lighter)

		// --- tail ---
		p(g, DRED, 25, 36,  9,  3);
		p(g, DRED, 29, 39,  6,  3);
		p(g, DRED, 32, 42,  4,  3);

		// --- legs ---
		p(g, DRED, 11, 36,  8,  7);
		p(g, DRED, 21, 36,  8,  7);
		// claws
		p(g, new Color(200,195,185), 10, 43,  3,  2);
		p(g, new Color(200,195,185), 14, 43,  3,  2);
		p(g, new Color(200,195,185), 20, 43,  3,  2);
		p(g, new Color(200,195,185), 24, 43,  3,  2);

		// --- head ---
		p(g, DRED, 10,  8, 20, 14);
		// horns
		p(g, BRED, 12,  3,  4,  6);
		p(g, BRED, 24,  3,  4,  6);
		// snout/mouth area
		p(g, BRED, 12, 18, 16,  5);
		// mouth opening (dark)
		p(g, new Color(20,5,5), 13, 19, 14,  3);
		// teeth
		p(g, WHT,  14, 19,  2,  3);
		p(g, WHT,  17, 19,  2,  3);
		p(g, WHT,  20, 19,  2,  3);
		p(g, WHT,  23, 19,  2,  3);

		// --- eyes (gold) ---
		p(g, DGLD, 12, 11,  5,  4);
		p(g, DGLD, 23, 11,  5,  4);
		p(g, new Color(80,10,10), 13, 12,  3,  2);   // pupils
		p(g, new Color(80,10,10), 24, 12,  3,  2);
		p(g, WHT,  12, 11,  2,  1);   // eye shine
		p(g, WHT,  23, 11,  2,  1);

		// --- fire breath (below mouth) ---
		p(g, ORNGE,  9, 22,  6,  4);
		p(g, FIRE,  10, 23,  4,  3);
		p(g, ORNGE, 25, 22,  6,  4);
		p(g, FIRE,  26, 23,  4,  3);
	}

	// -------------------------------------------------------------------------
	// POTION -- cute red pixel-art health potion bottle
	// -------------------------------------------------------------------------
	private void drawPotion(Graphics2D g)
	{
		Color PTRED = new Color(200,  30,  40);
		Color LRED  = new Color(230,  80,  90);
		Color GLASS = new Color(210, 235, 245);
		Color CORK  = new Color(150, 105,  45);

		// --- glow effect (faint red behind bottle) ---
		p(g, new Color(120, 15, 20), 9, 18, 22, 30);
		p(g, new Color(80, 10, 15), 7, 22, 26, 22);

		// --- bottle body ---
		p(g, GLASS, 11, 22, 18, 24);   // glass outer
		p(g, GLASS,  9, 26, 22, 16);   // wider middle
		p(g, PTRED, 12, 24, 16, 20);   // red liquid inside
		p(g, PTRED,  10, 27, 20, 13);  // liquid wider
		p(g, LRED,  13, 26,  8,  4);   // liquid highlight top

		// --- neck ---
		p(g, GLASS, 16, 14,  8, 10);
		p(g, PTRED, 17, 15,  6,  8);   // liquid in neck

		// --- cork ---
		p(g, CORK,  15,  9, 10,  6);
		p(g, new Color(120,85,30), 15, 9, 10, 2);   // cork top shadow line

		// --- glass shine (white highlight) ---
		p(g, WHT,   13, 26,  2, 10);
		p(g, WHT,   13, 25,  4,  2);

		// --- health cross ---
		p(g, WHT,   19, 30,  2,  8);   // vertical
		p(g, WHT,   16, 33,  8,  2);   // horizontal

		// --- bottom of bottle ---
		p(g, GLASS, 11, 44, 18,  2);
	}
}
