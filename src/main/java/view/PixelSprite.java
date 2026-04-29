package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.Timer;
import util.ColorPalette;

/**
 * Renders tiny string-array sprites as scaled colored squares.
 */
public class PixelSprite extends JPanel {
    private static final int DEFAULT_CELL_SIZE = 10;
    private static final int FLASH_DURATION_MS = 220;
    private static final int LUNGE_DURATION_MS = 180;

    private String[] sprite;
    private int cellSize;
    private boolean damageFlash;
    private int offsetX;
    private int idleTick;
    private int shakeStep;
    private final Map<Character, Color> colorMap;

    public PixelSprite(String[] sprite, int cellSize) {
        this.sprite = sprite;
        this.cellSize = cellSize;
        this.colorMap = createColorMap();
        setOpaque(false);
        updatePreferredSize();
        Timer idleTimer = new Timer(90, event -> {
            idleTick++;
            repaint();
        });
        idleTimer.start();
    }

    public PixelSprite(String[] sprite) {
        this(sprite, DEFAULT_CELL_SIZE);
    }

    /**
     * Replaces the sprite data and recalculates size.
     *
     * @param sprite new sprite
     */
    public void setSprite(String[] sprite) {
        this.sprite = sprite;
        updatePreferredSize();
        repaint();
    }

    /**
     * Plays a short red flash and shake after damage.
     */
    public void playDamageFlash() {
        damageFlash = true;
        shakeStep = 0;
        Timer timer = new Timer(35, null);
        timer.addActionListener(event -> {
            shakeStep++;
            offsetX = shakeStep % 2 == 0 ? 4 : -4;
            if (shakeStep * 35 >= FLASH_DURATION_MS) {
                damageFlash = false;
                offsetX = 0;
                timer.stop();
            }
            repaint();
        });
        timer.start();
    }

    /**
     * Plays a small forward lunge for an attack.
     *
     * @param direction positive or negative x direction
     */
    public void playLunge(int direction) {
        final int[] tick = {0};
        Timer timer = new Timer(30, null);
        timer.addActionListener(event -> {
            tick[0]++;
            offsetX = tick[0] <= 3 ? direction * 8 : direction * 3;
            if (tick[0] * 30 >= LUNGE_DURATION_MS) {
                offsetX = 0;
                timer.stop();
            }
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (sprite == null) {
            return;
        }
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        int xBase = offsetX + Math.max(0, (getWidth() - maxColumns() * cellSize) / 2);
        int yBase = Math.max(0, (getHeight() - sprite.length * cellSize) / 2)
                + (int) Math.round(Math.sin(idleTick * 0.22) * 2);
        for (int row = 0; row < sprite.length; row++) {
            String line = sprite[row];
            for (int col = 0; col < line.length(); col++) {
                char key = line.charAt(col);
                if (key == ' ') {
                    continue;
                }
                Color color = damageFlash ? ColorPalette.HP_RED : colorMap.getOrDefault(key, ColorPalette.TEXT_PRIMARY);
                g2.setColor(color);
                g2.fillRect(xBase + col * cellSize, yBase + row * cellSize, cellSize, cellSize);
            }
        }
        g2.dispose();
    }

    private void updatePreferredSize() {
        int width = Math.max(1, maxColumns()) * cellSize + 24;
        int height = Math.max(1, sprite == null ? 1 : sprite.length) * cellSize + 24;
        setPreferredSize(new Dimension(width, height));
        revalidate();
    }

    private int maxColumns() {
        int max = 0;
        if (sprite != null) {
            for (String line : sprite) {
                max = Math.max(max, line.length());
            }
        }
        return max;
    }

    private Map<Character, Color> createColorMap() {
        Map<Character, Color> colors = new HashMap<>();
        colors.put('W', Color.decode("#E8E6D9"));
        colors.put('w', Color.decode("#BFC4CC"));
        colors.put('G', Color.decode("#4FA35D"));
        colors.put('g', Color.decode("#2F6B3D"));
        colors.put('R', Color.decode("#C43838"));
        colors.put('r', Color.decode("#7E1F1F"));
        colors.put('B', Color.decode("#4A90E2"));
        colors.put('b', Color.decode("#2E5D9F"));
        colors.put('M', Color.decode("#B36BE2"));
        colors.put('Y', ColorPalette.STAMINA_YELLOW);
        colors.put('K', Color.decode("#171717"));
        colors.put('S', Color.decode("#777777"));
        colors.put('P', Color.decode("#E3B08A"));
        colors.put('D', ColorPalette.BORDER_GOLD);
        colors.put('.', ColorPalette.BACKGROUND_DARK);
        return colors;
    }

    public static String[] heroBackSprite() {
        return new String[] {
            "   BBB   ",
            "  BBBBB  ",
            "  PBBBP  ",
            "   PPP   ",
            "  WPPPW  ",
            " WWWWWWW ",
            "   W W   "
        };
    }

    public static String[] warriorSprite() {
        return new String[] {
            "   WWW   ",
            "  WRRRW  ",
            " WRRRRRW ",
            "  PPPPP  ",
            " WWWRWWW ",
            "   RRR   ",
            "  W   W  "
        };
    }

    public static String[] mageSprite() {
        return new String[] {
            "   MMM   ",
            "  MMMMM  ",
            " MMMPMMM ",
            "   PPP   ",
            "  MMMMM  ",
            "  M B M  ",
            "  W   W  "
        };
    }

    public static String[] archerSprite() {
        return new String[] {
            "   GGG   ",
            "  GGGGG  ",
            "  GPPPG  ",
            "   PPP   ",
            "  GGGGG  ",
            " W  D  W ",
            "  W   W  "
        };
    }

    public static String[] ratSprite() {
        return new String[] {
            "        ",
            "  SSS   ",
            " SSSSS  ",
            " SS.SS  ",
            "  S S r "
        };
    }

    public static String[] goblinSprite() {
        return new String[] {
            "  G   G ",
            " GGGGGGG",
            " GG.K.GG",
            " GGGGGGG",
            "  ggggg ",
            "  G   G "
        };
    }

    public static String[] skeletonSprite() {
        return new String[] {
            "  WWW  ",
            " WWWWW ",
            "WW.K.WW",
            "WWWWWWW",
            " W W W ",
            " W   W "
        };
    }

    public static String[] dragonSprite() {
        return new String[] {
            "      RRRR      ",
            "  R  RRRRRR  R  ",
            " RRRRR.K.RRRRR  ",
            "RRRRRRRRRRRRRRR ",
            " rRRRRRRRRRRRr  ",
            "   RRRRRRRRR    ",
            "  RRR RRR RRR   ",
            " RRR       RRR  "
        };
    }
}
