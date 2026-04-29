import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import model.Dragon;
import model.Goblin;
import model.Rat;
import model.Skeleton;
import org.junit.jupiter.api.Test;

class EnemyTest {
    @Test
    void ratHasWeakStartingStats() {
        Rat rat = new Rat();

        assertEquals(25, rat.getMaxHealth());
        assertEquals(5, rat.getDamage());
        assertFalse(rat.isBoss());
    }

    @Test
    void goblinHasMiddleStats() {
        Goblin goblin = new Goblin();

        assertEquals(45, goblin.getMaxHealth());
        assertEquals(10, goblin.getDamage());
    }

    @Test
    void skeletonHasHigherHealthThanGoblin() {
        Skeleton skeleton = new Skeleton();

        assertEquals(60, skeleton.getMaxHealth());
        assertEquals(12, skeleton.getDamage());
    }

    @Test
    void enemyDamageClampsAtZero() {
        Skeleton skeleton = new Skeleton();

        skeleton.receiveDamage(999);

        assertEquals(0, skeleton.getHealth());
    }

    @Test
    void dragonUsesFireBreathEveryFourthAttack() {
        Dragon dragon = new Dragon();

        dragon.attack();
        dragon.attack();
        dragon.attack();
        dragon.attack();

        assertTrue(dragon.isBoss());
        assertEquals("Fire Breath", dragon.getAttackName());
        assertEquals(23, dragon.getDamage());
    }
}
