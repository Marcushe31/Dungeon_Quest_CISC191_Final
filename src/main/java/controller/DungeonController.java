package controller;

import exception.InvalidActionException;
import model.Door;
import model.Dungeon;
import model.Player;
import view.DungeonView;

/**
 * Handles dungeon choices and moves the player between doors and battles.
 */
public class DungeonController {
    private Dungeon dungeon;
    private Player player;
    private DungeonView dungeonView;
    private GameManager gameManager;

    public DungeonController(Dungeon dungeon, Player player, DungeonView dungeonView, GameManager gameManager) {
        this.dungeon = dungeon;
        this.player = player;
        this.dungeonView = dungeonView;
        this.gameManager = gameManager;
        if (dungeonView != null) {
            dungeonView.setDoorListener(this::handleDoorChoice);
        }
    }

    /**
     * Starts or resumes dungeon exploration.
     */
    public void runDungeon() {
        updateView();
    }

    /**
     * Resolves a door click.
     *
     * @param doorIndex selected door index
     */
    public void handleDoorChoice(int doorIndex) {
        Door[] doors = dungeon.getDoors();
        if (doorIndex < 0 || doorIndex >= doors.length || doors[doorIndex] == null) {
            throw new InvalidActionException("Door choice is out of range.");
        }
        Door door = doors[doorIndex];
        String message = door.reveal();
        dungeon.setEncounterOccurs(Door.ENCOUNTER.equals(door.getEventType()));
        dungeon.setRewardOccurs(Door.REWARD.equals(door.getEventType()));

        if (Door.ENCOUNTER.equals(door.getEventType()) && door.getEnemy() != null) {
            updateView(message);
            gameManager.startBattle(door.getEnemy());
            return;
        }
        if (Door.REWARD.equals(door.getEventType()) && door.getItem() != null) {
            player.addItem(door.getItem());
        }
        dungeon.advanceStage();
        updateView(message);
    }

    /**
     * Regenerates doors for the current stage.
     */
    public void updateModel() {
        dungeon.initializeDoors();
    }

    /**
     * Refreshes the dungeon screen.
     */
    public void updateView() {
        updateView(null);
    }

    private void updateView(String message) {
        if (dungeonView != null) {
            dungeonView.update(player, dungeon, message);
        }
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public Player getPlayer() {
        return player;
    }
}
