package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import model.Battle;
import model.Dragon;
import model.Enemy;
import model.Goblin;
import model.Item;
import model.Player;
import model.Rat;
import model.Skill;
import model.Skeleton;
import util.ColorPalette;

/**
 * Pokemon-style battle screen and action interface.
 */
public class BattleView extends JPanel {
    private final JLabel enemyNameLabel;
    private final JLabel playerNameLabel;
    private final JLabel promptLabel;
    private final HPBar enemyHpBar;
    private final HPBar playerHpBar;
    private final HPBar manaBar;
    private final HPBar staminaBar;
    private final PixelSprite enemySprite;
    private final PixelSprite playerSprite;
    private final BattleLog battleLog;
    private final JPanel actionPanel;
    private Consumer<String> actionHandler;
    private Battle currentBattle;

    public BattleView() {
        super(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
        setBackground(ColorPalette.BACKGROUND_DARK);

        JPanel scene = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics graphics) {
                Graphics2D g2 = (Graphics2D) graphics.create();
                g2.setPaint(new java.awt.GradientPaint(0, 0, Color.decode("#11182A"), 0, getHeight(),
                        Color.decode("#202746")));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(212, 169, 92, 24));
                g2.fillOval(-80, getHeight() - 160, 300, 220);
                g2.fillOval(getWidth() - 260, 40, 260, 190);
                g2.dispose();
                super.paintComponent(graphics);
            }
        };
        scene.setOpaque(false);
        scene.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_GOLD, 2));

        JPanel enemyPanel = createStatusPanel();
        enemyNameLabel = createLabel("ENEMY", 16, Font.BOLD);
        enemyHpBar = new HPBar("HP", 1, 1, ColorPalette.HP_GREEN, true);
        enemySprite = new PixelSprite(PixelSprite.skeletonSprite(), 10);
        enemyPanel.add(enemyNameLabel, BorderLayout.NORTH);
        enemyPanel.add(enemyHpBar, BorderLayout.CENTER);
        enemyPanel.add(enemySprite, BorderLayout.SOUTH);

        JPanel playerPanel = createStatusPanel();
        playerNameLabel = createLabel("HERO", 16, Font.BOLD);
        playerHpBar = new HPBar("HP", 1, 1, ColorPalette.HP_GREEN, true);
        manaBar = new HPBar("MP", 1, 1, ColorPalette.MANA_BLUE, false);
        staminaBar = new HPBar("ST", 1, 1, ColorPalette.STAMINA_YELLOW, false);
        playerSprite = new PixelSprite(PixelSprite.heroBackSprite(), 10);
        JPanel playerBars = new JPanel(new GridLayout(3, 1, 0, 4));
        playerBars.setOpaque(false);
        playerBars.add(playerHpBar);
        playerBars.add(manaBar);
        playerBars.add(staminaBar);
        playerPanel.add(playerNameLabel, BorderLayout.NORTH);
        playerPanel.add(playerSprite, BorderLayout.CENTER);
        playerPanel.add(playerBars, BorderLayout.SOUTH);

        GridBagConstraints enemyConstraints = new GridBagConstraints();
        enemyConstraints.gridx = 1;
        enemyConstraints.gridy = 0;
        enemyConstraints.anchor = GridBagConstraints.NORTHEAST;
        enemyConstraints.weightx = 1;
        enemyConstraints.weighty = 1;
        enemyConstraints.insets = new Insets(12, 12, 12, 28);
        scene.add(enemyPanel, enemyConstraints);

        GridBagConstraints playerConstraints = new GridBagConstraints();
        playerConstraints.gridx = 0;
        playerConstraints.gridy = 1;
        playerConstraints.anchor = GridBagConstraints.SOUTHWEST;
        playerConstraints.weightx = 1;
        playerConstraints.weighty = 1;
        playerConstraints.insets = new Insets(12, 28, 20, 12);
        scene.add(playerPanel, playerConstraints);
        add(scene, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(12, 12));
        bottom.setOpaque(false);
        JPanel promptPanel = new JPanel(new BorderLayout());
        promptPanel.setPreferredSize(new Dimension(360, 150));
        promptPanel.setBackground(ColorPalette.PANEL_BG);
        promptPanel.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_GOLD, 2));
        promptLabel = createLabel("What will HERO do?", 20, Font.BOLD);
        promptLabel.setHorizontalAlignment(SwingConstants.CENTER);
        promptPanel.add(promptLabel, BorderLayout.CENTER);

        actionPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        actionPanel.setOpaque(false);
        displayActions();

        battleLog = new BattleLog();
        JPanel commandRow = new JPanel(new GridLayout(1, 2, 12, 0));
        commandRow.setOpaque(false);
        commandRow.add(promptPanel);
        commandRow.add(actionPanel);
        bottom.add(commandRow, BorderLayout.NORTH);
        bottom.add(battleLog, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    /**
     * Rebuilds the primary action grid.
     */
    public void displayActions() {
        actionPanel.removeAll();
        actionPanel.add(createActionButton("FIGHT", () -> sendAction("FIGHT")));
        actionPanel.add(createActionButton("BAG", this::showItemMenu));
        actionPanel.add(createActionButton("SKILLS", this::showSkillMenu));
        actionPanel.add(createActionButton("BLOCK", () -> sendAction("BLOCK")));
        actionPanel.revalidate();
        actionPanel.repaint();
    }

    /**
     * Refreshes battle state on the screen.
     *
     * @param battle battle model
     */
    public void displayBattle(Battle battle) {
        currentBattle = battle;
        if (battle == null) {
            return;
        }
        Player player = battle.getPlayer();
        Enemy enemy = battle.getEnemy();
        enemyNameLabel.setText(enemy.getEnemyType().toUpperCase() + "  LV." + battle.getTurnNumber());
        playerNameLabel.setText(player.getCharacterClass().toUpperCase());
        promptLabel.setText("What will " + player.getCharacterClass().toUpperCase() + " do?");
        enemyHpBar.setValue(enemy.getHealth(), enemy.getMaxHealth());
        playerHpBar.setValue(player.getHealth(), player.getMaxHealth());
        manaBar.setValue(player.getMana(), player.getMaxMana());
        staminaBar.setValue(player.getStamina(), player.getMaxStamina());
        enemySprite.setSprite(spriteForEnemy(enemy));
        playerSprite.setSprite(spriteForPlayer(player));
        battleLog.setEntries(battle.getBattleLog());
    }

    /**
     * Shows a battle end state in the command panel.
     *
     * @param outcome outcome text
     * @param continueAction action for continue button
     */
    public void showEndState(String outcome, Runnable continueAction) {
        promptLabel.setText(outcome);
        actionPanel.removeAll();
        actionPanel.add(createActionButton("CONTINUE", continueAction));
        actionPanel.revalidate();
        actionPanel.repaint();
    }

    /**
     * Lets the controller receive battle commands.
     *
     * @param actionHandler action consumer
     */
    public void setActionHandler(Consumer<String> actionHandler) {
        this.actionHandler = actionHandler;
    }

    public void flashEnemy() {
        enemySprite.playDamageFlash();
    }

    public void flashPlayer() {
        playerSprite.playDamageFlash();
    }

    public void lungePlayer() {
        playerSprite.playLunge(1);
    }

    public void lungeEnemy() {
        enemySprite.playLunge(-1);
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setOpaque(false);
        return panel;
    }

    private JLabel createLabel(String text, int size, int style) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(ColorPalette.FONT_FAMILY, style, size));
        label.setForeground(ColorPalette.TEXT_PRIMARY);
        return label;
    }

    private JButton createActionButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font(ColorPalette.FONT_FAMILY, Font.BOLD, 16));
        button.setForeground(ColorPalette.TEXT_PRIMARY);
        button.setBackground(ColorPalette.PANEL_BG);
        button.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_GOLD, 2));
        button.addActionListener(event -> action.run());
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

    private void showSkillMenu() {
        if (currentBattle == null) {
            return;
        }
        List<Skill> skills = currentBattle.getPlayer().getSkills();
        JPopupMenu menu = new JPopupMenu();
        for (int i = 0; i < skills.size(); i++) {
            Skill skill = skills.get(i);
            JMenuItem item = new JMenuItem(skill.toString());
            int index = i;
            item.addActionListener(event -> sendAction("SKILL_" + index));
            menu.add(item);
        }
        menu.show(actionPanel, 0, actionPanel.getHeight() / 2);
    }

    private void showItemMenu() {
        if (currentBattle == null) {
            return;
        }
        List<Item> items = currentBattle.getPlayer().getInventory();
        JPopupMenu menu = new JPopupMenu();
        if (items.isEmpty()) {
            JMenuItem empty = new JMenuItem("Bag is empty");
            empty.setEnabled(false);
            menu.add(empty);
        }
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            JMenuItem menuItem = new JMenuItem(item.getDisplayName());
            int index = i;
            menuItem.addActionListener(event -> sendAction("ITEM_" + index));
            menu.add(menuItem);
        }
        menu.show(actionPanel, actionPanel.getWidth() / 2, actionPanel.getHeight() / 2);
    }

    private void sendAction(String action) {
        if (actionHandler != null) {
            actionHandler.accept(action);
        }
    }

    private String[] spriteForEnemy(Enemy enemy) {
        if (enemy instanceof Dragon) {
            return PixelSprite.dragonSprite();
        }
        if (enemy instanceof Skeleton) {
            return PixelSprite.skeletonSprite();
        }
        if (enemy instanceof Goblin) {
            return PixelSprite.goblinSprite();
        }
        if (enemy instanceof Rat) {
            return PixelSprite.ratSprite();
        }
        return PixelSprite.skeletonSprite();
    }

    private String[] spriteForPlayer(Player player) {
        return switch (player.getCharacterClass()) {
            case "Warrior" -> PixelSprite.warriorSprite();
            case "Mage" -> PixelSprite.mageSprite();
            case "Archer" -> PixelSprite.archerSprite();
            default -> PixelSprite.heroBackSprite();
        };
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(ColorPalette.BACKGROUND_DARK);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }
}
