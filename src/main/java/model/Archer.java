package model;

/**
 * Balanced class that can spend stamina to attack twice.
 */
public class Archer extends Player {
    private static final int HEALTH = 90;
    private static final int MANA = 50;
    private static final int STAMINA = 70;
    private static final int DAMAGE = 13;

    public Archer() {
        super("Archer", HEALTH, MANA, STAMINA, DAMAGE);
        addSkill(new Skill("Double Shot", 0, 15, RESOURCE_STAMINA, Skill.EFFECT_DOUBLE_HIT));
        addItem(new Item(Item.STAMINA_POTION, 25));
    }
}
