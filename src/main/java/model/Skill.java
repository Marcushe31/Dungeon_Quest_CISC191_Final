package model;

import exception.InsufficientResourceException;

/**
 * Player ability that can spend mana or stamina to damage an enemy.
 */
public class Skill {
    public static final String EFFECT_BURN = "BURN";
    public static final String EFFECT_DOUBLE_HIT = "DOUBLE_HIT";
    public static final String EFFECT_RAGE = "RAGE";

    private static final int BURN_TURNS = 3;

    private String name;
    private int damage;
    private int cost;
    private String resourceType;
    private String specialEffect;

    public Skill(String name, int damage, int cost, String resourceType, String specialEffect) {
        this.name = name;
        this.damage = damage;
        this.cost = cost;
        this.resourceType = resourceType;
        this.specialEffect = specialEffect;
    }

    /**
     * Pays the resource cost, applies skill damage, and registers any effect.
     *
     * @param user player using the skill
     * @param target enemy target
     * @param battle battle for logs and effects
     */
    public void activate(Player user, Enemy target, Battle battle) {
        if (user == null || target == null || battle == null) {
            return;
        }
        payCost(user);
        int dealt = calculateDamage(user);
        target.receiveDamage(dealt);
        battle.addLogEntry(user.getCharacterClass() + " used " + name + " for " + dealt + " damage!");
        if (EFFECT_BURN.equals(specialEffect)) {
            battle.applyEffect(EFFECT_BURN, BURN_TURNS);
            battle.addLogEntry(target.getEnemyType() + " is burning.");
        }
    }

    private void payCost(Player user) {
        boolean paid;
        if (Player.RESOURCE_MANA.equals(resourceType)) {
            paid = user.spendMana(cost);
        } else if (Player.RESOURCE_STAMINA.equals(resourceType)) {
            paid = user.spendStamina(cost);
        } else {
            paid = true;
        }
        if (!paid) {
            throw new InsufficientResourceException("Not enough " + resourceType.toLowerCase() + " to use " + name + ".");
        }
    }

    private int calculateDamage(Player user) {
        if (EFFECT_RAGE.equals(specialEffect)) {
            return user.getDamage() * 2;
        }
        if (EFFECT_DOUBLE_HIT.equals(specialEffect)) {
            return user.getDamage() * 2;
        }
        return damage > 0 ? damage : user.getDamage();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getSpecialEffect() {
        return specialEffect;
    }

    public void setSpecialEffect(String specialEffect) {
        this.specialEffect = specialEffect;
    }

    @Override
    public String toString() {
        return name + " (" + cost + " " + resourceType + ")";
    }
}
