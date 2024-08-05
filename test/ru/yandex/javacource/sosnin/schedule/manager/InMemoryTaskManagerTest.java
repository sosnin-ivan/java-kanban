package ru.yandex.javacource.sosnin.schedule.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.sosnin.schedule.tasks.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private static TaskManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefaultTaskManager();
    }

    // interface methods

    @Test
    void createTask() {
        Task task = new Task("Test createTask", "Test createTask desc");
        final int taskId = manager.createTask(task);
        final Task savedTask = manager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = manager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
    }

    @Test
    void createEpic() {
        Epic epic = new Epic("Test createEpic", "Test createEpic desc");
        final int epicId = manager.createEpic(epic);
        final Epic savedEpic = manager.getEpic(epicId);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final List<Epic> epics = manager.getAllEpics();

        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.getFirst(), "Эпики не совпадают.");
    }

    @Test
    void createSubtask() {
        Epic epic = new Epic("Epic for subtask", "Epic for subtask description");
        final int epicId = manager.createEpic(epic);

        Subtask subtask = new Subtask("Test createSubtask", "Test createSubtask desc", epicId);
        final int subtaskId = manager.createSubtask(subtask);
        final Subtask savedSubtask = manager.getSubtask(subtaskId);

        assertNotNull(savedSubtask, "Сабтаска не найдена.");
        assertEquals(subtask, savedSubtask, "Сабтаски не совпадают.");

        final List<Subtask> subtasks = manager.getAllSubtasks();

        assertNotNull(subtasks, "Сабтаски не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество сабтасок.");
        assertEquals(subtask, subtasks.getFirst(), "Сабтаски не совпадают.");

        Subtask failedSubtask = new Subtask("Test createSubtask", "Test createSubtask desc", 3);
        assertNull(manager.createSubtask(failedSubtask), "Сабтаска с неверным epicId создана.");
    }

    @Test
    void updateTask() {
        Task task = new Task("Test updateTask", "Test updateTask desc");
        int taskId = manager.createTask(task);
        String taskName = manager.getTask(taskId).getName();

        task.setName("Updated task");
        manager.updateTask(task);

        assertNotEquals(taskName, manager.getTask(taskId).getName(), "Задача не изменена.");
    }

    @Test
    void updateEpic() {
        Epic epic = new Epic("Test updateEpic", "Test updateEpic desc");
        int epicId = manager.createEpic(epic);
        String epicName = manager.getEpic(epicId).getName();

        epic.setName("Updated epic");
        manager.updateEpic(epic);

        assertNotEquals(epicName, manager.getEpic(epicId).getName(), "Эпик не изменен");
    }

    @Test
    void updateSubtask() {
        Epic epic = new Epic("Epic for subtask", "Epic for subtask desc");
        int epicId = manager.createEpic(epic);
        Subtask subtask = new Subtask("Test updateSubtask", "Test updateSubtask desc", epicId);
        int subtaskId = manager.createSubtask(subtask);
        String subtaskName = manager.getSubtask(subtaskId).getName();

        subtask.setName("Updated subtask");
        manager.updateSubtask(subtask);

        assertNotEquals(subtaskName, manager.getSubtask(subtaskId).getName());
    }

    @Test
    void deleteAllTasks() {
        Task task = new Task("Test task", "Test task desc");
        manager.createTask(task);
        manager.deleteAllTasks();

        assertEquals(0, manager.getAllTasks().size(), "Неверное количество задач.");
    }

    @Test
    void deleteAllEpics() {
        Epic epic = new Epic("Test epic", "Test epic desc");
        int epicId = manager.createEpic(epic);
        Subtask subtask = new Subtask("Test subtask", "Test subtask desc", epicId);
        manager.createSubtask(subtask);

        manager.deleteAllEpics();

        assertEquals(0, manager.getAllEpics().size(), "Неверное количество эпиков.");
        assertEquals(0, manager.getAllSubtasks().size(), "Неверное количество сабтасок.");
    }

    @Test
    void deleteAllSubtasks() {
        Epic epic = new Epic("Epic for subtask", "Epic for subtask desc");
        int epicId = manager.createEpic(epic);
        Subtask subtask = new Subtask("Test subtask", "Test subtask desc", epicId);
        manager.createSubtask(subtask);
        manager.deleteAllSubtasks();

        assertEquals(0, manager.getAllSubtasks().size(), "Неверное количество сабтасок.");
        assertEquals(1, manager.getAllEpics().size(), "Неверное количество эпиков.");
        assertEquals(0, manager.getEpic(epicId).getSubtaskIds().size(), "Неверное количество сабтасок.");
    }

    @Test
    void deleteTask() {
        Task task = new Task("Test task", "Test task desc");
        int taskId = manager.createTask(task);
        manager.deleteTask(taskId);

        assertEquals(0, manager.getAllTasks().size(), "Задача не удалена.");
    }

    @Test
    void deleteEpic() {
        Epic epic = new Epic("Test epic", "Test epic desc");
        int epicId = manager.createEpic(epic);
        Subtask subtask = new Subtask("Test subtask", "Test subtask desc", epicId);
        int subtaskId = manager.createSubtask(subtask);
        manager.deleteEpic(epicId);

        assertEquals(0, manager.getAllEpics().size(), "Эпик не удален.");
        assertEquals(0, manager.getAllSubtasks().size(), "Сабтаска не удалена.");
    }

    @Test
    void deleteSubtask() {
        Epic epic = new Epic("Epic for subtask", "Epic for subtask desc");
        int epicId = manager.createEpic(epic);
        Subtask subtask = new Subtask("Test subtask", "Test subtask desc", epicId);
        int subtaskId = manager.createSubtask(subtask);
        manager.deleteSubtask(subtaskId);

        assertEquals(0, manager.getAllSubtasks().size(), "Сабтаска не удалена.");
        assertEquals(0, manager.getEpic(epicId).getSubtaskIds().size(), "Неверное количество сабтасок.");
    }

    @Test
    void getHistory() {
        Task task = new Task("Test task", "Test task desc");
        int taskId = manager.createTask(task);
        manager.getTask(taskId);
        List<Task> history = manager.getHistory();
        assertEquals(1, history.size(), "Неверное количество записей в истории.");

        Task savedTask = manager.getTask(taskId);
        assertEquals(history.getFirst(), savedTask, "Задача не совпадает.");
    }

    // test cases

    @Test
    void epicStatusChangedToDoneWhenAllSubtasksAreDone() {
        Epic epic = new Epic("Epic for subtask", "Epic for subtask desc");
        int epicId = manager.createEpic(epic);

        assertEquals(manager.getEpic(epicId).getStatus(), Status.NEW, "Новый эпик должен быть в статусе NEW");

        Subtask subtask = new Subtask("Test subtask", "Test subtask desc", epicId);
        subtask.setStatus(Status.DONE);
        manager.createSubtask(subtask);

        assertEquals(manager.getEpic(epicId).getStatus(), Status.DONE, "Статус эпика не изменен.");
    }

    @Test
    void epicStatusChangedToInProgressWhenOneOfSubtasksAreNotDone() {
        Epic epic = new Epic("Epic for subtask", "Epic for subtask desc");
        int epicId = manager.createEpic(epic);

        assertEquals(manager.getEpic(epicId).getStatus(), Status.NEW, "Новый эпик должен быть в статусе NEW");

        Subtask subtask = new Subtask("Test subtask", "Test subtask desc", epicId);
        manager.createSubtask(subtask);
        Subtask subtask2 = new Subtask("Test subtask", "Test subtask desc", epicId);
        subtask2.setStatus(Status.DONE);
        manager.createSubtask(subtask2);

        assertEquals(manager.getEpic(epicId).getStatus(), Status.IN_PROGRESS, "Статус эпика не изменен.");
    }

    @Test
    void historySavePreviousVersionOfTask() {
        Task task = new Task("Test task", "Test task desc");
        int taskId = manager.createTask(task);
        manager.getTask(taskId);

        Task savedTask = new Task(task);
        savedTask.setName("Updated task");
        manager.updateTask(savedTask);
        manager.getTask(taskId);

        List<Task> history = manager.getHistory();

        assertEquals(1, history.size(), "Неверное количество записей в истории.");
    }

    @Test
    void historySavePreviousVersionOfEpic() {
        Epic epic = new Epic("Test epic", "Test epic desc");
        int epicId = manager.createEpic(epic);
        manager.getEpic(epicId);

        Epic savedEpic = new Epic(epic);
        savedEpic.setName("Updated epic");
        manager.updateEpic(savedEpic);
        manager.getEpic(epicId);

        List<Task> history = manager.getHistory();

        assertEquals(1, history.size(), "Неверное количество записей в истории.");
    }

    @Test
    void historySavePreviousVersionOfSubtask() {
        Epic epic = new Epic("Test epic", "Test epic desc");
        int epicId = manager.createEpic(epic);

        Subtask subtask = new Subtask("Test subtask", "Test subtask desc", epicId);
        int subtaskId = manager.createSubtask(subtask);
        manager.getSubtask(subtaskId);

        Subtask savedSubtask = new Subtask(subtask);
        savedSubtask.setName("Updated subtask");
        manager.updateSubtask(savedSubtask);
        manager.getSubtask(subtaskId);

        List<Task> history = manager.getHistory();

        assertEquals(1, history.size(), "Неверное количество записей в истории.");
    }
}
