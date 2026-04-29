package model;

/**
 * Final boss with a special attack every third turn.
 */
public class Dragon extends Enemy {
    private static final int FIRE_BREATH_INTERVAL = 3;
    private int attackCounter;

    public Dragon() {
        super("Dragon", 200, 25, true);
    }

    @Override
    public void attack() {
        attackCounter++;
    }

    @Override
    public int getDamage() {
        if (isFireBreathReady()) {
            return (int) Math.round(super.getDamage() * 1.5);
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
