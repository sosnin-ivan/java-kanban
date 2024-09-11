package ru.yandex.javacource.sosnin.schedule.manager;

import ru.yandex.javacource.sosnin.schedule.exceptions.TaskValidationException;
import ru.yandex.javacource.sosnin.schedule.tasks.*;

import java.util.List;

public interface TaskManager {
    // create

    int createTask(Task task) throws TaskValidationException;

    int createEpic(Epic epic);

    Integer createSubtask(Subtask subtask) throws TaskValidationException;

    // get

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    Task getTask(int id) throws NullPointerException;

    Epic getEpic(int id) throws NullPointerException;

    Subtask getSubtask(int id) throws NullPointerException;

    List<Subtask> getSubtasksByEpic(int id) throws NullPointerException;

    // update

    void updateTask(Task task) throws NullPointerException;

    void updateEpic(Epic epic) throws NullPointerException;

    void updateSubtask(Subtask subtask) throws NullPointerException;

    // delete

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    void deleteTask(int id) throws NullPointerException;

    void deleteEpic(int id) throws NullPointerException;

    void deleteSubtask(int id) throws NullPointerException;

    // history & prioritized

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();
}
