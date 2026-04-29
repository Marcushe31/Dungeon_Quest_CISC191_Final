package view;

import java.awt.BorderLayout;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.Timer;
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
public class BattleView extends DungeonBackdropPanel {
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
    private final JPanel scenePanel;
    private Consumer<String> actionHandler;
    private Battle currentBattle;
    private int sceneTick;
    private int attackEffectTicks;
    private boolean attackFromPlayer;

    public BattleView() {
        super(new BorderLayout(12, 12), Mood.BATTLE);
        setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

        scenePanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics graphics) {
                Graphics2D g2 = (Graphics2D) graphics.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                paintBattleScene(g2, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(graphics);
            }
        };
        scenePanel.setOpaque(false);
        scenePanel.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_GOLD, 2));

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
        scenePanel.add(enemyPanel, enemyConstraints);

        GridBagConstraints playerConstraints = new GridBagConstraints();
        playerConstraints.gridx = 0;
        playerConstraints.gridy = 1;
        playerConstraints.anchor = GridBagConstraints.SOUTHWEST;
        playerConstraints.weightx = 1;
        playerConstraints.weighty = 1;
        playerConstraints.insets = new Insets(12, 28, 20, 12);
        scenePanel.add(playerPanel, playerConstraints);
        add(scenePanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(12, 12));
        bottom.setOpaque(false);
        JPanel promptPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics graphics) {
                Graphics2D g2 = (Graphics2D) graphics.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ColorPalette.PANEL_OVERLAY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(212, 169, 92, 34));
                g2.fillRoundRect(8, 8, getWidth() - 16, getHeight() - 16, 8, 8);
                g2.dispose();
                super.paintComponent(graphics);
            }
        };
        promptPanel.setOpaque(false);
        promptPanel.setPreferredSize(new Dimension(360, 150));
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

        Timer sceneTimer = new Timer(50, event -> {
            sceneTick++;
            scenePanel.repaint();
        });
        sceneTimer.start();
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
        playAttackEffect(true);
        playerSprite.playLunge(1);
    }

    public void lungeEnemy() {
        playAttackEffect(false);
        enemySprite.playLunge(-1);
    }

    private void playAttackEffect(boolean fromPlayer) {
        attackFromPlayer = fromPlayer;
        attackEffectTicks = 9;
        Timer timer = new Timer(28, null);
        timer.addActionListener(event -> {
            attackEffectTicks--;
            scenePanel.repaint();
            if (attackEffectTicks <= 0) {
                timer.stop();
            }
        });
        timer.start();
    }

    private void paintBattleScene(Graphics2D g2, int width, int height) {
        g2.setPaint(new GradientPaint(0, 0, Color.decode("#111320"), 0, height, Color.decode("#271927")));
        g2.fillRect(0, 0, width, height);
        paintSceneGlow(g2, width, height);
        paintStoneFloor(g2, width, height);
        paintRuneCircle(g2, width - 210, 155, 108);
        paintRuneCircle(g2, 205, height - 115, 118);
        paintHangingChains(g2, width);
        paintAttackEffect(g2, width, height);
    }

    private void paintSceneGlow(Graphics2D g2, int width, int height) {
        float pulse = (float) (0.78 + Math.sin(sceneTick * 0.16) * 0.12);
        Color[] colors = {
            new Color(255, 147, 55, Math.round(80 * pulse)),
            new Color(157, 60, 43, 35),
            new Color(0, 0, 0, 0)
        };
        float[] distances = {0f, 0.45f, 1f};
        g2.setPaint(new RadialGradientPaint(new Point2D.Float(width - 120, 95), 230, distances, colors));
        g2.fillOval(width - 350, -120, 460, 430);
        g2.setPaint(new RadialGradientPaint(new Point2D.Float(120, height - 90), 250, distances, colors));
        g2.fillOval(-120, height - 310, 480, 430);
    }

    private void paintStoneFloor(Graphics2D g2, int width, int height) {
        int floorY = (int) (height * 0.62);
        g2.setPaint(new GradientPaint(0, floorY, new Color(65, 58, 58, 135), 0, height,
                new Color(7, 8, 13, 215)));
        g2.fillRect(0, floorY, width, height - floorY);
        g2.setColor(new Color(212, 169, 92, 32));
        g2.setStroke(new BasicStroke(1.4f));
        for (int y = floorY + 18; y < height; y += 32) {
            g2.drawLine(18, y, width - 18, y);
        }
        for (int x = -40; x < width + 80; x += 88) {
            g2.drawLine(x, floorY + 8, x - 70, height);
        }
    }

    private void paintRuneCircle(Graphics2D g2, int centerX, int centerY, int radius) {
        g2.setColor(new Color(212, 169, 92, 46));
        g2.setStroke(new BasicStroke(3f));
        g2.drawOval(centerX - radius / 2, centerY - radius / 4, radius, radius / 2);
        g2.setColor(new Color(74, 144, 226, 32));
        g2.fillOval(centerX - radius / 2, centerY - radius / 4, radius, radius / 2);
    }

    private void paintHangingChains(Graphics2D g2, int width) {
        g2.setColor(new Color(139, 139, 139, 80));
        for (int x = 80; x < width; x += 180) {
            for (int y = -10; y < 110; y += 18) {
                g2.drawOval(x, y, 8, 18);
            }
        }
    }

    private void paintAttackEffect(Graphics2D g2, int width, int height) {
        if (attackEffectTicks <= 0) {
            return;
        }
        int alpha = Math.min(210, attackEffectTicks * 24);
        g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(255, 211, 106, alpha));
        if (attackFromPlayer) {
            g2.drawLine(260, height - 165, width - 245, 145);
            g2.drawLine(280, height - 145, width - 220, 165);
        } else {
            g2.drawLine(width - 245, 145, 265, height - 162);
            g2.drawLine(width - 220, 165, 285, height - 145);
        }
        g2.setColor(new Color(232, 84, 42, Math.max(0, alpha - 60)));
        g2.fillOval(width / 2 - 14, height / 2 - 14, 28, 28);
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
        button.setBackground(ColorPalette.PANEL_OVERLAY);
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
                button.setBackground(ColorPalette.PANEL_OVERLAY);
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
        g2.setColor(new Color(0, 0, 0, 70));
        g2.fillRoundRect(14, 14, getWidth() - 28, getHeight() - 28, 18, 18);
        g2.dispose();
    }
}
