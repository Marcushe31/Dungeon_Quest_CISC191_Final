package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base player model shared by all playable character classes.
 */
public class Player implements Attackable {
    public static final String RESOURCE_MANA = "MANA";
    public static final String RESOURCE_STAMINA = "STAMINA";

    private String characterClass;
    private int health;
    private int maxHealth;
    private int mana;
    private int maxMana;
    private int stamina;
    private int maxStamina;
    private int damage;
    private List<Item> inventory;
    private List<Skill> skills;

    /**
     * Creates a flexible player. Subclasses provide the real class identities.
     *
     * @param characterClass visible class name
     * @param maxHealth maximum health
     * @param maxMana maximum mana
     * @param maxStamina maximum stamina
     * @param damage base damage
     */
    public Player(String characterClass, int maxHealth, int maxMana, int maxStamina, int damage) {
        this.characterClass = characterClass;
        this.maxHealth = Math.max(1, maxHealth);
        this.health = this.maxHealth;
        this.maxMana = Math.max(0, maxMana);
        this.mana = this.maxMana;
        this.maxStamina = Math.max(0, maxStamina);
        this.stamina = this.maxStamina;
        this.damage = Math.max(0, damage);
        this.inventory = new ArrayList<>();
        this.skills = new ArrayList<>();
    }

    /**
     * Creates a balanced default hero for tests and loading fallbacks.
     */
    public Player() {
        this("Hero", 100, 50, 50, 10);
    }

    @Override
    public void attack() {
        // Damage is resolved by BattleController so GUI and logs stay coordinated.
    }

    /**
     * Reduces health and clamps it at zero.
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
     * Returns a skill by index with normal Java bounds behavior.
     *
     * @param index skill index
     * @return selected skill
     */
    public Skill getSkill(int index) {
        return skills.get(index);
    }

    /**
     * Adds a skill to the player.
     *
     * @param skill skill to add
     */
    public void addSkill(Skill skill) {
        if (skill != null) {
            skills.add(skill);
        }
    }

    /**
     * Adds an item to the inventory.
     *
     * @param item item to add
     */
    public void addItem(Item item) {
        if (item != null) {
            inventory.add(item);
        }
    }

    /**
     * Removes an item by index after it has been used.
     *
     * @param index item index
     * @return removed item
     */
    public Item removeItem(int index) {
        return inventory.remove(index);
    }

    /**
     * Spends mana if enough is available.
     *
     * @param amount mana cost
     * @return true when payment succeeded
     */
    public boolean spendMana(int amount) {
        if (amount <= 0) {
            return true;
        }
        if (mana < amount) {
            return false;
        }
        mana -= amount;
        return true;
    }

    /**
     * Spends stamina if enough is available.
     *
     * @param amount stamina cost
     * @return true when payment succeeded
     */
    public boolean spendStamina(int amount) {
        if (amount <= 0) {
            return true;
        }
        if (stamina < amount) {
            return false;
        }
        stamina -= amount;
        return true;
    }

    /**
     * Restores health without exceeding max health.
     *
     * @param amount amount to restore
     */
    public void restoreHealth(int amount) {
        health = Math.min(maxHealth, health + Math.max(0, amount));
    }

    /**
     * Restores mana without exceeding max mana.
     *
     * @param amount amount to restore
     */
    public void restoreMana(int amount) {
        mana = Math.min(maxMana, mana + Math.max(0, amount));
    }

    /**
     * Restores stamina without exceeding max stamina.
     *
     * @param amount amount to restore
     */
    public void restoreStamina(int amount) {
        stamina = Math.min(maxStamina, stamina + Math.max(0, amount));
    }

    /**
     * Permanently increases base damage.
     *
     * @param amount amount to add
     */
    public void increaseDamage(int amount) {
        damage += Math.max(0, amount);
    }

    public String getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(String characterClass) {
        this.characterClass = characterClass;
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

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = Math.max(0, Math.min(maxMana, mana));
    }

    public int getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = Math.max(0, maxMana);
        this.mana = Math.min(mana, this.maxMana);
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = Math.max(0, Math.min(maxStamina, stamina));
    }

    public int getMaxStamina() {
        return maxStamina;
    }

    public void setMaxStamina(int maxStamina) {
        this.maxStamina = Math.max(0, maxStamina);
        this.stamina = Math.min(stamina, this.maxStamina);
    }

    @Override
    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = Math.max(0, damage);
    }

    public List<Item> getInventory() {
        return new ArrayList<>(inventory);
    }

    public void setInventory(List<Item> inventory) {
        this.inventory = inventory == null ? new ArrayList<>() : new ArrayList<>(inventory);
    }

    public List<Skill> getSkills() {
        return Collections.unmodifiableList(new ArrayList<>(skills));
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills == null ? new ArrayList<>() : new ArrayList<>(skills);
    }

    /**
     * Serializes core state in a compact line-friendly format.
     *
     * @return save-ready player summary
     */
    @Override
    public String toString() {
        return characterClass + "|" + health + "|" + maxHealth + "|" + mana + "|" + maxMana + "|"
                + stamina + "|" + maxStamina + "|" + damage;
    }
}
