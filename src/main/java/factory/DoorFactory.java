package factory;

import java.util.Random;

import model.Door;
import model.Dragon;
import model.Enemy;
import model.Goblin;
import model.Item;
import model.Rat;
import model.Skeleton;

/**
 * Creates randomized door contents appropriate for the current dungeon stage.
 */
public final class DoorFactory {
    private static final Random RANDOM = new Random();

    private DoorFactory() {
    }

    /**
     * Generates one door result for the supplied stage.
     *
     * @param stage current dungeon stage
     * @return generated door
     */
    public static Door generateDoor(int stage) {
        if (stage >= 5) {
            return new Door(Door.ENCOUNTER, new Dragon(), null);
        }
        int roll = RANDOM.nextInt(100);
        int rewardChance = stage == 4 ? 45 : 25;
        int encounterChance = stage == 4 ? 45 : 60;
        if (roll < encounterChance) {
            return new Door(Door.ENCOUNTER, generateEnemy(stage), null);
        }
        if (roll < encounterChance + rewardChance) {
            return new Door(Door.REWARD, null, generateItem(stage));
        }
        return new Door(Door.NOTHING, null, null);
    }

    private static Enemy generateEnemy(int stage) {
        return switch (stage) {
            case 1 -> new Rat();
            case 2 -> RANDOM.nextBoolean() ? new Rat() : new Goblin();
            case 3 -> RANDOM.nextBoolean() ? new Goblin() : new Skeleton();
            case 4 -> new Skeleton();
            default -> new Dragon();
        };
    }

    private static Item generateItem(int stage) {
        int strength = stage >= 4 ? 35 : 25;
        return switch (RANDOM.nextInt(4)) {
            case 0 -> new Item(Item.HEALTH_POTION, strength);
            case 1 -> new Item(Item.MANA_POTION, strength);
            case 2 -> new Item(Item.STAMINA_POTION, strength);
            default -> new Item(Item.DAMAGE_BOOST, Math.max(2, stage));
        };
    }
}
