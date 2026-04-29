package model;

/**
 * Shared combat contract for anything that can give and receive damage.
 */
public interface Attackable {
    /**
     * Marks that this actor has taken an attack action.
     */
    void attack();

    /**
     * Applies incoming damage to this actor.
     *
     * @param damage amount of damage to apply
     */
    void receiveDamage(int damage);

    /**
     * Returns this actor's base attack damage.
     *
     * @return attack damage
     */
    int getDamage();

    /**
     * Returns this actor's current health.
     *
     * @return current health
     */
    int getHealth();
}
