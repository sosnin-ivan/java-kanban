package ru.yandex.javacource.sosnin.schedule.tasks;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, int epicID) {
        super(name, description);
        this.epicId = epicID;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", epicId=" + epicId +
                '}';
    }
}
