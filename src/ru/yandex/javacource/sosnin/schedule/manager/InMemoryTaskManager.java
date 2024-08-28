package ru.yandex.javacource.sosnin.schedule.manager;

import ru.yandex.javacource.sosnin.schedule.tasks.*;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected int generatorId = 0;

    protected final Map<Integer, Task> tasks;
    protected final Map<Integer, Epic> epics;
    protected final Map<Integer, Subtask> subtasks;
    protected final HistoryManager history;
    protected final TreeSet<Task> prioritizedTasks;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.history = Managers.getDefaultHistory();
        this.prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public int createTask(Task task) {
        int id = ++generatorId;
        task.setId(id);
        tasks.put(generatorId, task);
        addTaskToPrioritized(task);
        return id;
    }

    @Override
    public int createEpic(Epic epic) {
        int id = ++generatorId;
        epic.setId(id);
        epics.put(generatorId, epic);
        addTaskToPrioritized(epic);
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
        addTaskToPrioritized(subtask);
        epic.addSubtaskId(id);
        updateEpicStatus(epic);

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
        updateEpicStatus(epic);
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
            updateEpicStatus(epic);
        }
        subtasks.clear();
    }

    @Override
    public void deleteTask(int id) {
        removeTaskFromPrioritized(tasks.get(id));
        tasks.remove(id);
        history.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        removeTaskFromPrioritized(epics.get(id));
        final Epic epic = epics.remove(id);
        history.remove(id);
        if (epic == null) {
            return;
        }
        for (Integer subtaskId : epic.getSubtaskIds()) {
            removeTaskFromPrioritized(subtasks.get(subtaskId));
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
        updateEpicStatus(epic);
        updateEpicDates(epic);
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    protected void addTaskToPrioritized(Task task) {
        if (task.getStartTime() != null && validateCorrectTime(task)) {
            prioritizedTasks.add(task);
        }
    }

    private void removeTaskFromPrioritized(Task task) {
        if (task.getStartTime() != null) {
            prioritizedTasks.remove(task);
        }
    }

    private boolean validateCorrectTime(Task newTask) {
        LocalDateTime startTime = newTask.getStartTime();
        for (Task task : prioritizedTasks) {
            if (startTime.isAfter(task.getStartTime()) && startTime.isBefore(task.getEndTime())) {
                return false;
            }
        }

        return true;
    }

    private void updateEpicStatus(Epic epic) {
        List<Subtask> subtasksByEpic = getSubtasksByEpic(epic.getId());

        ArrayList<TaskStatus> statuses = new ArrayList<>();
        for (Subtask s : subtasksByEpic) {
            statuses.add(s.getStatus());
        }

        if (isSimilarStatuses(statuses, TaskStatus.NEW)) {
            epic.setStatus(TaskStatus.NEW);
        } else if (isSimilarStatuses(statuses, TaskStatus.DONE)) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    protected void updateEpicDates(Epic epic) {
        List<Subtask> subtasksByEpic = getSubtasksByEpic(epic.getId());
        epic.setStartTime(subtasksByEpic);
        epic.setDuration(subtasksByEpic);
        epic.setEndTime();
    }

    private boolean isSimilarStatuses(ArrayList<TaskStatus> statuses, TaskStatus targetStatus) {
        boolean isSimilar = true;
        for (TaskStatus status : statuses) {
            if (status != targetStatus) {
                isSimilar = false;
                break;
            }
        }
        return isSimilar;
    }
}
