package model;

import factory.DoorFactory;

/**
 * Stores dungeon progress and the three current door choices.
 */
public class Dungeon {
    private static final int DEFAULT_DOORS = 3;
    private static final int DEFAULT_MAX_STAGES = 5;

    private int numberOfDoors;
    private int stage;
    private int maxStages;
    private String outcome;
    private boolean encounterOccurs;
    private boolean rewardOccurs;
    private Door[] doors;

    public Dungeon() {
        this.numberOfDoors = DEFAULT_DOORS;
        this.stage = 1;
        this.maxStages = DEFAULT_MAX_STAGES;
        this.outcome = "Exploring";
        this.doors = new Door[numberOfDoors];
        initializeDoors();
    }

    /**
     * Creates the current stage's doors through the factory.
     */
    public void initializeDoors() {
        doors = new Door[numberOfDoors];
        for (int i = 0; i < doors.length; i++) {
            doors[i] = DoorFactory.generateDoor(stage);
        }
    }

    /**
     * Moves to the next stage and refreshes the door choices.
     */
    public void advanceStage() {
        if (stage < maxStages) {
            stage++;
            initializeDoors();
        } else {
            outcome = "Victory";
        }
        encounterOccurs = false;
        rewardOccurs = false;
    }

    public int getNumberOfDoors() {
        return numberOfDoors;
    }

    public void setNumberOfDoors(int numberOfDoors) {
        this.numberOfDoors = DEFAULT_DOORS;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = Math.max(1, Math.min(maxStages, stage));
        initializeDoors();
    }

    public int getMaxStages() {
        return maxStages;
    }

    public void setMaxStages(int maxStages) {
        this.maxStages = Math.max(1, maxStages);
        this.stage = Math.min(stage, this.maxStages);
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public boolean isEncounterOccurs() {
        return encounterOccurs;
    }

    public void setEncounterOccurs(boolean encounterOccurs) {
        this.encounterOccurs = encounterOccurs;
    }

    public boolean isRewardOccurs() {
        return rewardOccurs;
    }

    public void setRewardOccurs(boolean rewardOccurs) {
        this.rewardOccurs = rewardOccurs;
    }

    public Door[] getDoors() {
        return doors.clone();
    }

    public void setDoors(Door[] doors) {
        this.doors = doors == null ? new Door[numberOfDoors] : doors.clone();
    }
}
