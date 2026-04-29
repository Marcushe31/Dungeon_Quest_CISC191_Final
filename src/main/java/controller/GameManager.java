package controller;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.nio.file.Path;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import model.Battle;
import model.Dungeon;
import model.Enemy;
import model.Player;
import util.ColorPalette;
import util.SaveManager;
import view.BattleView;
import view.ClassSelectView;
import view.DungeonView;
import view.TitleScreenView;

/**
 * Coordinates the top-level flow and swaps between views.
 */
public class GameManager {
    private static final String TITLE_VIEW = "title";
    private static final String CLASS_VIEW = "classSelect";
    private static final String DUNGEON_VIEW = "dungeon";
    private static final String BATTLE_VIEW = "battle";
    private static final String RESULT_VIEW = "result";
    private static final String DEFAULT_SAVE = "savegame.txt";

    private Dungeon dungeon;
    private Player player;
    private DungeonView dungeonView;
    private BattleView battleView;
    private DungeonController dungeonController;
    private BattleController battleController;
    private JFrame mainFrame;
    private CardLayout cardLayout;

    private final JPanel cards;
    private final TitleScreenView titleScreenView;
    private final ClassSelectView classSelectView;
    private final JPanel resultPanel;
    private final JLabel resultLabel;
    private final JButton resultButton;

    public GameManager() {
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        titleScreenView = new TitleScreenView();
        classSelectView = new ClassSelectView();
        dungeonView = new DungeonView();
        battleView = new BattleView();
        resultPanel = new JPanel(new BorderLayout(12, 12));
        resultPanel.setBackground(ColorPalette.BACKGROUND_DARK);
        resultPanel.setBorder(BorderFactory.createEmptyBorder(120, 80, 120, 80));
        resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setFont(new Font(ColorPalette.FONT_FAMILY, Font.BOLD, 42));
        resultLabel.setForeground(ColorPalette.BORDER_GOLD);
        resultButton = new JButton("CONTINUE");
        styleResultButton();
        resultPanel.add(resultLabel, BorderLayout.CENTER);
        resultPanel.add(resultButton, BorderLayout.SOUTH);

        configureActions();
        cards.add(titleScreenView, TITLE_VIEW);
        cards.add(classSelectView, CLASS_VIEW);
        cards.add(dungeonView, DUNGEON_VIEW);
        cards.add(battleView, BATTLE_VIEW);
        cards.add(resultPanel, RESULT_VIEW);

        mainFrame = new JFrame("Dungeon Quest");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setMinimumSize(new Dimension(1000, 720));
        mainFrame.setLocationByPlatform(true);
        mainFrame.setJMenuBar(createMenuBar());
        mainFrame.add(cards);
        mainFrame.pack();
    }

    /**
     * Displays the application window.
     */
    public void show() {
        switchView(TITLE_VIEW);
        mainFrame.setVisible(true);
    }

    /**
     * Switches the main frame to a named card.
     *
     * @param viewName card name
     */
    public void switchView(String viewName) {
        cardLayout.show(cards, viewName);
    }

    /**
     * Starts or resumes dungeon exploration.
     */
    public void startDungeon() {
        if (player == null) {
            switchView(CLASS_VIEW);
            return;
        }
        if (dungeon == null) {
            dungeon = new Dungeon();
        }
        dungeonController = new DungeonController(dungeon, player, dungeonView, this);
        dungeonController.runDungeon();
        switchView(DUNGEON_VIEW);
    }

    /**
     * Starts a battle against the provided enemy.
     *
     * @param enemy enemy to fight
     */
    public void startBattle(Enemy enemy) {
        Battle battle = new Battle(player, enemy);
        battle.startBattle();
        battleController = new BattleController(battle, battleView, this);
        battleController.updateView();
        battleView.displayActions();
        switchView(BATTLE_VIEW);
    }

    /**
     * Continues the game after a battle result.
     *
     * @param victory whether the player won
     */
    public void onBattleEnd(boolean victory) {
        if (!victory) {
            onGameOver();
            return;
        }
        if (battleController != null && battleController.getBattle().getEnemy().isBoss()) {
            onVictory();
            return;
        }
        dungeon.advanceStage();
        startDungeon();
    }

    /**
     * Shows the defeat screen.
     */
    public void onGameOver() {
        showResult("DEFEAT", "RETURN TO TITLE", () -> switchView(TITLE_VIEW));
    }

    /**
     * Shows the victory screen.
     */
    public void onVictory() {
        showResult("VICTORY!", "RETURN TO TITLE", () -> switchView(TITLE_VIEW));
    }

    private void configureActions() {
        titleScreenView.setNewGameAction(() -> switchView(CLASS_VIEW));
        titleScreenView.setLoadGameAction(this::loadGame);
        titleScreenView.setQuitAction(() -> mainFrame.dispose());
        classSelectView.setClassSelectedListener(selectedPlayer -> {
            player = selectedPlayer;
            dungeon = new Dungeon();
            startDungeon();
        });
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenuItem save = new JMenuItem("Save Game");
        JMenuItem load = new JMenuItem("Load Game");
        JMenuItem title = new JMenuItem("Title Screen");
        save.addActionListener(event -> saveGame());
        load.addActionListener(event -> loadGame());
        title.addActionListener(event -> switchView(TITLE_VIEW));
        gameMenu.add(save);
        gameMenu.add(load);
        gameMenu.add(title);
        menuBar.add(gameMenu);
        return menuBar;
    }

    private void saveGame() {
        if (player == null || dungeon == null) {
            JOptionPane.showMessageDialog(mainFrame, "Start a game before saving.");
            return;
        }
        SaveManager.save(player, dungeon, DEFAULT_SAVE);
        JOptionPane.showMessageDialog(mainFrame, "Saved to " + Path.of(DEFAULT_SAVE).toAbsolutePath());
    }

    private void loadGame() {
        try {
            SaveManager.GameState state = SaveManager.load(DEFAULT_SAVE);
            player = state.getPlayer();
            dungeon = state.getDungeon();
            startDungeon();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(mainFrame, "No save file could be loaded yet.");
        }
    }

    private void showResult(String text, String buttonText, Runnable action) {
        resultLabel.setText(text);
        resultButton.setText(buttonText);
        for (java.awt.event.ActionListener listener : resultButton.getActionListeners()) {
            resultButton.removeActionListener(listener);
        }
        resultButton.addActionListener(event -> action.run());
        switchView(RESULT_VIEW);
    }

    private void styleResultButton() {
        resultButton.setFocusPainted(false);
        resultButton.setFont(new Font(ColorPalette.FONT_FAMILY, Font.BOLD, 18));
        resultButton.setForeground(ColorPalette.TEXT_PRIMARY);
        resultButton.setBackground(ColorPalette.PANEL_BG);
        resultButton.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_GOLD, 2));
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public Player getPlayer() {
        return player;
    }

    public BattleController getBattleController() {
        return battleController;
    }
}
