package ru.yandex.javacource.sosnin.schedule.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        subtaskIds = new ArrayList<>();
    }

    public Epic(
            int id,
            String name,
            String description,
            TaskStatus status
    ) {
        super(id, name, description, status, LocalDateTime.MAX, Duration.ZERO);
        subtaskIds = new ArrayList<>();
    }

    public Epic(Epic epic) {
        super(epic);
        subtaskIds = new ArrayList<>();
        endTime = LocalDateTime.MAX;
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setStartTime(List<Subtask> subtasks) {
        for (Subtask subtask : subtasks) {
            LocalDateTime subtaskStartTime = subtask.getStartTime();
            if (subtaskStartTime != null) {
                startTime = startTime.isBefore(subtaskStartTime) ? subtaskStartTime : startTime;
            }
        }
    }

    public void setDuration(List<Subtask> subtasks) {
        for (Subtask subtask : subtasks) {
            Duration subtaskDuration = subtask.getDuration();
            if (subtaskDuration != null) {
                duration = duration.plus(subtaskDuration);
            }
        }
    }

    public void setEndTime() {
        if (startTime != null) {
            endTime = startTime.plus(duration);
        }
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(ArrayList<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    public void addSubtaskId(Integer subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public void removeSubtaskId(Integer subtaskId) {
        subtaskIds.remove(subtaskId);
    }

    public void removeAllSubtaskIds() {
        subtaskIds.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subtaskIds=" + subtaskIds +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", endTime=" + getEndTime() +
                '}';
    }
}
