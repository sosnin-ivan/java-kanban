package ru.yandex.javacource.sosnin.schedule.manager;

import java.io.File;

public class Managers {
    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getFileBackedTaskManager() {
        return new FileBackedTaskManager();
    }

    public static TaskManager getFileBackedTaskManagerWithFile(File file) {
        return new FileBackedTaskManager(file);
    }
}
