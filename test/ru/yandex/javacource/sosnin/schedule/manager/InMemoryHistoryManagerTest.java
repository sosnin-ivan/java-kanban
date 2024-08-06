package ru.yandex.javacource.sosnin.schedule.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.sosnin.schedule.tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private static HistoryManager history;

    @BeforeEach
    public void beforeEach() {
        history = Managers.getDefaultHistory();
    }

    @Test
    void add() {
        Task task1 = new Task("Test createTask 1", "Test createTask desc 1");
        Task task2 = new Task("Test createTask 2", "Test createTask desc 2");
        task1.setId(1);
        task2.setId(2);
        history.add(task1);
        history.add(task2);

        assertEquals(2, history.getHistory().size(), "Неверный размер истории");
        assertEquals(task1, history.getHistory().getFirst(), "Таска не совпадает с сохраненной в истории");
    }

    @Test
    void remove() {
        Task task1 = new Task("Test createTask 1", "Test createTask desc 1");
        Task task2 = new Task("Test createTask 2", "Test createTask desc 2");
        task1.setId(1);
        task2.setId(2);
        history.add(task1);
        history.add(task2);

        history.remove(1);

        assertEquals(1, history.getHistory().size(), "Неверный размер истории");
        assertEquals(task2, history.getHistory().getFirst(), "Таска не совпадает с сохраненной в истории");
    }
}
