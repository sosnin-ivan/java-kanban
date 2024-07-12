package ru.yandex.javacource.sosnin.schedule.manager;

import ru.yandex.javacource.sosnin.schedule.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    int MAX_HISTORY_SIZE = 10;

    private final List<Task> history;

    public InMemoryHistoryManager() {
        this.history = new ArrayList<>();
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        if (history.size() >= MAX_HISTORY_SIZE) {
            history.removeFirst();
        }

        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
