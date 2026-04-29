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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;
import util.ColorPalette;

/**
 * Title screen with styled menu buttons.
 */
public class TitleScreenView extends JPanel {
    private static final int FADE_DELAY = 35;

    private final JButton newGameButton;
    private final JButton loadGameButton;
    private final JButton quitButton;
    private float fadeProgress;

    public TitleScreenView() {
        super(new BorderLayout());
        setOpaque(true);
        setBackground(ColorPalette.BACKGROUND_DARK);
        fadeProgress = 0.0f;

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        newGameButton = createMenuButton("NEW GAME");
        loadGameButton = createMenuButton("LOAD GAME");
        quitButton = createMenuButton("QUIT");

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.insets = new Insets(8, 0, 8, 0);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(newGameButton, constraints);
        buttonPanel.add(loadGameButton, constraints);
        buttonPanel.add(quitButton, constraints);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(260, 0, 100, 0));
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
        button.setPreferredSize(new Dimension(220, 48));
        button.setFocusPainted(false);
        button.setFont(new Font(ColorPalette.FONT_FAMILY, Font.BOLD, 16));
        button.setForeground(ColorPalette.TEXT_PRIMARY);
        button.setBackground(ColorPalette.PANEL_BG);
        button.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_GOLD, 2));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent event) {
                button.setBackground(ColorPalette.PANEL_BG_LIGHT);
                button.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_GOLD, 3));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent event) {
                button.setBackground(ColorPalette.PANEL_BG);
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
        g2.setColor(ColorPalette.BACKGROUND_DARK);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(new Color(212, 169, 92, 25));
        g2.fillOval(-120, 60, 260, 260);
        g2.fillOval(getWidth() - 160, getHeight() - 260, 280, 280);

        String title = "DUNGEON QUEST";
        g2.setFont(new Font(ColorPalette.FONT_FAMILY, Font.BOLD, 42));
        int textWidth = g2.getFontMetrics().stringWidth(title);
        int x = (getWidth() - textWidth) / 2;
        int y = 150;
        int alpha = Math.round(fadeProgress * 255);
        g2.setColor(new Color(0, 0, 0, Math.min(alpha, 150)));
        g2.drawString(title, x + 4, y + 4);
        g2.setColor(new Color(ColorPalette.BORDER_GOLD.getRed(), ColorPalette.BORDER_GOLD.getGreen(),
                ColorPalette.BORDER_GOLD.getBlue(), alpha));
        g2.drawString(title, x, y);
        g2.dispose();
    }
}
