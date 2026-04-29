package model;

/**
 * Fragile class with strong magic and burn damage over time.
 */
public class Mage extends Player {
    private static final int HEALTH = 70;
    private static final int MANA = 100;
    private static final int STAMINA = 45;
    private static final int DAMAGE = 10;

    public Mage() {
        super("Mage", HEALTH, MANA, STAMINA, DAMAGE);
        addSkill(new Skill("Fireball", 30, 25, RESOURCE_MANA, Skill.EFFECT_BURN));
        addItem(new Item(Item.MANA_POTION, 30));
    }
}
