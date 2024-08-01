package ru.yandex.javacource.sosnin.schedule.manager;

import ru.yandex.javacource.sosnin.schedule.tasks.Task;

public class Node {
    protected Task task;
    protected Node next;
    protected Node prev;

    protected Node(Task task) {
        this.task = task;
    }
}
