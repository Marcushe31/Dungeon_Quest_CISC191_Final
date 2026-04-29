package model;

/**
 * Consumable item used from the battle bag or dungeon rewards.
 */
public class Item {
    public static final String HEALTH_POTION = "HEALTH_POTION";
    public static final String MANA_POTION = "MANA_POTION";
    public static final String STAMINA_POTION = "STAMINA_POTION";
    public static final String DAMAGE_BOOST = "DAMAGE_BOOST";

    private String type;
    private int effect;

    public Item(String type, int effect) {
        this.type = type;
        this.effect = effect;
    }

    /**
     * Applies this item's effect to the target player.
     *
     * @param target player receiving the effect
     */
    public void use(Player target) {
        if (target == null) {
            return;
        }
        switch (type) {
            case HEALTH_POTION -> target.restoreHealth(effect);
            case MANA_POTION -> target.restoreMana(effect);
            case STAMINA_POTION -> target.restoreStamina(effect);
            case DAMAGE_BOOST -> target.increaseDamage(effect);
            default -> throw new IllegalArgumentException("Unknown item type: " + type);
        }
    }

    public String getDisplayName() {
        return type == null ? "Unknown Item" : type.replace('_', ' ');
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getEffect() {
        return effect;
    }

    public void setEffect(int effect) {
        this.effect = effect;
    }

    @Override
    public String toString() {
        return type + ":" + effect;
    }
}
