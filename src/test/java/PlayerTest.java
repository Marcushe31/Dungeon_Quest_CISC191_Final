import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import exception.InsufficientResourceException;
import model.Archer;
import model.Battle;
import model.Goblin;
import model.Item;
import model.Mage;
import model.Player;
import model.Skill;
import model.Warrior;
import org.junit.jupiter.api.Test;

class PlayerTest {
    @Test
    void receiveDamageClampsHealthAtZero() {
        Player player = new Player("Test", 20, 10, 10, 4);

        player.receiveDamage(99);

        assertEquals(0, player.getHealth());
    }

    @Test
    void warriorStartsWithExpectedStatsAndRage() {
        Warrior warrior = new Warrior();

        assertEquals(120, warrior.getMaxHealth());
        assertEquals(30, warrior.getMaxMana());
        assertEquals("Rage", warrior.getSkill(0).getName());
    }

    @Test
    void mageStartsWithFireballAndHighMana() {
        Mage mage = new Mage();

        assertEquals(70, mage.getMaxHealth());
        assertEquals(100, mage.getMaxMana());
        assertEquals("Fireball", mage.getSkill(0).getName());
    }

    @Test
    void archerStartsWithDoubleShot() {
        Archer archer = new Archer();

        assertEquals(90, archer.getMaxHealth());
        assertEquals("Double Shot", archer.getSkill(0).getName());
    }

    @Test
    void healthPotionCannotHealPastMaximum() {
        Warrior warrior = new Warrior();
        warrior.receiveDamage(10);

        new Item(Item.HEALTH_POTION, 100).use(warrior);

        assertEquals(warrior.getMaxHealth(), warrior.getHealth());
    }

    @Test
    void damageBoostIncreasesBaseDamage() {
        Archer archer = new Archer();
        int startingDamage = archer.getDamage();

        new Item(Item.DAMAGE_BOOST, 3).use(archer);

        assertEquals(startingDamage + 3, archer.getDamage());
    }

    @Test
    void fireballThrowsWhenManaIsMissing() {
        Mage mage = new Mage();
        mage.setMana(0);
        Skill fireball = mage.getSkill(0);

        assertThrows(InsufficientResourceException.class,
                () -> fireball.activate(mage, new Goblin(), new Battle(mage, new Goblin())));
    }
}
