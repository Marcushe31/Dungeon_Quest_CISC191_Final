import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import exception.SaveLoadException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import model.Dungeon;
import model.Item;
import model.Mage;
import model.Warrior;
import org.junit.jupiter.api.Test;
import util.SaveManager;

class SaveManagerTest {
    @Test
    void saveAndLoadRoundTripKeepsPlayerClass() throws IOException {
        Path file = Files.createTempFile("dungeon-quest", ".save");
        Warrior warrior = new Warrior();
        Dungeon dungeon = new Dungeon();

        SaveManager.save(warrior, dungeon, file.toString());
        SaveManager.GameState state = SaveManager.load(file.toString());

        assertEquals("Warrior", state.getPlayer().getCharacterClass());
    }

    @Test
    void saveAndLoadRoundTripKeepsDungeonStage() throws IOException {
        Path file = Files.createTempFile("dungeon-stage", ".save");
        Mage mage = new Mage();
        Dungeon dungeon = new Dungeon();
        dungeon.setStage(4);

        SaveManager.save(mage, dungeon, file.toString());
        SaveManager.GameState state = SaveManager.load(file.toString());

        assertEquals(4, state.getDungeon().getStage());
    }

    @Test
    void saveAndLoadRestoresInventory() throws IOException {
        Path file = Files.createTempFile("dungeon-inventory", ".save");
        Warrior warrior = new Warrior();
        warrior.addItem(new Item(Item.DAMAGE_BOOST, 3));

        SaveManager.save(warrior, new Dungeon(), file.toString());
        SaveManager.GameState state = SaveManager.load(file.toString());

        assertEquals(warrior.getInventory().size(), state.getPlayer().getInventory().size());
    }

    @Test
    void missingFileThrowsSaveLoadException() {
        assertThrows(SaveLoadException.class, () -> SaveManager.load("missing-file.save"));
    }

    @Test
    void nullSaveStateThrowsSaveLoadException() {
        assertThrows(SaveLoadException.class, () -> SaveManager.save(null, new Dungeon(), "bad.save"));
    }
}
