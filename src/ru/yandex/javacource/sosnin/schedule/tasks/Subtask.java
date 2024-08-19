package ru.yandex.javacource.sosnin.schedule.tasks;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, int epicID) {
        super(name, description);
        this.epicId = epicID;
    }

    public Subtask(Subtask subtask) {
        super(subtask);
        this.epicId = subtask.getEpicId();
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public static Subtask fromString(String[] line) {
        Subtask subtask = new Subtask(line[2], line[4], Integer.parseInt(line[5]));
        subtask.setId(Integer.parseInt(line[0]));
        subtask.setStatus(Status.valueOf(line[3]));
        return subtask;
    }

    @Override
    public String toString() {
        return id + "," + Type.SUBTASK + "," + name + "," + status + "," + description + "," + epicId;
    }
}
