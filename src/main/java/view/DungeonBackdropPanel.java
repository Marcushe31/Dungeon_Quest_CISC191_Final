package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;
import util.ColorPalette;

/**
 * Animated dungeon background shared by the major screens.
 */
public class DungeonBackdropPanel extends JPanel {
    private static final int TIMER_DELAY = 45;
    private static final int EMBER_COUNT = 36;
    private static final int BRICK_HEIGHT = 38;
    private static final int BRICK_WIDTH = 92;

    public enum Mood {
        TITLE,
        DUNGEON,
        BATTLE,
        RESULT
    }

    private final Mood mood;
    private final float[] emberX;
    private final float[] emberY;
    private final float[] emberSpeed;
    private final int[] emberSize;
    private int animationTick;

    public DungeonBackdropPanel(LayoutManager layout, Mood mood) {
        super(layout);
        this.mood = mood;
        this.emberX = new float[EMBER_COUNT];
        this.emberY = new float[EMBER_COUNT];
        this.emberSpeed = new float[EMBER_COUNT];
        this.emberSize = new int[EMBER_COUNT];
        setOpaque(true);
        seedEmbers();
        Timer timer = new Timer(TIMER_DELAY, event -> {
            animationTick++;
            moveEmbers();
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintGradient(g2);
        paintStoneBlocks(g2);
        paintDepth(g2);
        paintTorches(g2);
        paintEmbers(g2);
        paintVignette(g2);
        g2.dispose();
    }

    private void paintGradient(Graphics2D g2) {
        Color floorColor = switch (mood) {
            case TITLE -> Color.decode("#17111D");
            case DUNGEON -> Color.decode("#171F2D");
            case BATTLE -> Color.decode("#201525");
            case RESULT -> Color.decode("#121520");
        };
        g2.setPaint(new GradientPaint(0, 0, ColorPalette.BACKGROUND_DARK, 0, getHeight(), floorColor));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    private void paintStoneBlocks(Graphics2D g2) {
        g2.setStroke(new BasicStroke(1f));
        for (int y = 0; y < getHeight(); y += BRICK_HEIGHT) {
            int offset = (y / BRICK_HEIGHT) % 2 == 0 ? 0 : BRICK_WIDTH / 2;
            for (int x = -offset; x < getWidth(); x += BRICK_WIDTH) {
                int shade = 18 + Math.floorMod(x + y, 18);
                g2.setColor(new Color(shade, shade + 7, shade + 18, 88));
                g2.fillRect(x, y, BRICK_WIDTH - 2, BRICK_HEIGHT - 2);
                g2.setColor(new Color(212, 169, 92, 18));
                g2.drawRect(x, y, BRICK_WIDTH - 2, BRICK_HEIGHT - 2);
            }
        }
    }

    private void paintDepth(Graphics2D g2) {
        int archWidth = Math.max(360, getWidth() / 2);
        int archHeight = Math.max(260, getHeight() / 2);
        int archX = (getWidth() - archWidth) / 2;
        int archY = 62;
        g2.setColor(new Color(0, 0, 0, 92));
        g2.fillRoundRect(archX, archY, archWidth, archHeight, 180, 180);
        g2.setColor(new Color(212, 169, 92, 36));
        g2.setStroke(new BasicStroke(3f));
        g2.drawRoundRect(archX + 8, archY + 8, archWidth - 16, archHeight - 16, 170, 170);

        int floorY = (int) (getHeight() * 0.72);
        g2.setPaint(new GradientPaint(0, floorY, new Color(48, 38, 36, 120), 0, getHeight(),
                new Color(5, 7, 12, 210)));
        g2.fillRect(0, floorY, getWidth(), getHeight() - floorY);
        g2.setColor(new Color(212, 169, 92, 28));
        for (int i = 0; i < 9; i++) {
            int y = floorY + i * 28;
            g2.drawLine(getWidth() / 2 - i * 48, y, getWidth() / 2 + i * 48, y);
        }
    }

    private void paintTorches(Graphics2D g2) {
        int leftX = Math.max(56, getWidth() / 10);
        int rightX = getWidth() - leftX;
        int y = Math.max(115, getHeight() / 4);
        paintTorch(g2, leftX, y);
        paintTorch(g2, rightX, y);
    }

    private void paintTorch(Graphics2D g2, int x, int y) {
        float pulse = (float) (0.74 + Math.sin(animationTick * 0.18 + x) * 0.16);
        int radius = Math.round(138 * pulse);
        float[] distances = {0.0f, 0.55f, 1.0f};
        Color[] colors = {
            new Color(244, 158, 54, 92),
            new Color(212, 92, 32, 42),
            new Color(0, 0, 0, 0)
        };
        g2.setPaint(new RadialGradientPaint(new Point2D.Float(x, y), radius, distances, colors));
        g2.fillOval(x - radius, y - radius, radius * 2, radius * 2);

        g2.setColor(new Color(72, 46, 36));
        g2.fillRoundRect(x - 5, y + 18, 10, 44, 5, 5);
        g2.setColor(ColorPalette.BORDER_GOLD);
        g2.fillRect(x - 18, y + 16, 36, 7);
        g2.setColor(new Color(255, 205, 92));
        g2.fillOval(x - 9, y - 12, 18, 32);
        g2.setColor(new Color(232, 84, 42));
        g2.fillOval(x - 5, y - 4, 10, 21);
    }

    private void paintEmbers(Graphics2D g2) {
        for (int i = 0; i < EMBER_COUNT; i++) {
            int x = Math.round(emberX[i] * Math.max(1, getWidth()));
            int y = Math.round(emberY[i] * Math.max(1, getHeight()));
            int alpha = 70 + (int) (Math.sin(animationTick * 0.12 + i) * 35);
            g2.setColor(new Color(240, 160, 48, Math.max(20, alpha)));
            g2.fillOval(x, y, emberSize[i], emberSize[i]);
        }
    }

    private void paintVignette(Graphics2D g2) {
        int radius = Math.max(1, Math.max(getWidth(), getHeight()));
        float[] distances = {0.0f, 0.72f, 1.0f};
        Color[] colors = {
            new Color(0, 0, 0, 0),
            new Color(0, 0, 0, 35),
            new Color(0, 0, 0, 185)
        };
        g2.setPaint(new RadialGradientPaint(new Point2D.Float(getWidth() / 2f, getHeight() / 2f),
                radius, distances, colors));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    private void seedEmbers() {
        Random random = new Random(191);
        for (int i = 0; i < EMBER_COUNT; i++) {
            emberX[i] = random.nextFloat();
            emberY[i] = random.nextFloat();
            emberSpeed[i] = 0.0015f + random.nextFloat() * 0.0035f;
            emberSize[i] = 2 + random.nextInt(4);
        }
    }

    private void moveEmbers() {
        for (int i = 0; i < EMBER_COUNT; i++) {
            emberY[i] -= emberSpeed[i];
            emberX[i] += Math.sin(animationTick * 0.05 + i) * 0.0008f;
            if (emberY[i] < -0.04f) {
                emberY[i] = 1.04f;
                emberX[i] = Math.floorMod(i * 37 + animationTick, 100) / 100f;
            }
        }
    }
}
