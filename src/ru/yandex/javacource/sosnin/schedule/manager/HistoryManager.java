package ru.yandex.javacource.sosnin.schedule.manager;

import ru.yandex.javacource.sosnin.schedule.tasks.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    List<Task> getHistory();
}
