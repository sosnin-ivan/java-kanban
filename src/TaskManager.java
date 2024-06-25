import Tasks.*;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private static int tasksIDs = 1;

    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, Subtask> subtasks;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
    }

    // create

    public void createTask(Task task) {
        tasks.put(tasksIDs, task);
        tasksIDs++;
    }

    public void createEpic(Epic epic) {
        epics.put(tasksIDs, epic);
        tasksIDs++;
    }

    public void createSubtask(Subtask subtask) {
        subtasks.put(tasksIDs, subtask);
        tasksIDs++;

        Epic epic = epics.get(subtask.getEpicID());
        if (epic.getStatus().equals(Status.DONE)) {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    // get

    public HashMap<Integer, Task> getAllTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getAllEpics() {
        return epics;
    }

    public HashMap<Integer, Subtask> getAllSubtasks() {
        return subtasks;
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    public ArrayList<Subtask> getSubtasksByEpic(int id) {
        ArrayList<Subtask> subtasksByEpic = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicID() == id) {
                subtasksByEpic.add(subtask);
            }
        }
        return subtasksByEpic;
    }

    // update

    public void updateTask(int id, Task task) {
        tasks.replace(id, task);
    }

    public void updateEpic(int id, Epic epic) {
        epics.replace(id, epic);
    }

    public void updateSubtask(int id, Subtask subtask) {
        subtasks.replace(id, subtask);
        updateEpicStatus(subtask);
    }

    // delete

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        for (Integer epicID : epics.keySet()) {
            deleteSubtasksByEpic(epicID);
        }
        epics.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteEpic(int id) {
        deleteSubtasksByEpic(id);
        epics.remove(id);
    }

    public void deleteSubtask(int id) {
        subtasks.remove(id);
    }

    public void deleteSubtasksByEpic(int epicID) {
        ArrayList<Integer> subtaskIDs = new ArrayList<>();
        for (Integer id : subtasks.keySet()) {
            Subtask subtask = subtasks.get(id);
            if (subtask.getEpicID() == epicID) {
                subtaskIDs.add(id);
            }
        }

        for (Integer id : subtaskIDs) {
            deleteSubtask(id);
        }
    }

    // statuses

    private void updateEpicStatus(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicID());
        ArrayList<Subtask> subtasksByEpic = getSubtasksByEpic(subtask.getEpicID());

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
