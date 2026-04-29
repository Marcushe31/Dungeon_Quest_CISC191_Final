import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import controller.BattleController;
import exception.InvalidActionException;
import model.Battle;
import model.Goblin;
import model.Mage;
import model.Player;
import model.Rat;
import model.Skill;
import model.Warrior;
import org.junit.jupiter.api.Test;

class BattleTest {
    @Test
    void startBattleActivatesAndLogsEncounter() {
        Battle battle = new Battle(new Warrior(), new Rat());

        battle.startBattle();

        assertTrue(battle.isActive());
        assertEquals(1, battle.getTurnNumber());
        assertFalse(battle.getBattleLog().isEmpty());
    }

    @Test
    void fightDamagesEnemyAndEnemyResponds() {
        Warrior warrior = new Warrior();
        Rat rat = new Rat();
        Battle battle = new Battle(warrior, rat);
        battle.startBattle();
        BattleController controller = new BattleController(battle, null, null);

        controller.handlePlayerAction("FIGHT");

        assertEquals(9, rat.getHealth());
        assertEquals(115, warrior.getHealth());
    }

    @Test
    void blockHalvesEnemyDamage() {
        Warrior warrior = new Warrior();
        Rat rat = new Rat();
        Battle battle = new Battle(warrior, rat);
        battle.startBattle();
        BattleController controller = new BattleController(battle, null, null);

        controller.handlePlayerAction("BLOCK");

        assertEquals(117, warrior.getHealth());
    }

    @Test
    void skillAppliesBurnEffect() {
        Mage mage = new Mage();
        Goblin goblin = new Goblin();
        Battle battle = new Battle(mage, goblin);
        battle.startBattle();
        BattleController controller = new BattleController(battle, null, null);

        controller.handlePlayerAction("SKILL_0");

        assertTrue(battle.getActiveEffects().containsKey(Skill.EFFECT_BURN));
        assertEquals(15, goblin.getHealth());
    }

    @Test
    void burnTicksAtStartOfNextPlayerAction() {
        Mage mage = new Mage();
        Goblin goblin = new Goblin();
        Battle battle = new Battle(mage, goblin);
        battle.startBattle();
        BattleController controller = new BattleController(battle, null, null);

        controller.handlePlayerAction("SKILL_0");
        controller.handlePlayerAction("BLOCK");

        assertEquals(10, goblin.getHealth());
    }

    @Test
    void invalidActionThrowsCustomException() {
        Battle battle = new Battle(new Warrior(), new Rat());
        battle.startBattle();
        BattleController controller = new BattleController(battle, null, null);

        assertThrows(InvalidActionException.class, () -> controller.handlePlayerAction("DANCE"));
    }

    @Test
    void victoryOutcomeStopsBattle() {
        Player player = new Player("Hero", 100, 10, 10, 100);
        Battle battle = new Battle(player, new Rat());
        battle.startBattle();
        BattleController controller = new BattleController(battle, null, null);

        controller.handlePlayerAction("FIGHT");

        assertFalse(battle.isActive());
        assertEquals(Battle.OUTCOME_VICTORY, battle.getOutcome());
    }
}
