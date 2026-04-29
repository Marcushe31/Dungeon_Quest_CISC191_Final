package model;

/**
 * Final boss with a special attack every fourth turn.
 */
public class Dragon extends Enemy {
    private static final int FIRE_BREATH_INTERVAL = 4;
    private static final double FIRE_BREATH_MULTIPLIER = 1.35;
    private int attackCounter;

    public Dragon() {
        super("Dragon", 150, 17, true);
    }

    @Override
    public void attack() {
        attackCounter++;
    }

    @Override
    public int getDamage() {
        if (isFireBreathReady()) {
            return (int) Math.round(super.getDamage() * FIRE_BREATH_MULTIPLIER);
        }
        return super.getDamage();
    }

    @Override
    public String getAttackName() {
        return isFireBreathReady() ? "Fire Breath" : "Claw Swipe";
    }

    public boolean isFireBreathReady() {
        return attackCounter > 0 && attackCounter % FIRE_BREATH_INTERVAL == 0;
    }

    public int getAttackCounter() {
        return attackCounter;
    }
}
