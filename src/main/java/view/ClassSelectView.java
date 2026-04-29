package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import model.Archer;
import model.Mage;
import model.Player;
import model.Warrior;
import util.ColorPalette;

/**
 * Class selection screen with three visual character cards.
 */
public class ClassSelectView extends JPanel {
    private Consumer<Player> classSelectedListener;

    public ClassSelectView() {
        super(new BorderLayout(18, 18));
        setBorder(BorderFactory.createEmptyBorder(28, 36, 36, 36));
        setBackground(ColorPalette.BACKGROUND_DARK);

        JLabel title = new JLabel("CHOOSE YOUR CLASS", SwingConstants.CENTER);
        title.setFont(new Font(ColorPalette.FONT_FAMILY, Font.BOLD, 28));
        title.setForeground(ColorPalette.BORDER_GOLD);
        add(title, BorderLayout.NORTH);

        JPanel cardPanel = new JPanel(new GridLayout(1, 3, 18, 0));
        cardPanel.setOpaque(false);
        cardPanel.add(new ClassCard("WARRIOR", "Rage: 2x damage", new Warrior(), PixelSprite.warriorSprite()));
        cardPanel.add(new ClassCard("MAGE", "Fireball: burn for 3 turns", new Mage(), PixelSprite.mageSprite()));
        cardPanel.add(new ClassCard("ARCHER", "Double Shot: attack twice", new Archer(), PixelSprite.archerSprite()));
        add(cardPanel, BorderLayout.CENTER);
    }

    /**
     * Receives the selected player class.
     *
     * @param listener selected-player listener
     */
    public void setClassSelectedListener(Consumer<Player> listener) {
        this.classSelectedListener = listener;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setPaint(new java.awt.GradientPaint(0, 0, ColorPalette.BACKGROUND_DARK, 0, getHeight(),
                Color.decode("#141D34")));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }

    private class ClassCard extends JPanel {
        private final Player previewPlayer;
        private boolean hovered;

        ClassCard(String title, String skillText, Player previewPlayer, String[] sprite) {
            super(new BorderLayout(8, 8));
            this.previewPlayer = previewPlayer;
            setOpaque(true);
            setBackground(ColorPalette.PANEL_BG);
            setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_GOLD, 2));
            setPreferredSize(new Dimension(240, 420));

            JLabel nameLabel = new JLabel(title, SwingConstants.CENTER);
            nameLabel.setFont(new Font(ColorPalette.FONT_FAMILY, Font.BOLD, 22));
            nameLabel.setForeground(ColorPalette.BORDER_GOLD);
            add(nameLabel, BorderLayout.NORTH);

            PixelSprite portrait = new PixelSprite(sprite, 12);
            add(portrait, BorderLayout.CENTER);

            JPanel stats = new JPanel(new GridLayout(5, 1, 4, 4));
            stats.setOpaque(false);
            stats.setBorder(BorderFactory.createEmptyBorder(0, 16, 18, 16));
            stats.add(makeStat("HP", previewPlayer.getMaxHealth(), 120, ColorPalette.HP_GREEN));
            stats.add(makeStat("MANA", previewPlayer.getMaxMana(), 100, ColorPalette.MANA_BLUE));
            stats.add(makeStat("STAMINA", previewPlayer.getMaxStamina(), 80, ColorPalette.STAMINA_YELLOW));
            stats.add(makeStat("DAMAGE", previewPlayer.getDamage(), 20, ColorPalette.BORDER_GOLD));
            JLabel skill = new JLabel(skillText, SwingConstants.CENTER);
            skill.setFont(new Font(ColorPalette.FONT_FAMILY, Font.PLAIN, 12));
            skill.setForeground(ColorPalette.TEXT_PRIMARY);
            stats.add(skill);
            add(stats, BorderLayout.SOUTH);

            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent event) {
                    if (classSelectedListener != null) {
                        classSelectedListener.accept(createFreshPlayer(previewPlayer.getCharacterClass()));
                    }
                }

                @Override
                public void mouseEntered(java.awt.event.MouseEvent event) {
                    hovered = true;
                    setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_GOLD, 4));
                    repaint();
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent event) {
                    hovered = false;
                    setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_GOLD, 2));
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            if (hovered) {
                Graphics2D g2 = (Graphics2D) graphics.create();
                g2.setColor(new Color(212, 169, 92, 40));
                g2.fillRoundRect(6, 6, getWidth() - 12, getHeight() - 12, 10, 10);
                g2.dispose();
            }
        }
    }

    private JPanel makeStat(String label, int value, int maximum, Color color) {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        panel.setOpaque(false);
        JLabel statLabel = new JLabel(label);
        statLabel.setFont(new Font(ColorPalette.FONT_FAMILY, Font.BOLD, 12));
        statLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        HPBar bar = new HPBar("", value, maximum, color, false);
        bar.setPreferredSize(new Dimension(120, 18));
        panel.add(statLabel, BorderLayout.WEST);
        panel.add(bar, BorderLayout.CENTER);
        return panel;
    }

    private Player createFreshPlayer(String className) {
        return switch (className) {
            case "Warrior" -> new Warrior();
            case "Mage" -> new Mage();
            case "Archer" -> new Archer();
            default -> new Warrior();
        };
    }
}
