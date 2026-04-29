package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.Timer;
import util.ColorPalette;

/**
 * Animated resource bar used for HP, mana, and stamina.
 */
public class HPBar extends JComponent {
    private static final int DEFAULT_WIDTH = 220;
    private static final int DEFAULT_HEIGHT = 26;
    private static final int ANIMATION_DELAY = 16;
    private static final int ANIMATION_STEP = 3;

    private String label;
    private int value;
    private int displayValue;
    private int maximum;
    private Color fixedColor;
    private boolean useHpColors;
    private Timer animationTimer;

    public HPBar(String label, int value, int maximum, Color fixedColor, boolean useHpColors) {
        this.label = label;
        this.value = Math.max(0, value);
        this.displayValue = this.value;
        this.maximum = Math.max(1, maximum);
        this.fixedColor = fixedColor;
        this.useHpColors = useHpColors;
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        setMinimumSize(new Dimension(120, DEFAULT_HEIGHT));
        setOpaque(false);
    }

    /**
     * Updates the bar and smoothly animates toward the new value.
     *
     * @param newValue target value
     * @param newMaximum target maximum
     */
    public void setValue(int newValue, int newMaximum) {
        maximum = Math.max(1, newMaximum);
        value = Math.max(0, Math.min(maximum, newValue));
        startAnimation();
    }

    private void startAnimation() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        animationTimer = new Timer(ANIMATION_DELAY, event -> {
            if (displayValue == value) {
                animationTimer.stop();
                return;
            }
            int direction = displayValue < value ? 1 : -1;
            displayValue += direction * Math.min(ANIMATION_STEP, Math.abs(displayValue - value));
            repaint();
        });
        animationTimer.start();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int arc = 8;
        g2.setColor(ColorPalette.BACKGROUND_DARK);
        g2.fillRoundRect(0, 0, width, height, arc, arc);

        double ratio = Math.max(0.0, Math.min(1.0, displayValue / (double) maximum));
        int fillWidth = (int) Math.round((width - 4) * ratio);
        g2.setColor(resolveBarColor(ratio));
        g2.fillRoundRect(2, 2, fillWidth, height - 4, arc, arc);

        g2.setColor(ColorPalette.BORDER_GOLD);
        g2.drawRoundRect(0, 0, width - 1, height - 1, arc, arc);

        String text = label + " " + value + "/" + maximum;
        g2.setFont(new Font(ColorPalette.FONT_FAMILY, Font.BOLD, 12));
        FontMetrics metrics = g2.getFontMetrics();
        int x = Math.max(6, (width - metrics.stringWidth(text)) / 2);
        int y = (height + metrics.getAscent() - metrics.getDescent()) / 2;
        g2.setColor(ColorPalette.SHADOW);
        g2.drawString(text, x + 1, y + 1);
        g2.setColor(ColorPalette.TEXT_PRIMARY);
        g2.drawString(text, x, y);
        g2.dispose();
    }

    private Color resolveBarColor(double ratio) {
        if (!useHpColors) {
            return fixedColor;
        }
        if (ratio > 0.5) {
            return ColorPalette.HP_GREEN;
        }
        if (ratio > 0.25) {
            return ColorPalette.HP_YELLOW;
        }
        return ColorPalette.HP_RED;
    }
}
