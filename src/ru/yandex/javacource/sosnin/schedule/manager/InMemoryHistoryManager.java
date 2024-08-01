package ru.yandex.javacource.sosnin.schedule.manager;

import ru.yandex.javacource.sosnin.schedule.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> history;
    private Node head;
    private Node tail;

    public InMemoryHistoryManager() {
        this.history = new HashMap<>();
    }

    public void linkLast(Task task) {
        if (head == null) {
            head = new Node(task);
            tail = head;
            history.put(task.getId(), head);
        } else {
            Node newTale = new Node(task);
            newTale.prev = tail;
            tail.next = newTale;
            tail = newTale;
            history.put(task.getId(), tail);
        }
    }

    public void removeNode(Node node) {
        if (node.prev == null) {
            head = head.next;
            head.prev = null;
        } else if (node.next == null) {
            tail = tail.prev;
            tail.next = null;
        }
        else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (history.containsKey(task.getId())) {
            Node node = history.get(task.getId());
            removeNode(node);
        }
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        Node node = history.get(id);
        if (node != null) {
            removeNode(node);
            history.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Node node : history.values()) {
            tasks.add(node.task);
        }
        return tasks;
    }
}
