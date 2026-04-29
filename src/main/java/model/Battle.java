package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Model object that stores battle state while the controller runs the turns.
 */
public class Battle {
    public static final String OUTCOME_NONE = "IN_PROGRESS";
    public static final String OUTCOME_VICTORY = "VICTORY";
    public static final String OUTCOME_DEFEAT = "DEFEAT";

    private static final int BURN_DAMAGE = 5;

    private boolean active;
    private Player player;
    private Enemy enemy;
    private String outcome;
    private int turnNumber;
    private List<String> battleLog;
    private Map<String, Integer> activeEffects;

    public Battle(Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
        this.outcome = OUTCOME_NONE;
        this.battleLog = new ArrayList<>();
        this.activeEffects = new HashMap<>();
    }

    /**
     * Starts the battle and resets public-facing progress fields.
     */
    public void startBattle() {
        active = true;
        outcome = OUTCOME_NONE;
        turnNumber = 1;
        battleLog.clear();
        activeEffects.clear();
        addLogEntry("A " + enemy.getEnemyType().toUpperCase() + " appears!");
    }

    /**
     * Marks the battle as finished.
     */
    public void endBattle() {
        active = false;
    }

    /**
     * Adds a visible entry to the battle log.
     *
     * @param entry text to show
     */
    public void addLogEntry(String entry) {
        if (entry != null && !entry.isBlank()) {
            battleLog.add(entry);
        }
    }

    /**
     * Applies damage-over-time effects and decreases their durations.
     */
    public void tickEffects() {
        Map<String, Integer> updatedEffects = new HashMap<>();
        for (Map.Entry<String, Integer> entry : activeEffects.entrySet()) {
            String effect = entry.getKey();
            int turnsLeft = entry.getValue();
            if (Skill.EFFECT_BURN.equals(effect) && turnsLeft > 0 && enemy.getHealth() > 0) {
                enemy.receiveDamage(BURN_DAMAGE);
                addLogEntry(enemy.getEnemyType() + " takes " + BURN_DAMAGE + " burn damage.");
            }
            if (turnsLeft - 1 > 0) {
                updatedEffects.put(effect, turnsLeft - 1);
            }
        }
        activeEffects = updatedEffects;
    }

    /**
     * Adds or refreshes an active status effect.
     *
     * @param effect effect key
     * @param turns number of turns
     */
    public void applyEffect(String effect, int turns) {
        if (effect != null && turns > 0) {
            activeEffects.put(effect, turns);
        }
    }

    /**
     * Advances the turn counter after a full round.
     */
    public void advanceTurn() {
        turnNumber++;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = Math.max(1, turnNumber);
    }

    public List<String> getBattleLog() {
        return new ArrayList<>(battleLog);
    }

    public Map<String, Integer> getActiveEffects() {
        return new HashMap<>(activeEffects);
    }
}
