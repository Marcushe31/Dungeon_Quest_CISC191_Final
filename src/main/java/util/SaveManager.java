package util;

import exception.SaveLoadException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import model.Archer;
import model.Dungeon;
import model.Item;
import model.Mage;
import model.Player;
import model.Warrior;

/**
 * Handles simple text save files for player and dungeon progress.
 */
public final class SaveManager {
    private static final String PLAYER_KEY = "PLAYER=";
    private static final String STAGE_KEY = "DUNGEON_STAGE=";
    private static final String INVENTORY_KEY = "INVENTORY=";

    private SaveManager() {
    }

    /**
     * Saves the current player and dungeon state to disk.
     *
     * @param player player to save
     * @param dungeon dungeon progress to save
     * @param filename target filename
     */
    public static void save(Player player, Dungeon dungeon, String filename) {
        if (player == null || dungeon == null) {
            throw new SaveLoadException("Cannot save without a player and dungeon.");
        }
        try {
            List<String> lines = new ArrayList<>();
            lines.add(PLAYER_KEY + player);
            lines.add(STAGE_KEY + dungeon.getStage());
            lines.add(INVENTORY_KEY + serializeInventory(player));
            Files.write(Path.of(filename), lines);
        } catch (IOException | RuntimeException ex) {
            throw new SaveLoadException("Could not save game to " + filename, ex);
        }
    }

    /**
     * Loads a save file and rebuilds the game state.
     *
     * @param filename save file to read
     * @return loaded game state
     */
    public static GameState load(String filename) {
        try {
            List<String> lines = Files.readAllLines(Path.of(filename));
            String playerLine = findValue(lines, PLAYER_KEY);
            String stageLine = findValue(lines, STAGE_KEY);
            String inventoryLine = findValue(lines, INVENTORY_KEY);
            Player player = parsePlayer(playerLine);
            parseInventory(inventoryLine).forEach(player::addItem);
            Dungeon dungeon = new Dungeon();
            dungeon.setStage(Integer.parseInt(stageLine));
            return new GameState(player, dungeon);
        } catch (IOException | RuntimeException ex) {
            throw new SaveLoadException("Could not load game from " + filename, ex);
        }
    }

    private static String findValue(List<String> lines, String key) {
        return lines.stream()
                .filter(line -> line.startsWith(key))
                .map(line -> line.substring(key.length()))
                .findFirst()
                .orElseThrow(() -> new SaveLoadException("Save file is missing " + key));
    }

    private static Player parsePlayer(String text) {
        String[] parts = text.split("\\|");
        if (parts.length != 8) {
            throw new SaveLoadException("Player save data is malformed.");
        }
        Player player = createPlayer(parts[0]);
        player.setMaxHealth(Integer.parseInt(parts[2]));
        player.setHealth(Integer.parseInt(parts[1]));
        player.setMaxMana(Integer.parseInt(parts[4]));
        player.setMana(Integer.parseInt(parts[3]));
        player.setMaxStamina(Integer.parseInt(parts[6]));
        player.setStamina(Integer.parseInt(parts[5]));
        player.setDamage(Integer.parseInt(parts[7]));
        return player;
    }

    private static Player createPlayer(String characterClass) {
        return switch (characterClass) {
            case "Warrior" -> new Warrior();
            case "Mage" -> new Mage();
            case "Archer" -> new Archer();
            default -> new Player(characterClass, 100, 50, 50, 10);
        };
    }

    private static String serializeInventory(Player player) {
        List<String> parts = new ArrayList<>();
        for (Item item : player.getInventory()) {
            parts.add(item.toString());
        }
        return String.join(",", parts);
    }

    private static List<Item> parseInventory(String text) {
        List<Item> items = new ArrayList<>();
        if (text == null || text.isBlank()) {
            return items;
        }
        String[] entries = text.split(",");
        for (String entry : entries) {
            String[] parts = entry.split(":");
            if (parts.length == 2) {
                items.add(new Item(parts[0], Integer.parseInt(parts[1])));
            }
        }
        return items;
    }

    /**
     * Loaded state container returned by SaveManager.
     */
    public static class GameState {
        private final Player player;
        private final Dungeon dungeon;

        public GameState(Player player, Dungeon dungeon) {
            this.player = player;
            this.dungeon = dungeon;
        }

        public Player getPlayer() {
            return player;
        }

        public Dungeon getDungeon() {
            return dungeon;
        }
    }
}
