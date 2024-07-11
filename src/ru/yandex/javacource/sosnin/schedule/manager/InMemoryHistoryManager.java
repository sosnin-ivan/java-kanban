package ru.yandex.javacource.sosnin.schedule.manager;

import ru.yandex.javacource.sosnin.schedule.tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private final ArrayList<Task> history;

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

        Task newTask = new Task(task);
        history.add(newTask);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }
}
