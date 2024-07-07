package ru.yandex.javacource.sosnin.schedule.manager;

import ru.yandex.javacource.sosnin.schedule.tasks.Task;

import java.util.ArrayList;

public interface HistoryManager {
    int MAX_HISTORY_SIZE = 10;

    void add(Task task);

    ArrayList<Task> getHistory();
}
