package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.util.function.IntConsumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import model.Dungeon;
import model.Player;
import util.ColorPalette;

/**
 * Dungeon exploration screen with three door choices.
 */
public class DungeonView extends JPanel {
    private final JLabel playerLabel;
    private final JLabel stageLabel;
    private final JLabel flavorLabel;
    private final JLabel inventoryLabel;
    private final HPBar hpBar;
    private final HPBar manaBar;
    private final HPBar staminaBar;
    private final JButton[] doorButtons;
    private IntConsumer doorListener;

    public DungeonView() {
        super(new BorderLayout(18, 18));
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        setBackground(ColorPalette.BACKGROUND_DARK);

        JPanel top = new JPanel(new BorderLayout(12, 8));
        top.setOpaque(false);
        playerLabel = createLabel("HERO", 15, Font.BOLD);
        stageLabel = createLabel("STAGE 1", 15, Font.BOLD);
        top.add(playerLabel, BorderLayout.WEST);
        top.add(stageLabel, BorderLayout.EAST);

        JPanel bars = new JPanel(new GridLayout(1, 3, 12, 0));
        bars.setOpaque(false);
        hpBar = new HPBar("HP", 100, 100, ColorPalette.HP_GREEN, true);
        manaBar = new HPBar("MP", 50, 50, ColorPalette.MANA_BLUE, false);
        staminaBar = new HPBar("ST", 50, 50, ColorPalette.STAMINA_YELLOW, false);
        bars.add(hpBar);
        bars.add(manaBar);
        bars.add(staminaBar);
        top.add(bars, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(18, 18));
        center.setOpaque(false);
        flavorLabel = createLabel("Stage 1: The Dusty Threshold", 22, Font.BOLD);
        flavorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        center.add(flavorLabel, BorderLayout.NORTH);

        JPanel doors = new JPanel(new GridLayout(1, 3, 18, 0));
        doors.setOpaque(false);
        doorButtons = new JButton[3];
        for (int i = 0; i < doorButtons.length; i++) {
            final int index = i;
            doorButtons[i] = createDoorButton("DOOR " + (i + 1));
            doorButtons[i].addActionListener(event -> {
                if (doorListener != null) {
                    doorListener.accept(index);
                }
            });
            doors.add(doorButtons[i]);
        }
        center.add(doors, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        inventoryLabel = createLabel("Inventory: 0 items", 14, Font.PLAIN);
        inventoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        inventoryLabel.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_GOLD));
        inventoryLabel.setPreferredSize(new Dimension(100, 42));
        add(inventoryLabel, BorderLayout.SOUTH);
    }

    /**
     * Updates the visible dungeon state.
     *
     * @param player current player
     * @param dungeon current dungeon
     * @param message latest room message
     */
    public void update(Player player, Dungeon dungeon, String message) {
        if (player == null || dungeon == null) {
            return;
        }
        playerLabel.setText(player.getCharacterClass().toUpperCase());
        stageLabel.setText("STAGE " + dungeon.getStage() + "/" + dungeon.getMaxStages());
        flavorLabel.setText(message == null ? stageText(dungeon.getStage()) : message);
        inventoryLabel.setText("Inventory: " + player.getInventory().size() + " items");
        hpBar.setValue(player.getHealth(), player.getMaxHealth());
        manaBar.setValue(player.getMana(), player.getMaxMana());
        staminaBar.setValue(player.getStamina(), player.getMaxStamina());
    }

    /**
     * Wires door buttons to the controller.
     *
     * @param doorListener index listener
     */
    public void setDoorListener(IntConsumer doorListener) {
        this.doorListener = doorListener;
    }

    private JLabel createLabel(String text, int size, int style) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(ColorPalette.FONT_FAMILY, style, size));
        label.setForeground(ColorPalette.TEXT_PRIMARY);
        return label;
    }

    private JButton createDoorButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font(ColorPalette.FONT_FAMILY, Font.BOLD, 18));
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

    private String stageText(int stage) {
        return switch (stage) {
            case 1 -> "Stage 1: The Dusty Threshold";
            case 2 -> "Stage 2: The Damp Corridor";
            case 3 -> "Stage 3: The Bone Hall";
            case 4 -> "Stage 4: The Treasure Vault";
            case 5 -> "Stage 5: The Dragon Gate";
            default -> "The dungeon shifts around you.";
        };
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(new java.awt.GradientPaint(0, 0, ColorPalette.BACKGROUND_DARK, 0, getHeight(),
                Color.decode("#18213B")));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(new Color(212, 169, 92, 32));
        g2.fillOval(30, 160, 220, 220);
        g2.fillOval(getWidth() - 260, 120, 220, 220);
        g2.dispose();
        super.paintComponent(graphics);
    }
}
