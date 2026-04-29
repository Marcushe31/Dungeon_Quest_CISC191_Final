package model;

/**
 * Base enemy model for all dungeon encounters.
 */
public class Enemy implements Attackable {
    private String enemyType;
    private int health;
    private int maxHealth;
    private int damage;
    private boolean boss;

    public Enemy(String enemyType, int maxHealth, int damage, boolean boss) {
        this.enemyType = enemyType;
        this.maxHealth = Math.max(1, maxHealth);
        this.health = this.maxHealth;
        this.damage = Math.max(0, damage);
        this.boss = boss;
    }

    @Override
    public void attack() {
        // Subclasses can update attack state before damage is read.
    }

    /**
     * Reduces enemy health and clamps it at zero.
     *
     * @param damage incoming damage
     */
    @Override
    public void receiveDamage(int damage) {
        if (damage <= 0) {
            return;
        }
        health = Math.max(0, health - damage);
    }

    /**
     * Gives the current attack name for battle logs.
     *
     * @return attack name
     */
    public String getAttackName() {
        return "Attack";
    }

    public String getEnemyType() {
        return enemyType;
    }

    public void setEnemyType(String enemyType) {
        this.enemyType = enemyType;
    }

    @Override
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(maxHealth, health));
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = Math.max(1, maxHealth);
        this.health = Math.min(health, this.maxHealth);
    }

    @Override
    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = Math.max(0, damage);
    }

    public boolean isBoss() {
        return boss;
    }

    public void setBoss(boolean boss) {
        this.boss = boss;
    }

    @Override
    public String toString() {
        return enemyType + "|" + health + "|" + maxHealth + "|" + damage + "|" + boss;
    }
}
