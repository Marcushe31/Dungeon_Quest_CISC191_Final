package model;

/**
 * High-health class with a stamina-powered rage strike.
 */
public class Warrior extends Player {
    private static final int HEALTH = 120;
    private static final int MANA = 30;
    private static final int STAMINA = 80;
    private static final int DAMAGE = 16;

    public Warrior() {
        super("Warrior", HEALTH, MANA, STAMINA, DAMAGE);
        addSkill(new Skill("Rage", 0, 20, RESOURCE_STAMINA, Skill.EFFECT_RAGE));
        addItem(new Item(Item.HEALTH_POTION, 30));
    }
}
