package ru.yandex.javacource.sosnin.schedule.manager;

import ru.yandex.javacource.sosnin.schedule.tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    protected int generatorId = 0;

    protected final Map<Integer, Task> tasks;
    protected final Map<Integer, Epic> epics;
    protected final Map<Integer, Subtask> subtasks;
    protected final HistoryManager history;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.history = Managers.getDefaultHistory();
    }

    @Override
    public int createTask(Task task) {
        int id = ++generatorId;
        task.setId(id);
        tasks.put(generatorId, task);
        return id;
    }

    @Override
    public int createEpic(Epic epic) {
        int id = ++generatorId;
        epic.setId(id);
        epics.put(generatorId, epic);
        return id;
    }

    @Override
    public Integer createSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);

        if (epic == null) {
            return null;
        }

        int id = ++generatorId;
        subtask.setId(id);
        subtasks.put(id, subtask);
        epic.addSubtaskId(id);
        updateEpicStatus(epicId);

        return id;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);

        history.add(task);
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        history.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        history.add(subtask);
        return subtask;
    }

    @Override
    public List<Subtask> getSubtasksByEpic(int id) {
        List<Subtask> subtasksByEpic = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == id) {
                subtasksByEpic.add(subtask);
            }
        }
        return subtasksByEpic;
    }

    @Override
    public void updateTask(Task task) {
        int id = task.getId();
        tasks.replace(id, task);
    }

    @Override
    public void updateEpic(Epic epic) {
        final Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null) {
            return;
        }
        epic.setSubtaskIds(savedEpic.getSubtaskIds());
        epic.setStatus(savedEpic.getStatus());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            return;
        }

        int id = subtask.getId();
        subtasks.replace(id, subtask);
        updateEpicStatus(subtask.getEpicId());
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.removeAllSubtaskIds();
            updateEpicStatus(epic.getId());
        }
        subtasks.clear();
    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        history.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        final Epic epic = epics.remove(id);
        history.remove(id);
        if (epic == null) {
            return;
        }
        for (Integer subtaskId : epic.getSubtaskIds()) {
            subtasks.remove(subtaskId);
            history.remove(subtaskId);
        }
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtaskId(id);
        subtasks.remove(id);
        history.remove(id);
        updateEpicStatus(epic.getId());
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        List<Subtask> subtasksByEpic = getSubtasksByEpic(epicId);

        ArrayList<Status> statuses = new ArrayList<>();
        for (Subtask s : subtasksByEpic) {
            statuses.add(s.getStatus());
        }

        if (isSimilarStatuses(statuses, Status.NEW)) {
            epic.setStatus(Status.NEW);
        } else if (isSimilarStatuses(statuses, Status.DONE)) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    private boolean isSimilarStatuses(ArrayList<Status> statuses, Status targetStatus) {
        boolean isSimilar = true;
        for (Status status : statuses) {
            if (status != targetStatus) {
                isSimilar = false;
                break;
            }
        }
        return isSimilar;
    }
}
