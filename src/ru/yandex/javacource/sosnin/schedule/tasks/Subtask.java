package ru.yandex.javacource.sosnin.schedule.tasks;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, int epicID) {
        super(name, description);
        this.epicId = epicID;
    }

    public Subtask(int id, String name, String description, TaskStatus status, int epicID) {
        super(id, name, description, status);
        this.epicId = epicID;
    }

    public Subtask(Subtask subtask) {
        super(subtask);
        this.epicId = subtask.getEpicId();
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
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
