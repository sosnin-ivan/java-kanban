package ru.yandex.javacource.sosnin.schedule.manager;

import ru.yandex.javacource.sosnin.schedule.tasks.*;

import java.util.ArrayList;

public interface TaskManager {
    // create

    int createTask(Task task);

    int createEpic(Epic epic);

    Integer createSubtask(Subtask subtask);

    // get

    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<Subtask> getAllSubtasks();

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);

    ArrayList<Subtask> getSubtasksByEpic(int id);

    // update

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    // delete

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubtask(int id);

    // history

    ArrayList<Task> getHistory();
}
