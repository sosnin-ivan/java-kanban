package ru.yandex.javacource.sosnin.schedule.tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds;

    public Epic(String name, String description) {
        super(name, description);
        subtaskIds = new ArrayList<>();
    }

    public Epic(Epic epic) {
        super(epic);
        subtaskIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(ArrayList<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    public void addSubtaskId(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public void removeSubtaskId(Integer subtaskId) {
        subtaskIds.remove(subtaskId);
    }

    public void removeAllSubtaskIds() {
        subtaskIds.clear();
    }

    public static Epic fromString(String[] line) {
        Epic epic = new Epic(line[2], line[4]);
        epic.setId(Integer.parseInt(line[0]));
        epic.setStatus(Status.valueOf(line[3]));
        return epic;
    }

    @Override
    public String toString() {
        return id + "," + Type.EPIC + "," + name + "," + status + "," + description;
    }
}
