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
        Task task = new Task("Test createTask", "Test createTask desc");
        history.add(task);

        assertEquals(1, history.getHistory().size(), "Неверный размер истории");
        assertEquals(task, history.getHistory().getFirst(), "Таска не совпадает с сохраненной в истории");
    }
}
