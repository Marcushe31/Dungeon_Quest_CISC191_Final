package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;
import util.ColorPalette;

/**
 * Title screen with styled menu buttons.
 */
public class TitleScreenView extends DungeonBackdropPanel {
    private static final int FADE_DELAY = 35;

    private final JButton newGameButton;
    private final JButton loadGameButton;
    private final JButton quitButton;
    private float fadeProgress;

    public TitleScreenView() {
        super(new BorderLayout(), Mood.TITLE);
        fadeProgress = 0.0f;

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        newGameButton = createMenuButton("NEW GAME");
        loadGameButton = createMenuButton("LOAD GAME");
        quitButton = createMenuButton("QUIT");

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.insets = new Insets(9, 0, 9, 0);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(newGameButton, constraints);
        buttonPanel.add(loadGameButton, constraints);
        buttonPanel.add(quitButton, constraints);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(310, 0, 88, 0));
        add(buttonPanel, BorderLayout.CENTER);
        startFadeIn();
    }

    /**
     * Hooks up the new-game button.
     *
     * @param action action to run
     */
    public void setNewGameAction(Runnable action) {
        newGameButton.addActionListener(event -> action.run());
    }

    /**
     * Hooks up the load-game button.
     *
     * @param action action to run
     */
    public void setLoadGameAction(Runnable action) {
        loadGameButton.addActionListener(event -> action.run());
    }

    /**
     * Hooks up the quit button.
     *
     * @param action action to run
     */
    public void setQuitAction(Runnable action) {
        quitButton.addActionListener(event -> action.run());
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(248, 50));
        button.setFocusPainted(false);
        button.setFont(new Font(ColorPalette.FONT_FAMILY, Font.BOLD, 17));
        button.setForeground(ColorPalette.TEXT_PRIMARY);
        button.setBackground(ColorPalette.PANEL_OVERLAY);
        button.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_GOLD, 2));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent event) {
                button.setBackground(ColorPalette.PANEL_BG_LIGHT);
                button.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_GOLD, 3));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent event) {
                button.setBackground(ColorPalette.PANEL_OVERLAY);
                button.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_GOLD, 2));
            }
        });
        return button;
    }

    private void startFadeIn() {
        Timer timer = new Timer(FADE_DELAY, null);
        timer.addActionListener(event -> {
            fadeProgress = Math.min(1.0f, fadeProgress + 0.05f);
            repaint();
            if (fadeProgress >= 1.0f) {
                timer.stop();
            }
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintTitleSigil(g2);

        String title = "DUNGEON QUEST";
        g2.setFont(new Font(ColorPalette.FONT_FAMILY, Font.BOLD, 54));
        int textWidth = g2.getFontMetrics().stringWidth(title);
        int x = (getWidth() - textWidth) / 2;
        int y = 168;
        int alpha = Math.round(fadeProgress * 255);
        g2.setColor(new Color(0, 0, 0, Math.min(alpha, 150)));
        g2.drawString(title, x + 5, y + 5);
        g2.setColor(new Color(ColorPalette.BORDER_GOLD.getRed(), ColorPalette.BORDER_GOLD.getGreen(),
                ColorPalette.BORDER_GOLD.getBlue(), alpha));
        g2.drawString(title, x, y);
        g2.setFont(new Font(ColorPalette.FONT_FAMILY, Font.BOLD, 15));
        String subtitle = "ENTER THE DEEP HALLS";
        int subtitleWidth = g2.getFontMetrics().stringWidth(subtitle);
        g2.setColor(new Color(ColorPalette.TEXT_PRIMARY.getRed(), ColorPalette.TEXT_PRIMARY.getGreen(),
                ColorPalette.TEXT_PRIMARY.getBlue(), Math.round(fadeProgress * 210)));
        g2.drawString(subtitle, (getWidth() - subtitleWidth) / 2, y + 42);
        g2.dispose();
    }

    private void paintTitleSigil(Graphics2D g2) {
        int centerX = getWidth() / 2;
        int topY = 84;
        g2.setColor(new Color(212, 169, 92, 35));
        g2.setStroke(new java.awt.BasicStroke(3f));
        g2.drawRoundRect(centerX - 315, topY - 28, 630, 150, 30, 30);
        g2.setColor(new Color(0, 0, 0, 86));
        g2.fillRoundRect(centerX - 300, topY - 18, 600, 132, 28, 28);

        Path2D blade = new Path2D.Double();
        blade.moveTo(centerX, topY - 18);
        blade.lineTo(centerX + 16, topY + 68);
        blade.lineTo(centerX, topY + 104);
        blade.lineTo(centerX - 16, topY + 68);
        blade.closePath();
        g2.setColor(new Color(232, 230, 217, 58));
        g2.fill(blade);
        g2.setColor(new Color(212, 169, 92, 88));
        g2.drawLine(centerX - 58, topY + 70, centerX + 58, topY + 70);
    }
}
