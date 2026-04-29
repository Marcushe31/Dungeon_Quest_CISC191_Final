import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import controller.DungeonController;
import exception.InvalidActionException;
import factory.DoorFactory;
import model.Door;
import model.Dragon;
import model.Dungeon;
import model.Goblin;
import model.Rat;
import model.Skeleton;
import model.Warrior;
import org.junit.jupiter.api.Test;

class DungeonTest {
    @Test
    void dungeonAlwaysCreatesThreeDoors() {
        Dungeon dungeon = new Dungeon();

        assertEquals(3, dungeon.getDoors().length);
    }

    @Test
    void advanceStageMovesForward() {
        Dungeon dungeon = new Dungeon();

        dungeon.advanceStage();

        assertEquals(2, dungeon.getStage());
    }

    @Test
    void stageOneEncounterOnlyCreatesRat() {
        for (int i = 0; i < 50; i++) {
            Door door = DoorFactory.generateDoor(1);
            if (Door.ENCOUNTER.equals(door.getEventType())) {
                assertInstanceOf(Rat.class, door.getEnemy());
            }
        }
    }

    @Test
    void stageThreeEncounterUsesGoblinOrSkeleton() {
        for (int i = 0; i < 50; i++) {
            Door door = DoorFactory.generateDoor(3);
            if (Door.ENCOUNTER.equals(door.getEventType())) {
                assertTrue(door.getEnemy() instanceof Goblin || door.getEnemy() instanceof Skeleton);
            }
        }
    }

    @Test
    void stageFiveAlwaysCreatesDragonEncounter() {
        Door door = DoorFactory.generateDoor(5);

        assertEquals(Door.ENCOUNTER, door.getEventType());
        assertInstanceOf(Dragon.class, door.getEnemy());
    }

    @Test
    void invalidDoorChoiceThrowsCustomException() {
        Dungeon dungeon = new Dungeon();
        DungeonController controller = new DungeonController(dungeon, new Warrior(), null, null);

        assertThrows(InvalidActionException.class, () -> controller.handleDoorChoice(99));
    }
}
