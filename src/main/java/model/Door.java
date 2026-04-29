package model;

/**
 * A dungeon choice that can reveal a fight, reward, or empty room.
 */
public class Door {
    public static final String ENCOUNTER = "ENCOUNTER";
    public static final String REWARD = "REWARD";
    public static final String NOTHING = "NOTHING";

    private String eventType;
    private Enemy enemy;
    private Item item;

    public Door(String eventType, Enemy enemy, Item item) {
        this.eventType = eventType;
        this.enemy = enemy;
        this.item = item;
    }

    /**
     * Reveals the hidden result behind this door.
     *
     * @return flavor text for the view
     */
    public String reveal() {
        if (ENCOUNTER.equals(eventType) && enemy != null) {
            return "A " + enemy.getEnemyType() + " lunges from the shadows!";
        }
        if (REWARD.equals(eventType) && item != null) {
            return "You found a " + item.getDisplayName() + ".";
        }
        return "The room is quiet. Too quiet.";
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
