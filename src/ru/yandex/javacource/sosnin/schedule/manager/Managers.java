package ru.yandex.javacource.sosnin.schedule.manager;

import java.io.File;

public class Managers {
    public static TaskManager getDefaultTaskManager() {
        return new FileBackedTaskManager(new File("resources/task.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getFileBackedTaskManagerWithFile(File file) {
        return new FileBackedTaskManager(file);
    }
}
