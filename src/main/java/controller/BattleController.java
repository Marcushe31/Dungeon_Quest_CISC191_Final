package controller;

import exception.InvalidActionException;
import exception.InsufficientResourceException;
import model.Battle;
import model.Item;
import model.Skill;
import view.BattleView;

/**
 * Runs battle turns and keeps battle state separate from the Swing view.
 */
public class BattleController {
    private Battle battle;
    private BattleView battleView;
    private GameManager gameManager;
    private boolean blockNextAttack;

    public BattleController(Battle battle, BattleView battleView, GameManager gameManager) {
        this.battle = battle;
        this.battleView = battleView;
        this.gameManager = gameManager;
        if (battleView != null) {
            battleView.setActionHandler(this::handlePlayerAction);
        }
    }

    /**
     * Refreshes the view for the current turn.
     */
    public void turn() {
        updateView();
    }

    /**
     * Handles a player command and advances the battle if the action succeeds.
     *
     * @param action FIGHT, BLOCK, SKILL_n, or ITEM_n
     */
    public void handlePlayerAction(String action) {
        if (battle == null || !battle.isActive()) {
            return;
        }
        tickEffectsBeforeAction();
        if (checkBattleEnd()) {
            return;
        }
        try {
            if ("FIGHT".equals(action)) {
                handleFight();
            } else if ("BLOCK".equals(action)) {
                handleBlock();
            } else if (action != null && action.startsWith("SKILL_")) {
                handleSkill(parseIndex(action, "SKILL_"));
            } else if (action != null && action.startsWith("ITEM_")) {
                handleItem(parseIndex(action, "ITEM_"));
            } else {
                throw new InvalidActionException("Unknown battle action: " + action);
            }
        } catch (InsufficientResourceException ex) {
            battle.addLogEntry(ex.getMessage());
            updateView();
            return;
        }

        if (!checkBattleEnd()) {
            enemyTurn();
            if (!checkBattleEnd()) {
                battle.advanceTurn();
                updateView();
            }
        }
    }

    /**
     * Runs the enemy response turn.
     */
    public void enemyTurn() {
        battle.getEnemy().attack();
        int damage = battle.getEnemy().getDamage();
        if (blockNextAttack) {
            damage = (int) Math.ceil(damage * 0.5);
            blockNextAttack = false;
            battle.addLogEntry("Block softened the incoming hit.");
        }
        battle.getPlayer().receiveDamage(damage);
        battle.addLogEntry(battle.getEnemy().getEnemyType() + " used " + battle.getEnemy().getAttackName()
                + " for " + damage + " damage!");
        if (battleView != null) {
            battleView.lungeEnemy();
            battleView.flashPlayer();
        }
    }

    /**
     * Pushes model state into the view.
     */
    public void updateView() {
        if (battleView != null) {
            battleView.displayBattle(battle);
        }
    }

    /**
     * Checks win/loss conditions.
     *
     * @return true if the battle is over
     */
    public boolean checkBattleEnd() {
        if (battle.getEnemy().getHealth() <= 0) {
            battle.setOutcome(Battle.OUTCOME_VICTORY);
            battle.addLogEntry("VICTORY!");
            battle.endBattle();
            updateView();
            if (battleView != null) {
                battleView.showEndState("VICTORY!", () -> {
                    if (gameManager != null) {
                        gameManager.onBattleEnd(true);
                    }
                });
            }
            return true;
        }
        if (battle.getPlayer().getHealth() <= 0) {
            battle.setOutcome(Battle.OUTCOME_DEFEAT);
            battle.addLogEntry("DEFEAT");
            battle.endBattle();
            updateView();
            if (battleView != null) {
                battleView.showEndState("DEFEAT", () -> {
                    if (gameManager != null) {
                        gameManager.onBattleEnd(false);
                    }
                });
            }
            return true;
        }
        return false;
    }

    private void handleFight() {
        battle.getPlayer().attack();
        int damage = battle.getPlayer().getDamage();
        battle.getEnemy().receiveDamage(damage);
        battle.addLogEntry(battle.getPlayer().getCharacterClass() + " attacked for " + damage + " damage!");
        if (battleView != null) {
            battleView.lungePlayer();
            battleView.flashEnemy();
        }
    }

    private void handleBlock() {
        blockNextAttack = true;
        battle.addLogEntry(battle.getPlayer().getCharacterClass() + " braced for impact.");
    }

    private void handleSkill(int index) {
        Skill skill = battle.getPlayer().getSkill(index);
        skill.activate(battle.getPlayer(), battle.getEnemy(), battle);
        if (battleView != null) {
            battleView.lungePlayer();
            battleView.flashEnemy();
        }
    }

    private void handleItem(int index) {
        Item item = battle.getPlayer().removeItem(index);
        item.use(battle.getPlayer());
        battle.addLogEntry(battle.getPlayer().getCharacterClass() + " used " + item.getDisplayName() + ".");
    }

    private void tickEffectsBeforeAction() {
        if (!battle.getActiveEffects().isEmpty()) {
            battle.tickEffects();
            updateView();
        }
    }

    private int parseIndex(String action, String prefix) {
        try {
            return Integer.parseInt(action.substring(prefix.length()));
        } catch (NumberFormatException ex) {
            throw new InvalidActionException("Invalid indexed action: " + action);
        }
    }

    public Battle getBattle() {
        return battle;
    }

    public BattleView getBattleView() {
        return battleView;
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}
